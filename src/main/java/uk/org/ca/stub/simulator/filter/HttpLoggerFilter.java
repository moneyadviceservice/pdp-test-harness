package uk.org.ca.stub.simulator.filter;

import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.org.ca.stub.simulator.utils.Commons.X_REQUEST_ID;

@Component
public class HttpLoggerFilter extends OncePerRequestFilter {
    /**
     * HTTP_LOGGER
     * This logger is declared in the resources/logback-spring.xml file; be careful if renamed
     */
    private static final String HTTP_LOGGER = "HTTP_LOGGER";
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTP_LOGGER);

    private final BuildProperties buildProperties;
    private final String clientAuthConfig;
    private final JsonMapper jsonMapper = new JsonMapper();


    public HttpLoggerFilter(BuildProperties buildProperties,
                            @Value("${server.ssl.client-auth}") String clientAuthConfig) {
        this.buildProperties = buildProperties;
        this.clientAuthConfig = clientAuthConfig;
    }

    private static final long MAX_PAYLOAD_BYTES = 1_048_576L;
    private static final List<String> SIZE_LIMITED_ENDPOINTS = List.of(
            "/service-availability/find",
            "/service-availability/view",
            "/view-response/response-time",
            "/view-response/calculations",
            "/view-response/request-number",
            "/view-response/unavailable"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var requestId = request.getHeader(X_REQUEST_ID);

        // Reject oversized payloads on reporting endpoints before deserialization
        String uri = request.getRequestURI();
        if (SIZE_LIMITED_ENDPOINTS.stream().anyMatch(uri::endsWith)) {
            long contentLength = request.getContentLengthLong();
            if (contentLength > MAX_PAYLOAD_BYTES) {
                LOGGER.warn("Payload too large ({} bytes) for {}", contentLength, uri);
                response.setStatus(413);
                response.setContentType("application/json");
                String body = "{\"type\":\"about:blank\",\"title\":\"Payload Too Large\",\"status\":413,\"errors\":[{\"code\":\"CONTENT_TOO_LARGE\"}]}";
                response.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);
                response.getWriter().write(body);
                return;
            }
        }

        var cachedRequest = new ContentCachingRequestWrapper(request);
        var cachedResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(cachedRequest, cachedResponse);

        if (requestId == null || ObjectUtils.isEmpty(requestId)) {
            var logUri = request.getRequestURI();
            var endpoints = List.of("/token", "/rreguri", "/introspect", "/perm","/jwk_uri");
            if (endpoints.stream().anyMatch(logUri::contains)) {
                LOGGER.debug("Not logging request and response due to x-request-id being null or empty for: {} ", request.getRequestURI());
            }
        } else if (LOGGER.isInfoEnabled()) {
            // build and log
            var httpLogDto = getRequestResponseInstance(request, cachedRequest, cachedResponse);
            httpLogDto.setBuildVersion(buildProperties.getVersion());
            LOGGER.info(httpLogDto.toString());
        }
        cachedResponse.copyBodyToResponse();
    }

    private HttpTrafficLogDto getRequestResponseInstance(HttpServletRequest request, ContentCachingRequestWrapper cachedRequest, ContentCachingResponseWrapper cachedResponse) throws IOException {
        var httpLogDto = HttpTrafficLogDto.builder();
        httpLogDto.mtlsInfo(new MtlsInfoDto(request, clientAuthConfig));
        // add request params
        httpLogDto.requestId(request.getHeader(X_REQUEST_ID))
                .requestUri(cachedRequest.getRequestURI())
                .requestMethod(cachedRequest.getMethod())
                .pathInfo(cachedRequest.getPathInfo());

        // request headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = cachedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = cachedRequest.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        if (!headers.isEmpty()) {
            httpLogDto.headers(headers);
        }

        // request parameters
        Map<String, String[]> requestParams = cachedRequest.getParameterMap();

        if (!requestParams.isEmpty()) {
            httpLogDto.requestParameters(requestParams);
        }

        // request body
        var requestContent = cachedRequest.getContentAsString();
        // note - below we check if requestParams for form data - won't parse as json and we get an unnecessary exception (e.g. introspect endpoint)
        if (requestParams.isEmpty() && !requestContent.isEmpty()) {
            try {
                Map<String, Object> myRequestBody = jsonMapper.readValue(requestContent, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                httpLogDto.requestBodyParameters(myRequestBody);
            } catch (IOException e) {
                LOGGER.error("Error parsing request body", e);
            }
        }

        // response content
        var contentType = cachedResponse.getContentType();
        byte[] content = cachedResponse.getContentAsByteArray();

        httpLogDto.responseStatus(cachedResponse.getStatus())
                .responseContentType(contentType)
                .responseContentEncoding(cachedResponse.getCharacterEncoding())
                .responseContentLength(content.length);

        // get content as string or json map
        var responseBody = new String(content, cachedResponse.getCharacterEncoding());
        if (contentType != null && contentType.contains("application/json") && cachedResponse.getStatus() >= 200 && cachedResponse.getStatus() < 300) {
            Map<String, Object> responseJson = jsonMapper.readValue(responseBody, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
            httpLogDto.responseJson(responseJson);
        } else {
            httpLogDto.responseText(responseBody);
        }

        return httpLogDto.build();
    }
}

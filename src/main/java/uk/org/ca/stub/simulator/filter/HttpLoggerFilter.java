package uk.org.ca.stub.simulator.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
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

    public HttpLoggerFilter(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var requestId = request.getHeader(X_REQUEST_ID);

        var cachedRequest = new ContentCachingRequestWrapper(request);
        var cachedResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(cachedRequest, cachedResponse);

        if (requestId == null || ObjectUtils.isEmpty(requestId)) {
            String uri = request.getRequestURI();
            List<String> endpoints = List.of("/token", "/rreguri", "/introspect", "/perm");
            if (endpoints.stream().anyMatch(uri::contains)) {
                LOGGER.debug("Not logging request and response due to x-request-id being null or empty for: {} ", request.getRequestURI());
            }
        } else if (LOGGER.isInfoEnabled()) {
            // build and log
            HttpTrafficLogDto httpLogDto = getRequestResponseInstance(requestId, cachedRequest, cachedResponse);
            httpLogDto.setBuildVersion(buildProperties.getVersion());
            LOGGER.info(httpLogDto.toString());
        }
        cachedResponse.copyBodyToResponse();
    }

    private HttpTrafficLogDto getRequestResponseInstance(String requestId, ContentCachingRequestWrapper cachedRequest, ContentCachingResponseWrapper cachedResponse) throws IOException {
        var httpLogDto = HttpTrafficLogDto.builder();

        // add request params
        httpLogDto.requestId(requestId)
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
        String requestContent = cachedRequest.getContentAsString();
        // note - below we check if requestParams for form data - won't parse as json and we get an unnecessary exception (e.g. introspect endpoint)
        if (requestParams.isEmpty() && !requestContent.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> myRequestBody = objectMapper.readValue(requestContent, Map.class);
                httpLogDto.requestBodyParameters(myRequestBody);
            } catch (IOException e) {
                LOGGER.error("Error parsing request body", e);
            }
        }

        // response content
        String contentType = cachedResponse.getContentType();
        byte[] content = cachedResponse.getContentAsByteArray();

        httpLogDto.responseStatus(cachedResponse.getStatus())
                .responseContentType(contentType)
                .responseContentEncoding(cachedResponse.getCharacterEncoding())
                .responseContentLength(content.length);

        // get content as string or json map
        String responseBody = new String(content, cachedResponse.getCharacterEncoding());

        if (contentType != null && contentType.contains("application/json") && cachedResponse.getStatus() >= 200 && cachedResponse.getStatus() < 300) {
            Map<String, String> responseJson = new ObjectMapper().readValue(responseBody, Map.class);
            httpLogDto.responseJson(responseJson);
        } else {
            httpLogDto.responseText(responseBody);
        }

        return httpLogDto.build();
    }
}

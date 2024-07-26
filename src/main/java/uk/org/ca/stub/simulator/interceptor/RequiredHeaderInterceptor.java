package uk.org.ca.stub.simulator.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;

import java.util.List;

import static uk.org.ca.stub.simulator.utils.Commons.X_REQUEST_ID;

public class RequiredHeaderInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequiredHeaderInterceptor.class);

    public static final String RREGURI = "/rreguri";
    private static final List<CasAPIRequest> FILTERED_ENDPOINTS = List.of(
            new CasAPIRequest("/token", HttpMethod.POST),
            new CasAPIRequest(RREGURI, HttpMethod.POST),
            new CasAPIRequest(RREGURI, HttpMethod.PATCH),
            new CasAPIRequest(RREGURI, HttpMethod.GET),
            new CasAPIRequest(RREGURI, HttpMethod.DELETE),
            new CasAPIRequest("/perm", HttpMethod.POST),
            new CasAPIRequest("/introspect", HttpMethod.POST),
            new CasAPIRequest("/events/audit", HttpMethod.POST),
            new CasAPIRequest("/events/operational", HttpMethod.POST),
            new CasAPIRequest("/events/MI", HttpMethod.POST)
    );

    public RequiredHeaderInterceptor() { /* explicitly public */ }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var currentRequest = new CasAPIRequest(request.getRequestURI(), HttpMethod.valueOf(request.getMethod().toUpperCase()));
        if (FILTERED_ENDPOINTS.contains(currentRequest)) {
            var requestId = request.getHeader(X_REQUEST_ID);
            LOGGER.trace("...... Processing request with x-request-id: {}  and URI: {} and method {}", requestId, request.getRequestURI(), request.getMethod());
            if (ObjectUtils.isEmpty(requestId)) {
                throw new InvalidRequestException("Missing 'x-request-id' header for request " + request.getRequestURI());
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}


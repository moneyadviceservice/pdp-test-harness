package uk.org.ca.stub.simulator.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.org.ca.stub.simulator.rest.api.IntrospectApi;
import uk.org.ca.stub.simulator.rest.model.IntrospectBody;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResult;
import uk.org.ca.stub.simulator.service.IntrospectService;

import java.util.UUID;

import static uk.org.ca.stub.simulator.configuration.OpenApiConfig.PAT_SECURITY_SCHEMA;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
public class IntrospectApiController extends AbstractApiController implements IntrospectApi {

    private final IntrospectService introspectService;

    public IntrospectApiController(IntrospectService introspectService) {
        this.introspectService = introspectService;
    }

    @Override
    @Operation(security = @SecurityRequirement(name = PAT_SECURITY_SCHEMA))
    public ResponseEntity<IntrospectionResult> postIntrospectionResultsRequest(UUID xRequestID, IntrospectBody introspectBody, HttpServletRequest request) {
        String pat = getTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        String rpt = introspectBody.getToken();

        ResponseEntity asssertedResponse = checkTokenForAssertionsForIntrospect(rpt);
        if (asssertedResponse != null) {
            return asssertedResponse;
        }

        IntrospectionResult introspectionResult = introspectService.introspectToken(rpt, pat);
        return ResponseEntity.ok().body(introspectionResult);
    }

}

package uk.org.ca.stub.simulator.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.org.ca.stub.simulator.rest.api.PermApi;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.model.InlineResponse201;
import uk.org.ca.stub.simulator.rest.model.PermBody;
import uk.org.ca.stub.simulator.service.PermService;

import java.util.UUID;

import static uk.org.ca.stub.simulator.configuration.OpenApiConfig.PAT_SECURITY_SCHEMA;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
public class PermApiController implements PermApi {

    private final PermService permService;

    public PermApiController(PermService permService) {
        this.permService = permService;
    }

    @Override
    @Operation(security = @SecurityRequirement(name = PAT_SECURITY_SCHEMA))
    public ResponseEntity<InlineResponse201> postPerm(UUID xRequestID, PermBody permBody, HttpServletRequest request) {
        try {
            var ticket = permService.generatePMT(permBody, xRequestID, request.getHeader(HttpHeaders.AUTHORIZATION));
            return new ResponseEntity<>(new InlineResponse201(ticket), HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException("PMT generation failed", e);
        }
    }
}

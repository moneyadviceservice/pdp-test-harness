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
import org.springframework.web.server.ServerErrorException;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.rest.api.RreguriApi;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.model.*;
import uk.org.ca.stub.simulator.service.RegisterService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import uk.org.ca.stub.simulator.utils.MatchStatusEnum;
import static uk.org.ca.stub.simulator.configuration.OpenApiConfig.PAT_SECURITY_SCHEMA;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
public class RreguriApiController extends AbstractApiController implements RreguriApi {

    private final RegisterService registerService;

    public RreguriApiController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    @Operation(security = @SecurityRequirement(name = PAT_SECURITY_SCHEMA))
    public ResponseEntity<Void> deleteRegisteredPeisId(UUID xRequestID, String deletionReason, String resourceId, HttpServletRequest request) {
        String pat = getTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        ResponseEntity asssertedResponse = checkTokenForAssertionsForRreguriDelete(pat);
        if (asssertedResponse != null) {
            return asssertedResponse;
        }

        return new ResponseEntity<>(registerService.deleteFind(resourceId, deletionReason, pat), HttpStatus.NO_CONTENT);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = PAT_SECURITY_SCHEMA))
    public ResponseEntity<RegisteredPei> getRegisteredPeisResourceId(UUID xRequestID, String resourceId, HttpServletRequest request) {
        String pat = getTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        var registeredPeisResourceId = registerService.getRegisteredPeisResourceId(resourceId, pat);
        return new ResponseEntity<>(transformToDto(registeredPeisResourceId), HttpStatus.OK);
    }

    private RegisteredPei transformToDto(RegisteredResource resource) {
        return new RegisteredPei(resource.getResourceId(),
                resource.getName(),
                resource.getDescription(),
                MatchStatusEnum.fromValue(resource.getMatchStatus()),
                resource.getResourceScopes().stream().map(s -> RegisteredPei.ResourceScopesEnum.fromValue(s.toString().toLowerCase())).toList()
        );
    }

    @Override
    @Operation(security = @SecurityRequirement(name = PAT_SECURITY_SCHEMA))
    public ResponseEntity<PatchRegisteredPeisId200Response> patchRegisteredPeisId(UUID xRequestID, String resourceId, RreguriResourceIdBody rreguriResourceIdBody, HttpServletRequest request) {
        String pat = getTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        ResponseEntity asssertedResponse = checkTokenForAssertionsForRreguriPatch(pat);
        if (asssertedResponse != null) {
            return asssertedResponse;
        }

        var updatedFind = registerService.updateStatus(resourceId, rreguriResourceIdBody.getMatchStatus(), pat);
        return new ResponseEntity<>(new PatchRegisteredPeisId200Response(updatedFind.resourceId().toString()), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = PAT_SECURITY_SCHEMA))
    public ResponseEntity<PostRegisteredPeis201Response> postRegisteredPeis(UUID xRequestID, RreguriBody rreguriBody, HttpServletRequest request) {
        String pat = getTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        ResponseEntity asssertedResponse = checkTokenForAssertionsForRreguriPost(pat);
        if (asssertedResponse != null) {
            return asssertedResponse;
        }

        try {
            var upsertedFind = registerService.upsertFind(rreguriBody, xRequestID, pat);
            var location = request.getRequestURL().toString() + "/" + upsertedFind.resourceId();

            if (Boolean.TRUE.equals(upsertedFind.alreadyRegistered())) {
                return ResponseEntity.ok().header(HttpHeaders.LOCATION, location).body(new PostRegisteredPeis201Response(upsertedFind.resourceId()));
            } else {
                return ResponseEntity.created(new URI(location)).header(HttpHeaders.LOCATION, location).body(new PostRegisteredPeis201Response(upsertedFind.resourceId()));
            }
        } catch (URISyntaxException e) {
            throw new InvalidRequestException("Cannot build location header ", e);
        } catch (JsonProcessingException e) {
            throw new ServerErrorException("Error generating the RPT token", e);
        }
    }
}

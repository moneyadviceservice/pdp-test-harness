package uk.org.ca.stub.simulator.rest.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import uk.org.ca.stub.simulator.rest.model.JwkSetsResponse;
import static uk.org.ca.stub.simulator.utils.Commons.APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE;

@Validated
@Tag(name = "jwks-requests", description = "Retrieve a JSON Web Key Set")
public interface JwksApi {

    @Operation(
        operationId = "getJwks",
        summary = "Retrieve a JSON Web Key Set",
        description = "Retrieve a JSON Web Key Set.",
        tags = { "jwks-requests" })
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "200", description = "Created", content = {
                @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway"),
            @ApiResponse(responseCode = "503", description = "Service Unavailable"),
            @ApiResponse(responseCode = "504", description = "Gateway Timeout")
        }
    )


    @GetMapping(
        path = "/jwk_uri",
        produces = APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE
    )
    default ResponseEntity<JwkSetsResponse> getJwks(HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}

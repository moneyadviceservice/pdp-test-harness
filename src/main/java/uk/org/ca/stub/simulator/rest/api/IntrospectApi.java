package uk.org.ca.stub.simulator.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.ca.stub.simulator.rest.model.IntrospectBody;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResult;

import java.util.UUID;

import static uk.org.ca.stub.simulator.utils.Commons.APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE;
import static uk.org.ca.stub.simulator.utils.Commons.APPLICATION_X_WWW_FORM_UTF_8_MEDIA_TYPE;

@Validated
@Tag(name = "introspect", description = "The introspect API")
public interface IntrospectApi {

    @Operation(
            operationId = "postIntrospectionResultsRequest",
            summary = "Introspect a provided token",
            description = "Create an introspection result base on provided RPT under the authority of the bearer PAT")
    @RequestBody(
            description = "Swagger does not interpret encoded form request body parameters properly. Replace { \"token\": \"string\"} with token=\"val\"",
            content =  @Content(mediaType = APPLICATION_X_WWW_FORM_UTF_8_MEDIA_TYPE)
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = IntrospectionResult.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, returned if the provided PAT does not allow access to the account referenced by the RPT or the PAT is expired."),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error"),
                    @ApiResponse(responseCode = "502", description = "Bad Gateway"),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable"),
                    @ApiResponse(responseCode = "504", description = "Gateway Timeout")
            }
    )
    @PostMapping(
            path = "/introspect",
            produces = APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE ,
            consumes = APPLICATION_X_WWW_FORM_UTF_8_MEDIA_TYPE
    )
    default ResponseEntity<IntrospectionResult> postIntrospectionResultsRequest(
            @NotNull @Parameter(name = "X-Request-ID", description = "A unique correlation id", required = true, in = ParameterIn.HEADER)
            @RequestHeader(value = "X-Request-ID") UUID xRequestID,
            @Valid @RequestBody IntrospectBody introspectBody,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}

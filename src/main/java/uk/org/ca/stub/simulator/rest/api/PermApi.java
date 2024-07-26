package uk.org.ca.stub.simulator.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.ca.stub.simulator.rest.model.InlineResponse201;
import uk.org.ca.stub.simulator.rest.model.PermBody;

import java.util.UUID;

import static uk.org.ca.stub.simulator.utils.Commons.APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE;

@Validated
@Tag(name = "permission-requests", description = "the permission-requests API")
public interface PermApi {

    @Operation(
        operationId = "postPerm",
        summary = "Retrieve a newly minted permission token",
        description = "Create a new permission token for a registered resource.",
        tags = { "permission-requests" })
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = InlineResponse201.class))
            }),
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
    @PostMapping(
        path = "/perm",
        produces = APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE,
        consumes = APPLICATION_JSON_UTF8_VALUE_MEDIA_TYPE
    )

    default ResponseEntity<InlineResponse201> postPerm(
        @NotNull @Parameter(name = "X-Request-ID", description = "A unique correlation id", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "X-Request-ID", required = true) UUID xRequestID,
        @Parameter(name = "PermBody", description = "Details of the permission being sort") @Valid
        @RequestBody(required = false) PermBody permBody,
        HttpServletRequest request
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}

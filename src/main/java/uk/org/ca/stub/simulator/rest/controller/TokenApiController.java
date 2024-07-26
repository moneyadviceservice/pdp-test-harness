package uk.org.ca.stub.simulator.rest.controller;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.org.ca.stub.simulator.entity.User;
import uk.org.ca.stub.simulator.rest.api.TokenApi;
import uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken200Response;
import uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken400Response;
import uk.org.ca.stub.simulator.service.TokenService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.*;
import static uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken400Response.ErrorEnum.*;

enum GrantType {
    UMA_TICKET("urn:ietf:params:oauth:grant-type:uma-ticket"),
    JWT_BEARER("urn:ietf:params:oauth:grant-type:jwt-bearer");

    private final String value;

    GrantType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static GrantType fromValue(String value) {
        for (GrantType grantType : GrantType.values()) {
            if (grantType.value.equals(value)) {
                return grantType;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

enum TokenScope {
    OWNER("owner"),
    UMA_PROTECTION("uma_protection");

    private final String value;

    TokenScope(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TokenScope fromValue(String value) {
        for (TokenScope tokenScope : TokenScope.values()) {
            if (tokenScope.value.equals(value)) {
                return tokenScope;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
public class TokenApiController implements TokenApi {

    private final TokenService tokenService;

    public TokenApiController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /*
        Different users with different UATs to reproduce different scenarios
            200 -> any `NOT_EXPIRED_TOKEN_UAT` stored in database
            expired -> use UMA `EXPIRED_TOKEN_UAT` stored in database
            400 invalid_request -> use `ASSERTION_FOR_400_INVALID_REQUEST` NOT stored in database
            400 invalid_grant -> use `ASSERTION_FOR_400_INVALID_GRANT` NOT stored in database
            400 unauthorised_client -> use `ASSERTION_FOR_400_UNAUTHORISED_CLIENT` NOT stored in database
            401 -> Use `ASSERTION_FOR_401` NOT stored in database
            403 -> Use `ASSERTION_FOR_403` NOT stored in database
            500 -> Use `ASSERTION_FOR_500` NOT stored in database
            502 -> Use `ASSERTION_FOR_502` NOT stored in database
            503 -> Use `ASSERTION_FOR_503` NOT stored in database
            504 -> Use `ASSERTION_FOR_504` NOT stored in database
        UAT - real JWT token with a 3 years  expiry date
        For negative scenarios - set a expiry date in the past for UAT
        PAT - real JWT  with a 3 years  expiry date (prod one 18 months
     */
    @Override
    public ResponseEntity<?> retrieveUmaToken(String grantType,
                                              UUID xRequestID,
                                              String ticket,
                                              String claimToken,
                                              String claimTokenFormat,
                                              String assertion,
                                              String scope) {
        // Open API does not specify assertion as required but it needs to be provided to retrieve the PAT
        if (assertion == null || assertion.isBlank()) {
            return new ResponseEntity<>(transformErrorToDto(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
        }

        var returnAssertion = switch (assertion) {
            case ASSERTION_FOR_400_INVALID_REQUEST ->
                    new ResponseEntity<>(transformErrorToDto(INVALID_REQUEST), HttpStatus.BAD_REQUEST);
            case ASSERTION_FOR_400_INVALID_GRANT ->
                    new ResponseEntity<>(transformErrorToDto(INVALID_GRANT), HttpStatus.BAD_REQUEST);
            case ASSERTION_FOR_400_UNAUTHORISED_CLIENT ->
                    new ResponseEntity<>(transformErrorToDto(UNAUTHORISED_CLIENT), HttpStatus.BAD_REQUEST);
            case ASSERTION_FOR_401 -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            case ASSERTION_FOR_403 -> new ResponseEntity<>(HttpStatus.FORBIDDEN);
            case ASSERTION_FOR_500 -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            case ASSERTION_FOR_502 -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            case ASSERTION_FOR_503 -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            case ASSERTION_FOR_504 -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };

        if (returnAssertion != null) {
            return returnAssertion;
        }

        try {
            GrantType gt = GrantType.fromValue(grantType);
            if (!(gt.equals(GrantType.JWT_BEARER))) {
                return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(transformErrorToDto(UNSUPPORTED_GRANT_TYPE), HttpStatus.BAD_REQUEST);
        }

        if (!(scope == null || scope.isBlank())) {
            try {
                TokenScope ts = TokenScope.fromValue(scope);
                if (!ts.equals(TokenScope.UMA_PROTECTION)) {
                    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
                }
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(transformErrorToDto(INVALID_SCOPE), HttpStatus.BAD_REQUEST);
            }
        }

        var userAccessToken = URLDecoder.decode(assertion, StandardCharsets.UTF_8);

        return new ResponseEntity<>(transformResponseToDto(tokenService.retrievePAT(userAccessToken)), HttpStatus.OK);
    }

    private RetrieveUmaToken200Response transformResponseToDto(User user) {
        return new RetrieveUmaToken200Response(user.getPat(), RetrieveUmaToken200Response.TokenTypeEnum.PAT);
    }

    private RetrieveUmaToken400Response transformErrorToDto(RetrieveUmaToken400Response.ErrorEnum reason) {
        RetrieveUmaToken400Response error = new RetrieveUmaToken400Response();
        error.setError(reason);
        return error;
    }

}

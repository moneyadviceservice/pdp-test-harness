package uk.org.ca.stub.simulator.rest.controller;

import org.springframework.http.*;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken400Response;

import java.util.random.RandomGenerator;

import static uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken400Response.ErrorEnum.INVALID_GRANT;
import static uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken400Response.ErrorEnum.INVALID_REQUEST;
import static uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken400Response.ErrorEnum.UNAUTHORISED_CLIENT;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

public abstract class AbstractApiController {
    protected static String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.toUpperCase().startsWith("BEARER ")) {
            throw new InvalidRequestException("Bearer token required");
        }
        return authorizationHeader.substring("Bearer ".length());
    }

    protected static ResponseEntity checkTokenForAssertionsForToken(String uat){
        if (uat == null || uat.isEmpty()){
            return null;
        }
        return switch (uat) {
            // 400
            case ASSERTION_UAT_FOR_400_INVALID_REQUEST ->
                    new ResponseEntity<>(transformErrorToDto(INVALID_REQUEST),HttpStatus.BAD_REQUEST);
            case ASSERTION_UAT_FOR_400_INVALID_GRANT ->
                    new ResponseEntity<>(transformErrorToDto(INVALID_GRANT),HttpStatus.BAD_REQUEST);
            case ASSERTION_UAT_FOR_400_UNAUTHORISED_CLIENT ->
                    new ResponseEntity<>(transformErrorToDto(UNAUTHORISED_CLIENT),HttpStatus.BAD_REQUEST);
            case ASSERTION_UAT_TOKEN_400_INCORRECT_SIGNATURE -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            // 401
            case ASSERTION_UAT_TOKEN_401_EXPIRED_PAT -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 429
            case ASSERTION_UAT_TOKEN_429_TOO_MANY_REQUESTS -> new ResponseEntity<>(getRetryAfterHeader(),HttpStatus.TOO_MANY_REQUESTS);
            // 500
            case ASSERTION_UAT_TOKEN_500_SERVER_ERROR -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // 502
            case ASSERTION_UAT_TOKEN_502_BAD_GATEWAY -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // 503
            case ASSERTION_UAT_TOKEN_503_SERVICE_UNAVAILABLE -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            // 504
            case ASSERTION_UAT_TOKEN_504_GATEWAY_TIMEOUT -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };
    }

    protected static ResponseEntity checkTokenForAssertionsForRreguriPost(String pat){
        if (pat == null || pat.isEmpty()){
            return null;
        }
        return switch (pat) {
            // 401
            case ASSERTION_PAT_RREGURI_POST_401_EXPIRED_PAT -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 404
            case ASSERTION_PAT_RREGURI_POST_404_USER_REMOVED -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // 429
            case ASSERTION_PAT_RREGURI_POST_429_TOO_MANY_REQUESTS -> new ResponseEntity<>(getRetryAfterHeader(),HttpStatus.TOO_MANY_REQUESTS);
            // 500
            case ASSERTION_PAT_RREGURI_POST_500_SERVER_ERROR -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // 502
            case ASSERTION_PAT_RREGURI_POST_502_BAD_GATEWAY -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // 503
            case ASSERTION_PAT_RREGURI_POST_503_SERVICE_UNAVAILABLE -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            // 504
            case ASSERTION_PAT_RREGURI_POST_504_GATEWAY_TIMEOUT -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };
    }

    protected static ResponseEntity checkTokenForAssertionsForRreguriPatch(String pat){
        if (pat == null || pat.isEmpty()){
            return null;
        }
        return switch (pat) {
            // 401
            case ASSERTION_PAT_RREGURI_PATCH_401_EXPIRED_PAT -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 404
            case ASSERTION_PAT_RREGURI_PATCH_404_RESOURCE_NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // 429
            case ASSERTION_PAT_RREGURI_PATCH_429_TOO_MANY_REQUESTS -> new ResponseEntity<>(getRetryAfterHeader(),HttpStatus.TOO_MANY_REQUESTS);
            // 500
            case ASSERTION_PAT_RREGURI_PATCH_500_SERVER_ERROR -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // 502
            case ASSERTION_PAT_RREGURI_PATCH_502_BAD_GATEWAY -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // 503
            case ASSERTION_PAT_RREGURI_PATCH_503_SERVICE_UNAVAILABLE -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            // 504
            case ASSERTION_PAT_RREGURI_PATCH_504_GATEWAY_TIMEOUT -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };
    }

    protected static ResponseEntity checkTokenForAssertionsForRreguriDelete(String pat){
        if (pat == null || pat.isEmpty()){
            return null;
        }
        return switch (pat) {
            // 401
            case ASSERTION_PAT_RREGURI_DELETE_401_EXPIRED_PAT -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 429
            case ASSERTION_PAT_RREGURI_DELETE_429_TOO_MANY_REQUESTS -> new ResponseEntity<>(getRetryAfterHeader(),HttpStatus.TOO_MANY_REQUESTS);
            // 500
            case ASSERTION_PAT_RREGURI_DELETE_500_SERVER_ERROR -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // 502
            case ASSERTION_PAT_RREGURI_DELETE_502_BAD_GATEWAY -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // 503
            case ASSERTION_PAT_RREGURI_DELETE_503_SERVICE_UNAVAILABLE -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            // 504
            case ASSERTION_PAT_RREGURI_DELETE_504_GATEWAY_TIMEOUT -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };
    }

    protected static ResponseEntity checkTokenForAssertionsForIntrospect(String rpt){
        if (rpt == null || rpt.isEmpty()){
            return null;
        }
        return switch (rpt) {
            // 400
            case ASSERTION_RPT_INTROSPECT_400_INCORRECT_SIGNATURE -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            // 401
            case ASSERTION_RPT_INTROSPECT_401_EXPIRED_PAT -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 404
            case ASSERTION_RPT_INTROSPECT_404_RESOURCE_NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // 500
            case ASSERTION_RPT_INTROSPECT_500_SERVER_ERROR -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // 502
            case ASSERTION_RPT_INTROSPECT_502_BAD_GATEWAY -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // 503
            case ASSERTION_RPT_INTROSPECT_503_SERVICE_UNAVAILABLE -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            // 504
            case ASSERTION_RPT_INTROSPECT_504_GATEWAY_TIMEOUT -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };
    }

    protected static ResponseEntity checkTokenForAssertionsForPerm(String pat){
        if (pat == null || pat.isEmpty()){
            return null;
        }
        return switch (pat) {
            // 401
            case ASSERTION_PAT_PERM_401_EXPIRED_PAT -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 404
            case ASSERTION_PAT_PERM_404_RESOURCE_NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // 500
            case ASSERTION_PAT_PERM_500_SERVER_ERROR -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // 502
            case ASSERTION_PAT_PERM_502_BAD_GATEWAY -> new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // 503
            case ASSERTION_PAT_PERM_503_SERVICE_UNAVAILABLE -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            // 504
            case ASSERTION_PAT_PERM_504_GATEWAY_TIMEOUT -> new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);

            default -> null;
        };
    }

    protected static RetrieveUmaToken400Response transformErrorToDto(RetrieveUmaToken400Response.ErrorEnum reason) {
        var error = new RetrieveUmaToken400Response();
        error.setError(reason);
        return error;
    }

    protected static HttpHeaders getRetryAfterHeader() {
        RandomGenerator random = RandomGenerator.of("Random");
        HttpHeaders headersWithRetryAfter = new HttpHeaders();
        headersWithRetryAfter.set(HttpHeaders.RETRY_AFTER, String.valueOf(random.nextInt(2, 11))); // random int between 2 and 10
        return headersWithRetryAfter;
    }
}

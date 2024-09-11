package uk.org.ca.stub.simulator.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

public class AbstractApiControllerTest {


    @Test
    void testGetTokenFromHeader() {
        assertAll("Bearer head scenarios",
                () -> assertThrows(InvalidRequestException.class, () -> AbstractApiController.getTokenFromHeader(null)),
                () -> assertThrows(InvalidRequestException.class, () -> AbstractApiController.getTokenFromHeader("not-valid")),
                () -> assertThrows(InvalidRequestException.class, () -> AbstractApiController.getTokenFromHeader("bearer-not-valid")),
                () -> assertDoesNotThrow( () -> AbstractApiController.getTokenFromHeader("bearer this-is-valid")),
                () -> assertEquals("1234567890",   AbstractApiController.getTokenFromHeader("bearer 1234567890"))
        );
    }

    @Test
    void testCheckTokenForAssertionsForToken() {
        assertAll("Assertion scenarios",
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForToken(null)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForToken(HAPPY_PATH_PAT_01)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForToken("invalid-pat")),
                () -> assertEquals(HttpStatus.BAD_REQUEST,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_FOR_400_INVALID_REQUEST).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_FOR_400_INVALID_GRANT).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_FOR_400_UNAUTHORISED_CLIENT).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_400_INCORRECT_SIGNATURE).getStatusCode()),
                () -> assertEquals(HttpStatus.UNAUTHORIZED,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_401_EXPIRED_PAT).getStatusCode()),
                () -> assertEquals(HttpStatus.TOO_MANY_REQUESTS,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_429_TOO_MANY_REQUESTS).getStatusCode()),
                () -> assertTrue(AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_429_TOO_MANY_REQUESTS).getHeaders().containsKey(HttpHeaders.RETRY_AFTER)),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_500_SERVER_ERROR).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_502_BAD_GATEWAY).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_503_SERVICE_UNAVAILABLE).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,AbstractApiController.checkTokenForAssertionsForToken(ASSERTION_UAT_TOKEN_504_GATEWAY_TIMEOUT).getStatusCode())
        );
    }

    @Test
    void testCheckTokenForAssertionsForRreguriPost() {
        assertAll("Assertion scenarios",
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriPost(null)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriPost(HAPPY_PATH_PAT_01)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriPost("invalid-pat")),
                () -> assertEquals(HttpStatus.UNAUTHORIZED,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_401_EXPIRED_PAT).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_FOUND,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_404_USER_REMOVED).getStatusCode()),
                () -> assertEquals(HttpStatus.TOO_MANY_REQUESTS,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_429_TOO_MANY_REQUESTS).getStatusCode()),
                () -> assertTrue(AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_429_TOO_MANY_REQUESTS).getHeaders().containsKey(HttpHeaders.RETRY_AFTER)),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_500_SERVER_ERROR).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_502_BAD_GATEWAY).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_503_SERVICE_UNAVAILABLE).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,AbstractApiController.checkTokenForAssertionsForRreguriPost(ASSERTION_PAT_RREGURI_POST_504_GATEWAY_TIMEOUT).getStatusCode())
        );
    }

    @Test
    void testCheckTokenForAssertionsForRreguriPatch() {
        assertAll("Assertion scenarios",
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriPatch(null)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriPatch(HAPPY_PATH_PAT_01)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriPatch("invalid-pat")),
                () -> assertEquals(HttpStatus.UNAUTHORIZED,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_401_EXPIRED_PAT).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_FOUND,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_404_RESOURCE_NOT_FOUND).getStatusCode()),
                () -> assertEquals(HttpStatus.TOO_MANY_REQUESTS,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_429_TOO_MANY_REQUESTS).getStatusCode()),
                () -> assertTrue(AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_429_TOO_MANY_REQUESTS).getHeaders().containsKey(HttpHeaders.RETRY_AFTER)),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_500_SERVER_ERROR).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_502_BAD_GATEWAY).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_503_SERVICE_UNAVAILABLE).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,AbstractApiController.checkTokenForAssertionsForRreguriPatch(ASSERTION_PAT_RREGURI_PATCH_504_GATEWAY_TIMEOUT).getStatusCode())
        );
    }

    @Test
    void testCheckTokenForAssertionsForRreguriDelete() {
        assertAll("Assertion scenarios",
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriDelete(null)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriDelete(HAPPY_PATH_PAT_01)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForRreguriDelete("invalid-pat")),
                () -> assertEquals(HttpStatus.UNAUTHORIZED,AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_401_EXPIRED_PAT).getStatusCode()),
                () -> assertEquals(HttpStatus.TOO_MANY_REQUESTS,AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_429_TOO_MANY_REQUESTS).getStatusCode()),
                () -> assertTrue(AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_429_TOO_MANY_REQUESTS).getHeaders().containsKey(HttpHeaders.RETRY_AFTER)),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_500_SERVER_ERROR).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_502_BAD_GATEWAY).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_503_SERVICE_UNAVAILABLE).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,AbstractApiController.checkTokenForAssertionsForRreguriDelete(ASSERTION_PAT_RREGURI_DELETE_504_GATEWAY_TIMEOUT).getStatusCode())
        );
    }

    @Test
    void testCheckTokenForAssertionsForIntrospect() {
        assertAll("Assertion scenarios",
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForIntrospect(null)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForIntrospect(HAPPY_PATH_PAT_01)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForIntrospect("invalid-pat")),
                () -> assertEquals(HttpStatus.BAD_REQUEST,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_400_INCORRECT_SIGNATURE).getStatusCode()),
                () -> assertEquals(HttpStatus.UNAUTHORIZED,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_401_EXPIRED_PAT).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_FOUND,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_404_RESOURCE_NOT_FOUND).getStatusCode()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_500_SERVER_ERROR).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_502_BAD_GATEWAY).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_503_SERVICE_UNAVAILABLE).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,AbstractApiController.checkTokenForAssertionsForIntrospect(ASSERTION_RPT_INTROSPECT_504_GATEWAY_TIMEOUT).getStatusCode())
        );
    }

    @Test
    void testCheckTokenForAssertionsForPerm() {
        assertAll("Assertion scenarios",
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForPerm(null)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForPerm(HAPPY_PATH_PAT_01)),
                () -> assertEquals(null,AbstractApiController.checkTokenForAssertionsForPerm("invalid-pat")),
                () -> assertEquals(HttpStatus.UNAUTHORIZED,AbstractApiController.checkTokenForAssertionsForPerm(ASSERTION_PAT_PERM_401_EXPIRED_PAT).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_FOUND,AbstractApiController.checkTokenForAssertionsForPerm(ASSERTION_PAT_PERM_404_RESOURCE_NOT_FOUND).getStatusCode()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,AbstractApiController.checkTokenForAssertionsForPerm(ASSERTION_PAT_PERM_500_SERVER_ERROR).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,AbstractApiController.checkTokenForAssertionsForPerm(ASSERTION_PAT_PERM_502_BAD_GATEWAY).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,AbstractApiController.checkTokenForAssertionsForPerm(ASSERTION_PAT_PERM_503_SERVICE_UNAVAILABLE).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,AbstractApiController.checkTokenForAssertionsForPerm(ASSERTION_PAT_PERM_504_GATEWAY_TIMEOUT).getStatusCode())
        );
    }
}

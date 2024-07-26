package uk.org.ca.stub.simulator.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer;
import uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken200Response;

import uk.org.ca.stub.simulator.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;

class TokenApiControllerTest extends AbstractControllerTest {

    @Autowired
    JwtService jwtService;

    @Test
    void testsEndpoint() {
        assertNotNull(restTemplate);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, restTemplate.getForEntity("http://localhost:" + port + "/token", String.class).getStatusCode());
    }

    @Test
    void testTokenScenarios() {
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_400_INVALID_REQUEST), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_400_INVALID_GRANT), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_400_UNAUTHORISED_CLIENT), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, restTemplate.postForEntity("http://localhost:" + port + "/token", createPostTokenRequestWithAssertionGrantAndScope(UserDbInitializer.NOT_EXPIRED_TOKEN_UAT,"illegal grant type","uma_protection"), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, restTemplate.postForEntity("http://localhost:" + port + "/token", createPostTokenRequestWithAssertionGrantAndScope(UserDbInitializer.NOT_EXPIRED_TOKEN_UAT,"urn:ietf:params:oauth:grant-type:jwt-bearer","illegal scope"), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.UNAUTHORIZED, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_401), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.FORBIDDEN, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_403), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_500), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_IMPLEMENTED, restTemplate.postForEntity("http://localhost:" + port + "/token", createPostTokenRequestWithAssertionGrantAndScope(UserDbInitializer.NOT_EXPIRED_TOKEN_UAT,"urn:ietf:params:oauth:grant-type:uma-ticket","uma_protection"), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_IMPLEMENTED, restTemplate.postForEntity("http://localhost:" + port + "/token", createPostTokenRequestWithAssertionGrantAndScope(UserDbInitializer.NOT_EXPIRED_TOKEN_UAT,"urn:ietf:params:oauth:grant-type:jwt-bearer","owner"), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_502), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_503), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT, restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.ASSERTION_FOR_504), String.class).getStatusCode()),
                () -> {
                    ResponseEntity<RetrieveUmaToken200Response> response = restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.NOT_EXPIRED_TOKEN_UAT), RetrieveUmaToken200Response.class);
                    assertFalse(jwtService.isTokenExpired(response.getBody().getAccessToken()));
                    assertEquals(RetrieveUmaToken200Response.TokenTypeEnum.PAT, response.getBody().getTokenType());
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                },
                () -> {
                    ResponseEntity<RetrieveUmaToken200Response> response = restTemplate.postForEntity("http://localhost:" + port + "/token", createValidPostTokenRequestWithAssertion(UserDbInitializer.EXPIRED_TOKEN_UAT), RetrieveUmaToken200Response.class);
                    assertTrue(jwtService.isTokenExpired(response.getBody().getAccessToken()));
                    assertEquals(RetrieveUmaToken200Response.TokenTypeEnum.PAT, response.getBody().getTokenType());
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                }
        );
    }

    private static HttpEntity<MultiValueMap<String, String>> createValidPostTokenRequestWithAssertion(String assertion) {
        return createPostTokenRequestWithAssertionGrantAndScope(assertion,"urn:ietf:params:oauth:grant-type:jwt-bearer","uma_protection");
    }

    private static HttpEntity<MultiValueMap<String, String>> createPostTokenRequestWithAssertionGrantAndScope(String assertion, String grant, String scope) {
        var dummyJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-Request-ID", "04e83484-af80-478c-96aa-f4c6ee58f427");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("assertion", assertion);
        map.add("grant_type", grant);
        map.add("ticket", dummyJwt);
        map.add("claim_token", dummyJwt);
        map.add("claim_token_format", "pension_dashboad_pat"); // yes, there is a typo, copied from original
        map.add("scope", scope);
        return new HttpEntity<>(map, headers);
    }
}

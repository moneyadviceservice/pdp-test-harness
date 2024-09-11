package uk.org.ca.stub.simulator.rest.controller;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.org.ca.stub.simulator.rest.model.RetrieveUmaToken200Response;

import uk.org.ca.stub.simulator.service.JwtService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.rest.controller.TokenScope.OWNER;
import static uk.org.ca.stub.simulator.rest.controller.TokenScope.UMA_PROTECTION;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

class TokenApiControllerTest extends AbstractControllerTest {

    public static final String GRANT_TYPE_BEARER = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    public static final String GRANT_TYPE_TICKET = "urn:ietf:params:oauth:grant-type:uma-ticket";

    @Autowired
    JwtService jwtService;

    private String endpoint;

    @BeforeEach
    void setUpEndpoint() {
        endpoint = "https://localhost:" + port + "/token";
    }

    @Test
    void testsEndpoint() {
        assertNotNull(restTemplate);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, restTemplate.getForEntity(endpoint, String.class).getStatusCode());
    }

    @Test
    @Description("Positive test scenarios")
    void shouldReturn200() {
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint,
                            createValidPostTokenRequestWithAssertion(NOT_EXPIRED_TOKEN_UAT), RetrieveUmaToken200Response.class);
                    assertFalse(jwtService.isTokenExpired(Objects.requireNonNull(response.getBody()).getAccessToken()));
                    assertEquals(RetrieveUmaToken200Response.TokenTypeEnum.PAT, response.getBody().getTokenType());
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint,
                            createValidPostTokenRequestWithAssertion(EXPIRED_TOKEN_UAT), RetrieveUmaToken200Response.class);
                    assertTrue(jwtService.isTokenExpired(Objects.requireNonNull(response.getBody()).getAccessToken()));
                    assertEquals(RetrieveUmaToken200Response.TokenTypeEnum.PAT, response.getBody().getTokenType());
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("All possible bad requests (400)")
    void shouldReturn400(){
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(""),
                                String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(null),
                                String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion("some random bad uat"),
                                String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createPostTokenRequestWithAssertionGrantAndScope(NOT_EXPIRED_TOKEN_UAT,
                                        "", UMA_PROTECTION.getValue()), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createPostTokenRequestWithAssertionGrantAndScope(NOT_EXPIRED_TOKEN_UAT,
                                        null, UMA_PROTECTION.getValue()), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createPostTokenRequestWithAssertionGrantAndScope(NOT_EXPIRED_TOKEN_UAT,
                                        "illegal grant type", UMA_PROTECTION.getValue()), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint, createPostTokenRequestWithAssertionGrantAndScope(NOT_EXPIRED_TOKEN_UAT,
                                "urn:ietf:params:oauth:grant-type:jwt-bearer",
                                "illegal scope"), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_FOR_400_INVALID_REQUEST),
                                String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_FOR_400_INVALID_GRANT), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_FOR_400_UNAUTHORISED_CLIENT), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_400_INCORRECT_SIGNATURE), String.class).getStatusCode())
        );
    }

    @Test
    @Description("401,429")
    void shouldReturn40X() {
        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_401_EXPIRED_PAT), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.TOO_MANY_REQUESTS,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_429_TOO_MANY_REQUESTS), String.class).getStatusCode()),
                () -> assertTrue(restTemplate.postForEntity(endpoint,
                        createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_429_TOO_MANY_REQUESTS), String.class).getHeaders().containsKey(HttpHeaders.RETRY_AFTER))
                );
    }

    @Test
    @Description("500,501,503,504")
    void shouldReturn50X() {
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_500_SERVER_ERROR), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_IMPLEMENTED,
                        restTemplate.postForEntity(endpoint,
                                createPostTokenRequestWithAssertionGrantAndScope(NOT_EXPIRED_TOKEN_UAT,GRANT_TYPE_TICKET,UMA_PROTECTION.getValue()), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.NOT_IMPLEMENTED,
                        restTemplate.postForEntity(endpoint,
                                createPostTokenRequestWithAssertionGrantAndScope(NOT_EXPIRED_TOKEN_UAT,GRANT_TYPE_BEARER,OWNER.getValue()), String.class).getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_GATEWAY,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_502_BAD_GATEWAY), String.class).getStatusCode()),
                () -> {
                    var res = restTemplate.postForEntity(endpoint,
                            createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_503_SERVICE_UNAVAILABLE), String.class);
                    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, res.getStatusCode());
                },
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT,
                        restTemplate.postForEntity(endpoint,
                                createValidPostTokenRequestWithAssertion(ASSERTION_UAT_TOKEN_504_GATEWAY_TIMEOUT), String.class).getStatusCode())
        );
    }

    private static HttpEntity<MultiValueMap<String, String>> createValidPostTokenRequestWithAssertion(String assertion) {
        return createPostTokenRequestWithAssertionGrantAndScope(assertion,GRANT_TYPE_BEARER,UMA_PROTECTION.getValue());
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

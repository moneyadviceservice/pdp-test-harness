package uk.org.ca.stub.simulator.rest.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.org.ca.stub.simulator.service.IntrospectService;
import uk.org.ca.stub.simulator.service.JwtService;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer.*;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.TOKEN_WITH_INVALID_SIGNATURE;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.*;
import static uk.org.ca.stub.simulator.utils.Commons.TOKEN;
import static uk.org.ca.stub.simulator.utils.Commons.X_REQUEST_ID;


public class IntrospectApiControllerTest extends AbstractControllerTest {

    @Autowired
    JwtService jwtService;

    @Autowired
    IntrospectService introspectService;

    private static MultiValueMap<String, String> buildFormForToken(String token) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(TOKEN, token);
        return formData;
    }

    private static HttpHeaders getHttpHeadersWithAuthentication() {
        var headers = new HttpHeaders();
        headers.set(X_REQUEST_ID, String.valueOf(UUID.randomUUID()));
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, VALID_AUTHORIZATION_HEADER);
        return headers;
    }

    @BeforeEach
    void setUp() {
        // warranty all test use the happy path scenario by default
        introspectService.setPatStoredValidator(ALWAYS_STORED);
        introspectService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
    }

    @Test
    void allTests() {
        assertNotNull(restTemplate);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, restTemplate.getForEntity("http://localhost:" + port + "/introspect", String.class).getStatusCode());
    }

    @Test
    void postRequest() throws Exception {
        var headers = getHttpHeadersWithAuthentication();

        MultiValueMap<String, String> formData = buildFormForToken(VALID_RPT_TOKEN);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/introspect",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var mapper = new JsonMapper();
        var responseContent = mapper.readValue(response.getBody(), Map.class);
        var active = responseContent.get("active").toString();
        var tokenType = responseContent.get("token_type").toString();
        var exp = responseContent.get("exp");
        var issuer = responseContent.get("iss").toString();
        var permissions = responseContent.get("permissions").toString();
        assertAll(
                () -> assertEquals("true",active),
                () -> assertEquals("pension_dashboard_rpt",tokenType),
                () -> assertEquals(Long.valueOf("1813411040"),((Integer) exp).longValue()),
                () -> assertEquals("https://stub-cas.co.uk",issuer),
                () -> assertEquals("[{resource_id=92476c2f-25b8-4d87-afde-18a9ee2631dc, resource_scopes=[owner], exp=1813411040}]",permissions)
        );
    }

    /**
     * In the following test with an expired token, we return 200 response with active flag = false
     */
    @Test ()
    void postRequestExpiredToken() throws Exception{
        var headers = getHttpHeadersWithAuthentication();

        MultiValueMap<String, String> formData = buildFormForToken(EXPIRED_RPT_TOKEN);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/introspect",
                HttpMethod.POST,
                request, String.class);


        var mapper = new JsonMapper();
        var responseContent = mapper.readValue(response.getBody(), Map.class);
        var active = responseContent.get("active").toString();
        var tokenType = responseContent.get("token_type").toString();
        var exp = responseContent.get("exp");
        var issuer = responseContent.get("iss").toString();
        var permissions = responseContent.get("permissions").toString();
        assertAll(
                () -> assertEquals("false",active),
                () -> assertEquals("pension_dashboard_rpt",tokenType),
                () -> assertEquals(Long.valueOf("1624197800"),((Integer) exp).longValue()),
                () -> assertEquals("https://stub-cas.co.uk",issuer),
                () -> assertEquals("[{resource_id=62933ca9-447e-4ce0-bb39-124e9fa3214f, resource_scopes=[owner], exp=1624197800}]",permissions)
        );
    }

    @Test
    void postRequestUnusedToken(){
        var headers = getHttpHeadersWithAuthentication();

        MultiValueMap<String, String> formData = buildFormForToken(UNUSED_RPT_TOKEN);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/introspect",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    @Test
    void postRequestMalformedToken(){
        var headers = getHttpHeadersWithAuthentication();

        MultiValueMap<String, String> formData = buildFormForToken("malformed-token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/introspect",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void postRequestInvalidSignatureToken(){
        var headers = getHttpHeadersWithAuthentication();

        MultiValueMap<String, String> formData = buildFormForToken(TOKEN_WITH_INVALID_SIGNATURE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/introspect",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void patNotStored() {
        introspectService.setPatStoredValidator(ALWAYS_NOT_STORED);
        introspectService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        var response = this.restClient.post()
                .uri("http://localhost:" + port + "/introspect")
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(buildFormForToken(VALID_RPT_TOKEN))
                .headers(h -> h.addAll(getHttpHeadersWithAuthentication()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, IGNORE_ERROR)
                .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void resourceNotAuthorizedForPat() {
        introspectService.setPatStoredValidator(ALWAYS_STORED);
        introspectService.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
        var response = this.restClient.post()
                .uri("http://localhost:" + port + "/introspect")
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(buildFormForToken(VALID_RPT_TOKEN))
                .headers(h -> h.addAll(getHttpHeadersWithAuthentication()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, IGNORE_ERROR)
                .toEntity(String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

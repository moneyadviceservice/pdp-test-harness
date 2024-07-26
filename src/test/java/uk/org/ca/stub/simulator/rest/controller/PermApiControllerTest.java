package uk.org.ca.stub.simulator.rest.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import uk.org.ca.stub.simulator.rest.model.PermBody;
import uk.org.ca.stub.simulator.service.AuthenticatedServiceTest;
import uk.org.ca.stub.simulator.service.JwtService;
import uk.org.ca.stub.simulator.service.PermService;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PermApiControllerTest extends AbstractControllerTest {

    @Autowired
    JwtService  jwtService;

    @Autowired
    PermService permService;

    @Test
    void allTests() {
        assertNotNull(restTemplate);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, restTemplate.getForEntity("http://localhost:" + port + "/perm", String.class).getStatusCode());
    }

    @Test
    void postRequest() throws Exception {
        permService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        permService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);
        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER);

        String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer

        var body = new PermBody();
        body.setResourceId(UUID.fromString(validResourceId));
        body.addResourceScopesItem(PermBody.ResourceScopesEnum.VALUE);
        body.addResourceScopesItem(PermBody.ResourceScopesEnum.OWNER);

        var request = new HttpEntity<>(body, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/perm",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtService.getSignature().getBytes(StandardCharsets.UTF_8)))
                .build();
        try {
            var mapper = new JsonMapper();
            var responseContent = mapper.readValue(response.getBody(), Map.class);
            var encodedPmt = responseContent.get("ticket").toString();
            var decodedPmt = Base64.getDecoder().decode(encodedPmt);
            var tokenMap = mapper.readValue(new String(decodedPmt), Map.class);
            assertEquals(JwtService.TokenInstance.PMT.getType(), tokenMap.get("token_type"));
            jwtParser.parse(tokenMap.get("access_token").toString());
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
    }

    @Test
    void postRequestResourceNotFound(){
        permService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        permService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);
        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER);

        var body = new PermBody();
        body.setResourceId(UUID.randomUUID());
        body.addResourceScopesItem(PermBody.ResourceScopesEnum.VALUE);
        body.addResourceScopesItem(PermBody.ResourceScopesEnum.OWNER);

        var request = new HttpEntity<>(body, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/perm",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    @Test
    void postRequestInvalidScopes(){
        permService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        permService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);

        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER);

        String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer

        var body = new PermBody();
        body.setResourceId(UUID.fromString(validResourceId));
        List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>(); // empty scopes list
        body.setResourceScopes(resourceScopes);

        var request = new HttpEntity<>(body, headers);

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/perm",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("size must be between 2 and 2"));
    }
}

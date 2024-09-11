package uk.org.ca.stub.simulator.rest.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import uk.org.ca.stub.simulator.rest.model.InlineResponse201;
import uk.org.ca.stub.simulator.rest.model.PermBody;
import uk.org.ca.stub.simulator.service.AuthenticatedServiceTest;
import uk.org.ca.stub.simulator.service.JwtService;
import uk.org.ca.stub.simulator.service.PermService;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

class PermApiControllerTest extends AbstractControllerTest {

    @Autowired
    JwtService  jwtService;

    @Autowired
    PermService permService;

    private String endpoint;

    @BeforeEach
    void setUp() {
        endpoint = "https://localhost:" + port + "/perm";

        permService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        permService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);
    }

    @Test
    void testsEndpoint() {
        assertNotNull(restTemplate);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, restTemplate.getForEntity(endpoint, String.class).getStatusCode());
    }

    @Test
    @Description("Positive test scenarios")
    void shouldReturn201() {
        assertAll(
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint, createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER,validResourceId,resourceScopes), InlineResponse201.class);
                    String token = response.getBody().getTicket();

                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                    assertDoesNotThrow(() -> validateJwtToken(token));
                }
        );
    }

    @Test
    @Description("All possible bad requests (400)")
    void shouldReturn400(){
        assertAll(
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);

                    var response = restTemplate.postForEntity(endpoint, createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER,validResourceId,resourceScopes), String.class);

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                    assertTrue(response.getBody().contains("size must be between 2 and 2"));
                },
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);

                    var response = restTemplate.postForEntity(endpoint, createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.INVALID_AUTHORIZATION_HEADER,validResourceId,resourceScopes), String.class);

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                }
        );
    }


    @Test
    @Description("401,404")
    void shouldReturn40X() {
        assertAll(
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint, createPostPermRequestWithResourceIdAndScopes(authHeaderForPat(ASSERTION_PAT_PERM_401_EXPIRED_PAT),validResourceId,resourceScopes), String.class);

                    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                },
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint, createPostPermRequestWithResourceIdAndScopes(authHeaderForPat(ASSERTION_PAT_PERM_404_RESOURCE_NOT_FOUND),validResourceId,resourceScopes), String.class);

                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                },
                () -> {
                    String invalidResourceId = "11111111-25b8-4d87-afde-18a9ee2631dc";
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint, createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER,invalidResourceId,resourceScopes), String.class);

                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                    assertTrue(response.getBody().contains("Resource not found"));
                }
        );
    }

    @Test
    @Description("500,502,503,504")
    void shouldReturn50X() {
        assertAll(
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint,
                            createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_PERM_500_SERVER_ERROR),validResourceId,resourceScopes), InlineResponse201.class);

                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                },
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint,
                            createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_PERM_502_BAD_GATEWAY),validResourceId,resourceScopes), InlineResponse201.class);

                    assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
                },
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint,
                            createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_PERM_503_SERVICE_UNAVAILABLE),validResourceId,resourceScopes), InlineResponse201.class);

                    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
                },
                () -> {
                    String validResourceId = "92476c2f-25b8-4d87-afde-18a9ee2631dc"; // first resource in dbinitializer
                    List<PermBody.ResourceScopesEnum> resourceScopes = new ArrayList<>();
                    resourceScopes.add(PermBody.ResourceScopesEnum.OWNER);
                    resourceScopes.add(PermBody.ResourceScopesEnum.VALUE);

                    var response = restTemplate.postForEntity(endpoint,
                            createPostPermRequestWithResourceIdAndScopes(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_PERM_504_GATEWAY_TIMEOUT),validResourceId,resourceScopes), InlineResponse201.class);

                    assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
                }
        );
    }

    private HttpEntity<PermBody> createPostPermRequestWithResourceIdAndScopes(String authorizationHeader, String resourceId, List<PermBody.ResourceScopesEnum> resourceScopes){
        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

        var body = new PermBody();
        if (resourceId != null) {
            body.setResourceId(UUID.fromString(resourceId));
        }
        if (resourceScopes != null) {
            body.setResourceScopes(resourceScopes);
        }

        return new HttpEntity<>(body, headers);
    }

    private void validateJwtToken(String token) throws Exception {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtService.getSignature().getBytes(StandardCharsets.UTF_8)))
                .build();
        try {
            var mapper = new JsonMapper();
            var decodedPmt = Base64.getDecoder().decode(token);
            var tokenMap = mapper.readValue(new String(decodedPmt), Map.class);
            assertEquals(JwtService.TokenInstance.PMT.getType(), tokenMap.get("token_type"));
            jwtParser.parse(tokenMap.get("access_token").toString());
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
    }

    private String authHeaderForPat(String pat) {
        return "Bearer " + pat;
    }
}

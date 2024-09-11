package uk.org.ca.stub.simulator.rest.controller;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResult;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResultPermissions;
import uk.org.ca.stub.simulator.service.AuthenticatedServiceTest;
import uk.org.ca.stub.simulator.service.IntrospectService;
import uk.org.ca.stub.simulator.service.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.*;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;
import static uk.org.ca.stub.simulator.utils.Commons.TOKEN;
import static uk.org.ca.stub.simulator.utils.Commons.X_REQUEST_ID;


public class IntrospectApiControllerTest extends AbstractControllerTest {

    @Autowired
    JwtService jwtService;

    @Autowired
    IntrospectService introspectService;

    private String endpoint;

    @BeforeEach
    void setUp() {
        endpoint = "https://localhost:" + port + "/introspect";
        // warranty all test use the happy path scenario by default
        introspectService.setPatStoredValidator(ALWAYS_STORED);
        introspectService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
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
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,VALID_RPT_TOKEN), IntrospectionResult.class);

                    assertEquals(HttpStatus.OK, response.getStatusCode());

                    List<IntrospectionResultPermissions> permissions = new ArrayList<>();
                    IntrospectionResultPermissions permission = new IntrospectionResultPermissions();
                    permission.resourceId(UUID.fromString("92476c2f-25b8-4d87-afde-18a9ee2631dc")).addResourceScopesItem(IntrospectionResultPermissions.ResourceScopesEnum.OWNER).exp(Long.valueOf("1813411040"));
                    permissions.add(permission);

                    IntrospectionResult targetResult = new IntrospectionResult()
                            .active(true)
                            .tokenType(IntrospectionResult.TokenTypeEnum.PENSION_DASHBOARD_RPT)
                            .exp(Long.valueOf("1813411040"))
                            .iss("https://stub-cas.co.uk")
                            .permissions(permissions);

                    assertIntrospectionResult(response, targetResult);
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,EXPIRED_RPT_TOKEN), IntrospectionResult.class);

                    assertEquals(HttpStatus.OK, response.getStatusCode());

                    List<IntrospectionResultPermissions> permissions = new ArrayList<>();
                    IntrospectionResultPermissions permission = new IntrospectionResultPermissions();
                    permission.resourceId(UUID.fromString("62933ca9-447e-4ce0-bb39-124e9fa3214f")).addResourceScopesItem(IntrospectionResultPermissions.ResourceScopesEnum.OWNER).exp(Long.valueOf("1624197800"));
                    permissions.add(permission);

                    IntrospectionResult targetResult = new IntrospectionResult()
                            .active(false)
                            .tokenType(IntrospectionResult.TokenTypeEnum.PENSION_DASHBOARD_RPT)
                            .exp(Long.valueOf("1624197800"))
                            .iss("https://stub-cas.co.uk")
                            .permissions(permissions);

                    assertIntrospectionResult(response, targetResult);
                }
        );
    }

    @Test
    @Description("All possible bad requests (400)")
    void shouldReturn400(){
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,"malformed-token"), String.class);

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,ASSERTION_RPT_INTROSPECT_400_INCORRECT_SIGNATURE), String.class);

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                },
                () -> {
                    introspectService.setPatStoredValidator(ALWAYS_NOT_STORED);
                    introspectService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,VALID_RPT_TOKEN), String.class);

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("401,404")
    void shouldReturn40X() {
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER, ASSERTION_RPT_INTROSPECT_401_EXPIRED_PAT), String.class);
                    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,ASSERTION_RPT_INTROSPECT_404_RESOURCE_NOT_FOUND), String.class);
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("500,502,503,504")
    void shouldReturn50X() {
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,ASSERTION_RPT_INTROSPECT_500_SERVER_ERROR), IntrospectionResult.class);
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,ASSERTION_RPT_INTROSPECT_502_BAD_GATEWAY), IntrospectionResult.class);
                    assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,ASSERTION_RPT_INTROSPECT_503_SERVICE_UNAVAILABLE), IntrospectionResult.class);
                    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostIntrospectRequestWithRpt(VALID_AUTHORIZATION_HEADER,ASSERTION_RPT_INTROSPECT_504_GATEWAY_TIMEOUT), IntrospectionResult.class);
                    assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
                }
        );
    }

    private static MultiValueMap<String, String> buildFormForToken(String token) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(TOKEN, token);
        return formData;
    }

    private static HttpHeaders getHttpHeadersWithAuthentication(String authHeader) {
        var headers = new HttpHeaders();
        headers.set(X_REQUEST_ID, String.valueOf(UUID.randomUUID()));
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        return headers;
    }

    private static HttpEntity<MultiValueMap<String, String>> createPostIntrospectRequestWithRpt(String authHeader, String rptToken) {
        var headers = getHttpHeadersWithAuthentication(authHeader);
        MultiValueMap<String, String> formData = buildFormForToken(rptToken);
        return new HttpEntity<>(formData, headers);
    }

    private static void assertIntrospectionResult(ResponseEntity<IntrospectionResult> response, IntrospectionResult targetResult) {
        assertAll(
                () -> assertEquals(targetResult.getActive(),response.getBody().getActive()),
                () -> assertEquals(targetResult.getTokenType(),response.getBody().getTokenType()),
                () -> assertEquals(targetResult.getExp(),response.getBody().getExp()),
                () -> assertEquals(targetResult.getIss(),response.getBody().getIss()),
                () -> assertEquals(targetResult.getPermissions(),response.getBody().getPermissions())
        );
    }

}

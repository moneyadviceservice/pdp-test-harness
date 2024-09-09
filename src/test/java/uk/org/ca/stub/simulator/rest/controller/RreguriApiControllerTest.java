package uk.org.ca.stub.simulator.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import uk.org.ca.stub.simulator.rest.exception.CasValidationError;
import uk.org.ca.stub.simulator.rest.model.RreguriBody;
import uk.org.ca.stub.simulator.service.AuthenticatedServiceTest;
import uk.org.ca.stub.simulator.service.RegisterService;
import uk.org.ca.stub.simulator.utils.MatchStatusEnum;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer.DEFAULT_RESOURCES;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

class RreguriApiControllerTest extends AbstractControllerTest {

    @Autowired
    RegisterService registerService;

    String endpoint;

    String newResourceId = "urn:pei:0e55140a-87d3-41cf-b6f7-bc822a4c3c3b:6e29eeb8-814c-44a6-a43f-b4830f3f45bb";

    @BeforeEach
    void setUp() {
        endpoint = "https://localhost:" + port + "/rreguri";

        registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        registerService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);
    }

    @Test
    void testsEndpoint() {
        assertNotNull(restTemplate);
    }

    // POST
    @Test
    @Description("POST 200,201")
    void postShouldReturn20x() {
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER, newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                    assertTrue(response.getHeaders().containsKey(HttpHeaders.LOCATION));
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER, DEFAULT_RESOURCES.getFirst().getName(),MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertTrue(response.getHeaders().containsKey(HttpHeaders.LOCATION));
                }
        );
    }

    @Test
    @Description("POST 400")
    void postShouldReturn400() {
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER, "urn:pei:11324243535",MatchStatusEnum.YES), String[].class);

                    var objectMapper = new ObjectMapper();
                    assertAll("name is invalid",
                            () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                            () -> assertNotNull(response.getBody()),
                            () -> {
                                var err = Objects.requireNonNull(response.getBody())[0];
                                CasValidationError casValidationError = objectMapper.readValue(err, CasValidationError.class);
                                assertAll(
                                        () -> assertEquals(1, response.getBody().length),
                                        () -> assertEquals("name", casValidationError.field()),
                                        () -> assertTrue(casValidationError.reason().contains("must match"))
                                );
                            }
                    );
                },
                () -> {
                    registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER, newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("POST 401,404,429")
    void postShouldReturn40x() {
        assertAll(
                () -> {
                    registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_401_EXPIRED_PAT), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                },
                () -> {
                    registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_404_USER_REMOVED), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                },
                () -> {
                    registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_429_TOO_MANY_REQUESTS), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
                    assertTrue(response.getHeaders().containsKey(HttpHeaders.RETRY_AFTER));
                }
        );
    }

    @Test
    @Description("POST 500,502,503,504")
    void postShouldReturn50x() {
        assertAll(
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_500_SERVER_ERROR), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_502_BAD_GATEWAY), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_503_SERVICE_UNAVAILABLE), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
                },
                () -> {
                    var response = restTemplate.postForEntity(endpoint, createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_POST_504_GATEWAY_TIMEOUT), newResourceId,MatchStatusEnum.YES), String.class);
                    assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
                }
        );
    }

    // GET
    @Test
    @Description("GET 200")
    void getShouldReturn200() {
        // TODO - postponed for now as get is unused
    }

    @Test
    @Description("GET 400")
    void getShouldReturn400() {
        assertAll(
                () -> {
                    registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);
                    var response = restTemplate.getForEntity(endpoint + "/" + UUID.randomUUID(), String.class);
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("GET 401,403")
    void getShouldReturn40x() {
        // TODO - postponed for now as get is not used
    }

    @Test
    @Description("GET 500,502,503,504")
    void getShouldReturn50x() {
        // TODO - postponed for now as get is not used
    }

    // PATCH
    @Test
    @Description("PATCH 200")
    void patchShouldReturn200() {
        assertAll(
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER, DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.OK, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("PATCH 400")
    void patchShouldReturn400() {
        assertAll(
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER, DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.NO);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                }
        );    }

    @Test
    @Description("PATCH 401,404,429")
    void patchShouldReturn40x() {
        assertAll(
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_401_EXPIRED_PAT), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                },
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_404_RESOURCE_NOT_FOUND), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                },
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_429_TOO_MANY_REQUESTS), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
                    assertTrue(response.getHeaders().containsKey(HttpHeaders.RETRY_AFTER));
                }
        );
    }

    @Test
    @Description("PATCH 500,502,503,504")
    void patchShouldReturn50x() {
        assertAll(
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_500_SERVER_ERROR), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                },
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_502_BAD_GATEWAY), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
                },
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_503_SERVICE_UNAVAILABLE), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
                },
                () -> {
                    HttpEntity<RreguriBody> entity = createPostPatchRrequriWithResourceName(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_PATCH_504_GATEWAY_TIMEOUT), DEFAULT_RESOURCES.getFirst().getResourceId(),MatchStatusEnum.YES);

                    var response = restTemplate.exchange(
                            endpoint + "/" + DEFAULT_RESOURCES.getFirst().getResourceId(),
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

                    assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
                }
        );
    }

    // DELETE
    @Test
    @Description("DELETE 204")
    void deleteShouldReturn204() {
        assertAll(
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER);
                    // resource with index 1 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(1).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("DELETE 400")
    void deleteShouldReturn400() {
        assertAll(
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER);
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.YES), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                }
        );
    }

    @Test
    @Description("DELETE 401,429")
    void deleteShouldReturn40x() {
        assertAll(
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_DELETE_401_EXPIRED_PAT));
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                },
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_DELETE_429_TOO_MANY_REQUESTS));
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
                    assertTrue(response.getHeaders().containsKey(HttpHeaders.RETRY_AFTER));
                }
        );
    }

    @Test
    @Description("DELETE 500,502,503,504")
    void deleteShouldReturn50x() {
        assertAll(
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_DELETE_500_SERVER_ERROR));
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                },
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_DELETE_502_BAD_GATEWAY));
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
                },
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_DELETE_503_SERVICE_UNAVAILABLE));
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
                },
                () -> {
                    HttpHeaders headers = getHttpHeaders(AuthenticatedServiceTest.authHeaderForPat(ASSERTION_PAT_RREGURI_DELETE_504_GATEWAY_TIMEOUT));
                    // resource with index 2 is match-possible as required
                    ResponseEntity<String> response = restTemplate.exchange(getDeleteEndpointForResourceAndReason(DEFAULT_RESOURCES.get(2).getResourceId(),MatchStatusEnum.TIMEOUT), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

                    assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getStatusCode());
                }
        );
    }

    private HttpEntity<RreguriBody> createPostPatchRrequriWithResourceName(String authorizationHeader, String resourceName, MatchStatusEnum matchStatus) {
        var headers = getHttpHeaders(authorizationHeader);
        var body = buildRreguriBodyForName(resourceName, matchStatus);

        return new HttpEntity<>(body, headers);
    }

//    private HttpEntity<String> createGetRrequri(String authorizationHeader) {
//        var headers = getHttpHeaders(authorizationHeader);
//
//        return new HttpEntity<>(headers);
//    }

    private String getDeleteEndpointForResourceAndReason(String resourceId, MatchStatusEnum deletionReason){
        return String.format("%s/%s?deletion_reason=%s",endpoint,resourceId,deletionReason);
    }


    private RreguriBody buildRreguriBodyForName(String name, MatchStatusEnum matchStatus) {
        var body = new RreguriBody();
        body.description("test resource");
        body.name(name);
        body.matchStatus(matchStatus);
        body.inboundRequestId("find");
        body.setResourceScopes(List.of(RreguriBody.ResourceScopesEnum.VALUE, RreguriBody.ResourceScopesEnum.DELEGATE));
        return body;
    }

    private HttpHeaders getHttpHeaders(String token) {
        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }
}

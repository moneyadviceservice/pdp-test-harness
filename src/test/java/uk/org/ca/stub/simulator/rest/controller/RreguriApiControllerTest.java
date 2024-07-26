package uk.org.ca.stub.simulator.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import uk.org.ca.stub.simulator.rest.exception.CasValidationError;
import uk.org.ca.stub.simulator.rest.model.RreguriBody;
import uk.org.ca.stub.simulator.service.AuthenticatedServiceTest;
import uk.org.ca.stub.simulator.service.RegisterService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RreguriApiControllerTest extends AbstractControllerTest {

    @Autowired
    RegisterService registerService;

    @Test
    void postRequest(){
        registerService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);
        registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        var body = buildRreguriBodyForName("urn:pei:0e55140a-87d3-41cf-b6f7-bc822a4c3c3b:6e29eeb8-814c-44a6-a43f-b4830f3f45bb");

        var request = new HttpEntity<>(body, getHttpHeaders());

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/rreguri",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.LOCATION));
    }

    @Test
    void invalidNamePostRequest(){
        registerService.setPatAuthorizationValidator(AuthenticatedServiceTest.ALWAYS_AUTHORIZED);
        registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_STORED);
        var body = buildRreguriBodyForName("urn:pei:11324243535");

        var request = new HttpEntity<>(body, getHttpHeaders());

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/rreguri",
                HttpMethod.POST,
                request, String[].class);
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
    }

    @Test
    void testAuthenticationNotStoredPatForPost() {
        registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);

        var body = buildRreguriBodyForName("urn:pei:0e55140a-87d3-41cf-b6f7-bc822a4c3c3b:6e29eeb8-814c-44a6-a43f-b4830f3f45bb");
        var request = new HttpEntity<>(body, getHttpHeaders());

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/rreguri",
                HttpMethod.POST,
                request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAuthenticationNotStoredPatForGet() {
        registerService.setPatStoredValidator(AuthenticatedServiceTest.ALWAYS_NOT_STORED);

        var body = buildRreguriBodyForName("urn:pei:0e55140a-87d3-41cf-b6f7-bc822a4c3c3b:6e29eeb8-814c-44a6-a43f-b4830f3f45bb");
        var request = new HttpEntity<>(body, getHttpHeaders());

        var response = this.restTemplate.exchange( "http://localhost:" + port + "/rreguri/"+UUID.randomUUID(),
                HttpMethod.GET,
                request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private RreguriBody buildRreguriBodyForName(String name) {
        var body = new RreguriBody();
        body.description("test resource");
        body.name(name);
        body.matchStatus(RreguriBody.MatchStatusEnum.POSSIBLE);
        body.inboundRequestId("find");
        body.setResourceScopes(List.of(RreguriBody.ResourceScopesEnum.VALUE, RreguriBody.ResourceScopesEnum.DELEGATE));
        return body;
    }

    private HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer token-here");
        return headers;
    }
}

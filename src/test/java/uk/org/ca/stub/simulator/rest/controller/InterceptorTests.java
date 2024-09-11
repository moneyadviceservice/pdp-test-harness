package uk.org.ca.stub.simulator.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import uk.org.ca.stub.simulator.rest.model.PermBody;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER;

class InterceptorTests extends AbstractControllerTest {

    @Test
    void checkAllowedWithoutXHeader(){
        var response = this.restTemplate.getForEntity("https://localhost:" + port + "/", String.class);
        assertAll("request without 'x-request-id' is allowed to pass like it owns the place",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(Objects.requireNonNull(response.getBody()).contains("C&A Stub"))
        );
    }

    @Test
    void checkNotAllowedWithoutXHeader(){
        assertAll("request without 'x-request-id' is dropped like a hot potato",
                () -> {
                    var response_rrequri = this.restTemplate.getForEntity("https://localhost:" + port + "/rreguri/{resource_id}",
                            String.class, "non-existing");
                    assertAll("/rreguri request without 'x-request-id' is dropped like a hot potato",
                            () -> assertEquals(HttpStatus.BAD_REQUEST, response_rrequri.getStatusCode()),
                            () -> assertTrue(Objects.requireNonNull(response_rrequri.getBody()).contains("Missing 'x-request-id' header"))
                    );
                },
                () -> {
                    var body = new PermBody();
                    body.setResourceId(UUID.randomUUID());
                    body.resourceScopes(List.of(PermBody.ResourceScopesEnum.VALUE, PermBody.ResourceScopesEnum.OWNER));
                    var response_perm = this.restTemplate.postForEntity("https://localhost:" + port + "/perm",body, String.class);
                    assertAll("/perm request without 'x-request-id' is dropped like a hot potato",
                            () -> assertEquals(HttpStatus.BAD_REQUEST, response_perm.getStatusCode()),
                            () -> assertTrue(Objects.requireNonNull(response_perm.getBody()).contains("Missing 'x-request-id' header"))
                    );
                }
        );
    }

    @Test
    void checkAllowedWithXHeader(){
        var headers = new HttpHeaders();
        headers.set("X-Request-Id", String.valueOf(UUID.randomUUID()));
        headers.set(HttpHeaders.AUTHORIZATION, VALID_AUTHORIZATION_HEADER);

        assertAll("request with 'x-request-id' is allowed to pass like it owns the place",
                () -> {
                    var request = new HttpEntity<>(headers);
                    var response = this.restTemplate.exchange( "https://localhost:" + port + "/rreguri/{resource_id}",
                            HttpMethod.GET, request, String.class, "non-existing");
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                },
                () -> {
                    var body = new PermBody();
                    body.setResourceId(UUID.randomUUID());
                    body.resourceScopes(List.of(PermBody.ResourceScopesEnum.VALUE, PermBody.ResourceScopesEnum.OWNER));
                    var request = new HttpEntity<>(body, headers);
                    var response = this.restTemplate.exchange( "https://localhost:" + port + "/perm",
                            HttpMethod.POST,
                            request,
                            String.class, "non-existing");
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                }
        );
    }

    // TODO add here more tests as the rest of endpoints get a handle method
}

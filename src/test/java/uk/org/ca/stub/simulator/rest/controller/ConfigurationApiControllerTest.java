package uk.org.ca.stub.simulator.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import uk.org.ca.stub.simulator.rest.model.StubConfiguration;
import uk.org.ca.stub.simulator.service.ConfigurationService;

class ConfigurationApiControllerTest extends AbstractControllerTest {

    @Autowired
    private ConfigurationService configurationService;

    private String endpoint;

    @BeforeEach
    void setUp() {
        endpoint = "https://localhost:" + port + "/configuration";
        configurationService.clearAllConfigurations();
    }

    @Test
    void shouldReturn204WhenConfigurationAccepted() {
        // Given
        String requestBody = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "count": 3,
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.postForEntity(endpoint, request, Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verify configuration was stored
        StubConfiguration config = configurationService.getAndDecrementConfiguration("/service-availability/find");
        assertNotNull(config);
        assertEquals(429, config.getStatus());
        assertEquals(2, config.getCount()); // Should be decremented to 2
    }

    @Test
    void shouldAcceptMultipleConfigurations() {
        // Given
        String requestBody = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "count": 2,
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  },
                  {
                    "endpoint": "/service-availability/view",
                    "status": 503,
                    "code": "SERVICE_UNAVAILABLE",
                    "count": 1,
                    "errors": [
                      {
                        "code": "SERVICE_UNAVAILABLE"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.postForEntity(endpoint, request, Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verify both configurations were stored
        assertNotNull(configurationService.getAndDecrementConfiguration("/service-availability/find"));
        assertNotNull(configurationService.getAndDecrementConfiguration("/service-availability/view"));
    }

    @Test
    void shouldClearAllConfigurationsWhenEmptyArraySent() {
        // Given - first configure some endpoints
        String configRequest = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "count": 3,
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  },
                  {
                    "endpoint": "/view-response/request-number",
                    "status": 503,
                    "code": "SERVICE_UNAVAILABLE",
                    "count": 5,
                    "errors": [
                      {
                        "code": "SERVICE_UNAVAILABLE"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForEntity(endpoint, new HttpEntity<>(configRequest, headers), Void.class);

        // When - send empty array to clear all
        String emptyRequest = "[]";
        var response = restTemplate.postForEntity(endpoint, new HttpEntity<>(emptyRequest, headers), Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify all configurations were cleared
        assertEquals(null, configurationService.getAndDecrementConfiguration("/service-availability/find"));
        assertEquals(null, configurationService.getAndDecrementConfiguration("/view-response/request-number"));
    }

    @Test
    void shouldReturn400WhenRequestBodyInvalid() {
        // Given - missing required field 'count'
        String requestBody = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.postForEntity(endpoint, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn400WhenCountIsNegative() {
        // Given - negative count
        String requestBody = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "count": -1,
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.postForEntity(endpoint, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn400WhenStatusCodeOutOfRange() {
        // Given - status code < 100
        String requestBody = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 99,
                    "code": "INVALID",
                    "count": 1,
                    "errors": [
                      {
                        "code": "INVALID"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.postForEntity(endpoint, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn400WhenInvalidEndpoint() {
        // Given - endpoint not in enum
        String requestBody = """
                [
                  {
                    "endpoint": "/invalid-endpoint",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "count": 1,
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.postForEntity(endpoint, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldOverwriteExistingConfiguration() {
        // Given - configure endpoint first time
        String firstRequest = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 429,
                    "code": "TOO_MANY_REQUESTS",
                    "count": 5,
                    "errors": [
                      {
                        "code": "TOO_MANY_REQUESTS"
                      }
                    ]
                  }
                ]
                """;

        String secondRequest = """
                [
                  {
                    "endpoint": "/service-availability/find",
                    "status": 503,
                    "code": "SERVICE_UNAVAILABLE",
                    "count": 2,
                    "errors": [
                      {
                        "code": "SERVICE_UNAVAILABLE"
                      }
                    ]
                  }
                ]
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // When - configure twice
        restTemplate.postForEntity(endpoint, new HttpEntity<>(firstRequest, headers), Void.class);
        var response = restTemplate.postForEntity(endpoint, new HttpEntity<>(secondRequest, headers), Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        StubConfiguration config = configurationService.getAndDecrementConfiguration("/service-availability/find");
        assertNotNull(config);
        assertEquals(503, config.getStatus());
        assertEquals("SERVICE_UNAVAILABLE", config.getCode());
    }
}

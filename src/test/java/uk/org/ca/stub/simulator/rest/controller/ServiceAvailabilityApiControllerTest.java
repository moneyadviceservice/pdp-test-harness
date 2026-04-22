package uk.org.ca.stub.simulator.rest.controller;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.org.ca.stub.simulator.service.ConfigurationService;

class ServiceAvailabilityApiControllerTest extends AbstractControllerTest {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ObjectMapper objectMapper;

    private String findEndpoint;
    private String viewEndpoint;

    @BeforeEach
    void setUp() {
        findEndpoint = "https://localhost:" + port + "/service-availability/find";
        viewEndpoint = "https://localhost:" + port + "/service-availability/view";
        configurationService.clearAllConfigurations();
    }

    // ========== Find Service Unavailability Data Tests ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn202WithDefaultResponse_whenNoConfiguration() throws Exception {
        // Given
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "planned maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(findEndpoint, request, String.class);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        assertEquals("ACCEPTED", responseBody.get("message").asText());
        assertNotNull(responseBody.get("datetimestamp"));
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400WithProblemDetails_whenConfigured() throws Exception {
        // Given - Configure stubbed error
        configureStub("/service-availability/find", 400, "INVALID_REQUEST", 1);

        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When/Then
        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            // Then
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertNotNull(errorBody.get("type"));
            assertEquals("Invalid Request", errorBody.get("title").asText());
            assertEquals(400, errorBody.get("status").asInt());
            assertNotNull(errorBody.get("timestamp"));
            assertTrue(errorBody.get("errors").isArray());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn503WithProblemDetails_whenConfigured() throws Exception {
        // Given - Configure stubbed error
        configureStub("/service-availability/find", 503, "SERVICE_UNAVAILABLE", 2);

        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When/Then - First call should return 503
        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (Exception e) {
            HttpClientErrorException exception = (HttpClientErrorException) e;
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
            
            JsonNode errorBody = objectMapper.readTree(exception.getResponseBodyAsString());
            assertEquals("Service Unavailable", errorBody.get("title").asText());
            assertEquals(503, errorBody.get("status").asInt());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturnNormalResponseAfterCountExpires() throws Exception {
        // Given - Configure stubbed error with count=1
        configureStub("/service-availability/find", 400, "INVALID_REQUEST", 1);

        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "planned maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When/Then - First call returns error
        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

        // Second call should return normal response
        ResponseEntity<String> response = restTemplate.postForEntity(findEndpoint, request, String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        assertEquals("ACCEPTED", responseBody.get("message").asText());
    }

    // ========== View Service Unavailability Data Tests ==========

    @Test
    void viewServiceUnavailabilityData_shouldReturn202WithDefaultResponse_whenNoConfiguration() throws Exception {
        // Given
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "planned maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(viewEndpoint, request, String.class);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        assertEquals("ACCEPTED", responseBody.get("message").asText());
        assertNotNull(responseBody.get("datetimestamp"));
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn400WithProblemDetails_whenConfigured() throws Exception {
        // Given - Configure stubbed error
        configureStub("/service-availability/view", 400, "INVALID_REQUEST", 1);

        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When/Then
        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            // Then
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertNotNull(errorBody.get("type"));
            assertEquals("Invalid Request", errorBody.get("title").asText());
            assertEquals(400, errorBody.get("status").asInt());
            assertNotNull(errorBody.get("timestamp"));
            assertTrue(errorBody.get("errors").isArray());
        }
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn429WithProblemDetails_whenRateLimited() throws Exception {
        // Given - Configure rate limit error
        configureStub("/service-availability/view", 429, "TOO_MANY_REQUESTS", 3);

        String requestBody = """
                {
                  "submissionId": "12345",
                  "startDate": "2026-01-01",
                  "endDate": "2026-02-13"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When/Then
        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (Exception e) {
            HttpClientErrorException exception = (HttpClientErrorException) e;
            assertEquals(HttpStatus.TOO_MANY_REQUESTS, exception.getStatusCode());
            
            JsonNode errorBody = objectMapper.readTree(exception.getResponseBodyAsString());
            assertEquals("Too Many Requests", errorBody.get("title").asText());
            assertEquals(429, errorBody.get("status").asInt());
        }
    }

    // ========== Validation Tests - Empty/Null body ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenEmptyBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_REQUEST_BODY", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn400_whenEmptyBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_REQUEST_BODY", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== Validation Tests - Null submission rule ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenNullSubmissionHasNonNullSubmissionId() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "null submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": []
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenNullSubmissionHasObjectItems() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": null,
                  "submission_reason": "null submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_UNEXPECTED_FIELD", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== Validation Tests - Missing required fields ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenMissingRecordId() throws Exception {
        String requestBody = """
                {
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_RECORD_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenMissingItems() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_HOLDERNAMEGUID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenMissingSubmissionReason() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_REASON", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenMissingSubmissionId() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn202_whenSubmissionIdIsExplicitNull() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": null,
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(findEndpoint, request, String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    // ========== Validation Tests - Invalid field formats ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidRecordIdFormat() throws Exception {
        String requestBody = """
                {
                  "record_id": "not-a-uuid",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_RECORD_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidSubmissionIdFormat() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "not-a-uuid",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidSubmissionReason() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "invalid reason",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_REASON", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidSubmissionStatus() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "invalid status",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_STATUS", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidReportingPeriodStartFormat() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "not-a-date",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_REPORTING_PERIOD_START", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== Validation Tests - Unexpected fields ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenUnexpectedTopLevelField() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}],
                  "unexpected_field": "value"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_UNEXPECTED_FIELD", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenUnexpectedNestedItemField() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z", "unexpected_nested": "value"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_UNEXPECTED_FIELD", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== Validation Tests - Item field formats ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenUnavailScheduledIsNotBoolean() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": "yes", "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_UNAVAIL_SCHEDULED", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidUnavailReason() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "invalid@reason!", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_UNAVAIL_REASON", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidStartDateTsFormat() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "not-a-date", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_START_DATE_TS", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenInvalidEndDateTsFormat() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "not-a-date"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_END_DATE_TS", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenMissingRequiredItemField() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_END_DATE_TS", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== View endpoint validation mirror tests ==========

    @Test
    void viewServiceUnavailabilityData_shouldReturn400_whenNullSubmissionHasNonNullSubmissionId() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "null submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": []
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn400_whenMissingSubmissionId() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_SUBMISSION_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn202_whenSubmissionIdIsExplicitNull() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": null,
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(viewEndpoint, request, String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn400_whenMissingItems() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_HOLDERNAMEGUID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn400_whenInvalidRecordIdFormat() throws Exception {
        String requestBody = """
                {
                  "record_id": "not-a-uuid",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_RECORD_ID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn202_whenNullSubmission() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": null,
                  "submission_reason": "null submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": []
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(viewEndpoint, request, String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        JsonNode responseBody = objectMapper.readTree(response.getBody());
        assertEquals("ACCEPTED", responseBody.get("message").asText());
    }

    @Test
    void viewServiceUnavailabilityData_shouldReturn400_whenUnexpectedTopLevelField() throws Exception {
        String requestBody = """
                {
                  "record_id": "6ac7f2a5-d518-44bf-9dff-ec5bf7e46e06",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": [{"unavail_scheduled": true, "unavail_reason": "maintenance", "start_date_ts": "2025-11-01T00:00:00.000Z", "end_date_ts": "2025-11-01T04:00:00.000Z"}]}],
                  "unexpected_field": "value"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(viewEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_UNEXPECTED_FIELD", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== Additional format edge case tests ==========

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenItemsIsNotArray() throws Exception {
        // checkFieldFormats: items present but not a list
        String requestBody = """
                {
                  "record_id": "3eac3a8b-fbdc-4897-b3ea-908a6b411d7f",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": "not-an-array"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_HOLDERNAMEGUID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenItemElementIsNotAnObject() throws Exception {
        // checkFieldFormats: item element is not a Map (e.g. a string)
        String requestBody = """
                {
                  "record_id": "3eac3a8b-fbdc-4897-b3ea-908a6b411d7f",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": ["not-a-map"]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_HOLDERNAMEGUID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    @Test
    void findServiceUnavailabilityData_shouldReturn400_whenHolderPeriodsIsNotArray() throws Exception {
        // checkFieldFormats: periods for a holder is not a list (e.g. a string value)
        String requestBody = """
                {
                  "record_id": "3eac3a8b-fbdc-4897-b3ea-908a6b411d7f",
                  "submission_id": "b955b911-838c-4179-bd2f-089630c61a9d",
                  "submission_reason": "initial submission",
                  "submission_status": "on time",
                  "reporting_period_start": "2025-11-01T00:00:00.000Z",
                  "items": [{"816f82b5-8e4f-407b-8c54-afb179c4af5a": "not-a-list"}]
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(findEndpoint, request, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
            assertEquals("INVALID_HOLDERNAMEGUID", errorBody.get("errors").get(0).get("code").asText());
        }
    }

    // ========== Helper Methods ==========

    private void configureStub(String endpoint, int status, String code, int count) {
        String configRequestBody = String.format("""
                [
                  {
                    "endpoint": "%s",
                    "status": %d,
                    "code": "%s",
                    "count": %d,
                    "errors": [
                      {
                        "code": "MISSING_REQUIRED_FIELD"
                      }
                    ]
                  }
                ]
                """, endpoint, status, code, count);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(configRequestBody, headers);

        String configEndpoint = "https://localhost:" + port + "/configuration";
        restTemplate.postForEntity(configEndpoint, request, Void.class);
    }
}

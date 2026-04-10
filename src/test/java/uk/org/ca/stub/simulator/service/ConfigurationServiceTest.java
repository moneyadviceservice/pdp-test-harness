package uk.org.ca.stub.simulator.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.org.ca.stub.simulator.rest.model.StubConfiguration;
import uk.org.ca.stub.simulator.rest.model.StubConfigurationErrorsInner;

class ConfigurationServiceTest {

    private ConfigurationService configurationService;

    @BeforeEach
    void setUp() {
        configurationService = new ConfigurationService();
    }

    @Test
    void shouldStoreConfiguration() {
        // Given
        StubConfiguration config = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_FIND,
                429,
                "TOO_MANY_REQUESTS",
                3
        );

        // When
        configurationService.configureStubs(List.of(config));

        // Then
        StubConfiguration retrieved = configurationService.getAndDecrementConfiguration("/service-availability/find");
        assertNotNull(retrieved);
        assertEquals(429, retrieved.getStatus());
        assertEquals("TOO_MANY_REQUESTS", retrieved.getCode());
        assertEquals(2, retrieved.getCount()); // Should be decremented to 2
    }

    @Test
    void shouldDecrementCountAndRemoveWhenZero() {
        // Given - the same StubConfiguration object is stored and mutated
        StubConfiguration config = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_VIEW,
                503,
                "SERVICE_UNAVAILABLE",
                3
        );
        configurationService.configureStubs(List.of(config));

        // When - Each call returns the same object with decremented count
        StubConfiguration first = configurationService.getAndDecrementConfiguration("/service-availability/view");
        StubConfiguration second = configurationService.getAndDecrementConfiguration("/service-availability/view");
        StubConfiguration third = configurationService.getAndDecrementConfiguration("/service-availability/view");
        StubConfiguration fourth = configurationService.getAndDecrementConfiguration("/service-availability/view");

        // Then - all returned objects are the same instance, showing final state
        assertNotNull(first);
        assertNotNull(second);
        assertNotNull(third);
        assertNull(fourth); // Config was removed when count reached 0
        
        // The same object is returned each time and mutated, so they all show final count
        // After 3 calls (3->2->1->0), count is 0
        assertEquals(0, first.getCount());
        assertEquals(0, second.getCount());
        assertEquals(0, third.getCount());
    }

    @Test
    void shouldReturnNullForNonExistentEndpoint() {
        // When
        StubConfiguration config = configurationService.getAndDecrementConfiguration("/non-existent");

        // Then
        assertNull(config);
    }

    @Test
    void shouldReturnNullWhenCountIsZero() {
        // Given
        StubConfiguration config = createTestConfiguration(
                StubConfiguration.EndpointEnum.VIEW_RESPONSE_CALCULATIONS,
                500,
                "INTERNAL_SERVER_ERROR",
                0
        );
        configurationService.configureStubs(List.of(config));

        // When
        StubConfiguration retrieved = configurationService.getAndDecrementConfiguration("/view-response/calculations");

        // Then
        assertNull(retrieved);
    }

    @Test
    void shouldOverwriteExistingConfiguration() {
        // Given
        StubConfiguration firstConfig = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_FIND,
                429,
                "TOO_MANY_REQUESTS",
                5
        );
        StubConfiguration secondConfig = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_FIND,
                503,
                "SERVICE_UNAVAILABLE",
                3
        );

        // When
        configurationService.configureStubs(List.of(firstConfig));
        configurationService.configureStubs(List.of(secondConfig));

        // Then
        StubConfiguration retrieved = configurationService.getAndDecrementConfiguration("/service-availability/find");
        assertNotNull(retrieved);
        assertEquals(503, retrieved.getStatus());
        assertEquals("SERVICE_UNAVAILABLE", retrieved.getCode());
        assertEquals(2, retrieved.getCount());
    }

    @Test
    void shouldConfigureMultipleEndpoints() {
        // Given
        StubConfiguration config1 = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_FIND,
                429,
                "TOO_MANY_REQUESTS",
                2
        );
        StubConfiguration config2 = createTestConfiguration(
                StubConfiguration.EndpointEnum.VIEW_RESPONSE_REQUEST_NUMBER,
                500,
                "INTERNAL_SERVER_ERROR",
                1
        );

        // When
        configurationService.configureStubs(List.of(config1, config2));

        // Then
        StubConfiguration retrieved1 = configurationService.getAndDecrementConfiguration("/service-availability/find");
        StubConfiguration retrieved2 = configurationService.getAndDecrementConfiguration("/view-response/request-number");
        
        assertNotNull(retrieved1);
        assertEquals(429, retrieved1.getStatus());
        
        assertNotNull(retrieved2);
        assertEquals(500, retrieved2.getStatus());
    }

    @Test
    void shouldClearSpecificConfiguration() {
        // Given
        StubConfiguration config = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_FIND,
                429,
                "TOO_MANY_REQUESTS",
                5
        );
        configurationService.configureStubs(List.of(config));

        // When
        configurationService.clearConfiguration("/service-availability/find");

        // Then
        StubConfiguration retrieved = configurationService.getAndDecrementConfiguration("/service-availability/find");
        assertNull(retrieved);
    }

    @Test
    void shouldClearAllConfigurations() {
        // Given
        StubConfiguration config1 = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_FIND,
                429,
                "TOO_MANY_REQUESTS",
                3
        );
        StubConfiguration config2 = createTestConfiguration(
                StubConfiguration.EndpointEnum.SERVICE_AVAILABILITY_VIEW,
                503,
                "SERVICE_UNAVAILABLE",
                2
        );
        configurationService.configureStubs(List.of(config1, config2));

        // When
        configurationService.clearAllConfigurations();

        // Then
        assertNull(configurationService.getAndDecrementConfiguration("/service-availability/find"));
        assertNull(configurationService.getAndDecrementConfiguration("/service-availability/view"));
    }

    private StubConfiguration createTestConfiguration(
            StubConfiguration.EndpointEnum endpoint,
            int status,
            String code,
            int count
    ) {
        StubConfigurationErrorsInner error = new StubConfigurationErrorsInner();
        error.setCode(code);

        StubConfiguration config = new StubConfiguration();
        config.setEndpoint(endpoint);
        config.setStatus(status);
        config.setCode(code);
        config.setCount(count);
        config.setErrors(List.of(error));

        return config;
    }
}

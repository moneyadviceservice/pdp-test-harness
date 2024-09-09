package uk.org.ca.stub.simulator.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MtlsUntrustedClientTest extends AbstractControllerTest {

    @Test
    void untrustedClient(){
        Exception exception = assertThrows(ResourceAccessException.class,
                ()->this.restTemplateUntrusted.getForObject("https://localhost:" + port + "/", String.class));

        assertTrue(exception.getMessage().contains("certificate_unknown"));
    }
}

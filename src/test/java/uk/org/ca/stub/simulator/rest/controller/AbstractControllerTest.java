package uk.org.ca.stub.simulator.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {

    @Autowired
    @Qualifier("trusted-test-client")
    protected RestTemplate restTemplate;

    @Autowired
    @Qualifier("untrusted-test-client")
    protected RestTemplate restTemplateUntrusted;

    @LocalServerPort
    protected int port;

}

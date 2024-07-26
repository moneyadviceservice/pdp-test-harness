package uk.org.ca.stub.simulator.rest.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest extends AbstractControllerTest {

    @Test
    void home(){
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/", String.class)).
                contains("C&A Stub");
    }

}

package uk.org.ca.stub.simulator.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "uk.org.ca.stub.simulator.rest",
        "uk.org.ca.stub.simulator.configuration",
        "uk.org.ca.stub.simulator.service",
        "uk.org.ca.stub.simulator.filter"
})
public class StubCasConfiguration {
}

package uk.org.ca.stub.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import uk.org.ca.stub.simulator.configuration.SpringDocConfiguration;
import uk.org.ca.stub.simulator.configuration.StubCasConfiguration;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
@Import({SpringDocConfiguration.class, StubCasConfiguration.class})
public class StubCasApplication {

    public static void main(String... args) {
        SpringApplication.run(StubCasApplication.class, args);
    }

}

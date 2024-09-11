package uk.org.ca.stub.simulator.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    private final BuildProperties buildProperties;

    public SpringDocConfiguration(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean(name = "apiInfo")
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("C&A Stub")
                                .description("C&A - Consent and Authorization - Stub for PDP")
                                .version(buildProperties.getVersion())
                );
    }
}

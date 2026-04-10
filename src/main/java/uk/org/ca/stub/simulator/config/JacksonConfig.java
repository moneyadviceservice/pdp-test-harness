package uk.org.ca.stub.simulator.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.org.ca.stub.simulator.rest.model.ProblemDetails;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemDetailsMixinCustomizer() {
        return builder -> builder.mixIn(ProblemDetails.class, ProblemDetailsMixin.class);
    }
}

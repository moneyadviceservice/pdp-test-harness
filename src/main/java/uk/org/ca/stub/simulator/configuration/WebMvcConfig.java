package uk.org.ca.stub.simulator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.org.ca.stub.simulator.interceptor.RequiredHeaderInterceptor;

@Configuration


public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequiredHeaderInterceptor());
    }
}

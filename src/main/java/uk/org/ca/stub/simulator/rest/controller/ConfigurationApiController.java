package uk.org.ca.stub.simulator.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.org.ca.stub.simulator.rest.api.ConfigurationApi;
import uk.org.ca.stub.simulator.rest.model.StubConfiguration;
import uk.org.ca.stub.simulator.service.ConfigurationService;

import java.util.List;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
@Slf4j
public class ConfigurationApiController implements ConfigurationApi {
    
    private final ConfigurationService configurationService;
    
    public ConfigurationApiController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
    
    @Override
    @Operation(summary = "Configure stubbed responses for selected endpoints")
    public ResponseEntity<Void> configurationPost(List<StubConfiguration> stubConfiguration) {
        log.info("Received configuration request with {} stub(s)", stubConfiguration.size());
        
        if (stubConfiguration.isEmpty()) {
            log.info("Empty configuration array received - clearing all stub configurations");
            configurationService.clearAllConfigurations();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        configurationService.configureStubs(stubConfiguration);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

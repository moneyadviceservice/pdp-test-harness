package uk.org.ca.stub.simulator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.rest.model.StubConfiguration;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ConfigurationService {
    
    private final ConcurrentHashMap<String, StubConfiguration> configMap = new ConcurrentHashMap<>();
    
    public void configureStubs(List<StubConfiguration> configurations) {
        log.info("Configuring {} stub endpoint(s)", configurations.size());
        
        for (StubConfiguration config : configurations) {
            String endpoint = config.getEndpoint().getValue();
            configMap.put(endpoint, config);
            log.info("Configured stub for endpoint: {} with status: {}, count: {}", endpoint, config.getStatus(), config.getCount());
        }
    }
    
    /**
     * Gets the configuration for an endpoint and atomically decrements its count.
     * Returns null if no configuration exists or count is already 0.
     * Automatically removes the configuration when count reaches 0.
     * 
     * @param endpoint The endpoint path to check
     * @return The stub configuration if active, null otherwise
     */
    public StubConfiguration getAndDecrementConfiguration(String endpoint) {
        StubConfiguration config = configMap.get(endpoint);
        
        if (config == null || config.getCount() <= 0) {
            return null;
        }
        
        int newCount = config.getCount() - 1;
        config.setCount(newCount);
        log.debug("Decremented count for endpoint: {} to {}", endpoint, newCount);
        
        if (newCount == 0) {
            configMap.remove(endpoint);
            log.info("Removed configuration for endpoint: {} (count reached 0)", endpoint);
        }
        
        return config;
    }
    
    public void clearConfiguration(String endpoint) {
        configMap.remove(endpoint);
        log.info("Cleared stub configuration for endpoint: {}", endpoint);
    }
    
    public void clearAllConfigurations() {
        configMap.clear();
        log.info("Cleared all stub configurations");
    }
}

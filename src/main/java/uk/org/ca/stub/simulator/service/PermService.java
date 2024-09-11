package uk.org.ca.stub.simulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.repository.ResourceRepository;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.model.PermBody;

import java.util.UUID;

@Service
public class PermService extends AbstractAuthenticatedService {
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final JwtService jwtService;

    public PermService(UserRepository userRepository, ResourceRepository resourceRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.jwtService = jwtService;
    }

    public String generatePMT(PermBody pb, UUID xRequestID, String pat) {
        this.validatePatStored(pat, userRepository);
        var resource = resourceRepository.findByResourceId(pb.getResourceId().toString())
                .orElseThrow(() -> new NotFoundException("Resource not found"));

        this.validatePatAuthentication(pat, resource);
        try {
            return jwtService.generatePmt(xRequestID, resource.getResourceId(), resource.getResourceScopes());
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException("PMT generation failed", e);
        }
    }
}

package uk.org.ca.stub.simulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.repository.ResourceRepository;
import uk.org.ca.stub.simulator.repository.UserRepository;
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

    public String generatePMT(PermBody pb, UUID xRequestID, String authenticationHeader) throws JsonProcessingException {
        String pat = getTokenFromHeader(authenticationHeader);
        this.validatePatStored(pat, userRepository);
        RegisteredResource resource = resourceRepository.findByResourceId(pb.getResourceId().toString())
                .orElseThrow(() -> new NotFoundException("Resource not found"));

        this.validatePatAuthentication(pat, resource);
        return jwtService.generatePmt(xRequestID, resource.getResourceId(), resource.getResourceScopes());
    }

}

package uk.org.ca.stub.simulator.service;

import lombok.Setter;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.UnauthorizedException;

import java.util.function.BiConsumer;

@Setter
public class AbstractAuthenticatedService {
    protected static final BiConsumer<String, RegisteredResource> validatePatAuthorizationFunction = (pat, resource) -> {
        if (pat == null || pat.isEmpty()) {
            throw new IllegalStateException("PAT is required");
        }
        String resourcePat = resource.getPat();
        if (resourcePat == null || resourcePat.isEmpty()) {
            // This scenario is probably invalid, as this is used in combination with Validate PAT is Stored
            throw new IllegalStateException("PAT is required");
        }
        if (!resourcePat.equals(pat)) {
            throw new UnauthorizedException("The resource is not associated with the PAT");
        }
    };

    protected static final BiConsumer<String, UserRepository> validatePatStoredFunction = (pat, repo) -> {
        if (pat == null || pat.isEmpty()) {
            throw new InvalidRequestException("Authorization header is required");
        }
        if (repo == null) {
            throw new IllegalStateException("User Repository is null required");
        }
        if (Boolean.FALSE.equals(repo.existsByPat(pat))) {
            throw new InvalidRequestException("PAT not found");
        }
    };

    // exposed using lombok setter for testing
    private BiConsumer<String, RegisteredResource> patAuthorizationValidator;
    private BiConsumer<String, UserRepository> patStoredValidator;

    // default validators for services
    AbstractAuthenticatedService() {
        this.patAuthorizationValidator = validatePatAuthorizationFunction;
        this.patStoredValidator = validatePatStoredFunction;
    }

    protected void validatePatAuthentication(String pat, RegisteredResource resource) {
        this.patAuthorizationValidator.accept(pat, resource);
    }

    protected void validatePatStored(String pat, UserRepository userRepository) {
        this.patStoredValidator.accept(pat, userRepository);
    }
}

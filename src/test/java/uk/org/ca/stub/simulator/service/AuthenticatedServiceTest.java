package uk.org.ca.stub.simulator.service;

import org.springframework.web.client.RestClient;
import uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.UnauthorizedException;

import java.util.function.BiConsumer;

public interface AuthenticatedServiceTest {
    String VALID_AUTHORIZATION_HEADER = "Bearer " + UserDbInitializer.users.getFirst().getPat();
    String INVALID_AUTHORIZATION_HEADER = "wrong format"; // todo: implement test for not valid headers

    BiConsumer<String, RegisteredResource> ALWAYS_AUTHORIZED = (a, x) -> {
    };

    BiConsumer<String, UserRepository> ALWAYS_STORED = (a, x) -> {
    };

    BiConsumer<String, RegisteredResource> ALWAYS_UNAUTHORIZED = (x, y) -> {
        throw new UnauthorizedException("testing");
    };

    BiConsumer<String, UserRepository> ALWAYS_NOT_STORED = (x, y) -> {
        throw new InvalidRequestException("testing");
    };

    RestClient.ResponseSpec.ErrorHandler IGNORE_ERROR = (request, response) -> {
    };
}

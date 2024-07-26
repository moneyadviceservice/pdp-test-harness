package uk.org.ca.stub.simulator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.NOT_EXPIRED_TOKEN_PAT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AbstractAuthenticatedServiceTest {
    public static final String NOT_EXISTING_PAT = "NOT-EXISTING";

    AbstractAuthenticatedService cut = new AbstractAuthenticatedService() {
    };

    @Autowired
    UserRepository userRepository;

    @Test
    void testPathAuthentication() {
        assertAll("PAT authenticate for resource",
                () -> assertThrows(UnauthorizedException.class, () -> cut.validatePatAuthentication(NOT_EXPIRED_TOKEN_PAT, RegisteredResource.builder().pat("not-the-same").build())),
                () -> assertThrows(IllegalStateException.class, () -> cut.validatePatAuthentication(NOT_EXPIRED_TOKEN_PAT, RegisteredResource.builder().pat("").build())),
                () -> assertThrows(IllegalStateException.class, () -> cut.validatePatAuthentication(NOT_EXPIRED_TOKEN_PAT, RegisteredResource.builder().pat(null).build())),
                () -> assertThrows(IllegalStateException.class, () -> cut.validatePatAuthentication(null, RegisteredResource.builder().pat("not-the-same").build())),
                () -> assertThrows(IllegalStateException.class, () -> cut.validatePatAuthentication("", RegisteredResource.builder().pat("not-the-same").build())),
                () -> assertDoesNotThrow(() -> cut.validatePatAuthentication(NOT_EXPIRED_TOKEN_PAT, RegisteredResource.builder().pat(NOT_EXPIRED_TOKEN_PAT).build()))
        );
    }

    @Test
    void testPathStored() {
        assertAll("PAT stored scenarios",
                () -> assertThrows(InvalidRequestException.class, () -> cut.validatePatStored(null, userRepository)),
                () -> assertThrows(InvalidRequestException.class, () -> cut.validatePatStored("", userRepository)),
                () -> assertThrows(IllegalStateException.class, () -> cut.validatePatStored(NOT_EXPIRED_TOKEN_PAT, null)),
                () -> assertThrows(InvalidRequestException.class, () -> cut.validatePatStored(NOT_EXISTING_PAT, userRepository)),
                () -> assertDoesNotThrow(() -> cut.validatePatStored(NOT_EXPIRED_TOKEN_PAT, userRepository))
        );
    }

    @Test
    void testGgetTokenFromHeader() {
        assertAll("Bearer head scenarios",
                () -> assertThrows(InvalidRequestException.class, () -> AbstractAuthenticatedService.getTokenFromHeader(null)),
                () -> assertThrows(InvalidRequestException.class, () -> AbstractAuthenticatedService.getTokenFromHeader("not-valid")),
                () -> assertThrows(InvalidRequestException.class, () -> AbstractAuthenticatedService.getTokenFromHeader("bearer-not-valid")),
                () -> assertDoesNotThrow( () -> AbstractAuthenticatedService.getTokenFromHeader("bearer this-is-valid")),
                () -> assertEquals("1234567890",   AbstractAuthenticatedService.getTokenFromHeader("bearer 1234567890"))
        );
    }
}

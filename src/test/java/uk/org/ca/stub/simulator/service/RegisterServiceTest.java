package uk.org.ca.stub.simulator.service;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.rest.exception.ConflictException;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.exception.UnauthorizedException;
import uk.org.ca.stub.simulator.rest.model.RreguriBody;
import static uk.org.ca.stub.simulator.service.AbstractAuthenticatedService.validatePatAuthorizationFunction;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.ALWAYS_AUTHORIZED;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.ALWAYS_NOT_STORED;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.ALWAYS_STORED;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.ALWAYS_UNAUTHORIZED;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.VALID_AUTHORIZATION_HEADER;
import uk.org.ca.stub.simulator.utils.MatchStatusEnum;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.NO;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.POSSIBLE;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.REMOVED;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.TIMEOUT;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.WITHDRAWN;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.YES;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RegisterServiceTest {

    @Autowired
    RegisterService cut;
    @Autowired
    private ResourceDbInitializer resourceDbInitializer;
    @Autowired
    private uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer userDbInitializer;
    @Autowired
    private uk.org.ca.stub.simulator.repository.ResourceRepository resourceRepository;
    @Autowired
    private uk.org.ca.stub.simulator.repository.UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.cut.setPatAuthorizationValidator(validatePatAuthorizationFunction);
        // Clear and reinitialize to ensure clean state
        resourceRepository.deleteAll();
        userRepository.deleteAll();
        userDbInitializer.init();
        resourceDbInitializer.init();
    }

    @Test
    void testsInitializer() {
        var first = ResourceDbInitializer.getDefaultResources().getFirst();
        cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        cut.setPatStoredValidator(ALWAYS_STORED);
        assertAll("initialization worked",
                () -> assertEquals(first.getResourceId(), cut.getRegisteredPeisResourceId(first.getResourceId(), VALID_AUTHORIZATION_HEADER).getResourceId()),
                () -> assertThrows(NotFoundException.class, () -> cut.getRegisteredPeisResourceId("nonexistent", VALID_AUTHORIZATION_HEADER))
        );
    }

    @Test
    void testForUpsertOK() {
        cut.setPatStoredValidator(ALWAYS_STORED);
        assertAll("Upsert OK new",
                () -> assertFalse(cut.upsertFind(buildRreguribody(YES, "non-existing-one"), UUID.randomUUID(), VALID_AUTHORIZATION_HEADER).alreadyRegistered()),
                () -> assertFalse(cut.upsertFind(buildRreguribody(POSSIBLE, "non-existing-two"), UUID.randomUUID(), VALID_AUTHORIZATION_HEADER).alreadyRegistered())
        );
        assertAll("Upsert OK existing",
                () -> assertTrue(cut.upsertFind(buildRreguribody(YES, ResourceDbInitializer.getDefaultResources().getFirst().getName()), UUID.randomUUID(), VALID_AUTHORIZATION_HEADER).alreadyRegistered()),
                () -> assertTrue(cut.upsertFind(buildRreguribody(POSSIBLE, ResourceDbInitializer.getDefaultResources().getFirst().getName()), UUID.randomUUID(), VALID_AUTHORIZATION_HEADER).alreadyRegistered())
        );
    }

    @Test
    void testForUpsertFail() {
        var reqId = UUID.randomUUID();
        assertAll("Upsert throws 409 (Conflict) is same inboundRequestId used twice",
                () -> assertFalse(cut.upsertFind(buildRreguribody(YES, "non-existing-one"), reqId, VALID_AUTHORIZATION_HEADER).alreadyRegistered()),
                () -> assertThrows(ConflictException.class, () -> cut.upsertFind(buildRreguribody(YES, "non-existing-one"), reqId, VALID_AUTHORIZATION_HEADER))
        );
    }



    private static RreguriBody buildRreguribody(MatchStatusEnum matchStatusEnum, String name) {
        RreguriBody rreguriBody = new RreguriBody();
        rreguriBody.setMatchStatus(matchStatusEnum);
        rreguriBody.setName(name);
        rreguriBody.setDescription("Test description");
        rreguriBody.setResourceScopes(java.util.List.of(RreguriBody.ResourceScopesEnum.OWNER));
        return rreguriBody;
    }

    @Test
    void testForUpdateOK() {
        cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        assertAll("Upsert OK new",
                () -> assertTrue(cut.updateStatus(ResourceDbInitializer.getDefaultResources().getFirst().getResourceId(), YES, VALID_AUTHORIZATION_HEADER).alreadyRegistered())
        );
    }

    @Test
    void testForUpdateError() {
        assertAll("Update errors",
                () -> assertThrows(NotFoundException.class, () -> cut.updateStatus("non-existing", YES, VALID_AUTHORIZATION_HEADER))
        );
    }

    @Test
    void testDeleteWithValidPadOk() {
        // set consumer for not to throw exception
        cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        assertAll("Correct states for deletion",
                () -> assertDoesNotThrow(() -> cut.deleteFind(ResourceDbInitializer.getDefaultResources().get(1).getResourceId(), NO.toString(), VALID_AUTHORIZATION_HEADER)),
                () -> assertThrows(NotFoundException.class, () -> cut.deleteFind("non-existed", YES.toString(), VALID_AUTHORIZATION_HEADER))
        );
    }

    @Test
    void testForDeleteInvalidPadErrors() {
        // set consumer for not to throw exception
        cut.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
        assertAll("Correct states for deletion",
                () -> assertThrows(UnauthorizedException.class, () -> cut.deleteFind(ResourceDbInitializer.getDefaultResources().get(1).getResourceId(), YES.toString(), VALID_AUTHORIZATION_HEADER))
        );

        cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        assertAll("Correct states for deletion",
                () -> assertDoesNotThrow(() -> cut.deleteFind(ResourceDbInitializer.getDefaultResources().get(1).getResourceId(), NO.toString(), VALID_AUTHORIZATION_HEADER)),
                () -> assertThrows(NotFoundException.class, () -> cut.deleteFind("non-existed", YES.toString(), VALID_AUTHORIZATION_HEADER))
        );
    }

    @Test
    void verifyValidDeleteState() {
        assertAll(
            // Match-yes
            // invalid scenarios
            () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), NO.toString())),
            () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), TIMEOUT.toString())),
            () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), "randomReason")),
            // valid scenarios
            () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), WITHDRAWN.toString())),
            () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), REMOVED.toString())),
            // Match-possible
            // invalid scenarios - match-withdrawn and random values are not valid for match-possible
            () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), "randomReason")),
            () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), WITHDRAWN.toString())),
            // valid scenarios - match-no, match-timeout and asset-removed are valid for match-possible
            () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), REMOVED.toString())),
            () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), TIMEOUT.toString())),
            () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), NO.toString()))
        );
    }
    private static RegisteredResource buildFindWithGivenState(MatchStatusEnum status) {
        return RegisteredResource.builder().matchStatus(status.toString()).build();
    }

    @Test
    void testAuthenticationErrors() {
        assertAll("Errors due to authentication",
                () -> {
                    cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
                    cut.setPatStoredValidator(ALWAYS_NOT_STORED);
                    assertThrows(InvalidRequestException.class, () -> cut.getRegisteredPeisResourceId(ResourceDbInitializer.getDefaultResources().getFirst().getResourceId(), VALID_AUTHORIZATION_HEADER), "Resource not found");
                },
                () -> {
                    cut.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
                    cut.setPatStoredValidator(ALWAYS_STORED);
                    assertThrows(UnauthorizedException.class, () -> cut.getRegisteredPeisResourceId(ResourceDbInitializer.getDefaultResources().getFirst().getResourceId(), VALID_AUTHORIZATION_HEADER), "Resource not found");
                }
        );
    }
}

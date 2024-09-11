package uk.org.ca.stub.simulator.service;

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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.*;
import static uk.org.ca.stub.simulator.service.RegisterService.validatePatAuthorizationFunction;
import uk.org.ca.stub.simulator.utils.MatchStatusEnum;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RegisterServiceTest {

    @Autowired
    RegisterService cut;
    @Autowired
    private ResourceDbInitializer resourceDbInitializer;

    @BeforeEach
    void setUp() {
        this.cut.setPatAuthorizationValidator(validatePatAuthorizationFunction);
        resourceDbInitializer.init();
    }

    @Test
    void testsInitializer() {
        var first = ResourceDbInitializer.DEFAULT_RESOURCES.getFirst();
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
                () -> assertTrue(cut.upsertFind(buildRreguribody(YES, ResourceDbInitializer.DEFAULT_RESOURCES.getFirst().getName()), UUID.randomUUID(), VALID_AUTHORIZATION_HEADER).alreadyRegistered()),
                () -> assertTrue(cut.upsertFind(buildRreguribody(POSSIBLE, ResourceDbInitializer.DEFAULT_RESOURCES.getFirst().getName()), UUID.randomUUID(), VALID_AUTHORIZATION_HEADER).alreadyRegistered())
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
        return rreguriBody;
    }

    @Test
    void testForUpdateOK() {
        cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        assertAll("Upsert OK new",
                () -> assertTrue(cut.updateStatus(ResourceDbInitializer.DEFAULT_RESOURCES.getFirst().getResourceId(), YES, VALID_AUTHORIZATION_HEADER).alreadyRegistered())
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
                () -> assertDoesNotThrow(() -> cut.deleteFind(ResourceDbInitializer.DEFAULT_RESOURCES.get(1).getResourceId(), NO.toString(), VALID_AUTHORIZATION_HEADER)),
                () -> assertThrows(NotFoundException.class, () -> cut.deleteFind("non-existed", YES.toString(), VALID_AUTHORIZATION_HEADER))
        );
    }

    @Test
    void testForDeleteInvalidPadErrors() {
        // set consumer for not to throw exception
        cut.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
        assertAll("Correct states for deletion",
                () -> assertThrows(UnauthorizedException.class, () -> cut.deleteFind(ResourceDbInitializer.DEFAULT_RESOURCES.get(1).getResourceId(), YES.toString(), VALID_AUTHORIZATION_HEADER))
        );

        cut.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        assertAll("Correct states for deletion",
                () -> assertDoesNotThrow(() -> cut.deleteFind(ResourceDbInitializer.DEFAULT_RESOURCES.get(1).getResourceId(), NO.toString(), VALID_AUTHORIZATION_HEADER)),
                () -> assertThrows(NotFoundException.class, () -> cut.deleteFind("non-existed", YES.toString(), VALID_AUTHORIZATION_HEADER))
        );
    }

    @Test
    void verifyValidDeleteState() {
        // todo: checked there is not other missing statuses
        assertAll("Correct states for deletion",
                // If the deletion_reason is ‘match-no’ or ‘match-timeout’ and the stored resource match_status is not ‘match-possible’ the stub C&A must return http status 400 (bad request).
                () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), NO.toString())),
                () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), TIMEOUT.toString())),

                // If the deletion_reason is ‘match-withdrawn’ or ‘asset-removed’ and the stored resource match_status is not ‘match-yes’ the stub C&A must return http status 400 (bad request).
                () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), REMOVED.toString())),
                () -> assertThrows(InvalidRequestException.class, () -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), WITHDRAWN.toString())),

                () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), NO.toString())),
                () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(POSSIBLE), TIMEOUT.toString())),

                () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), WITHDRAWN.toString())),
                () -> assertDoesNotThrow(() -> cut.verifyValidDeleteState(buildFindWithGivenState(YES), REMOVED.toString()))
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
                    assertThrows(InvalidRequestException.class, () -> cut.getRegisteredPeisResourceId(ResourceDbInitializer.DEFAULT_RESOURCES.getFirst().getResourceId(), VALID_AUTHORIZATION_HEADER), "Resource not found");
                },
                () -> {
                    cut.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
                    cut.setPatStoredValidator(ALWAYS_STORED);
                    assertThrows(UnauthorizedException.class, () -> cut.getRegisteredPeisResourceId(ResourceDbInitializer.DEFAULT_RESOURCES.getFirst().getResourceId(), VALID_AUTHORIZATION_HEADER), "Resource not found");
                }
        );
    }
}

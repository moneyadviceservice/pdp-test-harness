package uk.org.ca.stub.simulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.entity.Scope;
import uk.org.ca.stub.simulator.pojo.entity.UpsertFind;
import uk.org.ca.stub.simulator.repository.ResourceRepository;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.ConflictException;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.model.RreguriBody;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import uk.org.ca.stub.simulator.utils.MatchStatusEnum;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.*;

@Service
public class RegisterService extends AbstractAuthenticatedService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final JwtService jwtService;

    public RegisterService(UserRepository userRepository, ResourceRepository resourceRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.jwtService = jwtService;
    }

    public RegisteredResource getRegisteredPeisResourceId(String resourceId, String pat) {
        logger.debug(">>> Retrieving PEIs for resourceId {}", resourceId);
        this.validatePatStored(pat, userRepository);
        var registeredPei = resourceRepository.findByResourceId(resourceId)
                .orElseThrow(() -> new NotFoundException("Resource id not found"));

        this.validatePatAuthentication(pat, registeredPei);

        return registeredPei;
    }

    public UpsertFind upsertFind(RreguriBody rreguriBody, UUID xRequestID, String pat) throws JsonProcessingException {
        this.validatePatStored(pat, userRepository);
        var supportedStatesForPost = List.of(YES.toString(), POSSIBLE.toString());
        // The API won't allow this scenario as the other values are not valid, but just in case...
        if (!supportedStatesForPost.contains(rreguriBody.getMatchStatus().toString())) {
            throw new InvalidRequestException(String.format("The posted reason is not one of the allowed %s vs %s", rreguriBody.getMatchStatus(), supportedStatesForPost));
        }

        var foundByRequestId = resourceRepository.findByInboundRequestId(xRequestID.toString());
        if (foundByRequestId.isPresent()) {
            throw new ConflictException(String.format("xRequestId: %s already used!", xRequestID));
        }

        var foundByName = resourceRepository.findByName(rreguriBody.getName());
        // existing entry
        if (foundByName.isPresent())
            return new UpsertFind(UUID.fromString(foundByName.get().getResourceId()), true);

        String resourceId = UUID.randomUUID().toString();

        List<Scope> resourceScopes = getScopes(rreguriBody);

        // new entry
        var newFind = RegisteredResource.builder()
                .pat(pat)
                .name(rreguriBody.getName())
                .description(rreguriBody.getDescription())
                .matchStatus(rreguriBody.getMatchStatus().getValue())
                .resourceScopes(resourceScopes)
                .rpt(jwtService.generateRpt(xRequestID, resourceId, resourceScopes))
                .inboundRequestId(xRequestID.toString())
                .resourceId(resourceId)
                .build();
        var save = resourceRepository.save(newFind);
        return new UpsertFind(UUID.fromString(save.getResourceId()), false);
    }

    private static List<Scope> getScopes(RreguriBody rreguriBody) {
        return rreguriBody.getResourceScopes().stream().map(s -> {
            try {
                return new ObjectMapper().readValue(String.format("\"%s\"", s.toString()), Scope.class);
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException("Resource scopes could not be parsed", e);
            }
        }).toList();
    }

    public UpsertFind updateStatus(String resourceId, MatchStatusEnum matchStatus, final String pat) {
        // only support match-yes for patch
        if (matchStatus != MatchStatusEnum.YES){
            throw new InvalidRequestException(String.format("The match status for a patch command must be match-yes, but got %s", matchStatus));
        }
        this.validatePatStored(pat, userRepository);

        var find = resourceRepository.findByResourceId(resourceId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Find not found for {0}", resourceId)));

        this.validatePatAuthentication(pat, find);
        find.setMatchStatus(matchStatus.toString());
        return new UpsertFind(UUID.fromString(resourceRepository.save(find).getResourceId()), true);

    }

    protected List<MatchStatusEnum> supportedStatusForDeletion = List.of(
            NO,
            TIMEOUT,
            WITHDRAWN,
            REMOVED
    );

    public Void deleteFind(String resourceId, String deletionReason, String pat) {
        this.validatePatStored(pat, userRepository);
        var find = resourceRepository.findByResourceId(resourceId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Find not found for {0}", resourceId)));

        // If the PAT does not identify the user for the resource identified by the resource_id, return http status 401 (unauthorised).
        this.validatePatAuthentication(pat, find);
        verifyValidDeleteState(find, deletionReason);

        resourceRepository.delete(find);
        return null;
    }

    protected void verifyValidDeleteState(RegisteredResource resource, String deletionReason) {
        final String INVALID_STATE_FOR_DELETION_REASON_MSG = "The find match status must be %s for allow to be deleted using  %s or %s";
        //  the only allowed values for deletion_reason are ‘match-no’, ‘match-timeout’, ‘match-withdrawn’ and ‘asset-removed’. If the request does not conform, the C&A Stub must return http status 400
        if (supportedStatusForDeletion.stream().noneMatch(s -> s.toString().equals(deletionReason))) {
            throw new InvalidRequestException(String.format("The delete reason is not one of the allowed %s vs %s", resource.getMatchStatus(), supportedStatusForDeletion));
        }

        // If the deletion_reason is ‘match-no’ or ‘match-timeout’ and the stored resource match_status is not ‘match-possible’ the stub C&A must return http status 400 (bad request).
        if ((deletionReason.equals(NO.toString()) || deletionReason.equals(TIMEOUT.toString())) && !resource.getMatchStatus().equals(POSSIBLE.toString())) {
            throw new InvalidRequestException(String.format(INVALID_STATE_FOR_DELETION_REASON_MSG, POSSIBLE, NO, TIMEOUT));
        }

        // If the deletion_reason is ‘match-withdrawn’ or ‘asset-removed’ and the stored resource match_status is not ‘match-yes’ the stub C&A must return http status 400 (bad request).
        if ((deletionReason.equals(WITHDRAWN.toString()) || deletionReason.equals(REMOVED.toString())) && !resource.getMatchStatus().equals(YES.toString())) {
            throw new InvalidRequestException(String.format(INVALID_STATE_FOR_DELETION_REASON_MSG, YES, WITHDRAWN, REMOVED));
        }
    }
}

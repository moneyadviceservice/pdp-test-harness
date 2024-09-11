package uk.org.ca.stub.simulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import uk.org.ca.stub.simulator.repository.ResourceRepository;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResult;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResultPermissions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static uk.org.ca.stub.simulator.rest.model.IntrospectionResult.TokenTypeEnum.PMT_FOR_NEGATIVE_TEST;
import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

@Service
public class IntrospectService extends AbstractAuthenticatedService {

    private final ResourceRepository resourceRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public IntrospectService(ResourceRepository resourceRepository, JwtService jwtService, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public IntrospectionResult introspectToken(String token, String pat) {
        this.validatePatStored(pat, userRepository);

        String decodedString;
        try {
            // Decode the Base64 token
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            decodedString = new String(decodedBytes);
        } catch (IllegalArgumentException iar) {
            throw new InvalidRequestException("Token is malformed and cannot be decoded");
        }

        // Parse the access token within the RTP
        String accessToken;
        try {
            var jsonMapper = new JsonMapper();
            var jsonObject = jsonMapper.readValue(decodedString, Map.class);
            accessToken = (String) jsonObject.get("access_token");
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException("RPT token is malformed and could not be processed", e);
        }

        // validate token signature
        try {
            jwtService.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            // proceed - for expired token we want 200 response with active = false
        } catch (JwtException e) {
            throw new InvalidRequestException("RPT token signature could not be validated", e);
        }

        var resource = resourceRepository.findByRpt(token)
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        this.validatePatAuthentication(pat, resource);

        var result = new IntrospectionResult();

        // Build the response
        try {
            var exp = jwtService.getExpirirationTimestamp(accessToken);
            result.exp(exp);
            result.active(!jwtService.isTimestampExpired(exp));
            result.tokenType(IntrospectionResult.TokenTypeEnum.PENSION_DASHBOARD_RPT);
            result.iss(jwtService.getIssuer(accessToken));
            result.permissions(jwtService.getPermissions(accessToken, resource.getResourceId()));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException("Access token is malformed and could not be processed", e);
        }

        var permissions = result.getPermissions().getFirst();
        switch (token) {
            case RPT_WRONG_TOKEN_TYPE -> {
                result.tokenType(PMT_FOR_NEGATIVE_TEST);
                result.permissions(buildPermissionList(resource.getPat(), permissions.getExp(), permissions.getResourceScopes()));
            }
            case RPT_EMPTY_PERM_ARRAY -> result.permissions(new ArrayList<>());
            case RPT_SCOPES_NO_OWNER -> result.permissions(buildPermissionList(resource.getPat(), permissions.getExp(), permissions.getResourceScopes()));
            case RPT_EXPIRED_ACTIVE_TOKEN -> {
                result.exp(getExpiredTimestampInSeconds());
                result.permissions(buildPermissionList(resource.getPat(), permissions.getExp(), permissions.getResourceScopes()));
            }
            case RPT_EXPIRED_PERMISSION -> result.permissions(buildPermissionList(resource.getPat(), getExpiredTimestampInSeconds(), permissions.getResourceScopes()));
        }
        return result;
    }

    private List<IntrospectionResultPermissions> buildPermissionList(String pat, long exp, List<IntrospectionResultPermissions.ResourceScopesEnum> scopes) {
        List<IntrospectionResultPermissions> permissionsList = new ArrayList<>();
        var perm = buildPerm(pat, exp, scopes);
        permissionsList.add(perm);
        return permissionsList;
    }

    private IntrospectionResultPermissions buildPerm(String pat, long exp, List<IntrospectionResultPermissions.ResourceScopesEnum> scopes) {
        var perm = new IntrospectionResultPermissions();
        perm.resourceId(UUID.fromString(getLastRegisteredIdResourceByPat(pat)));
        perm.exp(exp);
        scopes.forEach(perm::addResourceScopesItem);
        return perm;
    }

    private String getLastRegisteredIdResourceByPat(String pat) {
        var resource = resourceRepository.findFirstByPatOrderByCreatedAtDesc(pat)
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        return resource.getResourceId();
    }

    private static long getExpiredTimestampInSeconds() {
        return Date.from(Instant.now().minus(1095, ChronoUnit.DAYS)).getTime() / 1000;
    }
}

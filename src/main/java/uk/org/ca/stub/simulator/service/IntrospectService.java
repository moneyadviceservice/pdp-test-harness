package uk.org.ca.stub.simulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.repository.ResourceRepository;
import uk.org.ca.stub.simulator.repository.UserRepository;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResult;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResultPermissions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer.*;
import static uk.org.ca.stub.simulator.rest.model.IntrospectionResult.TokenTypeEnum.PMT_FOR_NEGATIVE_TEST;

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

    public IntrospectionResult introspectToken(String token, String authenticationHeader) {
        var pat = getTokenFromHeader(authenticationHeader);
        this.validatePatStored(pat, userRepository);
        // Decode the Base64 token
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedString = new String(decodedBytes);

        // Parse the access token within the RTP
        String accessToken;
        try {
            var jsonMapper = new JsonMapper();
            var jsonObject = jsonMapper.readValue(decodedString, Map.class);
            accessToken = (String) jsonObject.get("access_token");
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("RPT token is malformed and could not be processed", e);
        }

        // validate token signature
        try {
            jwtService.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            // proceed - for expired token we want 200 response with active = false
        } catch (JwtException e) {
            throw new IllegalArgumentException("RPT token signature could not be validated", e);
        }

        var resource = resourceRepository.findByRpt(token)
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        this.validatePatAuthentication(pat, resource);

        IntrospectionResult result = new IntrospectionResult();

        // Build the response
        try {
            Long exp = jwtService.getExpirirationTimestamp(accessToken);
            result.exp(exp);
            result.active(!jwtService.isTimestampExpired(exp));
            result.tokenType(IntrospectionResult.TokenTypeEnum.PENSION_DASHBOARD_RPT);
            result.iss(jwtService.getIssuer(accessToken));
            result.permissions(jwtService.getPermissions(accessToken, resource.getResourceId()));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Access token is malformed and could not be processed", e);
        }

        IntrospectionResultPermissions permissions = result.getPermissions().getFirst();
        switch (token) {
            case RPT_WRONG_TOKEN_TYPE:
                result.tokenType(PMT_FOR_NEGATIVE_TEST);
                result.permissions(buildPermissionList(resource.getPat(), permissions.getExp(), permissions.getResourceScopes()));
                break;
            case RPT_EMPTY_PERM_ARRAY:
                result.permissions(new ArrayList<>());
                break;
            case RPT_SCOPES_NO_OWNER:
                result.permissions(buildPermissionList(resource.getPat(), permissions.getExp(), permissions.getResourceScopes()));
                break;
            case RPT_EXPIRED_ACTIVE_TOKEN:
                result.exp(getExpiredTimestampInSeconds());
                result.permissions(buildPermissionList(resource.getPat(), permissions.getExp(), permissions.getResourceScopes()));
                break;
            case RPT_EXPIRED_PERMISSION:
                result.permissions(buildPermissionList(resource.getPat(), getExpiredTimestampInSeconds(), permissions.getResourceScopes()));
                break;
            default:
                // do nothing
        }

        return result;
    }

    private List<IntrospectionResultPermissions> buildPermissionList(String pat, long exp, List<IntrospectionResultPermissions.ResourceScopesEnum> scopes) {
        List<IntrospectionResultPermissions> permissionsList = new ArrayList<>();
        IntrospectionResultPermissions perm = buildPerm(pat, exp, scopes);
        permissionsList.add(perm);
        return permissionsList;
    }

    private IntrospectionResultPermissions buildPerm(String pat, long exp, List<IntrospectionResultPermissions.ResourceScopesEnum> scopes) {
        IntrospectionResultPermissions perm = new IntrospectionResultPermissions();
        perm.resourceId(UUID.fromString(getLastRegisteredIdResourceByPat(pat)));
        perm.exp(exp);
        scopes.forEach(perm::addResourceScopesItem);
        return perm;
    }

    private String getLastRegisteredIdResourceByPat(String pat) {
        RegisteredResource resource = resourceRepository.findFirstByPatOrderByCreatedAtDesc(pat)
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        return resource.getResourceId();
    }

    private static long getExpiredTimestampInSeconds() {
        return Date.from(Instant.now().minus(1095, ChronoUnit.DAYS)).getTime() / 1000;
    }
}

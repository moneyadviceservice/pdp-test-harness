package uk.org.ca.stub.simulator.service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer;
import uk.org.ca.stub.simulator.rest.exception.InvalidRequestException;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.exception.UnauthorizedException;
import uk.org.ca.stub.simulator.rest.model.PermBody;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.*;
import static uk.org.ca.stub.simulator.utils.Commons.RESOURCE_ID_CLAIM_NAME;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PermServiceTest {

    @Autowired
    PermService permService;

    @Autowired
    JwtService jwtService;

    @Test
    void testValidResource() throws Exception {
        permService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        permService.setPatStoredValidator(ALWAYS_STORED);
        var firstResource = ResourceDbInitializer.DEFAULT_RESOURCES.getFirst();
        var body = new PermBody();
        body.setResourceId(UUID.fromString(firstResource.getResourceId()));
        body.resourceScopes(List.of(PermBody.ResourceScopesEnum.VALUE, PermBody.ResourceScopesEnum.OWNER));

        var pmt = permService.generatePMT(body, UUID.randomUUID(), VALID_AUTHORIZATION_HEADER);
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtService.getSignature().getBytes(StandardCharsets.UTF_8)))
                .build();
        try {
            var mapper = new JsonMapper();
            var decodedPmt = Base64.getDecoder().decode(pmt);
            var tokenMap = mapper.readValue(new String(decodedPmt), Map.class);
            assertEquals(JwtService.TokenInstance.PMT.getType(), tokenMap.get("token_type"));
            var jwt = tokenMap.get("access_token").toString();
            assertEquals(body.getResourceId().toString(), jwtParser.parseSignedClaims(jwt).getPayload().get("resource_id"));
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
    }

    @Test
    void testInvalidResource(){
        permService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        permService.setPatStoredValidator(ALWAYS_STORED);
        var body = new PermBody();
        body.setResourceId(UUID.randomUUID());
        body.resourceScopes(List.of(PermBody.ResourceScopesEnum.VALUE, PermBody.ResourceScopesEnum.OWNER));

        assertThrows(NotFoundException.class, () -> permService.generatePMT(body, UUID.randomUUID(), VALID_AUTHORIZATION_HEADER), "Resource not found");
    }

    @Test
    void testAuthenticationErrors() {
        var firstResource = ResourceDbInitializer.DEFAULT_RESOURCES.getFirst();
        var body = new PermBody();
        body.setResourceId(UUID.fromString(firstResource.getResourceId()));
        body.resourceScopes(List.of(PermBody.ResourceScopesEnum.VALUE, PermBody.ResourceScopesEnum.OWNER));

        assertAll("Errors due to authentication",
                () -> { // the pat is not stored
                    permService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
                    permService.setPatStoredValidator(ALWAYS_NOT_STORED);
                    assertThrows(InvalidRequestException.class, () -> permService.generatePMT(body, UUID.randomUUID(), VALID_AUTHORIZATION_HEADER), "Resource not found");
                },
                () -> { // the pat is stored but is not the one associated with the resource
                    permService.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
                    permService.setPatStoredValidator(ALWAYS_STORED);
                    assertThrows(UnauthorizedException.class, () -> permService.generatePMT(body, UUID.randomUUID(), VALID_AUTHORIZATION_HEADER), "Resource not found");
                },
                () -> { // the header doesn't look like a bearer
                    permService.setPatAuthorizationValidator(ALWAYS_UNAUTHORIZED);
                    permService.setPatStoredValidator(ALWAYS_STORED);
                    assertThrows(InvalidRequestException.class, () -> permService.generatePMT(body, UUID.randomUUID(), INVALID_AUTHORIZATION_HEADER), "Resource not found");
                }
        );
    }
}

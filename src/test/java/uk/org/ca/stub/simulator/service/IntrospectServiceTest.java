package uk.org.ca.stub.simulator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.org.ca.stub.simulator.rest.exception.NotFoundException;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResult;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResultPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer.*;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.TOKEN_WITH_INVALID_SIGNATURE;
import static uk.org.ca.stub.simulator.service.AuthenticatedServiceTest.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IntrospectServiceTest {
    @Autowired
    IntrospectService introspectService;

    @Autowired
    JwtService jwtService;

    @Test
    void testValidToken() throws Exception {
        introspectService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        introspectService.setPatStoredValidator(ALWAYS_STORED);
        IntrospectionResult res = introspectService.introspectToken(VALID_RPT_TOKEN, VALID_AUTHORIZATION_HEADER);

        try {
            var active = res.getActive();
            var tokenType = res.getTokenType();
            var exp = res.getExp();
            var issuer = res.getIss();
            var permissions = res.getPermissions();

            List<IntrospectionResultPermissions> targetPermissions = new ArrayList<>();

            IntrospectionResultPermissions permission = new IntrospectionResultPermissions();
            permission.resourceId(UUID.fromString("92476c2f-25b8-4d87-afde-18a9ee2631dc")); //resId matching the VALID_RPT_TOKEN
            permission.exp(exp);
            permission.addResourceScopesItem(IntrospectionResultPermissions.ResourceScopesEnum.OWNER);

            targetPermissions.add(permission);

            assertAll(
                    () -> assertEquals(true,active),
                    () -> assertEquals(IntrospectionResult.TokenTypeEnum.PENSION_DASHBOARD_RPT,tokenType),
                    () -> assertEquals(Long.valueOf("1813411040"),exp),
                    () -> assertEquals("https://stub-cas.co.uk",issuer),
                    () -> assertEquals(targetPermissions,permissions)
            );
        } catch (Exception e) {
            throw new Exception("The response could not be parsed correctly", e);
        }
    }

    @Test
    void testExpiredToken() throws Exception {
        introspectService.setPatAuthorizationValidator(ALWAYS_AUTHORIZED);
        introspectService.setPatStoredValidator(ALWAYS_STORED);
        IntrospectionResult res = introspectService.introspectToken(EXPIRED_RPT_TOKEN, VALID_AUTHORIZATION_HEADER);

        try {
            var active = res.getActive();
            var tokenType = res.getTokenType();
            var exp = res.getExp();
            var issuer = res.getIss();
            var permissions = res.getPermissions();

            List<IntrospectionResultPermissions> targetPermissions = new ArrayList<>();

            IntrospectionResultPermissions permission = new IntrospectionResultPermissions();
            permission.resourceId(UUID.fromString("62933ca9-447e-4ce0-bb39-124e9fa3214f")); //resId matching the EXPIRED_RPT_TOKEN
            permission.exp(exp);
            permission.addResourceScopesItem(IntrospectionResultPermissions.ResourceScopesEnum.OWNER);

            targetPermissions.add(permission);

            assertAll(
                    () -> assertEquals(false,active),
                    () -> assertEquals(IntrospectionResult.TokenTypeEnum.PENSION_DASHBOARD_RPT,tokenType),
                    () -> assertEquals(Long.valueOf("1624197800"),exp),
                    () -> assertEquals("https://stub-cas.co.uk",issuer),
                    () -> assertEquals(targetPermissions,permissions)
            );
        } catch (Exception e) {
            throw new Exception("The response could not be parsed correctly", e);
        }
    }

    @Test
    void testUnusedToken() {
        assertThrows(NotFoundException.class,() -> introspectService.introspectToken(UNUSED_RPT_TOKEN, VALID_AUTHORIZATION_HEADER));
    }

    @Test
    void testMalformedToken() {
        assertThrows(IllegalArgumentException.class,() -> introspectService.introspectToken("malformed-token", VALID_AUTHORIZATION_HEADER));
    }

    @Test
    void testInvalidSignatureToken() {
        assertThrows(IllegalArgumentException.class,() -> introspectService.introspectToken(TOKEN_WITH_INVALID_SIGNATURE, VALID_AUTHORIZATION_HEADER));
    }
}

package uk.org.ca.stub.simulator.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResultPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer.EXPIRED_RPT_ACCESS_TOKEN;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.ResourceDbInitializer.VALID_RPT_ACCESS_TOKEN;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.EXPIRED_TOKEN_PAT;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.NOT_EXPIRED_TOKEN_PAT;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.TOKEN_WITH_INVALID_SIGNATURE;
import static uk.org.ca.stub.simulator.configuration.dbinitializer.UserDbInitializer.TOKEN_WITH_ISSUER_AND_PERMISSIONS;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtServiceTest {

    @Autowired
    JwtService cut;

    @Test
    void validateToken() {
        assertAll(
                () -> assertDoesNotThrow(() -> cut.validateToken(VALID_RPT_ACCESS_TOKEN)),
                () -> assertThrows(ExpiredJwtException.class, () -> cut.validateToken(EXPIRED_RPT_ACCESS_TOKEN)),
                () -> assertThrows(JwtException.class, () -> cut.validateToken(TOKEN_WITH_INVALID_SIGNATURE))
        );
    }

    @Test
    void isTokenExpired() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> cut.isTokenExpired(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.isTokenExpired("")),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.isTokenExpired("not-a-token")),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.isTokenExpired("not.a.token")),
                () -> assertTrue(cut.isTokenExpired(EXPIRED_TOKEN_PAT)),
                () -> assertFalse(cut.isTokenNotExpired(EXPIRED_TOKEN_PAT)),
                () -> assertTrue(cut.isTokenNotExpired(NOT_EXPIRED_TOKEN_PAT)),
                () -> assertFalse(cut.isTokenExpired(NOT_EXPIRED_TOKEN_PAT))
        );
    }

    @Test
    void isTimestampExpired() {
        assertAll(
                () -> assertTrue(cut.isTimestampExpired(System.currentTimeMillis()/1000 - 10)),
                () -> assertFalse(cut.isTimestampExpired(System.currentTimeMillis()/1000 + 10))
        );
    }

    @Test
    void getExpirationTimestamp() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getExpirirationTimestamp(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getExpirirationTimestamp("")),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getExpirirationTimestamp("not-token")),
                () -> assertEquals(Long.valueOf("2538473359"),cut.getExpirirationTimestamp(NOT_EXPIRED_TOKEN_PAT))
        );
    }

    @Test
    void getIssuer() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getIssuer(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getIssuer("")),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getIssuer("not-token")),
                () -> assertEquals("https://stub-cas.co.uk",cut.getIssuer(TOKEN_WITH_ISSUER_AND_PERMISSIONS)),
                () -> assertEquals(null,cut.getIssuer(NOT_EXPIRED_TOKEN_PAT)) // token has no issuer field
        );
    }

    @Test
    void getPermissions() {
        var resId = UUID.randomUUID();

        List<IntrospectionResultPermissions> permissionsList = new ArrayList<>();

        IntrospectionResultPermissions res = new IntrospectionResultPermissions();
        res.resourceId(resId);
        res.exp(Long.valueOf("1813411040"));
        res.addResourceScopesItem(IntrospectionResultPermissions.ResourceScopesEnum.OWNER);
        permissionsList.add(res);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getPermissions(null,resId.toString())),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getPermissions("",resId.toString())),
                () -> assertThrows(IllegalArgumentException.class, () -> cut.getPermissions("not-token",resId.toString())),
                () -> assertEquals(1,cut.getPermissions(VALID_RPT_ACCESS_TOKEN,resId.toString()).size()),
                () -> assertEquals(permissionsList,cut.getPermissions(VALID_RPT_ACCESS_TOKEN,resId.toString()))
        );
    }
}

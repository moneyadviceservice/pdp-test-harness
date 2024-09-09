package uk.org.ca.stub.simulator.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.Scope;
import uk.org.ca.stub.simulator.rest.model.IntrospectionResultPermissions;
import uk.org.ca.stub.simulator.rest.model.RreguriBody;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static uk.org.ca.stub.simulator.utils.Commons.RESOURCE_ID_CLAIM_NAME;
import static uk.org.ca.stub.simulator.utils.Commons.RESOURCE_SCOPES_CLAIM_NAME;

@Getter
@Setter
@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "cas")
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public enum TokenInstance {
        UAT("pension_dashboard_uat", 1095, ChronoUnit.DAYS), // technical decision, 3y represented in days
        PAT("pension_dashboard_pat", 1095, ChronoUnit.DAYS), // technical decision, 3y represented in days
        PMT("pension_dashboard_pmt", 60, ChronoUnit.SECONDS), // spec value
        RPT("pension_dashboard_rpt", 1095, ChronoUnit.DAYS); // technical decision, 3y represented in days

        @Getter
        private final String type;
        private final Integer lifespan;
        private final ChronoUnit unit;

        TokenInstance(String type, Integer lifespan, ChronoUnit unit) {
            this.type = type;
            this.lifespan = lifespan;
            this.unit = unit;
        }

    }

    private String signature;

    public String getEncodedToken(TokenInstance token, Map<String, Object> claims, UUID xRequestID) throws JsonProcessingException {
        var jws = getSignedJwtToken(claims, xRequestID, token.lifespan, token.unit);
        return getTokenAsString(token, jws);
    }

    private String getTokenAsString(TokenInstance token, String jws) throws JsonProcessingException {
        var tokenMap = new HashMap<String, Object>();
        tokenMap.put("access_token", jws);
        tokenMap.put("token_type", token.getType());
        var jsonMapper = new JsonMapper();
        var ticket = jsonMapper.writeValueAsString(tokenMap);
        return Base64.getEncoder().encodeToString(ticket.getBytes());
    }

    public String getSignedJwtToken(Map<String, Object> claims, UUID xRequestID, Integer lifespan, ChronoUnit unit) {
        var now = Instant.now();
        return Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .claims(claims)
                .id(xRequestID.toString())
                .issuedAt(Date.from(now))
                .issuer("https://stub-cas.co.uk")
                .expiration(Date.from(now.plus(lifespan, unit)))
                .signWith(Keys.hmacShaKeyFor(signature.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public void validateToken(String accessToken) {
        byte[] sigBytes = signature.getBytes(StandardCharsets.UTF_8);
        Jwts.parser()
                .verifyWith(new SecretKeySpec(sigBytes, "HmacSHA256"))
                .build()
                .parseSignedClaims(accessToken);
    }

    public boolean isTokenNotExpired(String accessToken) throws JsonProcessingException {
        return !isTokenExpired(accessToken);
    }

    public boolean isTokenExpired(String accessToken) throws JsonProcessingException {
        var exp = getExpirirationTimestamp(accessToken);
        return isTimestampExpired(exp);
    }

    public boolean isTimestampExpired(Long exp) {
        return System.currentTimeMillis() / 1_000 > exp;
    }

    public Long getExpirirationTimestamp(String accessToken) throws JsonProcessingException {
        var stringObjectMap = parseTokenToStringObjectMap(accessToken);
        return ((Number) stringObjectMap.get("exp")).longValue();
    }

    public String getIssuer(String accessToken) throws JsonProcessingException {
        var stringObjectMap = parseTokenToStringObjectMap(accessToken);
        return (String) stringObjectMap.get("iss");
    }

    public List<IntrospectionResultPermissions> getPermissions(String accessToken, String resourceId) throws JsonProcessingException {
        var stringObjectMap = parseTokenToStringObjectMap(accessToken);

        var res = new IntrospectionResultPermissions();

        res.resourceId(UUID.fromString(resourceId));
        var exp = ((Number) stringObjectMap.get("exp")).longValue();
        res.exp(exp);

        List<IntrospectionResultPermissions> permissionsList = new ArrayList<>();
        var permissions = stringObjectMap.get(RESOURCE_SCOPES_CLAIM_NAME);
        // Some RPT used for testing have the wrong name for the `resource_scopes` field. `resourceScopes`
        if (permissions == null) {
            permissions = stringObjectMap.get("resourceScopes");
        }
        if (permissions != null) {
            // due to /rreguri Open API specs allow "delegate" as scope, but /introspect doesn't, it's ignored with
            // this `continue` until it's clarified and the specs are updated.
            ((ArrayList<String>) permissions).stream()
                    .filter(perm -> !RreguriBody.ResourceScopesEnum.DELEGATE.getValue().equals(perm))
                    .map(IntrospectionResultPermissions.ResourceScopesEnum::fromValue)
                    .forEach(res::addResourceScopesItem);
            permissionsList.add(res);
        }

        return permissionsList;
    }

    private Map<String, Object> parseTokenToStringObjectMap(String accessToken) throws JsonProcessingException {
        Map<String, Object> stringObjectMap;

        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("JWT token is null or empty");
        }
        var chunks = accessToken.split("\\.");
        if (chunks.length != 3) {
            throw new IllegalArgumentException("JWT token is malformed");
        }
        var decoder = Base64.getUrlDecoder();
        var payload = new String(decoder.decode(chunks[1]));
        var x = new ObjectMapper();
        stringObjectMap = x.readValue(payload, new TypeReference<>() {});

        return stringObjectMap;
    }

    // todo: validate PMT and RPT format
    public String generatePmt(UUID xRequestID, String resourceId, List<Scope> resourceScopes) throws JsonProcessingException {
        var claims = Map.of(RESOURCE_ID_CLAIM_NAME, resourceId, RESOURCE_SCOPES_CLAIM_NAME, resourceScopes);
        return getEncodedToken(JwtService.TokenInstance.PMT, claims, xRequestID);
    }

    // todo: validate PMT and RPT format
    public String generateRpt(UUID xRequestID, String resourceId, List<Scope> resourceScopes) throws JsonProcessingException {
        var claims = Map.of(RESOURCE_ID_CLAIM_NAME, resourceId, RESOURCE_SCOPES_CLAIM_NAME, resourceScopes);
        return getEncodedToken(JwtService.TokenInstance.RPT, claims, xRequestID);
    }
}

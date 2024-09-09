package uk.org.ca.stub.simulator.configuration.dbinitializer;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.entity.Scope;
import uk.org.ca.stub.simulator.repository.ResourceRepository;

import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;
import static uk.org.ca.stub.simulator.utils.MatchStatusEnum.*;

import java.util.List;

@Service
public class ResourceDbInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ResourceDbInitializer.class);

    private final ResourceRepository resourceRepository;

    public ResourceDbInitializer(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public static final List<RegisteredResource> DEFAULT_RESOURCES = List.of(
            RegisteredResource.builder()
                    .resourceId("92476c2f-25b8-4d87-afde-18a9ee2631dc")
                    .name("urn:pei:0e55140a-87d3-41cf-b6f7-bc822a4c3c3b:6e29eeb8-814c-44a6-a43f-b4830f3f4590")
                    .friendlyName("Resource with valid rpt token")
                    .description("description one")
                    .matchStatus(YES.toString())
                    .rpt(VALID_RPT_TOKEN)
                    .resourceScopes(List.of(Scope.OWNER))
                    .pat(HAPPY_PATH_PAT_01)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("62933ca9-447e-4ce0-bb39-124e9fa3214f")
                    .name("0b99ab46-500f-4f14-9e0b-09c8632caf4a:022d4035-5892-411e-859e-a87d6f6c9eea")
                    .friendlyName("Resource with expired rpt token")
                    .description("description two")
                    .matchStatus(POSSIBLE.toString())
                    .rpt(EXPIRED_RPT_TOKEN)
                    .resourceScopes(List.of(Scope.OWNER))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("970f7c7b-3240-4775-bce1-7b394837f850")
                    .name("f2529be2-7f27-4661-ac51-758f1d04f2fe:5c580512-2835-44df-b0a7-174e60e974c7")
                    .friendlyName("Second resource with valid rpt token")
                    .description("description three")
                    .matchStatus(POSSIBLE.toString())
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("e54a365a-a80a-489a-a13d-7dc269c2aaaf")
                    .name("negative-scenario:rpt-wrong-token-type")
                    .friendlyName("Resource returning wrong token type in rpt introspection")
                    .description("rpt returning pmt token type")
                    .matchStatus(YES.toString())
                    .rpt(RPT_WRONG_TOKEN_TYPE)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("6e401326-690d-4481-87a4-5da57dec742b")
                    .name("negative-scenario:rpt-empty-perm-array")
                    .friendlyName("Resource returning empty permissions array in rpt introspection")
                    .description("rpt returning an empty permission array")
                    .matchStatus("match-yes")
                    .rpt(RPT_EMPTY_PERM_ARRAY)
                    .resourceScopes(List.of())
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("3cbfea2f-66db-4654-ae24-ad6d8d777ccd")
                    .name("negative-scenario:rpt-scopes-no-owner")
                    .friendlyName("Resource returning scope with no owner in rpt introspection")
                    .description("rpt not containing owner within scopes")
                    .matchStatus(YES.toString())
                    .rpt(RPT_SCOPES_NO_OWNER)
                    .resourceScopes(List.of(Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("4b45b78c-61a8-468a-96db-1fe8f0d61656")
                    .name("negative-scenario:rpt-expired-token-active")
                    .friendlyName("Resource returning active = true with expired token in rpt introspection")
                    .description("rpt which is expired but reported as active")
                    .matchStatus(YES.toString())
                    .rpt(RPT_EXPIRED_ACTIVE_TOKEN)
                    .resourceScopes(List.of(Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("21762ed0-8dde-429e-a48a-bdf52073e582")
                    .name("negative-scenario:rpt-expired-permission")
                    .friendlyName("Resource returning a permission with expired token in rpt introspection")
                    .description("rpt where a permission is expired")
                    .matchStatus(YES.toString())
                    .rpt(RPT_EXPIRED_PERMISSION)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("dc54132a-dbbf-4f4c-a250-fc71c9e7e96f")
                    .name("assertion-scenario:introspect-500")
                    .friendlyName("Assertion Resource for /introspect 500 server error scenario")
                    .description("rpt where a permission is expired")
                    .matchStatus(YES.toString())
                    .rpt(ASSERTION_RPT_INTROSPECT_500_SERVER_ERROR)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("16ea8f24-86cd-4827-aea9-ce9a9dbe3a3e")
                    .name("assertion-scenario:introspect-502")
                    .friendlyName("Assertion Resource for /introspect 502 bad gateway scenario")
                    .description("rpt where a permission is expired")
                    .matchStatus(YES.toString())
                    .rpt(ASSERTION_RPT_INTROSPECT_502_BAD_GATEWAY)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("f5e8770f-2c45-435c-9351-b9c96b656de6")
                    .name("assertion-scenario:introspect-503")
                    .friendlyName("Assertion Resource for /introspect 503 service unavailable scenario")
                    .description("rpt where a permission is expired")
                    .matchStatus(YES.toString())
                    .rpt(ASSERTION_RPT_INTROSPECT_503_SERVICE_UNAVAILABLE)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("77c0249a-5cc7-428a-8a84-ddc28b86733f")
                    .name("assertion-scenario:introspect-504")
                    .friendlyName("Assertion Resource for /introspect 504 gateway timeout scenario")
                    .description("rpt where a permission is expired")
                    .matchStatus(YES.toString())
                    .rpt(ASSERTION_RPT_INTROSPECT_504_GATEWAY_TIMEOUT)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(HAPPY_PATH_PAT_02)
                    .build()
    );

    @PostConstruct
    public void init() {
        logger.debug("Adding {} finds to the database", DEFAULT_RESOURCES.size());
        resourceRepository.deleteAll();
        resourceRepository.saveAll(DEFAULT_RESOURCES);
    }

}

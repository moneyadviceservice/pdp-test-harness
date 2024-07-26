package uk.org.ca.stub.simulator.configuration.dbinitializer;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.RegisteredResource;
import uk.org.ca.stub.simulator.entity.Scope;
import uk.org.ca.stub.simulator.repository.ResourceRepository;
import uk.org.ca.stub.simulator.service.RegisterService;

import java.util.List;

@Service
public class ResourceDbInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ResourceDbInitializer.class);

    private final ResourceRepository resourceRepository;

    public ResourceDbInitializer(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @SuppressWarnings("java:S6418")
    public static final String VALID_RPT_TOKEN = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2lNVEprTldNMU1UTXROalZsTVMwME5HVTJMVGswT1dZdFptTTNObUV6TmpVNFpHTTFJaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHNpYjNkdVpYSWlYU3dpYW5ScElqb2lPVFZqTmpKalpESXRNMlprTUMwME1EVmtMVGt4TTJZdE9XTm1NR0l6TjJJNU5tTTFJaXdpYVdGMElqb3hOekU0T0RBek1EUXdMQ0pwYzNNaU9pSm9kSFJ3Y3pvdkwzTjBkV0l0WTJGekxtTnZMblZySWl3aVpYaHdJam94T0RFek5ERXhNRFF3ZlEuRGw4cjVhTlpjV2I0NDViRmZwQ2xZbVZ3NWwyOTc3WDFManN2em1fREo5RSIsInRva2VuX3R5cGUiOiJwZW5zaW9uX2Rhc2JvYXJkX3JwdCJ9";
    @SuppressWarnings("java:S6418")
    public static final String VALID_RPT_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZXNvdXJjZUlkIjoiMTJkNWM1MTMtNjVlMS00NGU2LTk0OWYtZmM3NmEzNjU4ZGM1IiwicmVzb3VyY2VTY29wZXMiOlsib3duZXIiXSwianRpIjoiOTVjNjJjZDItM2ZkMC00MDVkLTkxM2YtOWNmMGIzN2I5NmM1IiwiaWF0IjoxNzE4ODAzMDQwLCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzNDExMDQwfQ.Dl8r5aNZcWb445bFfpClYmVw5l2977X1Ljsvzm_DJ9E";
    @SuppressWarnings("java:S6418")
    public static final String UNUSED_RPT_TOKEN = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2lNakkzT1dVeFlXSXRaVFZrTXkwME16QXdMVGt6TjJVdE16WTRZekZqWXpnelpUbGhJaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHNpYjNkdVpYSWlYU3dpYW5ScElqb2laak5tTnpBeE9EQXRaR1ZrTVMwME16Wm1MVGhrWkRVdFptRmhPVGxqWWpFeU4yWmlJaXdpYVdGMElqb3hOekU0TnpJMU1EazJMQ0pwYzNNaU9pSm9kSFJ3Y3pvdkwzTjBkV0l0WTJGekxtTnZMblZySWl3aVpYaHdJam94T0RFek16TXpNRGsyZlEuOXkxc2piYUt4MFU3MEFIZTBaN2hWd2VFUXBFYzg2bEZPQXJQa3ZNV3RYbyIsInRva2VuX3R5cGUiOiJwZW5zaW9uX2Rhc2JvYXJkX3JwdCJ9";
    @SuppressWarnings("java:S6418")
    public static final String EXPIRED_RPT_TOKEN = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2lZelV5WkdObE9UY3RNemxtWmkwMFpEZG1MV0V6Tm1FdFlXWTJNVE13WkdVNFpXWTBJaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHNpYjNkdVpYSWlYU3dpYW5ScElqb2lPVFZqTmpKalpESXRNMlprTUMwME1EVmtMVGt4TTJZdE9XTm1NR0l6TjJJNU5tTTFJaXdpYVdGMElqb3hOekU0T0RBMU9EQXdMQ0pwYzNNaU9pSm9kSFJ3Y3pvdkwzTjBkV0l0WTJGekxtTnZMblZySWl3aVpYaHdJam94TmpJME1UazNPREF3ZlEuQlpiVTI2RlB5TXc2LUtYUkVuejFMdDJud19VQmI0b1FVRUpHUEZlSVNFZyIsInRva2VuX3R5cGUiOiJwZW5zaW9uX2Rhc2JvYXJkX3JwdCJ9";
    @SuppressWarnings("java:S6418")
    public static final String EXPIRED_RPT_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZXNvdXJjZUlkIjoiYzUyZGNlOTctMzlmZi00ZDdmLWEzNmEtYWY2MTMwZGU4ZWY0IiwicmVzb3VyY2VTY29wZXMiOlsib3duZXIiXSwianRpIjoiOTVjNjJjZDItM2ZkMC00MDVkLTkxM2YtOWNmMGIzN2I5NmM1IiwiaWF0IjoxNzE4ODA1ODAwLCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxNjI0MTk3ODAwfQ.BZbU26FPyMw6-KXREnz1Lt2nw_UBb4oQUEJGPFeISEg";

    @SuppressWarnings("java:S6418")
    public static final String RPT_WRONG_TOKEN_TYPE = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2laVFUwWVRNMk5XRXRZVGd3WVMwME9EbGhMV0V4TTJRdE4yUmpNalk1WXpKaFlXRm1JaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHNpYjNkdVpYSWlMQ0oyWVd4MVpTSmRMQ0pxZEdraU9pSTNabU5qWkRjellpMHlNalEyTFRRMk1qWXRPR0psTVMxaU56WTFZbVExTjJZM05EWWlMQ0pwWVhRaU9qRTNNVGt5TkRFME5qTXNJbWx6Y3lJNkltaDBkSEJ6T2k4dmMzUjFZaTFqWVhNdVkyOHVkV3NpTENKbGVIQWlPakU0TVRNNE5EazBOak45LjRPRjJIN2tpUUx5NjBualdzTHJaNkFhYnNRQ2NoNFZ6R2ZpVXRybDJCWEUiLCJ0b2tlbl90eXBlIjoicGVuc2lvbl9kYXNib2FyZF9wbXQifQ==";
    public static final String RPT_EMPTY_PERM_ARRAY = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2lObVUwTURFek1qWXROamt3WkMwME5EZ3hMVGczWVRRdE5XUmhOVGRrWldNM05ESmlJaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHRkTENKcWRHa2lPaUkzWm1OalpEY3pZaTB5TWpRMkxUUTJNall0T0dKbE1TMWlOelkxWW1RMU4yWTNORFlpTENKcFlYUWlPakUzTVRreU5ESXhNRFFzSW1semN5STZJbWgwZEhCek9pOHZjM1IxWWkxallYTXVZMjh1ZFdzaUxDSmxlSEFpT2pFNE1UTTROVEF4TURSOS5CVjhVQkEySXk1QkFWWEZLZ1VLbEd3YzJsSTBOWUdWWmRnNDdMb1N1TEI0IiwidG9rZW5fdHlwZSI6InBlbnNpb25fZGFzYm9hcmRfcnB0In0=";
    public static final String RPT_SCOPES_NO_OWNER = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2lNMk5pWm1WaE1tWXROalprWWkwME5qVTBMV0ZsTWpRdFlXUTJaRGhrTnpjM1kyTmtJaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHNpZG1Gc2RXVWlYU3dpYW5ScElqb2lOMlpqWTJRM00ySXRNakkwTmkwME5qSTJMVGhpWlRFdFlqYzJOV0prTlRkbU56UTJJaXdpYVdGMElqb3hOekU1TWpReU1qZ3hMQ0pwYzNNaU9pSm9kSFJ3Y3pvdkwzTjBkV0l0WTJGekxtTnZMblZySWl3aVpYaHdJam94T0RFek9EVXdNamd4ZlEueUtpVGVXaUwxUFI0RllfbGNteVBXc2tHOV96U0xPRGNjek41V3hYYzl1cyIsInRva2VuX3R5cGUiOiJwZW5zaW9uX2Rhc2JvYXJkX3JwdCJ9";
    @SuppressWarnings("java:S6418")
    public static final String RPT_EXPIRED_ACTIVE_TOKEN = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlY5cFpDSTZJalJpTkRWaU56aGpMVFl4WVRndE5EWTRZUzA1Tm1SaUxURm1aVGhtTUdRMk1UWTFOaUlzSW5KbGMyOTFjbU5sWDNOamIzQmxjeUk2V3lKdmQyNWxjaUlzSW5aaGJIVmxJbDBzSW1wMGFTSTZJamd6T0RaaVlXUmxMVGN6WXpVdE5EWXhNeTA1TlRFNExXTXpZelU0T0dVd1pUVTRZU0lzSW1saGRDSTZNVGN4T1RJME5UVXhNQ3dpYVhOeklqb2lhSFIwY0hNNkx5OXpkSFZpTFdOaGN5NWpieTUxYXlJc0ltVjRjQ0k2TVRneE16ZzFNelV4TUgwLnNMVzJfZGotU3ZVajZfOHFhdHhxdmFsbS1aYU1JallodmJLUWFxMmZZclkiLCJ0b2tlbl90eXBlIjoicGVuc2lvbl9kYXNib2FyZF9ycHQifQ==";
    public static final String RPT_EXPIRED_PERMISSION = "eyJhY2Nlc3NfdG9rZW4iOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKeVpYTnZkWEpqWlVsa0lqb2lNakUzTmpKbFpEQXRPR1JrWlMwME1qbGxMV0UwT0dFdFltUm1OVEl3TnpObE5UZ3lJaXdpY21WemIzVnlZMlZUWTI5d1pYTWlPbHNpYjNkdVpYSWlMQ0oyWVd4MVpTSmRMQ0pxZEdraU9pSTNabU5qWkRjellpMHlNalEyTFRRMk1qWXRPR0psTVMxaU56WTFZbVExTjJZM05EWWlMQ0pwWVhRaU9qRTNNVGt5TkRJM05qWXNJbWx6Y3lJNkltaDBkSEJ6T2k4dmMzUjFZaTFqWVhNdVkyOHVkV3NpTENKbGVIQWlPakU0TVRNNE5UQTNOalo5LlNsWm9oazd4eHplOExnYVZBNm5jZW9ja2s4N1BLTjBoSTBIQnVBbkc5djgiLCJ0b2tlbl90eXBlIjoicGVuc2lvbl9kYXNib2FyZF9ycHQifQ==";

    public static final List<RegisteredResource> DEFAULT_RESOURCES = List.of(
            RegisteredResource.builder()
                    .resourceId("92476c2f-25b8-4d87-afde-18a9ee2631dc")
                    .name("urn:pei:0e55140a-87d3-41cf-b6f7-bc822a4c3c3b:6e29eeb8-814c-44a6-a43f-b4830f3f4590")
                    .friendlyName("Resource with valid rpt token")
                    .description("description one")
                    .matchStatus(RegisterService.MatchStatusEnum.YES.toString())
                    .rpt(VALID_RPT_TOKEN)
                    .resourceScopes(List.of(Scope.OWNER))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_01)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("62933ca9-447e-4ce0-bb39-124e9fa3214f")
                    .name("0b99ab46-500f-4f14-9e0b-09c8632caf4a:022d4035-5892-411e-859e-a87d6f6c9eea")
                    .friendlyName("Resource with expired rpt token")
                    .description("description two")
                    .matchStatus(RegisterService.MatchStatusEnum.POSSIBLE.toString())
                    .rpt(EXPIRED_RPT_TOKEN)
                    .resourceScopes(List.of(Scope.OWNER))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("970f7c7b-3240-4775-bce1-7b394837f850")
                    .name("f2529be2-7f27-4661-ac51-758f1d04f2fe:5c580512-2835-44df-b0a7-174e60e974c7")
                    .friendlyName("Second resource with valid rpt token")
                    .description("description three")
                    .matchStatus(RegisterService.MatchStatusEnum.YES.toString())
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("e54a365a-a80a-489a-a13d-7dc269c2aaaf")
                    .name("negative-scenario:rpt-wrong-token-type")
                    .friendlyName("Resource returning wrong token type in rpt introspection")
                    .description("rpt returning pmt token type")
                    .matchStatus(RegisterService.MatchStatusEnum.YES.toString())
                    .rpt(RPT_WRONG_TOKEN_TYPE)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("6e401326-690d-4481-87a4-5da57dec742b")
                    .name("negative-scenario:rpt-empty-perm-array")
                    .friendlyName("Resource returning empty permissions array in rpt introspection")
                    .description("rpt returning an empty permission array")
                    .matchStatus("match-yes")
                    .rpt(RPT_EMPTY_PERM_ARRAY)
                    .resourceScopes(List.of())
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("3cbfea2f-66db-4654-ae24-ad6d8d777ccd")
                    .name("negative-scenario:rpt-scopes-no-owner")
                    .friendlyName("Resource returning scope with no owner in rpt introspection")
                    .description("rpt not containing owner within scopes")
                    .matchStatus(RegisterService.MatchStatusEnum.YES.toString())
                    .rpt(RPT_SCOPES_NO_OWNER)
                    .resourceScopes(List.of(Scope.VALUE))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("4b45b78c-61a8-468a-96db-1fe8f0d61656")
                    .name("negative-scenario:rpt-expired-token-active")
                    .friendlyName("Resource returning active = true with expired token in rpt introspection")
                    .description("rpt which is expired but reported as active")
                    .matchStatus(RegisterService.MatchStatusEnum.YES.toString())
                    .rpt(RPT_EXPIRED_ACTIVE_TOKEN)
                    .resourceScopes(List.of(Scope.VALUE))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build(),
            RegisteredResource.builder()
                    .resourceId("21762ed0-8dde-429e-a48a-bdf52073e582")
                    .name("negative-scenario:rpt-expired-permission")
                    .friendlyName("Resource returning a permission with expired token in rpt introspection")
                    .description("rpt where a permission is expired")
                    .matchStatus(RegisterService.MatchStatusEnum.YES.toString())
                    .rpt(RPT_EXPIRED_PERMISSION)
                    .resourceScopes(List.of(Scope.OWNER, Scope.VALUE))
                    .pat(UserDbInitializer.HAPPY_PATH_PAT_02)
                    .build()
    );

    @PostConstruct
    public void init() {
        logger.debug("Adding {} finds to the database", DEFAULT_RESOURCES.size());
        resourceRepository.deleteAll();
        resourceRepository.saveAll(DEFAULT_RESOURCES);
    }

}

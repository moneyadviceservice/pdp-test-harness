package uk.org.ca.stub.simulator.configuration.dbinitializer;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.User;
import uk.org.ca.stub.simulator.repository.UserRepository;

import java.util.List;

import static uk.org.ca.stub.simulator.utils.AssertionsConstants.*;

@Service
public class UserDbInitializer {
    private static final Logger logger = LoggerFactory.getLogger(UserDbInitializer.class);

    private final UserRepository userRepository;

    public UserDbInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static final List<User> users;

    static {

        users = List.of(
                User.builder()
                        .friendlyName("Valid UAT and PAT")
                        .pat(NOT_EXPIRED_TOKEN_PAT)
                        .uat(NOT_EXPIRED_TOKEN_UAT)
                        .build(),
                User.builder() // expired pat
                        .friendlyName("Expired PAT")
                        .pat(EXPIRED_TOKEN_PAT)
                        .uat(EXPIRED_TOKEN_UAT)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 01")
                        .pat(HAPPY_PATH_PAT_01)
                        .uat(HAPPY_PATH_UAT_01)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 02")
                        .pat(HAPPY_PATH_PAT_02)
                        .uat(HAPPY_PATH_UAT_02)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 03")
                        .pat(HAPPY_PATH_PAT_03)
                        .uat(HAPPY_PATH_UAT_03)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 04")
                        .pat(HAPPY_PATH_PAT_04)
                        .uat(HAPPY_PATH_UAT_04)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 05")
                        .pat(HAPPY_PATH_PAT_05)
                        .uat(HAPPY_PATH_UAT_05)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 06")
                        .pat(HAPPY_PATH_PAT_06)
                        .uat(HAPPY_PATH_UAT_06)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 07")
                        .pat(HAPPY_PATH_PAT_07)
                        .uat(HAPPY_PATH_UAT_07)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 08")
                        .pat(HAPPY_PATH_PAT_08)
                        .uat(HAPPY_PATH_UAT_08)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 09")
                        .pat(HAPPY_PATH_PAT_09)
                        .uat(HAPPY_PATH_UAT_09)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 10")
                        .pat(HAPPY_PATH_PAT_10)
                        .uat(HAPPY_PATH_UAT_10)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 11")
                        .pat(HAPPY_PATH_PAT_11)
                        .uat(HAPPY_PATH_UAT_11)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 12")
                        .pat(HAPPY_PATH_PAT_12)
                        .uat(HAPPY_PATH_UAT_12)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 13")
                        .pat(HAPPY_PATH_PAT_13)
                        .uat(HAPPY_PATH_UAT_13)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 14")
                        .pat(HAPPY_PATH_PAT_14)
                        .uat(HAPPY_PATH_UAT_14)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 15")
                        .pat(HAPPY_PATH_PAT_15)
                        .uat(HAPPY_PATH_UAT_15)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 16")
                        .pat(HAPPY_PATH_PAT_16)
                        .uat(HAPPY_PATH_UAT_16)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 17")
                        .pat(HAPPY_PATH_PAT_17)
                        .uat(HAPPY_PATH_UAT_17)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 18")
                        .pat(HAPPY_PATH_PAT_18)
                        .uat(HAPPY_PATH_UAT_18)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 19")
                        .pat(HAPPY_PATH_PAT_19)
                        .uat(HAPPY_PATH_UAT_19)
                        .build(),
                User.builder()
                        .friendlyName("Happy path user 20")
                        .pat(HAPPY_PATH_PAT_20)
                        .uat(HAPPY_PATH_UAT_20)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 401 expired PAT scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_401_EXPIRED_PAT)
                        .uat(ASSERTION_UAT_RREGURI_POST_401_EXPIRED_PAT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 404 user removed scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_404_USER_REMOVED)
                        .uat(ASSERTION_UAT_RREGURI_POST_404_USER_REMOVED)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 429 too many requests scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_429_TOO_MANY_REQUESTS)
                        .uat(ASSERTION_UAT_RREGURI_POST_429_TOO_MANY_REQUESTS)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 500 server error scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_500_SERVER_ERROR)
                        .uat(ASSERTION_UAT_RREGURI_POST_500_SERVER_ERROR)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 502 bad gateway scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_502_BAD_GATEWAY)
                        .uat(ASSERTION_UAT_RREGURI_POST_502_BAD_GATEWAY)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 503 service unavailable scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_503_SERVICE_UNAVAILABLE)
                        .uat(ASSERTION_UAT_RREGURI_POST_503_SERVICE_UNAVAILABLE)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri POST 504 gateway timeout scenario")
                        .pat(ASSERTION_PAT_RREGURI_POST_504_GATEWAY_TIMEOUT)
                        .uat(ASSERTION_UAT_RREGURI_POST_504_GATEWAY_TIMEOUT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 401 expired PAT scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_401_EXPIRED_PAT)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_401_EXPIRED_PAT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 404 resource not found scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_404_RESOURCE_NOT_FOUND)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_404_RESOURCE_NOT_FOUND)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 429 too many requests scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_429_TOO_MANY_REQUESTS)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_429_TOO_MANY_REQUESTS)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 500 server error scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_500_SERVER_ERROR)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_500_SERVER_ERROR)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 502 bad gateway scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_502_BAD_GATEWAY)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_502_BAD_GATEWAY)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 503 service unavailable scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_503_SERVICE_UNAVAILABLE)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_503_SERVICE_UNAVAILABLE)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 504 gateway timeout scenario")
                        .pat(ASSERTION_PAT_RREGURI_PATCH_504_GATEWAY_TIMEOUT)
                        .uat(ASSERTION_UAT_RREGURI_PATCH_504_GATEWAY_TIMEOUT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri DELETE 401 invalid mtls scenario")
                        .pat(ASSERTION_PAT_RREGURI_DELETE_401_EXPIRED_PAT)
                        .uat(ASSERTION_UAT_RREGURI_DELETE_401_EXPIRED_PAT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri DELETE 429 too many requests scenario")
                        .pat(ASSERTION_PAT_RREGURI_DELETE_429_TOO_MANY_REQUESTS)
                        .uat(ASSERTION_UAT_RREGURI_DELETE_429_TOO_MANY_REQUESTS)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri DELETE 500 server error scenario")
                        .pat(ASSERTION_PAT_RREGURI_DELETE_500_SERVER_ERROR)
                        .uat(ASSERTION_UAT_RREGURI_DELETE_500_SERVER_ERROR)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri DELETE 502 bad gateway scenario")
                        .pat(ASSERTION_PAT_RREGURI_DELETE_502_BAD_GATEWAY)
                        .uat(ASSERTION_UAT_RREGURI_DELETE_502_BAD_GATEWAY)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri DELETE 503 service unavailable scenario")
                        .pat(ASSERTION_PAT_RREGURI_DELETE_503_SERVICE_UNAVAILABLE)
                        .uat(ASSERTION_UAT_RREGURI_DELETE_503_SERVICE_UNAVAILABLE)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /rreguri PATCH 504 gateway timeout scenario")
                        .pat(ASSERTION_PAT_RREGURI_DELETE_504_GATEWAY_TIMEOUT)
                        .uat(ASSERTION_UAT_RREGURI_DELETE_504_GATEWAY_TIMEOUT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /perm 401 expired PAT scenario")
                        .pat(ASSERTION_PAT_PERM_401_EXPIRED_PAT)
                        .uat(ASSERTION_UAT_PERM_401_EXPIRED_PAT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /perm 404 resource not found scenario")
                        .pat(ASSERTION_PAT_PERM_404_RESOURCE_NOT_FOUND)
                        .uat(ASSERTION_UAT_PERM_404_RESOURCE_NOT_FOUND)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /perm 500 server error scenario")
                        .pat(ASSERTION_PAT_PERM_500_SERVER_ERROR)
                        .uat(ASSERTION_UAT_PERM_500_SERVER_ERROR)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /perm 502 bad gateway scenario")
                        .pat(ASSERTION_PAT_PERM_502_BAD_GATEWAY)
                        .uat(ASSERTION_UAT_PERM_502_BAD_GATEWAY)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /perm 503 service unavailable scenario")
                        .pat(ASSERTION_PAT_PERM_503_SERVICE_UNAVAILABLE)
                        .uat(ASSERTION_UAT_PERM_503_SERVICE_UNAVAILABLE)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /perm 504 gateway timeout scenario")
                        .pat(TOKEN_WITH_VALID_SIGNATURE)
                        .uat(ASSERTION_UAT_PERM_504_GATEWAY_TIMEOUT)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /token 500 server error scenario")
                        .pat(TOKEN_WITH_VALID_SIGNATURE)
                        .uat(ASSERTION_UAT_TOKEN_500_SERVER_ERROR)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /token 502 bad gateway scenario")
                        .pat(TOKEN_WITH_VALID_SIGNATURE)
                        .uat(ASSERTION_UAT_TOKEN_502_BAD_GATEWAY)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /token 503 service unavailable scenario")
                        .pat(TOKEN_WITH_VALID_SIGNATURE)
                        .uat(ASSERTION_UAT_TOKEN_503_SERVICE_UNAVAILABLE)
                        .build(),
                User.builder()
                        .friendlyName("Assertion User for /token 504 gateway timeout scenario")
                        .pat(TOKEN_WITH_VALID_SIGNATURE)
                        .uat(ASSERTION_UAT_TOKEN_504_GATEWAY_TIMEOUT)
                        .build()
        );
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            logger.debug("Initializer did not add users to the database, since some already exist!");
            return;
        }
        logger.debug("Adding {} users to the database", users.size());
        userRepository.saveAll(users);
    }

}

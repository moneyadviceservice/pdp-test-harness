package uk.org.ca.stub.simulator.configuration.dbinitializer;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.org.ca.stub.simulator.entity.User;
import uk.org.ca.stub.simulator.repository.UserRepository;

import java.util.List;

@Service
public class UserDbInitializer {
    private static final Logger logger = LoggerFactory.getLogger(UserDbInitializer.class);

    private final UserRepository userRepository;

    public UserDbInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static final List<User> users;

    public static final String ASSERTION_FOR_400_INVALID_REQUEST = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDQwMCBpbnZhbGlkX3JlcXVlc3QiLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6MzU3ODk3ODk3LCJqdGkiOjU0MjM2MjM5ODB9.mpyYRtiPB_ApqRmtHGRKJ1Ye_rxczLRgJ_-Hbj_Pdi8";
    public static final String ASSERTION_FOR_400_INVALID_GRANT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDQwMCBpbnZhbGlkX2dyYW50IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjM1Nzg5Nzg5NywianRpIjo1NDIzNjIzOTgwfQ.hNVaq8QDOP1cqLC5AooiiiKNFKjm5NG2EhJqbCjKdvE";
    @SuppressWarnings("java:S6418")
    public static final String ASSERTION_FOR_400_UNAUTHORISED_CLIENT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDQwMCB1bmF1dGhvcmlzZWRfY2xpZW50IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjM1Nzg5Nzg5NywianRpIjo1NDIzNjIzOTgwfQ.VraB0sI8vXXSRZPJY6l90uNDABuQyUiu_uauH_dBVfI";

    public static final String ASSERTION_FOR_401 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDQwMSIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjozNTc4OTc4OTcsImp0aSI6NTQyMzYyMzk4MH0.TctNc2rDRG66TNbj7xs89do662JGBB3DdNsCinGfB_k";
    public static final String ASSERTION_FOR_403 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDQwMyIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjozNTc4OTc4OTcsImp0aSI6NTQyMzYyMzk4MH0.eWVfU5mDRTt25oz1gW5mMmjDjMcQJititS8gULtm1Pk";
    public static final String ASSERTION_FOR_500 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDUwMCIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjozNTc4OTc4OTcsImp0aSI6NTQyMzYyMzk4MH0.uI7pBfECNl68c25HtYeU1ASWp8BDCFUJhsqn2bdoBEo";
    public static final String ASSERTION_FOR_502 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDUwMiIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjozNTc4OTc4OTcsImp0aSI6NTQyMzYyMzk4MH0.5G1nFt7F7BbSXHaKc-Qu0UNNloI0Z-G_HCGAeYIeTeI";
    public static final String ASSERTION_FOR_503 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDUwMyIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjozNTc4OTc4OTcsImp0aSI6NTQyMzYyMzk4MH0.SjTZ0__Ix6H4zxOOioQKaD80gqCRn7EUqYP9XTsI6lk";
    public static final String ASSERTION_FOR_504 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIDUwNCIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjozNTc4OTc4OTcsImp0aSI6NTQyMzYyMzk4MH0.NeeGQtiPMk7kHy65rVfGyjWyEFb6kM7YZ5UZkX-C7Es";

    @SuppressWarnings("java:S6418")
    public static final String EXPIRED_TOKEN_UAT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIGV4cGlyZWQiLCJpYXQiOjE1MTYyMzkwMjIsImp0aSI6NTQyMzYyMzk4MH0.CH4IOj7qku1aEdsom53QunbbFWpmDA8cFWspvpxriIM";
    @SuppressWarnings("java:S6418")
    public static final String EXPIRED_TOKEN_PAT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImVycm9yIGV4cGlyZWQiLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6MzU3ODk3ODk3LCJqdGkiOjU0MjM2MjM5ODB9.kwkRHsHstRm3AwVtP7kHMeL3Mg_uNuZwFIqQlXXqDGk";
    @SuppressWarnings("java:S6418")
    public static final String NOT_EXPIRED_TOKEN_PAT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjI1Mzg0NzMzNTl9.vi5GogGgxVimdFcT-Ul99oqo4BdAHDEQZLcZHblEQYI";
    @SuppressWarnings("java:S6418")
    public static final String NOT_EXPIRED_TOKEN_UAT = "eyJ0eXAiOiJKV1QiLCJraWQiOiJ1c2VyLWFjY291bnQtc2lnbmF0dXJlLWtleSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI0NzhjNThhOC1lNGRhLTQxMTktODhlMC1lNThkMGYyNDg4M2UiLCJhdWQiOiJodHRwczovL3N5cy5jYXMtc3lzLWs4cy5kZXYucGVuc2lvbmRhc2hib2FyZC5vcmcvYW0vb2F1dGgyIiwibmJmIjoxNjUxMTgyMDE2LCJpc3MiOiJodHRwOi8vcGVuc2lvbnNkYXNoYm9hcmQub3JnL3Bmc2lzc3VlciIsIm5hbWUiOiIwZTRkNGJlYjc1Y2Y2ZTU4ZDI3Njc2YWE0ZjcxOTFjMTA1NzE5NDI3IiwiZXhwIjoxNzUxMTg5MTY2LCJpYXQiOjE2NTExODIwMTZ9.GtLX4HGZzy3BNiqBizgCtHC4gZxolHaoPdkYpKVk03JJkHG3D-If-Ccfdrr-bYs6hSWGzExKK03UbXOsMJyPCW3JK1YKsSNFUHZXl101bSb5oPphCsjSOJ6LFWUacsW7U4G3KHZSjgNV-qethTCtMQY8GUfzjlmbbjeU0kWT2LU_BWF5HgOrWovw1h3PVeLU8JPbn5KY9d2UJuGDKNvh025oblApgxJIQ_kyZVRE5UXSpY9WMm_pNm-ExtbpUC7j8oFsxb-anYfrclcp8R6t-I-nOp8yxooYKWfgYAhYUFo3VNGmDkbO66bHD2IsgR5iWL0Lb2jSIdJcLKPeU44vVA";

    @SuppressWarnings("java:S6418")
    public static final String TOKEN_WITH_ISSUER_AND_PERMISSIONS = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZXNvdXJjZUlkIjoiZTJlYzkyNGEtYmM4Ni00OWQwLWE0ZmYtZjkzZWJmZTdjZjUyIiwicmVzb3VyY2VTY29wZXMiOlsib3duZXIiXSwianRpIjoiZjNmNzAxODAtZGVkMS00MzZmLThkZDUtZmFhOTljYjEyN2ZiIiwiaWF0IjoxNzE4NzE4MjQ1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMzI2MjQ1fQ.uKL3MQkbzoIm464StejPX2FrEJyWA5n3wIGf5w_Kdqo";
    @SuppressWarnings("java:S6418")
    public static final String TOKEN_WITH_VALID_SIGNATURE = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjEiLCJqdGkiOiJkYmMzZTYxNi1iN2Y5LTRhNWEtODdmNC1iNTRiZDNkMzI3MDUiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.vie7eWRJpmfN-YaL6iiZh57J5AyZQiv5-IXVmpWBhhw";
    @SuppressWarnings("java:S6418")
    public static final String TOKEN_WITH_INVALID_SIGNATURE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlzcyI6ImV4YW1wbGVJc3N1ZXIiLCJpYXQiOjE2NDIzMzE4MDAsImV4cCI6MTY0MjMzNTQwMH0.-PxchmoKZT8dVG65PQns9Q8-Bv3wn8C8uEZh9JNDueQ";

    public static final String HAPPY_PATH_UAT_01 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjEiLCJqdGkiOiJkYmMzZTYxNi1iN2Y5LTRhNWEtODdmNC1iNTRiZDNkMzI3MDUiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.vie7eWRJpmfN-YaL6iiZh57J5AyZQiv5-IXVmpWBhhw";
    public static final String HAPPY_PATH_PAT_01 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDEiLCJpYXQiOjE1MTYyMzkwMjJ9.PUew0CeXIRdGxtwQhqazV7shV_rL7SlKYIzkRzmomTs";
    public static final String HAPPY_PATH_UAT_02 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjIiLCJqdGkiOiI4OTlmODBlMC00OWM1LTQ5OWYtODdmMC02ODQ3MjJmMWI3MjgiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.Mbnby3x4-iC3u_01PuueNBprej7mGFid44IjLbJhgw8";
    public static final String HAPPY_PATH_PAT_02 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDIiLCJpYXQiOjE1MTYyMzkwMjJ9.RG-5z6TixeTBX_V4bkM5HMC7zlN_E6b98EG6RulMZuc";
    public static final String HAPPY_PATH_UAT_03 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjMiLCJqdGkiOiI4MzZkMzJjZi05NjFhLTQ0YjEtYTFiNS00MmZiMWQzMDJiOTMiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.VsBP7Px8Ib0q5kHI5mWfS8QoxUrh9yCNy1yK3YyyS9o";
    public static final String HAPPY_PATH_PAT_03 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDMiLCJpYXQiOjE1MTYyMzkwMjJ9.oaOSmNZA2MPMb-mzypKyxF-w76UTRYKzapuIMDmfobs";
    public static final String HAPPY_PATH_UAT_04 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjQiLCJqdGkiOiIwNmExOGIwMS0yNTNhLTQwOGMtYjc0NC00NTZiMDkwNDQxZmEiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.xWZOwhWbhLL4SkBjzzUfAzoD0sYHhMIY4JDOz75WCm0";
    public static final String HAPPY_PATH_PAT_04 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDQiLCJpYXQiOjE1MTYyMzkwMjJ9.decYA4TpfmOzTNn17oKla3k6YdcCEl3vAVKBX2ar-iM";
    public static final String HAPPY_PATH_UAT_05 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjUiLCJqdGkiOiI0ZmM2ZGM0My1jYjQ5LTRhN2UtYmE0Mi0xODYzZjMzM2JhYzUiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.fNEpDGR8Ej-9Hazyiy6GFBH5Eg-YAartavj7u3oC7Xc";
    public static final String HAPPY_PATH_PAT_05 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDUiLCJpYXQiOjE1MTYyMzkwMjJ9.7YT-yYS0cGcNovk8noIUovVXNg6GFx6-qWUL8802gbs";
    public static final String HAPPY_PATH_UAT_06 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjYiLCJqdGkiOiI0YzU0NWI1ZC0xNWVkLTRhMmEtOTZhOS01NzM2NzBlYTY1MDciLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.hLn79hd1lugW59Y6r4HMFRD4iB3t1kRX-xVoMV2cOGQ";
    public static final String HAPPY_PATH_PAT_06 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDYiLCJpYXQiOjE1MTYyMzkwMjJ9.SG2wv0OjfhF5HxXrPyThUbiQxDq7SKb_6a1Xx1uk2YQ";
    public static final String HAPPY_PATH_UAT_07 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjciLCJqdGkiOiI2MTE4ZTM3Yi0zNTNhLTQxNDQtYTZkMC05ODhmNDA5ZDI0MTEiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.QyLJd59g0Q2WinyxGALNPsj5Gl-gRNloSW4QZzYgB04";
    public static final String HAPPY_PATH_PAT_07 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDciLCJpYXQiOjE1MTYyMzkwMjJ9.RiO54I6bx5gxrTiP8hzTwbwdCBIEZeQx6aj9Ik87zLg";
    public static final String HAPPY_PATH_UAT_08 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjgiLCJqdGkiOiJkYzQwOTcyNy1iOTRjLTRiODAtODNiNS1kMmJlMjhiZjJlNzUiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.pxjCzbFdh948Bt-RN-0Or-S1Fl0IjvmKFdg2KTfUbew";
    public static final String HAPPY_PATH_PAT_08 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDgiLCJpYXQiOjE1MTYyMzkwMjJ9.7WTK2Qva_f7wHiXvwclh1kwEVYpAgd4GtFxJ4APyx-4";
    public static final String HAPPY_PATH_UAT_09 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjkiLCJqdGkiOiIxNjhjNjczYy00YTdkLTRlNjQtOTAxMi1lZTFiZTlkYjQxYmMiLCJpYXQiOjE3MTg2MzM5MjUsImlzcyI6Imh0dHBzOi8vc3R1Yi1jYXMuY28udWsiLCJleHAiOjE4MTMyNDE5MjV9.7Bq2vM1TnOTg6p91mLKzc9SEBV7sQttb2wgLGLdyyoE";
    public static final String HAPPY_PATH_PAT_09 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMDkiLCJpYXQiOjE1MTYyMzkwMjJ9.eCqeNZkWkEHRD_MFlVT6Zj5hZ9ChFZniRYzCz_Rx8ig";
    public static final String HAPPY_PATH_UAT_10 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjEwIiwianRpIjoiMGIyOGExNTMtMDA1Yi00NzJjLThkZTYtMDYzMTQ1NzA2YjQyIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.JcRf_QKZBY5FK9_pyKUcgVEOKICZQkDp8AOyTuI-KX8";
    public static final String HAPPY_PATH_PAT_10 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTAiLCJpYXQiOjE1MTYyMzkwMjJ9.jaqI1GgpY8DqJhF55ovuFIjnp95mDpXrjXGHvFHjoPo";
    public static final String HAPPY_PATH_UAT_11 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjExIiwianRpIjoiNjExY2RjMjMtOWFjNi00MGNkLTk1ZmItZDcxMDgwNWM2MmFmIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.rfXYL0sYAmLfHuZ6Wuzdr1GCYIUjFZ7JHExtOZ0zB0Y";
    public static final String HAPPY_PATH_PAT_11 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTEiLCJpYXQiOjE1MTYyMzkwMjJ9.jyMvYx_l9qlano02oDuzavFqPMfZLUO1nTtG1H6fmhQ";
    public static final String HAPPY_PATH_UAT_12 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjEyIiwianRpIjoiMTgwZTZkN2EtYzVjZS00OTY4LTg3MTYtMDIyZDY1OTcxMGM2IiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.BsMpjlUFFPl1xA_sgBHKDmnsBDAcZFDtZSIWiFLs654";
    public static final String HAPPY_PATH_PAT_12 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTIiLCJpYXQiOjE1MTYyMzkwMjJ9.rzpNXEIAERFb9f0LWYrJdvqqlMqT8VcgHO1BThpRixE";
    public static final String HAPPY_PATH_UAT_13 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjEzIiwianRpIjoiMTYzNDc4NjktNDFiYS00MDM2LTgwOWEtMDVkM2RjODRkNmQxIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.3XOA7bA5xfBIw1G1xow6T7-UBRAWGBKZvakIj8zWNDM";
    public static final String HAPPY_PATH_PAT_13 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTMiLCJpYXQiOjE1MTYyMzkwMjJ9.WlnT83adUsfbHId_kYYfDT5PqGhl0y3KMTjwKxMNOaU";
    public static final String HAPPY_PATH_UAT_14 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjE0IiwianRpIjoiNDkxMGY1YTItNGQ0NC00NDQ0LWIyNjAtY2EyY2U0MjQ1ZGZlIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.7K4n3926RS8Z_8IK1oG_onGjesVqSKkoQkePlaMNsqc";
    public static final String HAPPY_PATH_PAT_14 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTQiLCJpYXQiOjE1MTYyMzkwMjJ9.6cRuqDWiRjbVyiH3a9bS1RLGyAkk2sh8uSuAIqld08o";
    public static final String HAPPY_PATH_UAT_15 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjE1IiwianRpIjoiZjM4ZmFkNTAtM2RmZC00ODUyLTg4OTYtM2YxY2RiZjYwMTQ5IiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.dl-us8-OEt0dfC0zEhob-rjGRrEttePp8KmcloCwIaQ";
    public static final String HAPPY_PATH_PAT_15 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTUiLCJpYXQiOjE1MTYyMzkwMjJ9.RYLCQCHTYgw8CMnebx16feEwDdpmi7rSWUSLGRtV3zg";
    public static final String HAPPY_PATH_UAT_16 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjE2IiwianRpIjoiNDRjN2YzYjUtZTliNS00MzkwLWJkZmQtNmEyNWVhMzBmYTA1IiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.UHL-88UkjnqOLG_PpRehKPaQqhixirML40B9fZwhfZc";
    public static final String HAPPY_PATH_PAT_16 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTYiLCJpYXQiOjE1MTYyMzkwMjJ9.dJKOeqCP4zy3nopEgf7d6rbmMygnEi5yggFup7x8V1I";
    public static final String HAPPY_PATH_UAT_17 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjE3IiwianRpIjoiZmU1OGJmZWUtN2M4Zi00NDgzLWI1MWItM2JkNjgyZTllZmEwIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.a8xbwmfSD6XK8HVGSqacVbkf0F_Nyl3Z8ePY7iSRk6c";
    public static final String HAPPY_PATH_PAT_17 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTciLCJpYXQiOjE1MTYyMzkwMjJ9.KJ4AH2dL0kirS3wwr0UUu7GD6co1hwjUhvaHvQ95sas";
    public static final String HAPPY_PATH_UAT_18 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjE4IiwianRpIjoiMTY1ZmJjYmQtMGVlYy00OWU4LTg4MjYtMWJkYmVlOGNmN2FjIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.uyDRaCKbvEq93ylrh2h89rm4ZftoCk7SU2orRexWwIU";
    public static final String HAPPY_PATH_PAT_18 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTgiLCJpYXQiOjE1MTYyMzkwMjJ9.rzZJd_aLIx2zBnd0hQwkIdGoTQ-e_xA2mPn08Fu_11w";
    public static final String HAPPY_PATH_UAT_19 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjE5IiwianRpIjoiYzRkMWI1ZDItOTRjMS00M2MxLTkzMWQtNzZiZThiMjYyNzYyIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.0lYhoULb7a_XvdFboIQPGV-AWyKv9QjqJgj2mc5fH1M";
    public static final String HAPPY_PATH_PAT_19 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMTkiLCJpYXQiOjE1MTYyMzkwMjJ9.bo9DeFjHT838amer2aQ_QUiXAGtcDQGSkE1zoJXruYo";
    public static final String HAPPY_PATH_UAT_20 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IjIwIiwianRpIjoiOWJhNDkxZDQtYzc1YS00MDY2LTk0NjAtMmZlODQxMDZiNTBjIiwiaWF0IjoxNzE4NjMzOTI1LCJpc3MiOiJodHRwczovL3N0dWItY2FzLmNvLnVrIiwiZXhwIjoxODEzMjQxOTI1fQ.EqLbb2b_7at7dpIPEyDHr8Os1eoA-UhAnjLh-m2zrE0";
    public static final String HAPPY_PATH_PAT_20 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImhhcHB5X3BhdGhfMjAiLCJpYXQiOjE1MTYyMzkwMjJ9.5JkW4IbxLAyogP9PGLmw3c9MeKLReEt0ZW1Op97b3ek";

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

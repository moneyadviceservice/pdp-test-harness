package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import uk.org.ca.stub.simulator.pojo.entity.Key;

import java.util.Arrays;
import java.util.List;

@Getter
public class JwkSetsResponse {
    @JsonProperty("keys")
    List<Key> keys = Arrays.asList(Key.builder()
                    .e("AQAB")
                    .kid("7e9a69a2-4df9-4127-bf74-0caad8921794")
                    .kty("RSA")
                    .n("qgnuTp_fiDs73FMEhJKdKGS0z6jbU_mA0_ffNAcfRF8SWe8Kex1RkJ_7FAAj_hfxmEKH82RtMZc9pwupr62vDn0CJhcMxG7C_xhmrvBh8XlJq-aVmHWUR89P7RJbovCsywO58Kugyg3Re7d1f8hqi5nWOn9sKJhFxy9P11Zj0vBaA41sMvpBo0zAr_S-LIYQcYpLqeTWWo7qmNS7VKwWROVfHPULKokCnByUCw_9e38BpgDYVv3p4mC8DOpWz5EcUwNfDIX0bnlcu9YL2NSJao9Nycn5O6w2rZBMTjfp2wLSULd8rLk2j910O3x-gh9v2L3g-Zsyx0WDiyxptKVIow")
                    .build(),
            Key.builder()
                    .e("AQAB")
                    .kid("490c09cb-1861-491b-ac46-711a6187128d")
                    .kty("RSA")
                    .n("p3E_TmSL4uVcp924ZbDqgUNjSdd_czJVDprODsVELPHK443pl1XsALOkJd164Zu-f6BqXIq7PeiQ32VfWYEQ1fECAKiUhB9b_csqc_gqV7NSzllQGk8D2PTMVyvsD9-NHoYsjPjwdSRWMu6ie1kxLwicqWNgYIwJbp79R6QA7CZ377EbNahHezag-BgehTlNf5OK9MXYZ9ZPK6el8c6mvQwmm_7nf5mKxsLBS6_oTXkr8ShkhfYdmIK_rP_G975CD6ZT3jVSKGeEQMN65cFLcxnUb0Bdg5M5wfJUjaBFBXbkcCKAM7Ss1Nvu6x1b3Jrqqsse9peAZ-EACUtyjkxBZw")
                    .build(),
            Key.builder().
                    e("AQAB").
                    kid("9fd81973-d10d-4514-88ce-f9d57a60f281").
                    kty("RSA").
                    n("rING3bhS6NAFNPE4DYxQ_byGCtVoELWYE_pZJGV2JzCjKjw_F2J1fiWl7DbIg7kUERyUW6bU4pra_Y8dm8wWoSvO7K97KU8iCCLcMa1JFSHEL3iVGEGrAzZ0W4ISz0g_lSkYl9-lYtNUIjzfv4JsDSSG0Xmu9fa6naI2UHMo1RXlsPMs7L9jk7fvHNwuIXTdgeX_DxG3PtYgaWZTw6bLOfc4bmdm2hettn1DPhypD_K86f7OzI7UY3Tos61bzbA53V7LDmotFLMTbWh_ZY10cKOJ6FdVo5EoQIibMDD5738yH6D0HUlhuYn3G9dxB5VfBuHMePaKpW7kj_qLMcuyPw")
                    .build()
    );
}

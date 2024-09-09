package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

/**
 * RetrieveUmaToken200Response
 */

@JsonTypeName("retrieve_uma_token_200_response")
public class RetrieveUmaToken200Response {

  private String accessToken;

  /**
   * The type of the generated access token
   */
  public enum TokenTypeEnum {
    RPT("pension_dashboard_rpt"),
    
    PEI_URL("pension_dashboard_pei_url"),
    
    PAT("pension_dashboard_pat");

    private String value;

    TokenTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TokenTypeEnum fromValue(String value) {
      for (TokenTypeEnum b : TokenTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TokenTypeEnum tokenType;

  private String idToken;

  private Boolean upgraded;

  private String pct;

  public RetrieveUmaToken200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RetrieveUmaToken200Response(String accessToken, TokenTypeEnum tokenType) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
  }

  public RetrieveUmaToken200Response accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * The generated access token
   * @return accessToken
  */
  @NotNull @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+/=]*)") 
  @Schema(name = "access_token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2Mzh9.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI", description = "The generated access token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("access_token")
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public RetrieveUmaToken200Response tokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * The type of the generated acccess token
   * @return tokenType
  */
  @NotNull 
  @Schema(name = "token_type", example = "pension_dashboard_rpt", description = "The type of the generated acccess token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token_type")
  public TokenTypeEnum getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
  }

  public RetrieveUmaToken200Response idToken(String idToken) {
    this.idToken = idToken;
    return this;
  }

  /**
   * The generated access token
   * @return idToken
  */
  @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+/=]*)") 
  @Schema(name = "id_token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdF9oYXNoIjoiS19GcVpMcnRyNDhkaU1lQmtHa0NEQSIsInN1YiI6ImNmNjY4ZDQ3LWVlNTgtNGUzMy1iYzA1LWZlYjcwNThkZTU4ZCIsImF1ZGl0VHJhY2tpbmdJZCI6ImI1NjI4NWFjLTBkNTAtNDk3NS1iNmZkLTc4Njc0YTUzYWEzNy04MTIiLCJzdWJuYW1lIjoiY2Y2NjhkNDctZWU1OC00ZTMzLWJjMDUtZmViNzA1OGRlNThkIiwiaXNzIjoiaHR0cHM6Ly9wZXJmLnBlbnNpb25zZGFzaGJvYXJkcy5vcmcudWsvYW0vb2F1dGgyIiwidG9rZW5OYW1lIjoiaWRfdG9rZW4iLCJwZWlzX2lkIjoiNmFhZDFhY2UyZmEwZjAwNGI0YzJiOGI1N2FlNGNmMzEyNjRiMzY0OSIsInNpZCI6IkpxQk1zYzdnd01WSkdRZlBGaURlMXd4RFptL1BaT3ZPei9FckpONGl3b2s9IiwiYXVkIjoiREItT0UwMDEiLCJjX2hhc2giOiJxaGp2d3FiMDJENkNZSmtHT1BOYXh3IiwiYWNyIjoiMCIsIm9yZy5mb3JnZXJvY2sub3BlbmlkY29ubmVjdC5vcHMiOiJvUEUyeG4zMzV0T1FpMjB1NS1UOVVGVmFFdEUiLCJhenAiOiJEQi1PRTAwMSIsImF1dGhfdGltZSI6MTcxNTY3ODkxMCwicmVhbG0iOiIvIiwiZXhwIjoxNzE1NjgyNTEyLCJ0b2tlblR5cGUiOiJKV1RUb2tlbiIsImlhdCI6MTcxNTY3ODkxMn0.JplsVv4KJk8uuCFLEBW2xbjC8ZispSCRJA_DozbD_HQ", description = "The generated access token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id_token")
  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  public RetrieveUmaToken200Response upgraded(Boolean upgraded) {
    this.upgraded = upgraded;
    return this;
  }

  /**
   * Defines if the token has been upgraded, currently not used
   * @return upgraded
  */
  
  @Schema(name = "upgraded", description = "Defines if the token has been upgraded, currently not used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgraded")
  public Boolean getUpgraded() {
    return upgraded;
  }

  public void setUpgraded(Boolean upgraded) {
    this.upgraded = upgraded;
  }

  public RetrieveUmaToken200Response pct(String pct) {
    this.pct = pct;
    return this;
  }

  /**
   * An additional PCT token
   * @return pct
  */
  @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+/=]*)") 
  @Schema(name = "pct", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2Mzh9.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI", description = "An additional PCT token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pct")
  public String getPct() {
    return pct;
  }

  public void setPct(String pct) {
    this.pct = pct;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetrieveUmaToken200Response retrieveUmaToken200Response = (RetrieveUmaToken200Response) o;
    return Objects.equals(this.accessToken, retrieveUmaToken200Response.accessToken) &&
        Objects.equals(this.tokenType, retrieveUmaToken200Response.tokenType) &&
        Objects.equals(this.idToken, retrieveUmaToken200Response.idToken) &&
        Objects.equals(this.upgraded, retrieveUmaToken200Response.upgraded) &&
        Objects.equals(this.pct, retrieveUmaToken200Response.pct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, tokenType, idToken, upgraded, pct);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveUmaToken200Response {\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    idToken: ").append(toIndentedString(idToken)).append("\n");
    sb.append("    upgraded: ").append(toIndentedString(upgraded)).append("\n");
    sb.append("    pct: ").append(toIndentedString(pct)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


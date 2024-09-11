package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IntrospectionResult
 */

@JsonTypeName("introspection_result")
public class IntrospectionResult {

  private Boolean active;

  /**
   * The type of the token that has been introspected.
   */
  public enum TokenTypeEnum {
    PENSION_DASHBOARD_RPT("pension_dashboard_rpt"),
    PMT_FOR_NEGATIVE_TEST("pension_dashboard_pmt");

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
      for (var b : TokenTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TokenTypeEnum tokenType;

  private Long exp;

  private String iss;

  @Valid
  private List<@Valid IntrospectionResultPermissions> permissions = new ArrayList<>();

  public IntrospectionResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IntrospectionResult(Boolean active) {
    this.active = active;
  }

  public IntrospectionResult active(Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Indicator of whether or not the presented token is currently active
   * @return active
  */
  @NotNull
  @Schema(name = "active", description = "Indicator of whether or not the presented token is currently active", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("active")
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public IntrospectionResult tokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * The type of the token that has been introspected.
   * @return tokenType
  */

  @Schema(name = "token_type", description = "The type of the token that has been introspected.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token_type")
  public TokenTypeEnum getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
  }

  public IntrospectionResult exp(Long exp) {
    this.exp = exp;
    return this;
  }

  /**
   * The epoch seconds representation of the expiry date of the introspected token.
   * minimum: 1
   * @return exp
  */
  @Min(1)
  @Schema(name = "exp", description = "The epoch seconds representation of the expiry date of the introspected token.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exp")
  public Long getExp() {
    return exp;
  }

  public void setExp(Long exp) {
    this.exp = exp;
  }

  public IntrospectionResult iss(String iss) {
    this.iss = iss;
    return this;
  }

  /**
   * The iss for the registered participant that the passed token was generated for.
   * @return iss
  */

  @Schema(name = "iss", description = "The iss for the registered participant that the passed token was generated for.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("iss")
  public String getIss() {
    return iss;
  }

  public void setIss(String iss) {
    this.iss = iss;
  }

  public IntrospectionResult permissions(List<@Valid IntrospectionResultPermissions> permissions) {
    this.permissions = permissions;
    return this;
  }

  public IntrospectionResult addPermissionsItem(IntrospectionResultPermissions permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * The set of resource permissions defined by the provided token in conjuction with the provided autorisation.
   * @return permissions
  */
  @Valid
  @Schema(name = "permissions", description = "The set of resource permissions defined by the provided token in conjuction with the provided autorisation. ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<@Valid IntrospectionResultPermissions> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<@Valid IntrospectionResultPermissions> permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntrospectionResult introspectionResult = (IntrospectionResult) o;
    return Objects.equals(this.active, introspectionResult.active) &&
        Objects.equals(this.tokenType, introspectionResult.tokenType) &&
        Objects.equals(this.exp, introspectionResult.exp) &&
        Objects.equals(this.iss, introspectionResult.iss) &&
        Objects.equals(this.permissions, introspectionResult.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, tokenType, exp, iss, permissions);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("class IntrospectionResult {\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
    sb.append("    iss: ").append(toIndentedString(iss)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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


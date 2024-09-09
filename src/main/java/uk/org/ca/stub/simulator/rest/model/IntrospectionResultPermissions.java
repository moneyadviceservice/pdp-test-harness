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
import java.util.UUID;

/**
 * IntrospectionResultPermissions
 */

@JsonTypeName("introspection_result_permissions")
public class IntrospectionResultPermissions {

  private UUID resourceId;

  /**
   * Gets or Sets resourceScopes
   */
  public enum ResourceScopesEnum {
    OWNER("owner"),

    VALUE("value");

    private final String value;

    ResourceScopesEnum(String value) {
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
    public static ResourceScopesEnum fromValue(String value) {
      for (ResourceScopesEnum b : ResourceScopesEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<ResourceScopesEnum> resourceScopes = new ArrayList<>();

  private Long exp;

  public IntrospectionResultPermissions() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IntrospectionResultPermissions(UUID resourceId, List<ResourceScopesEnum> resourceScopes) {
    this.resourceId = resourceId;
    this.resourceScopes = resourceScopes;
  }

  public IntrospectionResultPermissions resourceId(UUID resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * The resource_id of a registered PeI
   * @return resourceId
  */
  @NotNull @Valid
  @Schema(name = "resource_id", description = "The resource_id of a registered PeI", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_id")
  public UUID getResourceId() {
    return resourceId;
  }

  public void setResourceId(UUID resourceId) {
    this.resourceId = resourceId;
  }

  public IntrospectionResultPermissions resourceScopes(List<ResourceScopesEnum> resourceScopes) {
    this.resourceScopes = resourceScopes;
    return this;
  }

  public IntrospectionResultPermissions addResourceScopesItem(ResourceScopesEnum resourceScopesItem) {
    if (this.resourceScopes == null) {
      this.resourceScopes = new ArrayList<>();
    }
    this.resourceScopes.add(resourceScopesItem);
    return this;
  }

  /**
   * The role(s) which the requester has been granted in respect of the resource.
   * @return resourceScopes
  */
  @NotNull
  @Schema(name = "resource_scopes", description = "The role(s) which the requester has been granted in respect of the resource.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_scopes")
  public List<ResourceScopesEnum> getResourceScopes() {
    return resourceScopes;
  }

  public void setResourceScopes(List<ResourceScopesEnum> resourceScopes) {
    this.resourceScopes = resourceScopes;
  }

  public IntrospectionResultPermissions exp(Long exp) {
    this.exp = exp;
    return this;
  }

  /**
   * The epoch seconds representation of the expiry date of the permission.
   * minimum: 1
   * @return exp
  */
  @Min(1)
  @Schema(name = "exp", description = "The epoch seconds representation of the expiry date of the permission.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exp")
  public Long getExp() {
    return exp;
  }

  public void setExp(Long exp) {
    this.exp = exp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var introspectionResultPermissions = (IntrospectionResultPermissions) o;
    return Objects.equals(this.resourceId, introspectionResultPermissions.resourceId) &&
        Objects.equals(this.resourceScopes, introspectionResultPermissions.resourceScopes) &&
        Objects.equals(this.exp, introspectionResultPermissions.exp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId, resourceScopes, exp);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("class IntrospectionResultPermissions {\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    resourceScopes: ").append(toIndentedString(resourceScopes)).append("\n");
    sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
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


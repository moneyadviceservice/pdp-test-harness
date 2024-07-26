package uk.org.ca.stub.simulator.rest.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PermBody
 */

@JsonTypeName("perm_body")
public class PermBody {

  private UUID resourceId;

  /**
   * Gets or Sets resourceScopes
   */
  public enum ResourceScopesEnum {
    VALUE("value"),

    OWNER("owner");

    private String value;

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

  private List<ResourceScopesEnum> resourceScopes = new ArrayList<>();

  public PermBody() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PermBody(UUID resourceId, List<ResourceScopesEnum> resourceScopes) {
    this.resourceId = resourceId;
    this.resourceScopes = resourceScopes;
  }

  public PermBody resourceId(UUID resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * The id of the registered resource for to which access is required.
   * @return resourceId
  */
  @NotNull @Valid
  @Schema(name = "resource_id", example = "96478f28-73a2-424e-85b8-18453f830f61", description = "The id of the registered resource for to which access is required.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_id")
  public UUID getResourceId() {
    return resourceId;
  }

  public void setResourceId(UUID resourceId) {
    this.resourceId = resourceId;
  }

  public PermBody resourceScopes(List<ResourceScopesEnum> resourceScopes) {
    this.resourceScopes = resourceScopes;
    return this;
  }

  public PermBody addResourceScopesItem(ResourceScopesEnum resourceScopesItem) {
    if (this.resourceScopes == null) {
      this.resourceScopes = new ArrayList<>();
    }
    this.resourceScopes.add(resourceScopesItem);
    return this;
  }

  /**
   * The required access scopes. This must be \"value\" and \"owner\".
   * @return resourceScopes
  */
  @NotNull @Size(min = 2, max = 2)
  @Schema(name = "resource_scopes", description = "The required access scopes. This must be \"value\" and \"owner\".", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_scopes")
  public List<ResourceScopesEnum> getResourceScopes() {
    return resourceScopes;
  }

  public void setResourceScopes(List<ResourceScopesEnum> resourceScopes) {
    this.resourceScopes = resourceScopes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermBody permBody = (PermBody) o;
    return Objects.equals(this.resourceId, permBody.resourceId) &&
        Objects.equals(this.resourceScopes, permBody.resourceScopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId, resourceScopes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermBody {\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    resourceScopes: ").append(toIndentedString(resourceScopes)).append("\n");
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


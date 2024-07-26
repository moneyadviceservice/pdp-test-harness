package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The model representing a registered PeI Resource
 */

@Schema(name = "registered_pei", description = "The model representing a registered PeI Resource")
@JsonTypeName("registered_pei")
public class RegisteredPei {

  private String resourceId;

  private String name;

  private String description;

  /**
   * The current match status of the registered PeI
   */
  public enum MatchStatusEnum {
    YES("match-yes"),

    POSSIBLE("match-possible");

    private String value;

    MatchStatusEnum(String value) {
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
    public static MatchStatusEnum fromValue(String value) {
      for (MatchStatusEnum b : MatchStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MatchStatusEnum matchStatus;

  /**
   * Gets or Sets resourceScopes
   */
  public enum ResourceScopesEnum {
    VALUE("value"),

    OWNER("owner"),

    DELEGATE("delegate");

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

  @Valid
  private List<ResourceScopesEnum> resourceScopes = new ArrayList<>();

  public RegisteredPei() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegisteredPei(String resourceId, String name, String description, MatchStatusEnum matchStatus, List<ResourceScopesEnum> resourceScopes) {
    this.resourceId = resourceId;
    this.name = name;
    this.description = description;
    this.matchStatus = matchStatus;
    this.resourceScopes = resourceScopes;
  }

  public RegisteredPei resourceId(String resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * The unique id of the registered resource.
   * @return resourceId
  */
  @NotNull
  @Schema(name = "resource_id", example = "e6623b31-47ab-4c4c-9e88-23b3b4735c72", description = "The unique id of the registered resource.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_id")
  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public RegisteredPei name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The URN for the resource in the form urn:pei:<holderGuid>:<assetGuid>  Where <holderGuid> is the guid issued to the data provider during onboarding and <assetGuid> is the unique id by which the matched pension asset/citizen user match is known within the pension data provider's system.
   * @return name
  */
  @NotNull @Pattern(regexp = "^urn:pei:(?:\\{{0,1}(?:[0-9a-fA-F]){8}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){12}\\}{0,1}?:\\{{0,1}(?:[0-9a-fA-F]){8}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){12}\\}{0,1})$")
  @Schema(name = "name", example = "urn:pei:a704ecce-06c0-46ad-a399-ab9eb43568df:a3f38ece-b586-45a6-890c-9b4c045747c8", description = "The URN for the resource in the form urn:pei:<holderGuid>:<assetGuid>  Where <holderGuid> is the guid issued to the data provider during onboarding and <assetGuid> is the unique id by which the matched pension asset/citizen user match is known within the pension data provider's system.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RegisteredPei description(String description) {
    this.description = description;
    return this;
  }

  /**
   * The description of the pension that the PeI is being registered in relation to. This must match the scheme name that will be provided when the pension detail are retrieved.
   * @return description
  */
  @NotNull @Size(min = 1)
  @Schema(name = "description", example = "My Pension Scheme", description = "The description of the pension that the PeI is being registered in relation to. This must match the scheme name that will be provided when the pension detail are retrieved.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public RegisteredPei matchStatus(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
    return this;
  }

  /**
   * The current match status of the registered PeI
   * @return matchStatus
  */
  @NotNull
  @Schema(name = "match_status", description = "The current match status of the registered PeI", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("match_status")
  public MatchStatusEnum getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
  }

  public RegisteredPei resourceScopes(List<ResourceScopesEnum> resourceScopes) {
    this.resourceScopes = resourceScopes;
    return this;
  }

  public RegisteredPei addResourceScopesItem(ResourceScopesEnum resourceScopesItem) {
    if (this.resourceScopes == null) {
      this.resourceScopes = new ArrayList<>();
    }
    this.resourceScopes.add(resourceScopesItem);
    return this;
  }

  /**
   * The full list of scopes for which consent to access the resource can be given.
   * @return resourceScopes
  */
  @NotNull
  @Schema(name = "resource_scopes", description = "The full list of scopes for which consent to access the resource can be given.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    RegisteredPei registeredPei = (RegisteredPei) o;
    return Objects.equals(this.resourceId, registeredPei.resourceId) &&
        Objects.equals(this.name, registeredPei.name) &&
        Objects.equals(this.description, registeredPei.description) &&
        Objects.equals(this.matchStatus, registeredPei.matchStatus) &&
        Objects.equals(this.resourceScopes, registeredPei.resourceScopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId, name, description, matchStatus, resourceScopes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisteredPei {\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    matchStatus: ").append(toIndentedString(matchStatus)).append("\n");
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


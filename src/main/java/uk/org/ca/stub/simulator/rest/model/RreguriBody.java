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
 * RreguriBody
 */

@JsonTypeName("rreguri_body")
public class RreguriBody {

  private String name;

  private String description;

  /**
   * The match status at the time of registration.
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

  private String inboundRequestId;

  /**
   * Gets or Sets resourceScopes
   */
  public enum ResourceScopesEnum {
    OWNER("owner"),

    VALUE("value"),

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

  public RreguriBody() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RreguriBody(String name, String description, MatchStatusEnum matchStatus, List<ResourceScopesEnum> resourceScopes) {
    this.name = name;
    this.description = description;
    this.matchStatus = matchStatus;
    this.resourceScopes = resourceScopes;
  }

  public RreguriBody name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The URN for the resource in the form urn:pei:<holderGuid>:<assetGuid>  Where <holderGuid> is the guid issued to the data provider during onboarding and <assetGuid> is the unique id by which the matched pension within the pension data provider's system.
   * @return name
  */
  @NotNull
  @Pattern(regexp = "^urn:pei:(?:(?:[0-9a-fA-F]){8}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){12}?:(?:[0-9a-fA-F]){8}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){4}-(?:[0-9a-fA-F]){12})$")
  @Schema(name = "name", example = "urn:pei:a704ecce-06c0-46ad-a399-ab9eb43568df:a3f38ece-b586-45a6-890c-9b4c045747c8", description = "The URN for the resource in the form urn:pei:<holderGuid>:<assetGuid>  Where <holderGuid> is the guid issued to the data provider during onboarding and <assetGuid> is the unique id by which the matched pension within the pension data provider's system.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RreguriBody description(String description) {
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

  public RreguriBody matchStatus(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
    return this;
  }

  /**
   * The match status at the time of registration.
   * @return matchStatus
  */
  @NotNull
  @Schema(name = "match_status", description = "The match status at the time of registration.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("match_status")
  public MatchStatusEnum getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
  }

  public RreguriBody inboundRequestId(String inboundRequestId) {
    this.inboundRequestId = inboundRequestId;
    return this;
  }

  /**
   * If the registration resulted from a request from the CDA, the X-Request-ID of the cda request that resulted in the registration of the PeI.
   * @return inboundRequestId
  */

  @Schema(name = "inbound_request_id", description = "If the registration resulted from a request from the CDA, the X-Request-ID of the cda request that resulted in the registration of the PeI.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inbound_request_id")
  public String getInboundRequestId() {
    return inboundRequestId;
  }

  public void setInboundRequestId(String inboundRequestId) {
    this.inboundRequestId = inboundRequestId;
  }

  public RreguriBody resourceScopes(List<ResourceScopesEnum> resourceScopes) {
    this.resourceScopes = resourceScopes;
    return this;
  }

  public RreguriBody addResourceScopesItem(ResourceScopesEnum resourceScopesItem) {
    if (this.resourceScopes == null) {
      this.resourceScopes = new ArrayList<>();
    }
    this.resourceScopes.add(resourceScopesItem);
    return this;
  }

  /**
   * The scopes of access to the PeI being registered.
   * @return resourceScopes
  */
  @NotNull
  @Schema(name = "resource_scopes", description = "The scopes of access to the PeI being registered.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    RreguriBody rreguriBody = (RreguriBody) o;
    return Objects.equals(this.name, rreguriBody.name) &&
        Objects.equals(this.description, rreguriBody.description) &&
        Objects.equals(this.matchStatus, rreguriBody.matchStatus) &&
        Objects.equals(this.inboundRequestId, rreguriBody.inboundRequestId) &&
        Objects.equals(this.resourceScopes, rreguriBody.resourceScopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, matchStatus, inboundRequestId, resourceScopes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RreguriBody {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    matchStatus: ").append(toIndentedString(matchStatus)).append("\n");
    sb.append("    inboundRequestId: ").append(toIndentedString(inboundRequestId)).append("\n");
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


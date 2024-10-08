package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * PatchRegisteredPeisId200Response
 */

@JsonTypeName("patch_registered_peis_id_200_response")
public class PatchRegisteredPeisId200Response {

  private String resourceId;

  public PatchRegisteredPeisId200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PatchRegisteredPeisId200Response(String resourceId) {
    this.resourceId = resourceId;
  }

  public PatchRegisteredPeisId200Response resourceId(String resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * The unique id of the registered PeI resource.
   * @return resourceId
  */
  @NotNull @Size(min = 1) 
  @Schema(name = "resource_id", description = "The unique id of the registered PeI resource.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_id")
  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var patchRegisteredPeisId200Response = (PatchRegisteredPeisId200Response) o;
    return Objects.equals(this.resourceId, patchRegisteredPeisId200Response.resourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("class PatchRegisteredPeisId200Response {\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
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


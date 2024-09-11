package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * PostRegisteredPeis201Response
 */

@JsonTypeName("post_registered_peis_201_response")
public class PostRegisteredPeis201Response {

  private UUID resourceId;

  public PostRegisteredPeis201Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PostRegisteredPeis201Response(UUID resourceId) {
    this.resourceId = resourceId;
  }

  public PostRegisteredPeis201Response resourceId(UUID resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * The unique id of the newly created resource.
   * @return resourceId
  */
  @NotNull @Valid 
  @Schema(name = "resource_id", example = "a9482564-5391-49e7-b43f-fdec1aed3005", description = "The unique id of the newly created resource.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource_id")
  public UUID getResourceId() {
    return resourceId;
  }

  public void setResourceId(UUID resourceId) {
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
    var postRegisteredPeis201Response = (PostRegisteredPeis201Response) o;
    return Objects.equals(this.resourceId, postRegisteredPeis201Response.resourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("class PostRegisteredPeis201Response {\n");
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


package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PostRegisteredPeis201Response
 */

@JsonTypeName("post_registered_peis_201_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class PostRegisteredPeis201Response {

  private String resourceId;

  public PostRegisteredPeis201Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PostRegisteredPeis201Response(String resourceId) {
    this.resourceId = resourceId;
  }

  public PostRegisteredPeis201Response resourceId(String resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * The unique id of the newly created resource.
   * @return resourceId
  */
  @NotNull
  @Schema(name = "resource_id", example = "a9482564-5391-49e7-b43f-fdec1aed30050", description = "The unique id of the newly created resource.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    PostRegisteredPeis201Response postRegisteredPeis201Response = (PostRegisteredPeis201Response) o;
    return Objects.equals(this.resourceId, postRegisteredPeis201Response.resourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
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


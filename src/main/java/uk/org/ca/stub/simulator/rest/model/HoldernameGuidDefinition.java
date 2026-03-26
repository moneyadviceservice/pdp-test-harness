package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HoldernameGuidDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class HoldernameGuidDefinition {

  private UUID holdernameGuid;

  public HoldernameGuidDefinition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HoldernameGuidDefinition(UUID holdernameGuid) {
    this.holdernameGuid = holdernameGuid;
  }

  public HoldernameGuidDefinition holdernameGuid(UUID holdernameGuid) {
    this.holdernameGuid = holdernameGuid;
    return this;
  }

  /**
   * HoldernameGuid unique identifier associated with one or more parts of regulated pension providers or schemes.  see [Link](https://www.pensionsdashboardsprogramme.org.uk/standards/technical-standards) for details  
   * @return holdernameGuid
  */
  @NotNull @Valid 
  @Schema(name = "holdernameGuid", example = "816f82b5-8e4f-407b-8c54-afb179c4af5a", description = "HoldernameGuid unique identifier associated with one or more parts of regulated pension providers or schemes.  see [Link](https://www.pensionsdashboardsprogramme.org.uk/standards/technical-standards) for details  ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("holdernameGuid")
  public UUID getHoldernameGuid() {
    return holdernameGuid;
  }

  public void setHoldernameGuid(UUID holdernameGuid) {
    this.holdernameGuid = holdernameGuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HoldernameGuidDefinition holdernameGuidDefinition = (HoldernameGuidDefinition) o;
    return Objects.equals(this.holdernameGuid, holdernameGuidDefinition.holdernameGuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(holdernameGuid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HoldernameGuidDefinition {\n");
    sb.append("    holdernameGuid: ").append(toIndentedString(holdernameGuid)).append("\n");
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


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
 * SubmissionIdDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class SubmissionIdDefinition {

  private UUID submissionId = null;

  public SubmissionIdDefinition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SubmissionIdDefinition(UUID submissionId) {
    this.submissionId = submissionId;
  }

  public SubmissionIdDefinition submissionId(UUID submissionId) {
    this.submissionId = submissionId;
    return this;
  }

  /**
   * The submission id is a unique identifier for the batch of records as part of the same  data being reported. When submission_reason is \"null submission\", this field must be null. 
   * @return submissionId
  */
  @NotNull @Valid 
  @Schema(name = "submission_id", example = "b955b911-838c-4179-bd2f-089630c61a9d", description = "The submission id is a unique identifier for the batch of records as part of the same  data being reported. When submission_reason is \"null submission\", this field must be null. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("submission_id")
  public UUID getSubmissionId() {
    return submissionId;
  }

  public void setSubmissionId(UUID submissionId) {
    this.submissionId = submissionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmissionIdDefinition submissionIdDefinition = (SubmissionIdDefinition) o;
    return Objects.equals(this.submissionId, submissionIdDefinition.submissionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submissionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmissionIdDefinition {\n");
    sb.append("    submissionId: ").append(toIndentedString(submissionId)).append("\n");
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


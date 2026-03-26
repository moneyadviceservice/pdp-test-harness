package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SubmissionReasonNullNotIncluded
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class SubmissionReasonNullNotIncluded {

  /**
   * List the reason for this data upload/submission via api. Null submission is not included in this schema. 
   */
  public enum SubmissionReasonEnum {
    INITIAL_SUBMISSION("initial submission"),
    
    RESUBMISSION("resubmission");

    private String value;

    SubmissionReasonEnum(String value) {
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
    public static SubmissionReasonEnum fromValue(String value) {
      for (SubmissionReasonEnum b : SubmissionReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SubmissionReasonEnum submissionReason;

  public SubmissionReasonNullNotIncluded() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SubmissionReasonNullNotIncluded(SubmissionReasonEnum submissionReason) {
    this.submissionReason = submissionReason;
  }

  public SubmissionReasonNullNotIncluded submissionReason(SubmissionReasonEnum submissionReason) {
    this.submissionReason = submissionReason;
    return this;
  }

  /**
   * List the reason for this data upload/submission via api. Null submission is not included in this schema. 
   * @return submissionReason
  */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "submission_reason", example = "initial submission", description = "List the reason for this data upload/submission via api. Null submission is not included in this schema. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("submission_reason")
  public SubmissionReasonEnum getSubmissionReason() {
    return submissionReason;
  }

  public void setSubmissionReason(SubmissionReasonEnum submissionReason) {
    this.submissionReason = submissionReason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmissionReasonNullNotIncluded submissionReasonNullNotIncluded = (SubmissionReasonNullNotIncluded) o;
    return Objects.equals(this.submissionReason, submissionReasonNullNotIncluded.submissionReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submissionReason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmissionReasonNullNotIncluded {\n");
    sb.append("    submissionReason: ").append(toIndentedString(submissionReason)).append("\n");
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


package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GeneralReportingProperties
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class GeneralReportingProperties {

  private UUID recordId;

  /**
   * This is the status for this data upload/submission via the reporting api. 
   */
  public enum SubmissionStatusEnum {
    ON_TIME("on time"),
    
    CORRECTION("correction"),
    
    LATE("late");

    private String value;

    SubmissionStatusEnum(String value) {
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
    public static SubmissionStatusEnum fromValue(String value) {
      for (SubmissionStatusEnum b : SubmissionStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SubmissionStatusEnum submissionStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime reportingPeriodStart;

  public GeneralReportingProperties() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GeneralReportingProperties(UUID recordId, SubmissionStatusEnum submissionStatus, OffsetDateTime reportingPeriodStart) {
    this.recordId = recordId;
    this.submissionStatus = submissionStatus;
    this.reportingPeriodStart = reportingPeriodStart;
  }

  public GeneralReportingProperties recordId(UUID recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * The record_id is a unique identifier for the record for which data is being reported. This must be a valid UUID v4 format and is required for all reporting operations. 
   * @return recordId
  */
  @NotNull @Valid 
  @Schema(name = "record_id", example = "3eac3a8b-fbdc-4897-b3ea-908a6b411d7f", description = "The record_id is a unique identifier for the record for which data is being reported. This must be a valid UUID v4 format and is required for all reporting operations. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("record_id")
  public UUID getRecordId() {
    return recordId;
  }

  public void setRecordId(UUID recordId) {
    this.recordId = recordId;
  }

  public GeneralReportingProperties submissionStatus(SubmissionStatusEnum submissionStatus) {
    this.submissionStatus = submissionStatus;
    return this;
  }

  /**
   * This is the status for this data upload/submission via the reporting api. 
   * @return submissionStatus
  */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "submission_status", example = "correction", description = "This is the status for this data upload/submission via the reporting api. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("submission_status")
  public SubmissionStatusEnum getSubmissionStatus() {
    return submissionStatus;
  }

  public void setSubmissionStatus(SubmissionStatusEnum submissionStatus) {
    this.submissionStatus = submissionStatus;
  }

  public GeneralReportingProperties reportingPeriodStart(OffsetDateTime reportingPeriodStart) {
    this.reportingPeriodStart = reportingPeriodStart;
    return this;
  }

  /**
   * Reporting period start for which data was uploaded/submitted via api. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) 
   * @return reportingPeriodStart
  */
  @NotNull @Valid 
  @Schema(name = "reporting_period_start", example = "2025-03-19T00:00Z", description = "Reporting period start for which data was uploaded/submitted via api. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reporting_period_start")
  public OffsetDateTime getReportingPeriodStart() {
    return reportingPeriodStart;
  }

  public void setReportingPeriodStart(OffsetDateTime reportingPeriodStart) {
    this.reportingPeriodStart = reportingPeriodStart;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeneralReportingProperties generalReportingProperties = (GeneralReportingProperties) o;
    return Objects.equals(this.recordId, generalReportingProperties.recordId) &&
        Objects.equals(this.submissionStatus, generalReportingProperties.submissionStatus) &&
        Objects.equals(this.reportingPeriodStart, generalReportingProperties.reportingPeriodStart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordId, submissionStatus, reportingPeriodStart);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeneralReportingProperties {\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    submissionStatus: ").append(toIndentedString(submissionStatus)).append("\n");
    sb.append("    reportingPeriodStart: ").append(toIndentedString(reportingPeriodStart)).append("\n");
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


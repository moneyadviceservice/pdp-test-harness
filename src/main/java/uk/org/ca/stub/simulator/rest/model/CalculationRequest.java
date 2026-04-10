package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.ca.stub.simulator.rest.model.CalculationDataArray;
import uk.org.ca.stub.simulator.rest.model.MultipleCalculationDataMultipleCalculation;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CalculationRequest
 */


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class CalculationRequest {

  /**
   * Type of calculation for which data is reported for.  which can be single or multiple. 
   */
  public enum CalculationTypeEnum {
    SINGLE("single"),
    
    MULTIPLE("multiple");

    private String value;

    CalculationTypeEnum(String value) {
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
    public static CalculationTypeEnum fromValue(String value) {
      for (CalculationTypeEnum b : CalculationTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CalculationTypeEnum calculationType;

  private UUID holdernameGuid;

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

  /**
   * List the reason for this data upload/submission via api 
   */
  public enum SubmissionReasonEnum {
    INITIAL_SUBMISSION("initial submission"),
    
    RESUBMISSION("resubmission"),
    
    NULL_SUBMISSION("null submission");

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

  private UUID submissionId = null;

  private CalculationDataArray singleCalculation;

  private MultipleCalculationDataMultipleCalculation multipleCalculation;

  public CalculationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculationRequest(CalculationTypeEnum calculationType, UUID holdernameGuid, UUID recordId, SubmissionStatusEnum submissionStatus, OffsetDateTime reportingPeriodStart, SubmissionReasonEnum submissionReason, UUID submissionId, CalculationDataArray singleCalculation, MultipleCalculationDataMultipleCalculation multipleCalculation) {
    this.calculationType = calculationType;
    this.holdernameGuid = holdernameGuid;
    this.recordId = recordId;
    this.submissionStatus = submissionStatus;
    this.reportingPeriodStart = reportingPeriodStart;
    this.submissionReason = submissionReason;
    this.submissionId = submissionId;
    this.singleCalculation = singleCalculation;
    this.multipleCalculation = multipleCalculation;
  }

  public CalculationRequest calculationType(CalculationTypeEnum calculationType) {
    this.calculationType = calculationType;
    return this;
  }

  /**
   * Type of calculation for which data is reported for.  which can be single or multiple. 
   * @return calculationType
  */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "calculation_type", example = "multiple", description = "Type of calculation for which data is reported for.  which can be single or multiple. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculation_type")
  public CalculationTypeEnum getCalculationType() {
    return calculationType;
  }

  public void setCalculationType(CalculationTypeEnum calculationType) {
    this.calculationType = calculationType;
  }

  public CalculationRequest holdernameGuid(UUID holdernameGuid) {
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

  public CalculationRequest recordId(UUID recordId) {
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

  public CalculationRequest submissionStatus(SubmissionStatusEnum submissionStatus) {
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

  public CalculationRequest reportingPeriodStart(OffsetDateTime reportingPeriodStart) {
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

  public CalculationRequest submissionReason(SubmissionReasonEnum submissionReason) {
    this.submissionReason = submissionReason;
    return this;
  }

  /**
   * List the reason for this data upload/submission via api 
   * @return submissionReason
  */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "submission_reason", example = "initial submission", description = "List the reason for this data upload/submission via api ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("submission_reason")
  public SubmissionReasonEnum getSubmissionReason() {
    return submissionReason;
  }

  public void setSubmissionReason(SubmissionReasonEnum submissionReason) {
    this.submissionReason = submissionReason;
  }

  public CalculationRequest submissionId(UUID submissionId) {
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

  public CalculationRequest singleCalculation(CalculationDataArray singleCalculation) {
    this.singleCalculation = singleCalculation;
    return this;
  }

  /**
   * Get singleCalculation
   * @return singleCalculation
  */
  @NotNull @Valid 
  @Schema(name = "single_calculation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("single_calculation")
  public CalculationDataArray getSingleCalculation() {
    return singleCalculation;
  }

  public void setSingleCalculation(CalculationDataArray singleCalculation) {
    this.singleCalculation = singleCalculation;
  }

  public CalculationRequest multipleCalculation(MultipleCalculationDataMultipleCalculation multipleCalculation) {
    this.multipleCalculation = multipleCalculation;
    return this;
  }

  /**
   * Get multipleCalculation
   * @return multipleCalculation
  */
  @NotNull @Valid 
  @Schema(name = "multiple_calculation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("multiple_calculation")
  public MultipleCalculationDataMultipleCalculation getMultipleCalculation() {
    return multipleCalculation;
  }

  public void setMultipleCalculation(MultipleCalculationDataMultipleCalculation multipleCalculation) {
    this.multipleCalculation = multipleCalculation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculationRequest calculationRequest = (CalculationRequest) o;
    return Objects.equals(this.calculationType, calculationRequest.calculationType) &&
        Objects.equals(this.holdernameGuid, calculationRequest.holdernameGuid) &&
        Objects.equals(this.recordId, calculationRequest.recordId) &&
        Objects.equals(this.submissionStatus, calculationRequest.submissionStatus) &&
        Objects.equals(this.reportingPeriodStart, calculationRequest.reportingPeriodStart) &&
        Objects.equals(this.submissionReason, calculationRequest.submissionReason) &&
        Objects.equals(this.submissionId, calculationRequest.submissionId) &&
        Objects.equals(this.singleCalculation, calculationRequest.singleCalculation) &&
        Objects.equals(this.multipleCalculation, calculationRequest.multipleCalculation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calculationType, holdernameGuid, recordId, submissionStatus, reportingPeriodStart, submissionReason, submissionId, singleCalculation, multipleCalculation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculationRequest {\n");
    sb.append("    calculationType: ").append(toIndentedString(calculationType)).append("\n");
    sb.append("    holdernameGuid: ").append(toIndentedString(holdernameGuid)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    submissionStatus: ").append(toIndentedString(submissionStatus)).append("\n");
    sb.append("    reportingPeriodStart: ").append(toIndentedString(reportingPeriodStart)).append("\n");
    sb.append("    submissionReason: ").append(toIndentedString(submissionReason)).append("\n");
    sb.append("    submissionId: ").append(toIndentedString(submissionId)).append("\n");
    sb.append("    singleCalculation: ").append(toIndentedString(singleCalculation)).append("\n");
    sb.append("    multipleCalculation: ").append(toIndentedString(multipleCalculation)).append("\n");
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


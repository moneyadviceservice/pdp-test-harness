package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.ca.stub.simulator.rest.model.ViewResponseUnavailableRequestAllOfItems;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ViewResponseUnavailableRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class ViewResponseUnavailableRequest {

  private UUID holdernameGuid;

  private UUID submissionId = null;

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

  @Valid
  private List<ViewResponseUnavailableRequestAllOfItems> items = new ArrayList<>();

  public ViewResponseUnavailableRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ViewResponseUnavailableRequest(UUID holdernameGuid, UUID submissionId, UUID recordId, SubmissionStatusEnum submissionStatus, OffsetDateTime reportingPeriodStart, SubmissionReasonEnum submissionReason) {
    this.holdernameGuid = holdernameGuid;
    this.submissionId = submissionId;
    this.recordId = recordId;
    this.submissionStatus = submissionStatus;
    this.reportingPeriodStart = reportingPeriodStart;
    this.submissionReason = submissionReason;
  }

  public ViewResponseUnavailableRequest holdernameGuid(UUID holdernameGuid) {
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

  public ViewResponseUnavailableRequest submissionId(UUID submissionId) {
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

  public ViewResponseUnavailableRequest recordId(UUID recordId) {
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

  public ViewResponseUnavailableRequest submissionStatus(SubmissionStatusEnum submissionStatus) {
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

  public ViewResponseUnavailableRequest reportingPeriodStart(OffsetDateTime reportingPeriodStart) {
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

  public ViewResponseUnavailableRequest submissionReason(SubmissionReasonEnum submissionReason) {
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

  public ViewResponseUnavailableRequest items(List<ViewResponseUnavailableRequestAllOfItems> items) {
    this.items = items;
    return this;
  }

  public ViewResponseUnavailableRequest addItemsItem(ViewResponseUnavailableRequestAllOfItems itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
  */
  @Valid @Size(min = 1, max = 740) 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<ViewResponseUnavailableRequestAllOfItems> getItems() {
    return items;
  }

  public void setItems(List<ViewResponseUnavailableRequestAllOfItems> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ViewResponseUnavailableRequest viewResponseUnavailableRequest = (ViewResponseUnavailableRequest) o;
    return Objects.equals(this.holdernameGuid, viewResponseUnavailableRequest.holdernameGuid) &&
        Objects.equals(this.submissionId, viewResponseUnavailableRequest.submissionId) &&
        Objects.equals(this.recordId, viewResponseUnavailableRequest.recordId) &&
        Objects.equals(this.submissionStatus, viewResponseUnavailableRequest.submissionStatus) &&
        Objects.equals(this.reportingPeriodStart, viewResponseUnavailableRequest.reportingPeriodStart) &&
        Objects.equals(this.submissionReason, viewResponseUnavailableRequest.submissionReason) &&
        Objects.equals(this.items, viewResponseUnavailableRequest.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(holdernameGuid, submissionId, recordId, submissionStatus, reportingPeriodStart, submissionReason, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ViewResponseUnavailableRequest {\n");
    sb.append("    holdernameGuid: ").append(toIndentedString(holdernameGuid)).append("\n");
    sb.append("    submissionId: ").append(toIndentedString(submissionId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    submissionStatus: ").append(toIndentedString(submissionStatus)).append("\n");
    sb.append("    reportingPeriodStart: ").append(toIndentedString(reportingPeriodStart)).append("\n");
    sb.append("    submissionReason: ").append(toIndentedString(submissionReason)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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


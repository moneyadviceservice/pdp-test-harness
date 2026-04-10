package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * SubmitAuditEvent202Response
 */

@JsonTypeName("submit_audit_event_202_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-29T11:37:10.465265500Z[Europe/London]", comments = "Generator version: 7.5.0")
public class SubmitAuditEvent202Response {

  private UUID eventId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACCEPTED("ACCEPTED"),
    
    QUEUED("queued");

    private String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime receivedAt;

  public SubmitAuditEvent202Response eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Unique identifier for the submitted event
   * @return eventId
  */
  @Valid 
  @Schema(name = "event_id", example = "7f3e8d90-1234-5678-9abc-def012345678", description = "Unique identifier for the submitted event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public SubmitAuditEvent202Response status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  
  @Schema(name = "status", example = "ACCEPTED", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public SubmitAuditEvent202Response receivedAt(OffsetDateTime receivedAt) {
    this.receivedAt = receivedAt;
    return this;
  }

  /**
   * Timestamp when the event was received
   * @return receivedAt
  */
  @Valid 
  @Schema(name = "received_at", example = "2026-01-29T10:30:01Z", description = "Timestamp when the event was received", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("received_at")
  public OffsetDateTime getReceivedAt() {
    return receivedAt;
  }

  public void setReceivedAt(OffsetDateTime receivedAt) {
    this.receivedAt = receivedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmitAuditEvent202Response submitAuditEvent202Response = (SubmitAuditEvent202Response) o;
    return Objects.equals(this.eventId, submitAuditEvent202Response.eventId) &&
        Objects.equals(this.status, submitAuditEvent202Response.status) &&
        Objects.equals(this.receivedAt, submitAuditEvent202Response.receivedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, status, receivedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmitAuditEvent202Response {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    receivedAt: ").append(toIndentedString(receivedAt)).append("\n");
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


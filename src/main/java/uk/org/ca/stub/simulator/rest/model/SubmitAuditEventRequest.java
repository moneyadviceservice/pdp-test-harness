package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SubmitAuditEventRequest
 */

@JsonTypeName("submit_audit_event_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-29T11:37:10.465265500Z[Europe/London]", comments = "Generator version: 7.5.0")
public class SubmitAuditEventRequest {

  /**
   * The type of audit event
   */
  public enum EventTypeEnum {
    DAPA001("DAPA001"),
    
    DASA001("DASA001");

    private String value;

    EventTypeEnum(String value) {
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
    public static EventTypeEnum fromValue(String value) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EventTypeEnum eventType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private UUID userId;

  private UUID resourceId;

  private String dashboardId;

  private String sessionId;

  private String ipAddress;

  private String userAgent;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public SubmitAuditEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SubmitAuditEventRequest(EventTypeEnum eventType, OffsetDateTime timestamp) {
    this.eventType = eventType;
    this.timestamp = timestamp;
  }

  public SubmitAuditEventRequest eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * The type of audit event
   * @return eventType
  */
  @NotNull 
  @Schema(name = "event_type", example = "DAPA001", description = "The type of audit event", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_type")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public SubmitAuditEventRequest timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * ISO 8601 timestamp of when the event occurred
   * @return timestamp
  */
  @NotNull @Valid 
  @Schema(name = "timestamp", example = "2026-01-29T10:30Z", description = "ISO 8601 timestamp of when the event occurred", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public SubmitAuditEventRequest userId(UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User identifier
   * @return userId
  */
  @Valid 
  @Schema(name = "user_id", example = "550e8400-e29b-41d4-a716-446655440000", description = "User identifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user_id")
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public SubmitAuditEventRequest resourceId(UUID resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * Resource identifier (for view events)
   * @return resourceId
  */
  @Valid 
  @Schema(name = "resource_id", example = "92476c2f-25b8-4d87-afde-18a9ee2631dc", description = "Resource identifier (for view events)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resource_id")
  public UUID getResourceId() {
    return resourceId;
  }

  public void setResourceId(UUID resourceId) {
    this.resourceId = resourceId;
  }

  public SubmitAuditEventRequest dashboardId(String dashboardId) {
    this.dashboardId = dashboardId;
    return this;
  }

  /**
   * Dashboard identifier (for redirect events)
   * @return dashboardId
  */
  
  @Schema(name = "dashboard_id", example = "DB-001", description = "Dashboard identifier (for redirect events)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dashboard_id")
  public String getDashboardId() {
    return dashboardId;
  }

  public void setDashboardId(String dashboardId) {
    this.dashboardId = dashboardId;
  }

  public SubmitAuditEventRequest sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Session identifier
   * @return sessionId
  */
  
  @Schema(name = "session_id", example = "sess_abc123def456", description = "Session identifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public SubmitAuditEventRequest ipAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    return this;
  }

  /**
   * Client IP address
   * @return ipAddress
  */
  
  @Schema(name = "ip_address", example = "192.168.1.100", description = "Client IP address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ip_address")
  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public SubmitAuditEventRequest userAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  /**
   * Client user agent
   * @return userAgent
  */
  
  @Schema(name = "user_agent", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)", description = "Client user agent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user_agent")
  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public SubmitAuditEventRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public SubmitAuditEventRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Additional event metadata
   * @return metadata
  */
  
  @Schema(name = "metadata", description = "Additional event metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmitAuditEventRequest submitAuditEventRequest = (SubmitAuditEventRequest) o;
    return Objects.equals(this.eventType, submitAuditEventRequest.eventType) &&
        Objects.equals(this.timestamp, submitAuditEventRequest.timestamp) &&
        Objects.equals(this.userId, submitAuditEventRequest.userId) &&
        Objects.equals(this.resourceId, submitAuditEventRequest.resourceId) &&
        Objects.equals(this.dashboardId, submitAuditEventRequest.dashboardId) &&
        Objects.equals(this.sessionId, submitAuditEventRequest.sessionId) &&
        Objects.equals(this.ipAddress, submitAuditEventRequest.ipAddress) &&
        Objects.equals(this.userAgent, submitAuditEventRequest.userAgent) &&
        Objects.equals(this.metadata, submitAuditEventRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, timestamp, userId, resourceId, dashboardId, sessionId, ipAddress, userAgent, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmitAuditEventRequest {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    dashboardId: ").append(toIndentedString(dashboardId)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
    sb.append("    userAgent: ").append(toIndentedString(userAgent)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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


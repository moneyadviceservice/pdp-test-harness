package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RequestDetails
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class RequestDetails {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime reqDateTs;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime respDateTs;

  public RequestDetails() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RequestDetails(OffsetDateTime reqDateTs, OffsetDateTime respDateTs) {
    this.reqDateTs = reqDateTs;
    this.respDateTs = respDateTs;
  }

  public RequestDetails reqDateTs(OffsetDateTime reqDateTs) {
    this.reqDateTs = reqDateTs;
    return this;
  }

  /**
   * The date and time of the view request for each view response that exceeds 10 seconds per pension provider or scheme. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) 
   * @return reqDateTs
  */
  @NotNull @Valid 
  @Schema(name = "req_date_ts", example = "2021-11-25T00:00Z", description = "The date and time of the view request for each view response that exceeds 10 seconds per pension provider or scheme. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("req_date_ts")
  public OffsetDateTime getReqDateTs() {
    return reqDateTs;
  }

  public void setReqDateTs(OffsetDateTime reqDateTs) {
    this.reqDateTs = reqDateTs;
  }

  public RequestDetails respDateTs(OffsetDateTime respDateTs) {
    this.respDateTs = respDateTs;
    return this;
  }

  /**
   * The date and time of the view request for each view response that exceeds 10 seconds per pension provider or scheme. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) 
   * @return respDateTs
  */
  @NotNull @Valid 
  @Schema(name = "resp_date_ts", example = "2021-11-25T00:00Z", description = "The date and time of the view request for each view response that exceeds 10 seconds per pension provider or scheme. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resp_date_ts")
  public OffsetDateTime getRespDateTs() {
    return respDateTs;
  }

  public void setRespDateTs(OffsetDateTime respDateTs) {
    this.respDateTs = respDateTs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestDetails requestDetails = (RequestDetails) o;
    return Objects.equals(this.reqDateTs, requestDetails.reqDateTs) &&
        Objects.equals(this.respDateTs, requestDetails.respDateTs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reqDateTs, respDateTs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestDetails {\n");
    sb.append("    reqDateTs: ").append(toIndentedString(reqDateTs)).append("\n");
    sb.append("    respDateTs: ").append(toIndentedString(respDateTs)).append("\n");
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


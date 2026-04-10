package uk.org.ca.stub.simulator.rest.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DefaultResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class DefaultResponse {

  private String message;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime datetimestamp;

  public DefaultResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DefaultResponse(String message, OffsetDateTime datetimestamp) {
    this.message = message;
    this.datetimestamp = datetimestamp;
  }

  public DefaultResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * message for the response
   * @return message
  */
  @NotNull @Pattern(regexp = "^[a-zA-Z0-9_]*$") @Size(min = 1, max = 255) 
  @Schema(name = "message", example = "ACCEPTED", description = "message for the response", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public DefaultResponse datetimestamp(OffsetDateTime datetimestamp) {
    this.datetimestamp = datetimestamp;
    return this;
  }

  /**
   * Reporting data submission time stamp. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) 
   * @return datetimestamp
  */
  @NotNull @Valid 
  @Schema(name = "datetimestamp", example = "2021-11-25T00:00:00.000Z", description = "Reporting data submission time stamp. For more Details see [rfc3339](https://www.rfc-editor.org/rfc/rfc3339#section-5.8) ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datetimestamp")
  @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = uk.org.ca.stub.simulator.config.OffsetDateTimeMillisSerializer.class)
  public OffsetDateTime getDatetimestamp() {
    return datetimestamp;
  }

  public void setDatetimestamp(OffsetDateTime datetimestamp) {
    this.datetimestamp = datetimestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultResponse defaultResponse = (DefaultResponse) o;
    return Objects.equals(this.message, defaultResponse.message) &&
        Objects.equals(this.datetimestamp, defaultResponse.datetimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, datetimestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultResponse {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    datetimestamp: ").append(toIndentedString(datetimestamp)).append("\n");
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


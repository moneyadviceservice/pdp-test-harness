package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RequestNumber
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class RequestNumber {

  private Integer reqCount;

  public RequestNumber() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RequestNumber(Integer reqCount) {
    this.reqCount = reqCount;
  }

  public RequestNumber reqCount(Integer reqCount) {
    this.reqCount = reqCount;
    return this;
  }

  /**
   * The number of view requests received per pension provider or scheme.
   * minimum: 0
   * @return reqCount
  */
  @NotNull @Min(0) 
  @Schema(name = "req_count", example = "100", description = "The number of view requests received per pension provider or scheme.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("req_count")
  public Integer getReqCount() {
    return reqCount;
  }

  public void setReqCount(Integer reqCount) {
    this.reqCount = reqCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestNumber requestNumber = (RequestNumber) o;
    return Objects.equals(this.reqCount, requestNumber.reqCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reqCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestNumber {\n");
    sb.append("    reqCount: ").append(toIndentedString(reqCount)).append("\n");
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


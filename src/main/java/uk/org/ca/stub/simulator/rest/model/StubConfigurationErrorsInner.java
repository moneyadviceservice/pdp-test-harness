package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StubConfigurationErrorsInner
 */

@JsonTypeName("StubConfiguration_errors_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class StubConfigurationErrorsInner {

  private String code;

  public StubConfigurationErrorsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StubConfigurationErrorsInner(String code) {
    this.code = code;
  }

  public StubConfigurationErrorsInner code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Machine-readable error code.
   * @return code
  */
  @NotNull 
  @Schema(name = "code", example = "TOO_MANY_REQUESTS", description = "Machine-readable error code.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StubConfigurationErrorsInner stubConfigurationErrorsInner = (StubConfigurationErrorsInner) o;
    return Objects.equals(this.code, stubConfigurationErrorsInner.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StubConfigurationErrorsInner {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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


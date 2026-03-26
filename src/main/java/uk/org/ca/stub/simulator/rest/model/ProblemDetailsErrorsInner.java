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
 * ProblemDetailsErrorsInner
 */

@JsonTypeName("ProblemDetails_errors_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class ProblemDetailsErrorsInner {

  private String code;

  public ProblemDetailsErrorsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProblemDetailsErrorsInner(String code) {
    this.code = code;
  }

  public ProblemDetailsErrorsInner code(String code) {
    this.code = code;
    return this;
  }

  /**
   * A machine-readable and human understandable code for this occurrence of the problem and defined by the PDP.
   * @return code
  */
  @NotNull @Size(max = 100) 
  @Schema(name = "code", example = "INVALID_X_REQUEST_ID", description = "A machine-readable and human understandable code for this occurrence of the problem and defined by the PDP.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    ProblemDetailsErrorsInner problemDetailsErrorsInner = (ProblemDetailsErrorsInner) o;
    return Objects.equals(this.code, problemDetailsErrorsInner.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProblemDetailsErrorsInner {\n");
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


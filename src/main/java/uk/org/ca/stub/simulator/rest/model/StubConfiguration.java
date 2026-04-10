package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.org.ca.stub.simulator.rest.model.StubConfigurationErrorsInner;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StubConfiguration
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class StubConfiguration {

  /**
   * One of the supported stub endpoints to override.
   */
  public enum EndpointEnum {
    SERVICE_AVAILABILITY_FIND("/service-availability/find"),
    
    SERVICE_AVAILABILITY_VIEW("/service-availability/view"),
    
    VIEW_RESPONSE_REQUEST_NUMBER("/view-response/request-number"),
    
    VIEW_RESPONSE_RESPONSE_TIME("/view-response/response-time"),
    
    VIEW_RESPONSE_CALCULATIONS("/view-response/calculations"),
    
    VIEW_RESPONSE_UNAVAILABLE("/view-response/unavailable");

    private String value;

    EndpointEnum(String value) {
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
    public static EndpointEnum fromValue(String value) {
      for (EndpointEnum b : EndpointEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EndpointEnum endpoint;

  private Integer status;

  private String code;

  private Integer count;

  @Valid
  private List<@Valid StubConfigurationErrorsInner> errors = new ArrayList<>();

  public StubConfiguration() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StubConfiguration(EndpointEnum endpoint, Integer status, String code, Integer count, List<@Valid StubConfigurationErrorsInner> errors) {
    this.endpoint = endpoint;
    this.status = status;
    this.code = code;
    this.count = count;
    this.errors = errors;
  }

  public StubConfiguration endpoint(EndpointEnum endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  /**
   * One of the supported stub endpoints to override.
   * @return endpoint
  */
  @NotNull 
  @Schema(name = "endpoint", description = "One of the supported stub endpoints to override.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endpoint")
  public EndpointEnum getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(EndpointEnum endpoint) {
    this.endpoint = endpoint;
  }

  public StubConfiguration status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * HTTP status code to return for this endpoint.
   * minimum: 100
   * maximum: 599
   * @return status
  */
  @NotNull @Min(100) @Max(599) 
  @Schema(name = "status", example = "429", description = "HTTP status code to return for this endpoint.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public StubConfiguration code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Top-level machine-readable error code for this configuration.
   * @return code
  */
  @NotNull 
  @Schema(name = "code", example = "TOO_MANY_REQUESTS", description = "Top-level machine-readable error code for this configuration.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public StubConfiguration count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Number of times to return the configured error before reverting to normal behaviour.
   * minimum: 0
   * @return count
  */
  @NotNull @Min(0) 
  @Schema(name = "count", example = "3", description = "Number of times to return the configured error before reverting to normal behaviour.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("count")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public StubConfiguration errors(List<@Valid StubConfigurationErrorsInner> errors) {
    this.errors = errors;
    return this;
  }

  public StubConfiguration addErrorsItem(StubConfigurationErrorsInner errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Array of error entries that will appear in the stubbed response.
   * @return errors
  */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "errors", description = "Array of error entries that will appear in the stubbed response.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("errors")
  public List<@Valid StubConfigurationErrorsInner> getErrors() {
    return errors;
  }

  public void setErrors(List<@Valid StubConfigurationErrorsInner> errors) {
    this.errors = errors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StubConfiguration stubConfiguration = (StubConfiguration) o;
    return Objects.equals(this.endpoint, stubConfiguration.endpoint) &&
        Objects.equals(this.status, stubConfiguration.status) &&
        Objects.equals(this.code, stubConfiguration.code) &&
        Objects.equals(this.count, stubConfiguration.count) &&
        Objects.equals(this.errors, stubConfiguration.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, status, code, count, errors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StubConfiguration {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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


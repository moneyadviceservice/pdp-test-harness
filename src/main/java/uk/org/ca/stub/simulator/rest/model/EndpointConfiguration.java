package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EndpointConfiguration
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-05T14:38:53.220391Z[Europe/London]", comments = "Generator version: 7.5.0")
public class EndpointConfiguration {

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

  private Integer responseCode;

  @Valid
  private Map<String, Object> responseBody = new HashMap<>();

  private Integer count;

  public EndpointConfiguration() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EndpointConfiguration(EndpointEnum endpoint, Integer responseCode, Integer count) {
    this.endpoint = endpoint;
    this.responseCode = responseCode;
    this.count = count;
  }

  public EndpointConfiguration endpoint(EndpointEnum endpoint) {
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

  public EndpointConfiguration responseCode(Integer responseCode) {
    this.responseCode = responseCode;
    return this;
  }

  /**
   * HTTP status code to return for this endpoint while the configuration is active.
   * minimum: 100
   * maximum: 599
   * @return responseCode
  */
  @NotNull @Min(100) @Max(599) 
  @Schema(name = "response_code", description = "HTTP status code to return for this endpoint while the configuration is active.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("response_code")
  public Integer getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(Integer responseCode) {
    this.responseCode = responseCode;
  }

  public EndpointConfiguration responseBody(Map<String, Object> responseBody) {
    this.responseBody = responseBody;
    return this;
  }

  public EndpointConfiguration putResponseBodyItem(String key, Object responseBodyItem) {
    if (this.responseBody == null) {
      this.responseBody = new HashMap<>();
    }
    this.responseBody.put(key, responseBodyItem);
    return this;
  }

  /**
   * JSON body to return for this endpoint while the configuration is active.
   * @return responseBody
  */
  
  @Schema(name = "response_body", description = "JSON body to return for this endpoint while the configuration is active.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response_body")
  public Map<String, Object> getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(Map<String, Object> responseBody) {
    this.responseBody = responseBody;
  }

  public EndpointConfiguration count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Number of times this configuration should be applied before normal behavior resumes.
   * minimum: 1
   * @return count
  */
  @NotNull @Min(1) 
  @Schema(name = "count", description = "Number of times this configuration should be applied before normal behavior resumes.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("count")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointConfiguration endpointConfiguration = (EndpointConfiguration) o;
    return Objects.equals(this.endpoint, endpointConfiguration.endpoint) &&
        Objects.equals(this.responseCode, endpointConfiguration.responseCode) &&
        Objects.equals(this.responseBody, endpointConfiguration.responseBody) &&
        Objects.equals(this.count, endpointConfiguration.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, responseCode, responseBody, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointConfiguration {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    responseCode: ").append(toIndentedString(responseCode)).append("\n");
    sb.append("    responseBody: ").append(toIndentedString(responseBody)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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


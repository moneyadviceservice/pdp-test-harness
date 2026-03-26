package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BaseCalculationRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class BaseCalculationRequest {

  /**
   * Type of calculation for which data is reported for.  which can be single or multiple. 
   */
  public enum CalculationTypeEnum {
    SINGLE("single"),
    
    MULTIPLE("multiple");

    private String value;

    CalculationTypeEnum(String value) {
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
    public static CalculationTypeEnum fromValue(String value) {
      for (CalculationTypeEnum b : CalculationTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CalculationTypeEnum calculationType;

  private UUID holdernameGuid;

  public BaseCalculationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BaseCalculationRequest(CalculationTypeEnum calculationType, UUID holdernameGuid) {
    this.calculationType = calculationType;
    this.holdernameGuid = holdernameGuid;
  }

  public BaseCalculationRequest calculationType(CalculationTypeEnum calculationType) {
    this.calculationType = calculationType;
    return this;
  }

  /**
   * Type of calculation for which data is reported for.  which can be single or multiple. 
   * @return calculationType
  */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "calculation_type", example = "multiple", description = "Type of calculation for which data is reported for.  which can be single or multiple. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculation_type")
  public CalculationTypeEnum getCalculationType() {
    return calculationType;
  }

  public void setCalculationType(CalculationTypeEnum calculationType) {
    this.calculationType = calculationType;
  }

  public BaseCalculationRequest holdernameGuid(UUID holdernameGuid) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseCalculationRequest baseCalculationRequest = (BaseCalculationRequest) o;
    return Objects.equals(this.calculationType, baseCalculationRequest.calculationType) &&
        Objects.equals(this.holdernameGuid, baseCalculationRequest.holdernameGuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calculationType, holdernameGuid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseCalculationRequest {\n");
    sb.append("    calculationType: ").append(toIndentedString(calculationType)).append("\n");
    sb.append("    holdernameGuid: ").append(toIndentedString(holdernameGuid)).append("\n");
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


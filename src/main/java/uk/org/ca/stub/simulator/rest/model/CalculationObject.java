package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CalculationObject
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class CalculationObject {

  /**
   * Must report per pension provider or scheme, each instance where at least one ERI value is not available for immediate return. For each instance:  whether all the benefits for which calculations are required are DC or not (by reporting the unavailable code returned as per the data standards) codes ‘[DBC’ and /’DCC’] for data standards 2.301/2.401.).  
   */
  public enum ValueCodeEnum {
    DBC("DBC"),
    
    DCC("DCC");

    private String value;

    ValueCodeEnum(String value) {
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
    public static ValueCodeEnum fromValue(String value) {
      for (ValueCodeEnum b : ValueCodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ValueCodeEnum valueCode;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime calculationStartDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime calculationEndDate;

  public CalculationObject() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculationObject(ValueCodeEnum valueCode, OffsetDateTime calculationStartDate, OffsetDateTime calculationEndDate) {
    this.valueCode = valueCode;
    this.calculationStartDate = calculationStartDate;
    this.calculationEndDate = calculationEndDate;
  }

  public CalculationObject valueCode(ValueCodeEnum valueCode) {
    this.valueCode = valueCode;
    return this;
  }

  /**
   * Must report per pension provider or scheme, each instance where at least one ERI value is not available for immediate return. For each instance:  whether all the benefits for which calculations are required are DC or not (by reporting the unavailable code returned as per the data standards) codes ‘[DBC’ and /’DCC’] for data standards 2.301/2.401.).  
   * @return valueCode
  */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "value_code", example = "DBC", description = "Must report per pension provider or scheme, each instance where at least one ERI value is not available for immediate return. For each instance:  whether all the benefits for which calculations are required are DC or not (by reporting the unavailable code returned as per the data standards) codes ‘[DBC’ and /’DCC’] for data standards 2.301/2.401.).  ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value_code")
  public ValueCodeEnum getValueCode() {
    return valueCode;
  }

  public void setValueCode(ValueCodeEnum valueCode) {
    this.valueCode = valueCode;
  }

  public CalculationObject calculationStartDate(OffsetDateTime calculationStartDate) {
    this.calculationStartDate = calculationStartDate;
    return this;
  }

  /**
   * The calculation time (time taken for the data to be available for return). This is expressed in date format consistent with ISO 8601:2004 date format. 
   * @return calculationStartDate
  */
  @NotNull @Valid 
  @Schema(name = "calculation_start_date", example = "2021-11-15T00:00Z", description = "The calculation time (time taken for the data to be available for return). This is expressed in date format consistent with ISO 8601:2004 date format. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculation_start_date")
  public OffsetDateTime getCalculationStartDate() {
    return calculationStartDate;
  }

  public void setCalculationStartDate(OffsetDateTime calculationStartDate) {
    this.calculationStartDate = calculationStartDate;
  }

  public CalculationObject calculationEndDate(OffsetDateTime calculationEndDate) {
    this.calculationEndDate = calculationEndDate;
    return this;
  }

  /**
   * The calculation time (time taken for the data to be available for return). This is expressed in date format consistent with ISO 8601:2004 date format. 
   * @return calculationEndDate
  */
  @NotNull @Valid 
  @Schema(name = "calculation_end_date", example = "2021-11-25T00:00Z", description = "The calculation time (time taken for the data to be available for return). This is expressed in date format consistent with ISO 8601:2004 date format. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculation_end_date")
  public OffsetDateTime getCalculationEndDate() {
    return calculationEndDate;
  }

  public void setCalculationEndDate(OffsetDateTime calculationEndDate) {
    this.calculationEndDate = calculationEndDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculationObject calculationObject = (CalculationObject) o;
    return Objects.equals(this.valueCode, calculationObject.valueCode) &&
        Objects.equals(this.calculationStartDate, calculationObject.calculationStartDate) &&
        Objects.equals(this.calculationEndDate, calculationObject.calculationEndDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valueCode, calculationStartDate, calculationEndDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculationObject {\n");
    sb.append("    valueCode: ").append(toIndentedString(valueCode)).append("\n");
    sb.append("    calculationStartDate: ").append(toIndentedString(calculationStartDate)).append("\n");
    sb.append("    calculationEndDate: ").append(toIndentedString(calculationEndDate)).append("\n");
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


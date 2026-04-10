package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.org.ca.stub.simulator.rest.model.CalculationDataArray;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MultipleCalculationDataMultipleCalculation
 */

@JsonTypeName("MultipleCalculationData_multiple_calculation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class MultipleCalculationDataMultipleCalculation {

  private CalculationDataArray eriCalculationData;

  private CalculationDataArray accruedCalculationData;

  public MultipleCalculationDataMultipleCalculation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MultipleCalculationDataMultipleCalculation(CalculationDataArray eriCalculationData, CalculationDataArray accruedCalculationData) {
    this.eriCalculationData = eriCalculationData;
    this.accruedCalculationData = accruedCalculationData;
  }

  public MultipleCalculationDataMultipleCalculation eriCalculationData(CalculationDataArray eriCalculationData) {
    this.eriCalculationData = eriCalculationData;
    return this;
  }

  /**
   * Get eriCalculationData
   * @return eriCalculationData
  */
  @NotNull @Valid 
  @Schema(name = "eriCalculationData", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eriCalculationData")
  public CalculationDataArray getEriCalculationData() {
    return eriCalculationData;
  }

  public void setEriCalculationData(CalculationDataArray eriCalculationData) {
    this.eriCalculationData = eriCalculationData;
  }

  public MultipleCalculationDataMultipleCalculation accruedCalculationData(CalculationDataArray accruedCalculationData) {
    this.accruedCalculationData = accruedCalculationData;
    return this;
  }

  /**
   * Get accruedCalculationData
   * @return accruedCalculationData
  */
  @NotNull @Valid 
  @Schema(name = "accruedCalculationData", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accruedCalculationData")
  public CalculationDataArray getAccruedCalculationData() {
    return accruedCalculationData;
  }

  public void setAccruedCalculationData(CalculationDataArray accruedCalculationData) {
    this.accruedCalculationData = accruedCalculationData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultipleCalculationDataMultipleCalculation multipleCalculationDataMultipleCalculation = (MultipleCalculationDataMultipleCalculation) o;
    return Objects.equals(this.eriCalculationData, multipleCalculationDataMultipleCalculation.eriCalculationData) &&
        Objects.equals(this.accruedCalculationData, multipleCalculationDataMultipleCalculation.accruedCalculationData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eriCalculationData, accruedCalculationData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultipleCalculationDataMultipleCalculation {\n");
    sb.append("    eriCalculationData: ").append(toIndentedString(eriCalculationData)).append("\n");
    sb.append("    accruedCalculationData: ").append(toIndentedString(accruedCalculationData)).append("\n");
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


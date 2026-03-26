package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import uk.org.ca.stub.simulator.rest.model.MultipleCalculationDataMultipleCalculation;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MultipleCalculationData
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class MultipleCalculationData {

  private MultipleCalculationDataMultipleCalculation multipleCalculation;

  public MultipleCalculationData() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MultipleCalculationData(MultipleCalculationDataMultipleCalculation multipleCalculation) {
    this.multipleCalculation = multipleCalculation;
  }

  public MultipleCalculationData multipleCalculation(MultipleCalculationDataMultipleCalculation multipleCalculation) {
    this.multipleCalculation = multipleCalculation;
    return this;
  }

  /**
   * Get multipleCalculation
   * @return multipleCalculation
  */
  @NotNull @Valid 
  @Schema(name = "multiple_calculation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("multiple_calculation")
  public MultipleCalculationDataMultipleCalculation getMultipleCalculation() {
    return multipleCalculation;
  }

  public void setMultipleCalculation(MultipleCalculationDataMultipleCalculation multipleCalculation) {
    this.multipleCalculation = multipleCalculation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultipleCalculationData multipleCalculationData = (MultipleCalculationData) o;
    return Objects.equals(this.multipleCalculation, multipleCalculationData.multipleCalculation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(multipleCalculation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultipleCalculationData {\n");
    sb.append("    multipleCalculation: ").append(toIndentedString(multipleCalculation)).append("\n");
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


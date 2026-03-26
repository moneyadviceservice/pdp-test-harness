package uk.org.ca.stub.simulator.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import uk.org.ca.stub.simulator.rest.model.CalculationDataArray;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SingleCalculationData
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class SingleCalculationData {

  private CalculationDataArray singleCalculation;

  public SingleCalculationData() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SingleCalculationData(CalculationDataArray singleCalculation) {
    this.singleCalculation = singleCalculation;
  }

  public SingleCalculationData singleCalculation(CalculationDataArray singleCalculation) {
    this.singleCalculation = singleCalculation;
    return this;
  }

  /**
   * Get singleCalculation
   * @return singleCalculation
  */
  @NotNull @Valid 
  @Schema(name = "single_calculation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("single_calculation")
  public CalculationDataArray getSingleCalculation() {
    return singleCalculation;
  }

  public void setSingleCalculation(CalculationDataArray singleCalculation) {
    this.singleCalculation = singleCalculation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SingleCalculationData singleCalculationData = (SingleCalculationData) o;
    return Objects.equals(this.singleCalculation, singleCalculationData.singleCalculation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(singleCalculation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SingleCalculationData {\n");
    sb.append("    singleCalculation: ").append(toIndentedString(singleCalculation)).append("\n");
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


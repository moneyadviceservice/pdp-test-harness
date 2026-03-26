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
 * AccruedUnavailableData
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class AccruedUnavailableData {

  private Integer accruedUnavailAnoCount;

  private Integer accruedUnavailPpfCount;

  private Integer accruedUnavailTrnCount;

  public AccruedUnavailableData() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AccruedUnavailableData(Integer accruedUnavailAnoCount, Integer accruedUnavailPpfCount, Integer accruedUnavailTrnCount) {
    this.accruedUnavailAnoCount = accruedUnavailAnoCount;
    this.accruedUnavailPpfCount = accruedUnavailPpfCount;
    this.accruedUnavailTrnCount = accruedUnavailTrnCount;
  }

  public AccruedUnavailableData accruedUnavailAnoCount(Integer accruedUnavailAnoCount) {
    this.accruedUnavailAnoCount = accruedUnavailAnoCount;
    return this;
  }

  /**
   * Total count of returns of item 2.401 (accrued unavailable) as code ‘ANO’.  
   * minimum: 0
   * @return accruedUnavailAnoCount
  */
  @NotNull @Min(0) 
  @Schema(name = "accrued_unavail_ano_count", example = "0", description = "Total count of returns of item 2.401 (accrued unavailable) as code ‘ANO’.  ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accrued_unavail_ano_count")
  public Integer getAccruedUnavailAnoCount() {
    return accruedUnavailAnoCount;
  }

  public void setAccruedUnavailAnoCount(Integer accruedUnavailAnoCount) {
    this.accruedUnavailAnoCount = accruedUnavailAnoCount;
  }

  public AccruedUnavailableData accruedUnavailPpfCount(Integer accruedUnavailPpfCount) {
    this.accruedUnavailPpfCount = accruedUnavailPpfCount;
    return this;
  }

  /**
   * Total count of returns of item 2.401 (accrued unavailable) as code ‘PPF’.    
   * minimum: 0
   * @return accruedUnavailPpfCount
  */
  @NotNull @Min(0) 
  @Schema(name = "accrued_unavail_ppf_count", example = "0", description = "Total count of returns of item 2.401 (accrued unavailable) as code ‘PPF’.    ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accrued_unavail_ppf_count")
  public Integer getAccruedUnavailPpfCount() {
    return accruedUnavailPpfCount;
  }

  public void setAccruedUnavailPpfCount(Integer accruedUnavailPpfCount) {
    this.accruedUnavailPpfCount = accruedUnavailPpfCount;
  }

  public AccruedUnavailableData accruedUnavailTrnCount(Integer accruedUnavailTrnCount) {
    this.accruedUnavailTrnCount = accruedUnavailTrnCount;
    return this;
  }

  /**
   * Total count of returns of item 2.401 (accrued unavailable) as code ‘TRN’.    
   * minimum: 0
   * @return accruedUnavailTrnCount
  */
  @NotNull @Min(0) 
  @Schema(name = "accrued_unavail_trn_count", example = "0", description = "Total count of returns of item 2.401 (accrued unavailable) as code ‘TRN’.    ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accrued_unavail_trn_count")
  public Integer getAccruedUnavailTrnCount() {
    return accruedUnavailTrnCount;
  }

  public void setAccruedUnavailTrnCount(Integer accruedUnavailTrnCount) {
    this.accruedUnavailTrnCount = accruedUnavailTrnCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccruedUnavailableData accruedUnavailableData = (AccruedUnavailableData) o;
    return Objects.equals(this.accruedUnavailAnoCount, accruedUnavailableData.accruedUnavailAnoCount) &&
        Objects.equals(this.accruedUnavailPpfCount, accruedUnavailableData.accruedUnavailPpfCount) &&
        Objects.equals(this.accruedUnavailTrnCount, accruedUnavailableData.accruedUnavailTrnCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accruedUnavailAnoCount, accruedUnavailPpfCount, accruedUnavailTrnCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccruedUnavailableData {\n");
    sb.append("    accruedUnavailAnoCount: ").append(toIndentedString(accruedUnavailAnoCount)).append("\n");
    sb.append("    accruedUnavailPpfCount: ").append(toIndentedString(accruedUnavailPpfCount)).append("\n");
    sb.append("    accruedUnavailTrnCount: ").append(toIndentedString(accruedUnavailTrnCount)).append("\n");
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


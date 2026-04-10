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
 * EriUnavailableData
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class EriUnavailableData {

  private Integer eriUnavailAnoCount;

  private Integer eriUnavailPpfCount;

  private Integer eriUnavailTrnCount;

  public EriUnavailableData() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EriUnavailableData(Integer eriUnavailAnoCount, Integer eriUnavailPpfCount, Integer eriUnavailTrnCount) {
    this.eriUnavailAnoCount = eriUnavailAnoCount;
    this.eriUnavailPpfCount = eriUnavailPpfCount;
    this.eriUnavailTrnCount = eriUnavailTrnCount;
  }

  public EriUnavailableData eriUnavailAnoCount(Integer eriUnavailAnoCount) {
    this.eriUnavailAnoCount = eriUnavailAnoCount;
    return this;
  }

  /**
   * Total count of returns of item 2.301 (ERI unavailable) as code ‘ANO’.   
   * minimum: 0
   * @return eriUnavailAnoCount
  */
  @NotNull @Min(0) 
  @Schema(name = "eri_unavail_ano_count", example = "0", description = "Total count of returns of item 2.301 (ERI unavailable) as code ‘ANO’.   ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eri_unavail_ano_count")
  public Integer getEriUnavailAnoCount() {
    return eriUnavailAnoCount;
  }

  public void setEriUnavailAnoCount(Integer eriUnavailAnoCount) {
    this.eriUnavailAnoCount = eriUnavailAnoCount;
  }

  public EriUnavailableData eriUnavailPpfCount(Integer eriUnavailPpfCount) {
    this.eriUnavailPpfCount = eriUnavailPpfCount;
    return this;
  }

  /**
   * Total count of returns of item 2.301 (ERI unavailable) as code ‘PPF’.    
   * minimum: 0
   * @return eriUnavailPpfCount
  */
  @NotNull @Min(0) 
  @Schema(name = "eri_unavail_ppf_count", example = "0", description = "Total count of returns of item 2.301 (ERI unavailable) as code ‘PPF’.    ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eri_unavail_ppf_count")
  public Integer getEriUnavailPpfCount() {
    return eriUnavailPpfCount;
  }

  public void setEriUnavailPpfCount(Integer eriUnavailPpfCount) {
    this.eriUnavailPpfCount = eriUnavailPpfCount;
  }

  public EriUnavailableData eriUnavailTrnCount(Integer eriUnavailTrnCount) {
    this.eriUnavailTrnCount = eriUnavailTrnCount;
    return this;
  }

  /**
   * Total count of returns of item 2.301 (ERI unavailable) as code ‘TRN’.    
   * minimum: 0
   * @return eriUnavailTrnCount
  */
  @NotNull @Min(0) 
  @Schema(name = "eri_unavail_trn_count", example = "0", description = "Total count of returns of item 2.301 (ERI unavailable) as code ‘TRN’.    ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eri_unavail_trn_count")
  public Integer getEriUnavailTrnCount() {
    return eriUnavailTrnCount;
  }

  public void setEriUnavailTrnCount(Integer eriUnavailTrnCount) {
    this.eriUnavailTrnCount = eriUnavailTrnCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EriUnavailableData eriUnavailableData = (EriUnavailableData) o;
    return Objects.equals(this.eriUnavailAnoCount, eriUnavailableData.eriUnavailAnoCount) &&
        Objects.equals(this.eriUnavailPpfCount, eriUnavailableData.eriUnavailPpfCount) &&
        Objects.equals(this.eriUnavailTrnCount, eriUnavailableData.eriUnavailTrnCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eriUnavailAnoCount, eriUnavailPpfCount, eriUnavailTrnCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EriUnavailableData {\n");
    sb.append("    eriUnavailAnoCount: ").append(toIndentedString(eriUnavailAnoCount)).append("\n");
    sb.append("    eriUnavailPpfCount: ").append(toIndentedString(eriUnavailPpfCount)).append("\n");
    sb.append("    eriUnavailTrnCount: ").append(toIndentedString(eriUnavailTrnCount)).append("\n");
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


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
 * ViewResponseUnavailableRequestAllOfItems
 */

@JsonTypeName("ViewResponseUnavailableRequest_allOf_items")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class ViewResponseUnavailableRequestAllOfItems {

  private Integer contactCount;

  private Integer missingadminCount;

  private Integer temperrorCount;

  private Integer eriUnavailAnoCount;

  private Integer eriUnavailPpfCount;

  private Integer eriUnavailTrnCount;

  private Integer accruedUnavailAnoCount;

  private Integer accruedUnavailPpfCount;

  private Integer accruedUnavailTrnCount;

  public ViewResponseUnavailableRequestAllOfItems() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ViewResponseUnavailableRequestAllOfItems(Integer contactCount, Integer missingadminCount, Integer temperrorCount, Integer eriUnavailAnoCount, Integer eriUnavailPpfCount, Integer eriUnavailTrnCount, Integer accruedUnavailAnoCount, Integer accruedUnavailPpfCount, Integer accruedUnavailTrnCount) {
    this.contactCount = contactCount;
    this.missingadminCount = missingadminCount;
    this.temperrorCount = temperrorCount;
    this.eriUnavailAnoCount = eriUnavailAnoCount;
    this.eriUnavailPpfCount = eriUnavailPpfCount;
    this.eriUnavailTrnCount = eriUnavailTrnCount;
    this.accruedUnavailAnoCount = accruedUnavailAnoCount;
    this.accruedUnavailPpfCount = accruedUnavailPpfCount;
    this.accruedUnavailTrnCount = accruedUnavailTrnCount;
  }

  public ViewResponseUnavailableRequestAllOfItems contactCount(Integer contactCount) {
    this.contactCount = contactCount;
    return this;
  }

  /**
   * Total count of returns of item 2.004 (contact pension provider or scheme) populated ‘true’.  Code to indicate there has been a match but the user should contact the pension provider/scheme  before it provides any or all the user’s pensions information. 
   * minimum: 0
   * @return contactCount
  */
  @NotNull @Min(0) 
  @Schema(name = "contact_count", example = "12", description = "Total count of returns of item 2.004 (contact pension provider or scheme) populated ‘true’.  Code to indicate there has been a match but the user should contact the pension provider/scheme  before it provides any or all the user’s pensions information. ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contact_count")
  public Integer getContactCount() {
    return contactCount;
  }

  public void setContactCount(Integer contactCount) {
    this.contactCount = contactCount;
  }

  public ViewResponseUnavailableRequestAllOfItems missingadminCount(Integer missingadminCount) {
    this.missingadminCount = missingadminCount;
    return this;
  }

  /**
   * Total count of returns of item 2.005 (administrative details not available, new member case) populated ‘true’.    
   * minimum: 0
   * @return missingadminCount
  */
  @NotNull @Min(0) 
  @Schema(name = "missingadmin_count", example = "0", description = "Total count of returns of item 2.005 (administrative details not available, new member case) populated ‘true’.    ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("missingadmin_count")
  public Integer getMissingadminCount() {
    return missingadminCount;
  }

  public void setMissingadminCount(Integer missingadminCount) {
    this.missingadminCount = missingadminCount;
  }

  public ViewResponseUnavailableRequestAllOfItems temperrorCount(Integer temperrorCount) {
    this.temperrorCount = temperrorCount;
    return this;
  }

  /**
   * Total view responses item 2.006 (temporary system error) is populated 'true'.   Code to explain to the user that some or all of the pensions information is not  available due to a temporary system error and they should re-try shortly.  
   * minimum: 0
   * @return temperrorCount
  */
  @NotNull @Min(0) 
  @Schema(name = "temperror_count", example = "0", description = "Total view responses item 2.006 (temporary system error) is populated 'true'.   Code to explain to the user that some or all of the pensions information is not  available due to a temporary system error and they should re-try shortly.  ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("temperror_count")
  public Integer getTemperrorCount() {
    return temperrorCount;
  }

  public void setTemperrorCount(Integer temperrorCount) {
    this.temperrorCount = temperrorCount;
  }

  public ViewResponseUnavailableRequestAllOfItems eriUnavailAnoCount(Integer eriUnavailAnoCount) {
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

  public ViewResponseUnavailableRequestAllOfItems eriUnavailPpfCount(Integer eriUnavailPpfCount) {
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

  public ViewResponseUnavailableRequestAllOfItems eriUnavailTrnCount(Integer eriUnavailTrnCount) {
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

  public ViewResponseUnavailableRequestAllOfItems accruedUnavailAnoCount(Integer accruedUnavailAnoCount) {
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

  public ViewResponseUnavailableRequestAllOfItems accruedUnavailPpfCount(Integer accruedUnavailPpfCount) {
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

  public ViewResponseUnavailableRequestAllOfItems accruedUnavailTrnCount(Integer accruedUnavailTrnCount) {
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
    ViewResponseUnavailableRequestAllOfItems viewResponseUnavailableRequestAllOfItems = (ViewResponseUnavailableRequestAllOfItems) o;
    return Objects.equals(this.contactCount, viewResponseUnavailableRequestAllOfItems.contactCount) &&
        Objects.equals(this.missingadminCount, viewResponseUnavailableRequestAllOfItems.missingadminCount) &&
        Objects.equals(this.temperrorCount, viewResponseUnavailableRequestAllOfItems.temperrorCount) &&
        Objects.equals(this.eriUnavailAnoCount, viewResponseUnavailableRequestAllOfItems.eriUnavailAnoCount) &&
        Objects.equals(this.eriUnavailPpfCount, viewResponseUnavailableRequestAllOfItems.eriUnavailPpfCount) &&
        Objects.equals(this.eriUnavailTrnCount, viewResponseUnavailableRequestAllOfItems.eriUnavailTrnCount) &&
        Objects.equals(this.accruedUnavailAnoCount, viewResponseUnavailableRequestAllOfItems.accruedUnavailAnoCount) &&
        Objects.equals(this.accruedUnavailPpfCount, viewResponseUnavailableRequestAllOfItems.accruedUnavailPpfCount) &&
        Objects.equals(this.accruedUnavailTrnCount, viewResponseUnavailableRequestAllOfItems.accruedUnavailTrnCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactCount, missingadminCount, temperrorCount, eriUnavailAnoCount, eriUnavailPpfCount, eriUnavailTrnCount, accruedUnavailAnoCount, accruedUnavailPpfCount, accruedUnavailTrnCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ViewResponseUnavailableRequestAllOfItems {\n");
    sb.append("    contactCount: ").append(toIndentedString(contactCount)).append("\n");
    sb.append("    missingadminCount: ").append(toIndentedString(missingadminCount)).append("\n");
    sb.append("    temperrorCount: ").append(toIndentedString(temperrorCount)).append("\n");
    sb.append("    eriUnavailAnoCount: ").append(toIndentedString(eriUnavailAnoCount)).append("\n");
    sb.append("    eriUnavailPpfCount: ").append(toIndentedString(eriUnavailPpfCount)).append("\n");
    sb.append("    eriUnavailTrnCount: ").append(toIndentedString(eriUnavailTrnCount)).append("\n");
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


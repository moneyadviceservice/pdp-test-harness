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
 * ViewResponseUnavailable
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-13T15:34:29.284844400Z[Europe/London]", comments = "Generator version: 7.5.0")
public class ViewResponseUnavailable {

  private Integer contactCount;

  private Integer missingadminCount;

  private Integer temperrorCount;

  public ViewResponseUnavailable() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ViewResponseUnavailable(Integer contactCount, Integer missingadminCount, Integer temperrorCount) {
    this.contactCount = contactCount;
    this.missingadminCount = missingadminCount;
    this.temperrorCount = temperrorCount;
  }

  public ViewResponseUnavailable contactCount(Integer contactCount) {
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

  public ViewResponseUnavailable missingadminCount(Integer missingadminCount) {
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

  public ViewResponseUnavailable temperrorCount(Integer temperrorCount) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ViewResponseUnavailable viewResponseUnavailable = (ViewResponseUnavailable) o;
    return Objects.equals(this.contactCount, viewResponseUnavailable.contactCount) &&
        Objects.equals(this.missingadminCount, viewResponseUnavailable.missingadminCount) &&
        Objects.equals(this.temperrorCount, viewResponseUnavailable.temperrorCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactCount, missingadminCount, temperrorCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ViewResponseUnavailable {\n");
    sb.append("    contactCount: ").append(toIndentedString(contactCount)).append("\n");
    sb.append("    missingadminCount: ").append(toIndentedString(missingadminCount)).append("\n");
    sb.append("    temperrorCount: ").append(toIndentedString(temperrorCount)).append("\n");
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


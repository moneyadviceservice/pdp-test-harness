package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import uk.org.ca.stub.simulator.utils.MatchStatusEnum;

import java.util.Objects;

/**
 * RreguriResourceIdBody
 */

@JsonTypeName("rreguri_resource_id_body")
public class RreguriResourceIdBody {


  private MatchStatusEnum matchStatus;

  private String inboundRequestId;

  public RreguriResourceIdBody() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RreguriResourceIdBody(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
  }

  public RreguriResourceIdBody matchStatus(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
    return this;
  }

  /**
   * The new match status.
   * @return matchStatus
  */
  @NotNull
  @Schema(name = "match_status", description = "The new match status.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("match_status")
  public MatchStatusEnum getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(MatchStatusEnum matchStatus) {
    this.matchStatus = matchStatus;
  }

  public RreguriResourceIdBody inboundRequestId(String inboundRequestId) {
    this.inboundRequestId = inboundRequestId;
    return this;
  }

  /**
   * If the updated resulted from a request from the CDA, the X-Request-ID of the cda request that resulted in the updated of the PeI.
   * @return inboundRequestId
  */

  @Schema(name = "inbound_request_id", example = "10a4b3c7-3061-4339-9981-04490d1d2f27", description = "If the updated resulted from a request from the CDA, the X-Request-ID of the cda request that resulted in the updated of the PeI.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inbound_request_id")
  public String getInboundRequestId() {
    return inboundRequestId;
  }

  public void setInboundRequestId(String inboundRequestId) {
    this.inboundRequestId = inboundRequestId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var rreguriResourceIdBody = (RreguriResourceIdBody) o;
    return Objects.equals(this.matchStatus, rreguriResourceIdBody.matchStatus) &&
        Objects.equals(this.inboundRequestId, rreguriResourceIdBody.inboundRequestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchStatus, inboundRequestId);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("class RreguriResourceIdBody {\n");
    sb.append("    matchStatus: ").append(toIndentedString(matchStatus)).append("\n");
    sb.append("    inboundRequestId: ").append(toIndentedString(inboundRequestId)).append("\n");
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


package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

/**
 * InlineResponse201
 */

@JsonTypeName("inline_response_201")
public class InlineResponse201 {

  private String ticket;

  public InlineResponse201() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InlineResponse201(String ticket) {
    this.ticket = ticket;
  }

  public InlineResponse201 ticket(String ticket) {
    this.ticket = ticket;
    return this;
  }

  /**
   * The permissions token for the requested resource_id for the requested scopes.
   * @return ticket
  */
  @NotNull @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+/=]*)")
  @Schema(name = "ticket", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2NjY4ODk3Ny0yNzNlLTQ0NzItOTI5ZS0xODExNTc0Mzc0MzMiLCJhdWQiOiJodHRwczovL2ludC5wZW5zaW9uc2Rhc2hib2FyZHMub3JnLnVrL2FtL29hdXRoMiIsIm5iZiI6MTcwNDM3MzI5OCwiaXNzIjoiaHR0cDovL3BlbnNpb25zZGFzaGJvYXJkLm9yZy9wZnNpc3N1ZXIiLCJuYW1lIjoiYTFlNWNlNDJhOTgxMGUyNTkxMzVmZTA2YWJmMDIzOGUxM2U5MzA0YSIsImV4cCI6MTcwNDM3MzM1MCwiaWF0IjoxNzA0MzczMjk4fQ.U7JTocRa2rejt29Bg418CbZx2YJBrqvqhCe4jNo3FEQ", description = "The permissions token for the requested resource_id for the requested scopes.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticket")
  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse201 inlineResponse201 = (InlineResponse201) o;
    return Objects.equals(this.ticket, inlineResponse201.ticket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticket);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse201 {\n");
    sb.append("    ticket: ").append(toIndentedString(ticket)).append("\n");
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


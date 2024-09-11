package uk.org.ca.stub.simulator.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 * RetrieveUmaToken400Response
 */

@JsonTypeName("retrieve_uma_token_400_response")
public class RetrieveUmaToken400Response {

  /**
   * Error codes as defined in https://datatracker.ietf.org/doc/html/rfc6749#section-5.2  invalid_request – the request is missing a parameter so the server can’t proceed with the request, this may also be returned if the request includes an unsupported parameter or repeats a parameter   invalid_grant – the authorization code (or user’s password for the password grant type) is invalid or expired, this is also the error returned when the redirect URL given  invalid_scope – for access token requests that include a scope (password or client_credentials grants), this error indicates an invalid scope value in the request   unauthorised_client – this client is not authorized to use the requested grant type  unsupported_grant_type – the grant_type value is not recognised by the authorisation server
   */
  public enum ErrorEnum {
    INVALID_REQUEST("invalid_request"),
    
    INVALID_GRANT("invalid_grant"),
    
    INVALID_SCOPE("invalid_scope"),
    
    UNAUTHORISED_CLIENT("unauthorised_client"),
    
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type");

    private String value;

    ErrorEnum(String value) {
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
    public static ErrorEnum fromValue(String value) {
      for (ErrorEnum b : ErrorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ErrorEnum error;

  public RetrieveUmaToken400Response error(ErrorEnum error) {
    this.error = error;
    return this;
  }

  /**
   * Error codes as defined in https://datatracker.ietf.org/doc/html/rfc6749#section-5.2  invalid_request – the request is missing a parameter so the server can’t proceed with the request, this may also be returned if the request includes an unsupported parameter or repeats a parameter   invalid_grant – the authorization code (or user’s password for the password grant type) is invalid or expired, this is also the error returned when the redirect URL given  invalid_scope – for access token requests that include a scope (password or client_credentials grants), this error indicates an invalid scope value in the request   unauthorised_client – this client is not authorized to use the requested grant type  unsupported_grant_type – the grant_type value is not recognised by the authorisation server
   * @return error
  */
  
  @Schema(name = "error", description = "Error codes as defined in https://datatracker.ietf.org/doc/html/rfc6749#section-5.2  invalid_request – the request is missing a parameter so the server can’t proceed with the request, this may also be returned if the request includes an unsupported parameter or repeats a parameter   invalid_grant – the authorization code (or user’s password for the password grant type) is invalid or expired, this is also the error returned when the redirect URL given  invalid_scope – for access token requests that include a scope (password or client_credentials grants), this error indicates an invalid scope value in the request   unauthorised_client – this client is not authorized to use the requested grant type  unsupported_grant_type – the grant_type value is not recognised by the authorisation server", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public ErrorEnum getError() {
    return error;
  }

  public void setError(ErrorEnum error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var retrieveUmaToken400Response = (RetrieveUmaToken400Response) o;
    return Objects.equals(this.error, retrieveUmaToken400Response.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("class RetrieveUmaToken400Response {\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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


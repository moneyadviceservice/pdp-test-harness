package uk.org.ca.stub.simulator.rest.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("cas_validation_error")
public record CasValidationError(
        @JsonProperty("field") String field,
        @JsonProperty("rejected_value") Object rejectedValue,
        @JsonProperty("reason") String reason) {
}


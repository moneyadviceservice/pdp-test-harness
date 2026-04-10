package uk.org.ca.stub.simulator.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Jackson mixin for ProblemDetails that suppresses the as_uri field
 * from all serialized error responses.
 */
public abstract class ProblemDetailsMixin {

    @JsonIgnore
    public abstract java.net.URI getAsUri();
}

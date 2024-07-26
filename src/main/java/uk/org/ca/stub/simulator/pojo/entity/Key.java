package uk.org.ca.stub.simulator.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Key{
    @JsonProperty("kty")
    String kty;

    @JsonProperty("e")
    String e;

    @JsonProperty("kid")
    String kid;

    @JsonProperty("n")
    String n;
}

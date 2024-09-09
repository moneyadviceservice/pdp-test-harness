package uk.org.ca.stub.simulator.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MatchStatusEnum {
    YES("match-yes"),
    NO("match-no"),
    POSSIBLE("match-possible"),
    TIMEOUT("match-timeout"),
    WITHDRAWN("match-withdrawn"),
    REMOVED("asset-removed");

    private final String value;

    MatchStatusEnum(String value) {
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
    public static MatchStatusEnum fromValue(String value) {
        for (MatchStatusEnum b : MatchStatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

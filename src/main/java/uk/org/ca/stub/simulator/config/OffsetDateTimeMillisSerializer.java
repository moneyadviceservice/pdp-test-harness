package uk.org.ca.stub.simulator.config;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serializes OffsetDateTime values with exactly 3 millisecond digits, e.g. 2026-02-26T15:13:50.450Z.
 * Used via @JsonSerialize on the specific timestamp fields that must match the ISO_8601_REGEX pattern.
 */
public class OffsetDateTimeMillisSerializer extends StdSerializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public OffsetDateTimeMillisSerializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(FORMATTER.format(value));
    }
}

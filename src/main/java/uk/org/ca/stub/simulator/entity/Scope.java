package uk.org.ca.stub.simulator.entity;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonSerialize(using = Scope.ScopeSerializer.class)
@JsonDeserialize(using = Scope.ScopeDeserializer.class)
public enum Scope {
    OWNER,
    VALUE,
    DELEGATE;

    public static final class ScopeSerializer extends JsonSerializer<Scope> {
        @Override
        public void serialize(final Scope enumValue, final JsonGenerator gen, final SerializerProvider serializer) throws IOException {
            gen.writeString(enumValue.toString().toLowerCase());
        }
    }

    public static final class ScopeDeserializer extends JsonDeserializer<Scope> {
        @Override
        public Scope deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final String jsonValue = parser.getText().toUpperCase();
            return Scope.valueOf(jsonValue);
        }
    }

}

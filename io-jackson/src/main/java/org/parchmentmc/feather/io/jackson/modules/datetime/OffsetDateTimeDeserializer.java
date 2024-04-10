package org.parchmentmc.feather.io.jackson.modules.datetime;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeDeserializer extends FromStringDeserializer<OffsetDateTime> {
    
    private final DateTimeFormatter formatter;

    public OffsetDateTimeDeserializer(DateTimeFormatter formatter) {
        super(OffsetDateTime.class);
        this.formatter = formatter;
    }

    @Override
    protected OffsetDateTime _deserialize(String value, DeserializationContext ctxt) {
        return OffsetDateTime.parse(value, formatter);
    }
}

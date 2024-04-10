package org.parchmentmc.feather.io.jackson.modules.datetime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeSerializer extends StdScalarSerializer<OffsetDateTime> {
    
    private final DateTimeFormatter formatter;

    public OffsetDateTimeSerializer(DateTimeFormatter formatter) {
        super(OffsetDateTime.class);
        this.formatter = formatter;
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(formatter.format(value));
    }
}

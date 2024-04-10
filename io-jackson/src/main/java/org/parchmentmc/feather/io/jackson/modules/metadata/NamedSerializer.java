package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Map;

public class NamedSerializer extends StdSerializer<Named> {

    public NamedSerializer() {
        super(Named.class);
    }

    @Override
    public void serialize(Named value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<String, String> entry : value.getNames().entrySet()) {
            final String schema = entry.getKey();
            final String name = entry.getValue();
            gen.writePOJOField(schema, name);
        }
        gen.writeEndObject();
    }
}

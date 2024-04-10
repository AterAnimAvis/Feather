package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.RecordMetadata;

import java.io.IOException;

public class RecordMetadataSerializer extends StdSerializer<RecordMetadata> {
    
    public RecordMetadataSerializer() {
        super(RecordMetadata.class);
    }

    @Override
    public void serialize(RecordMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("owner", value.getOwner());
        gen.writePOJOField("field", value.getField());
        gen.writePOJOField("getter", value.getGetter());
        gen.writeEndObject();
    }
}

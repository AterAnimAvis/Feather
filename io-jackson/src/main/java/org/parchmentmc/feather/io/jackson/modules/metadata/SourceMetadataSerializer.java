package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.SourceMetadata;

import java.io.IOException;

public class SourceMetadataSerializer extends StdSerializer<SourceMetadata> {

    public SourceMetadataSerializer() {
        super(SourceMetadata.class);
    }

    @Override
    public void serialize(SourceMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("specVersion", value.getSpecificationVersion());
        gen.writePOJOField("minecraftVersion", value.getMinecraftVersion());
        gen.writePOJOField("classes", value.getClasses());
        gen.writeEndObject();
    }
}

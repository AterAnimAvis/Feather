package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.BouncingTargetMetadata;

import java.io.IOException;

public class BouncingTargetMetadataSerializer extends StdSerializer<BouncingTargetMetadata> {

    public BouncingTargetMetadataSerializer() {
        super(BouncingTargetMetadata.class);
    }

    @Override
    public void serialize(BouncingTargetMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value.getTarget().isPresent()) gen.writePOJOField("target", value.getTarget().get());
        if (value.getOwner().isPresent()) gen.writePOJOField("owner", value.getOwner().get());
        gen.writeEndObject();
    }
}

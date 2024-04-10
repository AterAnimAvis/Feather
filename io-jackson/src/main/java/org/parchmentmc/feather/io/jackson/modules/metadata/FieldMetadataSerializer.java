package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.FieldMetadata;

import java.io.IOException;

public class FieldMetadataSerializer extends StdSerializer<FieldMetadata> {
    
    public FieldMetadataSerializer() {
        super(FieldMetadata.class);
    }

    @Override
    public void serialize(FieldMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        gen.writePOJOField("owner", value.getOwner());
        gen.writePOJOField("security", value.getSecuritySpecification());
        gen.writePOJOField("descriptor", value.getDescriptor());
        gen.writePOJOField("signature", value.getSignature());
        gen.writeEndObject();
    }
    
}

package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.Reference;

import java.io.IOException;

public class ReferenceSerializer extends StdSerializer<Reference> {
    
    public ReferenceSerializer() {
        super(Reference.class);
    }

    @Override
    public void serialize(Reference value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        gen.writePOJOField("owner", value.getOwner());
        gen.writePOJOField("descriptor", value.getDescriptor());
        gen.writePOJOField("signature", value.getSignature());
        gen.writeEndObject();
    }
}

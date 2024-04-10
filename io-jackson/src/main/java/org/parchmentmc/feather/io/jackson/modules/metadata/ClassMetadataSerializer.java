package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.ClassMetadata;

import java.io.IOException;

public class ClassMetadataSerializer extends StdSerializer<ClassMetadata> {
    
    public ClassMetadataSerializer() {
        super(ClassMetadata.class);
    }

    @Override
    public void serialize(ClassMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        gen.writePOJOField("owner", value.getOwner());
        gen.writePOJOField("security", value.getSecuritySpecification());
        gen.writePOJOField("extends", value.getSuperName());
        gen.writePOJOField("implements", value.getInterfaces());
        gen.writePOJOField("fields", value.getFields());
        gen.writePOJOField("methods", value.getMethods());
        gen.writePOJOField("records", value.getRecords());
        gen.writePOJOField("inner", value.getInnerClasses());
        gen.writePOJOField("signature", value.getSignature());
        gen.writePOJOField("record", value.isRecord());
        gen.writeEndObject();
    }
    
}

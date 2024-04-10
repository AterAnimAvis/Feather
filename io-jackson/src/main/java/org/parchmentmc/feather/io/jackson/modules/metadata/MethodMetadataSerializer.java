package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.metadata.MethodMetadata;

import java.io.IOException;

public class MethodMetadataSerializer extends StdSerializer<MethodMetadata> {
    
    public MethodMetadataSerializer() {
        super(MethodMetadata.class);
    }

    @Override
    public void serialize(MethodMetadata value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        gen.writePOJOField("owner", value.getOwner());
        gen.writePOJOField("security", value.getSecuritySpecification());
        gen.writePOJOField("descriptor", value.getDescriptor());
        gen.writePOJOField("signature", value.getSignature());
        gen.writePOJOField("lambda", value.isLambda());

        if (value.getBouncingTarget().isPresent()) gen.writePOJOField("bouncingTarget", value.getBouncingTarget().get());
        if (value.getParent().isPresent()) gen.writePOJOField("parent", value.getParent().get());
        if (!value.getOverrides().isEmpty()) gen.writePOJOField("overrides", value.getOverrides());
        if (value.getStartLine().isPresent()) gen.writePOJOField("startLine", value.getStartLine().orElse(0));
        if (value.getEndLine().isPresent()) gen.writePOJOField("endLine", value.getEndLine().orElse(0));

        gen.writeEndObject();
    }
    
}

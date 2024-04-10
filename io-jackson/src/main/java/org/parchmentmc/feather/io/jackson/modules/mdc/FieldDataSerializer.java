package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Collection;

public class FieldDataSerializer extends StdSerializer<MappingDataContainer.FieldData> {

    private final boolean ignoreNonDocumented;
    
    public FieldDataSerializer(boolean ignoreNonDocumented) {
        super(MappingDataContainer.FieldData.class);
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public boolean shouldIgnoreFields(Collection<? extends MappingDataContainer.FieldData> values) {
        return ignoreNonDocumented && values.stream().allMatch(this::shouldIgnoreField);
    }
    
    public boolean shouldIgnoreField(MappingDataContainer.FieldData value) {
        return ignoreNonDocumented && value.getJavadoc().isEmpty();
    }

    @Override
    public void serialize(MappingDataContainer.FieldData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (shouldIgnoreField(value)) {
            gen.writeNull();
            return;
        }
        
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        gen.writePOJOField("descriptor", value.getDescriptor());
        if (!value.getJavadoc().isEmpty()) gen.writePOJOField("javadoc", value.getJavadoc());
        gen.writeEndObject();
    }

    public boolean isIgnoreNonDocumented() {
        return ignoreNonDocumented;
    }
}

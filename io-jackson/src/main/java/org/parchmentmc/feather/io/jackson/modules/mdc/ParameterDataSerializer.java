package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Collection;

public class ParameterDataSerializer extends StdSerializer<MappingDataContainer.ParameterData> {
    
    private final boolean ignoreNonDocumented;
    
    public ParameterDataSerializer(boolean ignoreNonDocumented) {
        super(MappingDataContainer.ParameterData.class);
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public boolean shouldIgnoreParameters(Collection<? extends MappingDataContainer.ParameterData> values) {
        return ignoreNonDocumented && values.stream().allMatch(this::shouldIgnoreParameter);
    }

    public boolean shouldIgnoreParameter(MappingDataContainer.ParameterData value) {
        return ignoreNonDocumented && value.getName() == null && value.getJavadoc() == null;
    }
    
    @Override
    public void serialize(MappingDataContainer.ParameterData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (shouldIgnoreParameter(value)) {
            gen.writeNull();
            return;
        }
        
        gen.writeStartObject();
        gen.writePOJOField("index", value.getIndex());
        if (value.getName() != null) gen.writePOJOField("name", value.getName());
        if (value.getJavadoc() != null) gen.writePOJOField("javadoc", value.getJavadoc());
        gen.writeEndObject();        
    }

    public boolean isIgnoreNonDocumented() {
        return ignoreNonDocumented;
    }
}

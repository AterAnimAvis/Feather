package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Collection;

public class MethodDataSerializer extends StdSerializer<MappingDataContainer.MethodData> {

    private final boolean ignoreNonDocumented;
    
    public MethodDataSerializer(boolean ignoreNonDocumented) {
        super(MappingDataContainer.MethodData.class);
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public boolean shouldIgnoreMethods(ParameterDataSerializer parameterSerializer, Collection<? extends MappingDataContainer.MethodData> values) {
        return ignoreNonDocumented && values.stream().allMatch(methodData -> this.shouldIgnoreMethod(parameterSerializer, methodData));
    }

    public boolean shouldIgnoreMethod(ParameterDataSerializer parameterSerializer, MappingDataContainer.MethodData value) {
        return ignoreNonDocumented && value.getJavadoc().isEmpty() && parameterSerializer.shouldIgnoreParameters(value.getParameters());
    }

    @Override
    public void serialize(MappingDataContainer.MethodData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        JsonSerializer<?> parameterSerializer = getJsonSerializer(gen, provider);

        if (shouldIgnoreMethod((ParameterDataSerializer) parameterSerializer, value)) {
            gen.writeNull();
            return;
        }
        
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        gen.writePOJOField("descriptor", value.getDescriptor());
        if (!value.getJavadoc().isEmpty()) gen.writePOJOField("javadoc", value.getJavadoc());
        if (!value.getParameters().isEmpty()) gen.writePOJOField("parameters", value.getParameters());
        gen.writeEndObject();
    }

    private static JsonSerializer<?> getJsonSerializer(JsonGenerator gen, SerializerProvider provider) throws JsonMappingException, JsonGenerationException {
        JsonSerializer<?> parameterSerializer = provider.findValueSerializer(MappingDataContainer.ParameterData.class);
        final boolean isParameterSerializer = parameterSerializer instanceof ParameterDataSerializer;
        if (!isParameterSerializer) throw new JsonGenerationException("Could not get instance of ParameterDataSerializer", gen);
        return parameterSerializer;
    }

    public boolean isIgnoreNonDocumented() {
        return ignoreNonDocumented;
    }
}

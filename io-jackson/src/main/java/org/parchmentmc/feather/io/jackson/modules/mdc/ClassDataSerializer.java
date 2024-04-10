package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Collection;

public class ClassDataSerializer extends StdSerializer<MappingDataContainer.ClassData> {
    
    private final boolean ignoreNonDocumented;
    
    public ClassDataSerializer(boolean ignoreNonDocumented) {
        super(MappingDataContainer.ClassData.class);
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public boolean shouldIgnoreClasses(
            FieldDataSerializer fieldDataSerializer,
            MethodDataSerializer methodDataSerializer,
            ParameterDataSerializer parameterDataSerializer,
            Collection<? extends MappingDataContainer.ClassData> values
    ) {
        return ignoreNonDocumented && values.stream().allMatch(classData -> this.shouldIgnoreClass(
                fieldDataSerializer,
                methodDataSerializer,
                parameterDataSerializer,
                classData
        ));
    }

    public boolean shouldIgnoreClass(
            FieldDataSerializer fieldDataSerializer,
            MethodDataSerializer methodDataSerializer,
            ParameterDataSerializer parameterDataSerializer,
            MappingDataContainer.ClassData value
    ) {
        if (!ignoreNonDocumented) return false;
        if (!value.getJavadoc().isEmpty()) return false;
        
        boolean shouldIgnoreFields  = fieldDataSerializer.shouldIgnoreFields(value.getFields());
        if (!shouldIgnoreFields) return false;
        
        boolean shouldIgnoreMethods = methodDataSerializer.shouldIgnoreMethods(parameterDataSerializer, value.getMethods());
        return shouldIgnoreMethods;
    }
    
    @Override
    public void serialize(MappingDataContainer.ClassData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        final FieldDataSerializer     fieldDataSerializer   = Jackson.getSerializer(gen, provider, MappingDataContainer.FieldData.class, FieldDataSerializer.class);
        final MethodDataSerializer    methodDataSerializer  = Jackson.getSerializer(gen, provider, MappingDataContainer.MethodData.class, MethodDataSerializer.class);
        final ParameterDataSerializer packageDataSerializer = Jackson.getSerializer(gen, provider, MappingDataContainer.ParameterData.class, ParameterDataSerializer.class);

        if (shouldIgnoreClass(fieldDataSerializer, methodDataSerializer, packageDataSerializer, value)) {
            gen.writeNull();
            return;
        }
        
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        if (!value.getJavadoc().isEmpty()) gen.writePOJOField("javadoc", value.getJavadoc());
        if (!value.getFields().isEmpty()) gen.writePOJOField("fields", value.getFields());
        if (!value.getMethods().isEmpty()) gen.writePOJOField("methods", value.getMethods());
        gen.writeEndObject();
    }

    public boolean isIgnoreNonDocumented() {
        return ignoreNonDocumented;
    }
}

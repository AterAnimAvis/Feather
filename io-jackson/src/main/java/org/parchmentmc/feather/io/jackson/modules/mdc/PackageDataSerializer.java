package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;

public class PackageDataSerializer extends StdSerializer<MappingDataContainer.PackageData> {
    
    public PackageDataSerializer() {
        super(MappingDataContainer.PackageData.class);
    }

    @Override
    public void serialize(MappingDataContainer.PackageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("name", value.getName());
        if (!value.getJavadoc().isEmpty()) gen.writePOJOField("javadoc", value.getJavadoc());
        gen.writeEndObject();
    }
}

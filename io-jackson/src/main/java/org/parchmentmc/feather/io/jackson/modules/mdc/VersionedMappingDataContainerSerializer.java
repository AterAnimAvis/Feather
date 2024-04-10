package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;

import java.io.IOException;

public class VersionedMappingDataContainerSerializer extends StdSerializer<VersionedMappingDataContainer> {
    
    public VersionedMappingDataContainerSerializer() {
        super(VersionedMappingDataContainer.class);
    }

    @Override
    public void serialize(VersionedMappingDataContainer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writePOJOField("version", value.getFormatVersion());
        gen.writePOJOField("packages", value.getPackages());
        gen.writePOJOField("classes", value.getClasses());
        gen.writeEndObject();
    }
}

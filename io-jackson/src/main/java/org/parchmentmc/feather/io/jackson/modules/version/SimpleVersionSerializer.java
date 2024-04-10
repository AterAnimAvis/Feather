package org.parchmentmc.feather.io.jackson.modules.version;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;

public class SimpleVersionSerializer extends StdScalarSerializer<SimpleVersion> {

    public SimpleVersionSerializer() {
        super(SimpleVersion.class);
    }

    @Override
    public void serialize(SimpleVersion value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toString());
    }
}

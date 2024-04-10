package org.parchmentmc.feather.io.jackson.modules.version;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.parchmentmc.feather.util.SimpleVersion;

public class SimpleVersionDeserializer extends FromStringDeserializer<SimpleVersion> {

    public SimpleVersionDeserializer() {
        super(SimpleVersion.class);
    }

    @Override
    protected SimpleVersion _deserialize(String value, DeserializationContext ctxt) {
        return SimpleVersion.of(value);
    }
}

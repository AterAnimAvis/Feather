package org.parchmentmc.feather.io.jackson.modules;

import org.parchmentmc.feather.io.jackson.FeatherModule;
import org.parchmentmc.feather.io.jackson.modules.version.SimpleVersionDeserializer;
import org.parchmentmc.feather.io.jackson.modules.version.SimpleVersionSerializer;
import org.parchmentmc.feather.io.jackson.util.BaseModule;
import org.parchmentmc.feather.util.SimpleVersion;

public class SimpleVersionModule extends BaseModule {

    public SimpleVersionModule() {
        super("Feather$SimpleVersion", FeatherModule.VERSION);
        addSerializer(SimpleVersion.class, new SimpleVersionSerializer());
        addDeserializer(SimpleVersion.class, new SimpleVersionDeserializer());
    }

}

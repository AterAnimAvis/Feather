package org.parchmentmc.feather.io.jackson.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplified {@link SimpleModule} equivalent, with support for dependencies
 */
public class BaseModule extends Module {

    private final String name;
    private final Version version;
    private final List<Module> dependencies = new ArrayList<>();
    private final SimpleSerializers serializers = new SimpleSerializers();
    private final SimpleDeserializers deserializers = new SimpleDeserializers();
    
    public BaseModule(String name, Version version) {
        this.name    = name;
        this.version = version;
    }
    
    @Override
    public String getModuleName() {
        return name;
    }

    @Override
    public Version version() {
        return version;
    }

    protected <T> BaseModule addSerializer(Class<? extends T> type, JsonSerializer<T> serializer) {
        this.serializers.addSerializer(type, serializer);
        return this;
    }

    protected <T> BaseModule addDeserializer(Class<T> type, JsonDeserializer<? extends T> deserializer) {
        this.deserializers.addDeserializer(type, deserializer);
        return this;
    }

    protected BaseModule addDependency(Module module) {
        this.dependencies.add(module);
        return this;
    }

    @Override
    public Iterable<? extends Module> getDependencies() {
        return Collections.unmodifiableCollection(dependencies);
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }

}

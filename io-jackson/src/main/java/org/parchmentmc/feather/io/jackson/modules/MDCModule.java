package org.parchmentmc.feather.io.jackson.modules;

import org.parchmentmc.feather.io.jackson.FeatherModule;
import org.parchmentmc.feather.io.jackson.modules.mdc.*;
import org.parchmentmc.feather.io.jackson.util.BaseModule;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;

public class MDCModule extends BaseModule {

    private final boolean ignoreNonDocumented;

    public MDCModule() {
        this(false);
    }
    
    /**
     * @param ignoreNonDocumented whether this should ignore mapping data entries which have no javadocs.
     */
    public MDCModule(boolean ignoreNonDocumented) {
        super("Feather$MDC", FeatherModule.VERSION);
        addDependency(new SimpleVersionModule());

        addSerializer(VersionedMappingDataContainer.class, new VersionedMappingDataContainerSerializer());
        addDeserializer(VersionedMappingDataContainer.class, new VersionedMappingDataContainerDeserializer());
        addSerializer(MappingDataContainer.PackageData.class, new PackageDataSerializer());
        addDeserializer(MappingDataContainer.PackageData.class, new PackageDataDeserializer());
        addSerializer(MappingDataContainer.ClassData.class, new ClassDataSerializer(ignoreNonDocumented));
        addDeserializer(MappingDataContainer.ClassData.class, new ClassDataDeserializer());
        addSerializer(MappingDataContainer.FieldData.class, new FieldDataSerializer(ignoreNonDocumented));
        addDeserializer(MappingDataContainer.FieldData.class, new FieldDataDeserializer());
        addSerializer(MappingDataContainer.MethodData.class, new MethodDataSerializer(ignoreNonDocumented));
        addDeserializer(MappingDataContainer.MethodData.class, new MethodDataDeserializer());
        addSerializer(MappingDataContainer.ParameterData.class, new ParameterDataSerializer(ignoreNonDocumented));
        addDeserializer(MappingDataContainer.ParameterData.class, new ParameterDataDeserializer());
        
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public boolean isIgnoreNonDocumented() {
        return ignoreNonDocumented;
    }
    
}

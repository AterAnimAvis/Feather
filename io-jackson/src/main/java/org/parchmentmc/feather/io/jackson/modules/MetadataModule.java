package org.parchmentmc.feather.io.jackson.modules;

import org.parchmentmc.feather.io.jackson.FeatherModule;
import org.parchmentmc.feather.io.jackson.modules.metadata.*;
import org.parchmentmc.feather.io.jackson.util.BaseModule;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;

public class MetadataModule extends BaseModule {

    public MetadataModule() {
        super("Feather$Metadata", FeatherModule.VERSION);
        addDependency(new SimpleVersionModule());

        addSerializer(Named.class, new NamedSerializer());
        addDeserializer(Named.class, new NamedDeserializer());
        addSerializer(SourceMetadata.class, new SourceMetadataSerializer());
        addDeserializer(SourceMetadata.class, new SourceMetadataDeserializer());
        addSerializer(ClassMetadata.class, new ClassMetadataSerializer());
        addDeserializer(ClassMetadata.class, new ClassMetadataDeserializer());
        addSerializer(FieldMetadata.class, new FieldMetadataSerializer());
        addDeserializer(FieldMetadata.class, new FieldMetadataDeserializer());
        addSerializer(MethodMetadata.class, new MethodMetadataSerializer());
        addDeserializer(MethodMetadata.class, new MethodMetadataDeserializer());
        addSerializer(Reference.class, new ReferenceSerializer());
        addDeserializer(Reference.class, new ReferenceDeserializer());
        addSerializer(BouncingTargetMetadata.class, new BouncingTargetMetadataSerializer());
        addDeserializer(BouncingTargetMetadata.class, new BouncingTargetMetadataDeserializer());
        addSerializer(RecordMetadata.class, new RecordMetadataSerializer());
        addDeserializer(RecordMetadata.class, new RecordMetadataDeserializer());
    }
}

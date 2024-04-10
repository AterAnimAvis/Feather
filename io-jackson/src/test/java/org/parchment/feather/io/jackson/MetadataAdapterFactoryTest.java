package org.parchment.feather.io.jackson;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.jackson.JacksonTest;
import org.parchmentmc.feather.io.jackson.modules.MetadataModule;
import org.parchmentmc.feather.metadata.*;

public class MetadataAdapterFactoryTest extends JacksonTest implements MetadataTestConstants {
    public MetadataAdapterFactoryTest() {
        super(b -> b.registerModule(new MetadataModule()));
    }

    @Test
    public void testMethodReferences() {
        METHOD_REFERENCES.forEach(data -> test(Reference.class, data));
    }

    @Test
    public void testMethodMetadata() {
        METHOD_METADATA.forEach(data -> test(MethodMetadata.class, data));
    }

    @Test
    public void testFieldMetadata() {
        FIELD_METADATA.forEach(data -> test(FieldMetadata.class, data));
    }

    @Test
    public void testClassMetadata() {
        CLASS_METADATA.forEach(data -> test(ClassMetadata.class, data));
    }

    @Test
    public void testSourceMetadata() {
        SOURCE_METADATA.forEach(data -> test(SourceMetadata.class, data));
    }
}

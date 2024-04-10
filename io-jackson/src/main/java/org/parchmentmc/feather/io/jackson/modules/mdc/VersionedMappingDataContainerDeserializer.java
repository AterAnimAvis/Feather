package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.CommonTypes;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.mapping.ImmutableVersionedMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class VersionedMappingDataContainerDeserializer extends StdDeserializer<VersionedMappingDataContainer> {

    public VersionedMappingDataContainerDeserializer() {
        super(VersionedMappingDataContainer.class);
    }

    @Override
    public VersionedMappingDataContainer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;
        
        SimpleVersion version = null;
        Collection<? extends MappingDataContainer.PackageData> packages = null;
        Collection<? extends MappingDataContainer.ClassData> classes = null;

        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "version":
                    version = ctxt.readTreeAsValue(node.get(propertyName), SimpleVersion.class);
                    if (version != null && !version.isCompatibleWith(VersionedMappingDataContainer.CURRENT_FORMAT))
                        throw new JsonParseException("Version " + version + " is incompatible with current version "
                                + VersionedMappingDataContainer.CURRENT_FORMAT);
                    break;
                case "packages":
                    packages = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.COLLECTION_PACKAGE_DATA);
                    break;
                case "classes":
                    classes = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.COLLECTION_CLASS_DATA);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (packages == null) packages = Collections.emptyList();
        if (classes == null) classes = Collections.emptyList();
        if (version == null) throw new JsonParseException("No version found");
        
        return new ImmutableVersionedMappingDataContainer(version, packages, classes);
    }
}

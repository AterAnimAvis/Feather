package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.CommonTypes;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.ClassMetadata;
import org.parchmentmc.feather.metadata.SourceMetadata;
import org.parchmentmc.feather.metadata.SourceMetadataBuilder;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class SourceMetadataDeserializer extends StdDeserializer<SourceMetadata> {

    public SourceMetadataDeserializer() {
        super(SourceMetadata.class);
    }

    @Override
    public SourceMetadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        SimpleVersion specVersion = null;
        String minecraftVersion = null;
        LinkedHashSet<ClassMetadata> classes = null;

        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "specVersion":
                    specVersion = ctxt.readTreeAsValue(node.get(propertyName), SimpleVersion.class); // TODO: version checking
                    break;
                case "minecraftVersion":
                    minecraftVersion = node.get(propertyName).asText();
                    break;
                case "classes":
                    classes = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_CLASS_METADATA);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (specVersion == null) throw new JsonParseException("Specification version is not present");
        if (minecraftVersion == null) throw new JsonParseException("Minecraft version is not present");
        if (classes == null) throw new JsonParseException("Classes Set is not present");

        return SourceMetadataBuilder.create()
                .withMinecraftVersion(minecraftVersion)
                .withSpecVersion(specVersion)
                .withClasses(classes)
                .build();
    }
}

package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.*;

import java.io.IOException;
import java.util.Iterator;

public class BouncingTargetMetadataDeserializer extends StdDeserializer<BouncingTargetMetadata> {

    public BouncingTargetMetadataDeserializer() {
        super(BouncingTargetMetadata.class);
    }

    @Override
    public BouncingTargetMetadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        final BouncingTargetMetadataBuilder builder = BouncingTargetMetadataBuilder.create();

        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "target":
                    builder.withTarget(ctxt.readTreeAsValue(node.get(propertyName), Reference.class));
                    break;
                case "owner":
                    builder.withOwner(ctxt.readTreeAsValue(node.get(propertyName), Reference.class));
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        return builder.build();
    }
}

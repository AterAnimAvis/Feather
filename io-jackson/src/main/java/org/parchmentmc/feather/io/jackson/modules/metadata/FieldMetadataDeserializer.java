package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.FieldMetadata;
import org.parchmentmc.feather.metadata.FieldMetadataBuilder;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Iterator;

public class FieldMetadataDeserializer extends StdDeserializer<FieldMetadata> {
    
    public FieldMetadataDeserializer() {
        super(FieldMetadata.class);
    }

    @Override
    public FieldMetadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        Named name = Named.empty();
        Named owner = Named.empty();
        int security = -1;
        Named descriptor = Named.empty();
        Named signature = Named.empty();

        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "name":
                    name = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                case "owner":
                    owner = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                case "security":
                    security = node.get(propertyName).asInt();
                    break;
                case "descriptor":
                    descriptor = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                case "signature":
                    signature = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (name.isEmpty()) throw new JsonParseException("Field name is not present or empty");
        if (owner.isEmpty()) throw new JsonParseException("Field owner is not present or empty");
        if (security == -1) throw new JsonParseException("Field security specification is not present");
        if (descriptor.isEmpty()) throw new JsonParseException("Field descriptor is not present or empty");

        return FieldMetadataBuilder.create()
                .withOwner(owner)
                .withName(name)
                .withSecuritySpecification(security)
                .withSignature(signature)
                .withDescriptor(descriptor)
                .build();
    }
    
}

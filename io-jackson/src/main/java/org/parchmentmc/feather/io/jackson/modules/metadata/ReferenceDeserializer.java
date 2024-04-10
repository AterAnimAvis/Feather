package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.Reference;
import org.parchmentmc.feather.metadata.ReferenceBuilder;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Iterator;

public class ReferenceDeserializer extends StdDeserializer<Reference> {

    public ReferenceDeserializer() {
        super(Reference.class);
    }

    @Override
    public Reference deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        Named name = Named.empty();
        Named owner = Named.empty();
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

        if (name.isEmpty()) throw new JsonParseException("Method reference name is not present");
        if (owner.isEmpty()) throw new JsonParseException("Method reference owner is not present");
        if (descriptor.isEmpty()) throw new JsonParseException("Method reference descriptor is not present");

        return ReferenceBuilder.create()
                .withOwner(owner)
                .withName(name)
                .withSignature(signature)
                .withDescriptor(descriptor)
                .build();
    }
}

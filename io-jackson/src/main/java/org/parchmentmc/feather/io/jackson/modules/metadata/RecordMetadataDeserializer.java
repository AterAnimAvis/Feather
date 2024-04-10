package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.RecordMetadata;
import org.parchmentmc.feather.metadata.RecordMetadataBuilder;
import org.parchmentmc.feather.metadata.Reference;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Iterator;

public class RecordMetadataDeserializer extends StdDeserializer<RecordMetadata> {

    public RecordMetadataDeserializer() {
        super(RecordMetadata.class);
    }

    @Override
    public RecordMetadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        Named owner = Named.empty();
        Reference field = null;
        Reference getter = null;

        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "owner":
                    owner = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                case "field":
                    field = ctxt.readTreeAsValue(node.get(propertyName), Reference.class);
                    break;
                case "getter":
                    getter = ctxt.readTreeAsValue(node.get(propertyName), Reference.class);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (owner.isEmpty()) throw new JsonParseException("Field owner is not present or empty");
        if (field == null) throw new JsonParseException("Field field is not present or empty");
        if (getter == null) throw new JsonParseException("Field getter is not present or empty");

        return RecordMetadataBuilder.create()
                .withOwner(owner)
                .withField(field)
                .withGetter(getter)
                .build();
    }
}

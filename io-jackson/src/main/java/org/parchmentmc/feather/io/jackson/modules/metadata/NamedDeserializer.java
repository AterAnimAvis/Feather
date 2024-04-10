package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

import java.io.IOException;
import java.util.Iterator;

public class NamedDeserializer extends StdDeserializer<Named> {

    public NamedDeserializer() {
        super(Named.class);
    }

    @Override
    public Named deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;
        
        final NamedBuilder builder = NamedBuilder.create();
        
        final Iterator<String> schemas = node.fieldNames();
        while (schemas.hasNext()) {
            final String schema = schemas.next();
            final String name   = node.get(schema).asText();
            builder.with(schema, name);
        }
        
        return builder.build();
    }
}

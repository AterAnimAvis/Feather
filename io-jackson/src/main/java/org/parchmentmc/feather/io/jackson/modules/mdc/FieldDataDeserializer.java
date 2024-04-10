package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.CommonTypes;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FieldDataDeserializer extends StdDeserializer<MappingDataContainer.FieldData> {
    
    public FieldDataDeserializer() {
        super(MappingDataContainer.FieldData.class);
    }

    @Override
    public MappingDataContainer.FieldData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;
        
        String name = null;
        String descriptor = null;
        List<String> javadoc = null;
        
        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "name":
                    name = node.get(propertyName).asText();
                case "descriptor":
                    descriptor = node.get(propertyName).asText();
                    break;
                case "javadoc":
                    javadoc = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.LIST_STRING);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (name == null) throw new JsonParseException("Field name must not be null");
        if (descriptor == null) throw new JsonParseException("Field descriptor must not be null");
        if (javadoc == null) javadoc = Collections.emptyList();
        
        return new ImmutableMappingDataContainer.ImmutableFieldData(name, descriptor, javadoc);
    }
}

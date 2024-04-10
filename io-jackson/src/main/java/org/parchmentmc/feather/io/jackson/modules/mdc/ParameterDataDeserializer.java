package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Iterator;

public class ParameterDataDeserializer extends StdDeserializer<MappingDataContainer.ParameterData> {
    
    public ParameterDataDeserializer() {
        super(MappingDataContainer.ParameterData.class);
    }

    @Override
    public MappingDataContainer.ParameterData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        byte index = -1; //TODO: byte seems wrong it's signed iirc JVM supports unsigned 255 params
        String name = null;
        String javadoc = null;

        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "index":
                    index = (byte) node.get(propertyName).asInt();
                    break;
                case "name":
                    name = node.get(propertyName).asText();
                    break;
                case "javadoc":
                    javadoc = node.get(propertyName).asText();
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (index < 0) throw new JsonParseException("Parameter index must be present and positive");
        
        return new ImmutableMappingDataContainer.ImmutableParameterData(index, name, javadoc);
    }
}

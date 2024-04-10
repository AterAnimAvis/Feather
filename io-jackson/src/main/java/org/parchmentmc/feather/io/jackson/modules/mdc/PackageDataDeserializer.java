package org.parchmentmc.feather.io.jackson.modules.mdc;

import com.fasterxml.jackson.core.JacksonException;
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

public class PackageDataDeserializer extends StdDeserializer<MappingDataContainer.PackageData> {
    
    public PackageDataDeserializer() {
        super(MappingDataContainer.PackageData.class);
    }

    @Override
    public MappingDataContainer.PackageData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        String name = null;
        List<String> javadoc = null; 
        
        final Iterator<String> propertyNames = node.fieldNames();
        while (propertyNames.hasNext()) {
            final String propertyName = propertyNames.next();
            switch (propertyName) {
                case "name":
                    name = node.get(propertyName).asText();
                    break;
                case "javadoc":
                    javadoc = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.LIST_STRING);
                    break;
                default:
                    // do nothing
                    break;
            }
        }
        
        if (name == null) throw new JsonParseException("Package name must not be null");
        if (javadoc == null) javadoc = Collections.emptyList();

        return new ImmutableMappingDataContainer.ImmutablePackageData(name, javadoc);
    }
}

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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClassDataDeserializer extends StdDeserializer<MappingDataContainer.ClassData> {
    
    public ClassDataDeserializer() {
        super(MappingDataContainer.ClassData.class);
    }

    @Override
    public MappingDataContainer.ClassData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;
        
        String name = null;
        List<String> javadoc = null;
        Collection<? extends MappingDataContainer.FieldData> fields = null;
        Collection<? extends MappingDataContainer.MethodData> methods = null;

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
                case "fields":
                    fields = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.COLLECTION_FIELD_DATA);
                    break;
                case "methods":
                    methods = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.COLLECTION_METHOD_DATA);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (name == null) throw new JsonParseException("Class name must not be null");
        if (javadoc == null) javadoc = Collections.emptyList();
        if (fields == null) fields = Collections.emptyList();
        if (methods == null) methods = Collections.emptyList();

        return new ImmutableMappingDataContainer.ImmutableClassData(name, javadoc, fields, methods);
    }
}

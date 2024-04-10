package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.CommonTypes;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class ClassMetadataDeserializer extends StdDeserializer<ClassMetadata> {
    
    public ClassMetadataDeserializer() {
        super(ClassMetadata.class);
    }

    @Override
    public ClassMetadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        Named name = Named.empty();
        Named owner = Named.empty();
        int security = -1;
        Named superName = Named.empty();
        LinkedHashSet<Named> interfaces = null;
        LinkedHashSet<FieldMetadata> fields = null;
        LinkedHashSet<MethodMetadata> methods = null;
        LinkedHashSet<RecordMetadata> records = null;
        LinkedHashSet<ClassMetadata> innerClasses = null;
        Named signature = Named.empty();
        boolean isRecord = false;

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
                case "extends":
                    superName = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                case "implements":
                    interfaces = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_NAMED);
                    break;
                case "fields":
                    fields = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_FIELD_METADATA);
                    break;
                case "records":
                    records = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_RECORD_METADATA);
                    break;
                case "methods":
                    methods = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_METHOD_METADATA);
                    break;
                case "inner":
                    innerClasses = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_CLASS_METADATA);
                    break;
                case "signature":
                    signature = ctxt.readTreeAsValue(node.get(propertyName), Named.class);
                    break;
                case "record":
                    isRecord = node.get(propertyName).asBoolean();
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (name.isEmpty()) throw new JsonParseException("Class metadata name is not present or empty");
        // owner can be empty
        if (security == -1) throw new JsonParseException("Class metadata security specification is not present");
        // superName can be empty
        if (interfaces == null) methods = new LinkedHashSet<>();
        if (fields == null) fields = new LinkedHashSet<>();
        if (records == null) records = new LinkedHashSet<>();
        if (methods == null) methods = new LinkedHashSet<>();
        if (innerClasses == null) innerClasses = new LinkedHashSet<>();
        if (!records.isEmpty()) isRecord = true;

        return ClassMetadataBuilder.create()
                .withSuperName(superName)
                .withInterfaces(interfaces)
                .withOwner(owner)
                .withMethods(methods)
                .withFields(fields)
                .withInnerClasses(innerClasses)
                .withName(name)
                .withSecuritySpecifications(security)
                .withSignature(signature)
                .withRecords(records)
                .withIsRecord(isRecord)
                .build();
    }
    
}

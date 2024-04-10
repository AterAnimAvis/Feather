package org.parchmentmc.feather.io.jackson.modules.metadata;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.parchmentmc.feather.io.jackson.util.CommonTypes;
import org.parchmentmc.feather.io.jackson.util.Jackson;
import org.parchmentmc.feather.metadata.BouncingTargetMetadata;
import org.parchmentmc.feather.metadata.MethodMetadata;
import org.parchmentmc.feather.metadata.MethodMetadataBuilder;
import org.parchmentmc.feather.metadata.Reference;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MethodMetadataDeserializer extends StdDeserializer<MethodMetadata> {

    public MethodMetadataDeserializer() {
        super(MethodMetadata.class);
    }
    
    @Override
    public MethodMetadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = Jackson.getObjectNode(p);
        if (node == null) return null;

        Named name = Named.empty();
        Named owner = Named.empty();
        int security = -1;
        Named descriptor = Named.empty();
        Named signature = Named.empty();
        boolean lambda = false;
        BouncingTargetMetadata bouncingTarget = null;
        LinkedHashSet<Reference> overrides = null;
        int startLine = 0;
        int endLine = 0;
        Reference parent = null;

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
                case "lambda":
                    lambda = node.get(propertyName).asBoolean();
                    break;
                case "bouncingTarget":
                    bouncingTarget = ctxt.readTreeAsValue(node.get(propertyName), BouncingTargetMetadata.class);
                    break;
                case "parent":
                    parent = ctxt.readTreeAsValue(node.get(propertyName), Reference.class);
                    break;
                case "overrides":
                    overrides = ctxt.readTreeAsValue(node.get(propertyName), CommonTypes.SET_REFERENCE);
                    break;
                case "startLine":
                    startLine = node.get(propertyName).asInt();
                    break;
                case "endLine":
                    endLine = node.get(propertyName).asInt();
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        if (name.isEmpty()) throw new JsonParseException("Method metadata name is not present or empty");
        if (owner.isEmpty()) throw new JsonParseException("Method metadata owner is not present or empty");
        if (descriptor.isEmpty()) throw new JsonParseException("Method metadata descriptor is not present or empty");
        if (security == -1) throw new JsonParseException("Method metadata security specification is not present");
        // lambda is a primitive
        // bouncingTarget can be null
        if (overrides == null) overrides = new LinkedHashSet<>();
        if (startLine < 0) throw new JsonParseException("Method metadata contains negative start line");
        if (endLine < 0) throw new JsonParseException("Method metadata contains negative end line");
        if (endLine < startLine) throw new JsonParseException("Method metadata contains end before start");

        return MethodMetadataBuilder.create()
                .withBouncingTarget(bouncingTarget)
                .withName(name)
                .withOwner(owner)
                .withDescriptor(descriptor)
                .withSignature(signature)
                .withSecuritySpecification(security)
                .withParent(parent)
                .withOverrides(overrides)
                .withStartLine(startLine)
                .withEndLine(endLine)
                .withLambda(lambda)
                .build();
    }
    
}

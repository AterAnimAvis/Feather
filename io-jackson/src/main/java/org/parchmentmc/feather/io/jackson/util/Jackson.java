package org.parchmentmc.feather.io.jackson.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

public class Jackson {
    
    private Jackson() {
        throw new IllegalStateException("Can not instantiate an instance of: Jackson. This is a utility class");
    }

    public static <T, P extends JsonSerializer<T>> @NonNull P getSerializer(JsonGenerator gen, SerializerProvider provider, Class<T> typeClazz, Class<P> serializerClass) throws JsonMappingException, JsonGenerationException {
        JsonSerializer<?> rawSerializer = provider.findValueSerializer(typeClazz);
        if (!serializerClass.isInstance(rawSerializer)) throw new JsonGenerationException("Could not get instance of " + serializerClass.getSimpleName(), gen);
        return serializerClass.cast(rawSerializer);
    }

    public static @Nullable ObjectNode getObjectNode(JsonParser p) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isNull()) return null;
        if (!node.isObject()) throw new JsonParseException("Expected Object got " + node.getNodeType().name());
        return (ObjectNode) node;
    }
}

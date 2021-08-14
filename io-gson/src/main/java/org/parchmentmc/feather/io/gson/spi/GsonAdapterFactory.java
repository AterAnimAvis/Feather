package org.parchmentmc.feather.io.gson.spi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.io.gson.MDCGsonAdapterFactory;
import org.parchmentmc.feather.io.gson.OffsetDateTimeAdapter;
import org.parchmentmc.feather.io.gson.SimpleVersionAdapter;
import org.parchmentmc.feather.io.gson.metadata.MetadataAdapterFactory;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;
import org.parchmentmc.feather.util.SimpleVersion;

public class GsonAdapterFactory implements IOAdapterFactory {

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new MDCGsonAdapterFactory())
        .registerTypeAdapterFactory(new MetadataAdapterFactory())
        .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
        .registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter())
        .disableHtmlEscaping()
        .create();

    @Override
    public <T> IOAdapter<T> create(Type type, @Nullable String indent) {
        return new GsonWrapper<>(type, indent);
    }

    static class GsonWrapper<T> implements IOAdapter<T> {

        private final Type typeClass;
        private final @Nullable String indent;

        GsonWrapper(Type typeClass, @Nullable String indent) {
            this.typeClass = typeClass;
            this.indent = indent;
        }

        @Override
        public String name() {
            return "gson";
        }

        @Override
        public T fromJson(String input) {
            return gson.fromJson(input, typeClass);
        }

        @Override
        public String toJson(T value) {
            StringWriter writer = new StringWriter();
            gson.toJson(value, typeClass, createWriter(writer));
            return writer.toString();
        }

        @Override
        public T fromJson(Path input) throws IOException {
            try (BufferedReader reader = Files.newBufferedReader(input)) {
                return gson.fromJson(reader, typeClass);
            }
        }

        @Override
        public void toJson(Path output, T value) throws IOException {
            try (BufferedWriter writer = Files.newBufferedWriter(output)) {
                gson.toJson(value, typeClass, createWriter(writer));
            }
        }

        private JsonWriter createWriter(Writer writer) {
            JsonWriter jsonWriter = new JsonWriter(Streams.writerForAppendable(writer));
            jsonWriter.setSerializeNulls(false);
            if (indent != null) jsonWriter.setIndent(indent);
            return jsonWriter;
        }
    }

}

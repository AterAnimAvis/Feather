package org.parchmentmc.feather.io.moshi.spi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.io.moshi.LinkedHashSetMoshiAdapter;
import org.parchmentmc.feather.io.moshi.MDCMoshiAdapter;
import org.parchmentmc.feather.io.moshi.MetadataMoshiAdapter;
import org.parchmentmc.feather.io.moshi.OffsetDateTimeAdapter;
import org.parchmentmc.feather.io.moshi.SimpleVersionAdapter;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class MoshiAdapterFactory implements IOAdapterFactory {

    private static final Moshi moshi = new Moshi.Builder()
        .add(LinkedHashSetMoshiAdapter.FACTORY)
        .add(new MDCMoshiAdapter())
        .add(new MetadataMoshiAdapter())
        .add(new OffsetDateTimeAdapter())
        .add(new SimpleVersionAdapter())
        .build();

    @Override
    public <T> IOAdapter<T> create(Type type, @Nullable String indent) {
        return new MoshiWrapper<>(indent != null ? moshi.<T>adapter(type).indent(indent) : moshi.adapter(type));
    }

    static class MoshiWrapper<T> implements IOAdapter<T> {

        private final JsonAdapter<T> adapter;

        MoshiWrapper(JsonAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public String name() {
            return "moshi";
        }

        @Override
        public T fromJson(String input) throws IOException {
            return adapter.fromJson(input);
        }

        @Override
        public String toJson(T value) {
            return adapter.toJson(value);
        }

        @Override
        public T fromJson(Path input) throws IOException {
            try (BufferedSource source = Okio.buffer(Okio.source(input))) {
                return adapter.fromJson(source);
            }
        }

        @Override
        public void toJson(Path output, T value) throws IOException {
            try (BufferedSink sink = Okio.buffer(Okio.sink(output))) {
                adapter.toJson(sink, value);
            }
        }
    }

}

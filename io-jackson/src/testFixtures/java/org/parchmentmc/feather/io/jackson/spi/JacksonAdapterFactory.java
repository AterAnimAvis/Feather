package org.parchmentmc.feather.io.jackson.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.parchmentmc.feather.io.jackson.FeatherModule;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;

public class JacksonAdapterFactory implements IOAdapterFactory {
    
    private static final String NAME = "jackson";

    private static final ObjectMapper jackson = new ObjectMapper().registerModule(new FeatherModule());

    @Override
    public <T> IOAdapter<T> create(Class<T> clazz) {
        return new JacksonWrapper<>(clazz);
    }

    @Override
    public String name() {
        return NAME;
    }
    
    static class JacksonWrapper<T> implements IOAdapter<T> {

        private final Class<T> typeClass;

        JacksonWrapper(Class<T> typeClass) {
            this.typeClass = typeClass;
        }

        @Override
        public String name() {
            return NAME;
        }

        @Override
        public T fromJson(String input) throws JsonProcessingException {
            return jackson.readValue(input, typeClass);
        }

        @Override
        public String toJson(T value) throws JsonProcessingException {
            return jackson.writeValueAsString(value);
        }
    }

}

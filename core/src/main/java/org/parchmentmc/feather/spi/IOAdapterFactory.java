package org.parchmentmc.feather.spi;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface IOAdapterFactory {

    /**
     * Creates a {@link IOAdapter <T>} for the passed in Class
     * @param type The class of the object to convert
     * @param <T> The type of the object to convert
     * @return an {@link IOAdapter <T>} configured for the specified Clazz
     */
    default <T> IOAdapter<T> create(Class<T> type) {
        return create((Type) type, null);
    }

    /**
     * Creates a {@link IOAdapter <T>} for the passed in Class
     * @param clazz The class of the object to convert
     * @param <T> The type of the object to convert
     * @return an {@link IOAdapter <T>} configured for the specified Class
     */
    default <T> IOAdapter<T> create(Type clazz) {
        return create(clazz, null);
    }

    /**
     * Creates a {@link IOAdapter <T>} for the passed in Class
     * @param type The class of the object to convert
     * @param indent The indent to use for output json
     * @param <T> The type of the object to convert
     * @return an {@link IOAdapter <T>} configured for the specified Clazz
     */
    default <T> IOAdapter<T> create(Class<T> type, @Nullable String indent) {
        return create((Type) type, indent);
    }

    /**
     * Creates a {@link IOAdapter <T>} for the passed in Type
     * @param type The type of the object to convert
     * @param indent The indent to use for output json
     * @param <T> The type of the object to convert
     * @return an {@link IOAdapter <T>} configured for the specified Type
     */
    <T> IOAdapter<T> create(Type type, @Nullable String indent);

}

package org.parchmentmc.feather.io.storage.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import com.google.common.reflect.TypeToken;

/**
 * FOR INTERNAL USE ONLY
 *
 * <pre>{@code
 *     new Types<TYPE>() {}.subtypeCollection();
 * }</pre>
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class Types<T> {

    //TODO: Maybe?
    ///**
    // * @return a ParameterizedType representing a {@code T}
    // */
    //public Type get() {
    //    return new TypeToken<T>(getClass()) {}.getType();
    //}
    //
    ///**
    // * @return a ParameterizedType representing a {@code Collection<T>}
    // */
    //public ParameterizedType collection() {
    //    return (ParameterizedType) new TypeToken<Collection<T>>(getClass()) {}.getType();
    //}

    /**
     * @return a ParameterizedType representing a {@code Collection<? extends T>}
     */
    public ParameterizedType subtypeCollection() {
        return (ParameterizedType) new TypeToken<Collection<? extends T>>(getClass()) {}.getType();
    }

}

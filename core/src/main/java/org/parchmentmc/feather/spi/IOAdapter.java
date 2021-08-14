package org.parchmentmc.feather.spi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface IOAdapter<T> {

    /**
     * @return Simple descriptive name of the underlying implementation
     */
    String name();

    /**
     * Converts a Json representation into an instance of T
     * @param input Json
     * @return an instance of T
     */
    T fromJson(String input) throws IOException;

    /**
     * Converts an instance of T into an Json representation
     * @param value the Instance
     * @return Json
     */
    String toJson(T value) throws IOException;

    /**
     * Converts a Json representation into an instance of T
     * @param input the location of the file to read from
     * @return an instance of T
     */
    default T fromJson(Path input) throws IOException {
        return fromJson(new String(Files.readAllBytes(input), StandardCharsets.UTF_8));
    }

    /**
     * Converts an instance of T into an Json representation
     * @param output the location of the file to write to
     * @param value the Instance
     */
    default void toJson(Path output, T value) throws IOException {
        Files.write(output, toJson(value).getBytes(StandardCharsets.UTF_8));
    }
}

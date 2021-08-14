package org.parchmentmc.feather.io.storage;

import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;
import org.parchmentmc.feather.spi.IOAdapterFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class SingleFileDataIO implements MappingDataIO {
    private final IOAdapterFactory factory;
    private final String indent;

    public SingleFileDataIO(IOAdapterFactory factory) {
        this(factory, STANDARD_INDENT);
    }

    public SingleFileDataIO(IOAdapterFactory factory, String indent) {
        this.factory = factory;
        this.indent = indent;
    }

    @Override
    public void write(VersionedMappingDataContainer data, Path output) throws IOException {
        Files.deleteIfExists(output);
        if (output.getParent() != null) Files.createDirectories(output.getParent());

        factory.create(VersionedMappingDataContainer.class, indent).toJson(output, data);
    }

    @Override
    public VersionedMappingDataContainer read(Path input) throws IOException {
        VersionedMappingDataContainer data = factory.create(VersionedMappingDataContainer.class, indent).fromJson(input);
        Objects.requireNonNull(data, "Data from " + input + " was deserialized as null");
        return data;
    }
}

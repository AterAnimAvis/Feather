package org.parchmentmc.feather.io.storage;

import org.parchmentmc.feather.io.storage.utils.Types;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.VersionedMDCDelegate;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

// Writes out the data as folders based on package
public class ExplodedDataIO implements MappingDataIO {
    private final IOAdapterFactory factory;
    private final String indent;

    public ExplodedDataIO(IOAdapterFactory factory) {
        this(factory, STANDARD_INDENT);
    }

    public ExplodedDataIO(IOAdapterFactory factory, String indent) {
        this.factory = factory;
        this.indent = indent;
    }

    private static final ParameterizedType PACKAGE_COLLECTION_TYPE = new Types<MappingDataContainer.PackageData>() {}.subtypeCollection();
    private static final String EXTENSION = ".json";

    public void write(VersionedMappingDataContainer data, Path base) throws IOException {
        if (Files.exists(base)) {
            // noinspection ResultOfMethodCallIgnored
            Files.walk(base)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
        Files.createDirectories(base);

        // Write out version data
        DataInfo info = new DataInfo();
        info.version = data.getFormatVersion();
        factory.create(DataInfo.class, indent).toJson(base.resolve("info.json"), info);

        // Write out packages.json
        factory.create(PACKAGE_COLLECTION_TYPE, indent).toJson(base.resolve("packages.json"), data.getPackages());

        IOAdapter<MappingDataContainer.ClassData> classAdapter = factory.create(MappingDataContainer.ClassData.class, indent);
        Path classesBase = base.resolve("classes");
        for (MappingDataContainer.ClassData classData : data.getClasses()) {
            String className = classData.getName() + EXTENSION;

            String json = classAdapter.toJson(classData);
            if (json.isEmpty()) continue;

            Path classPath = classesBase.resolve(className);
            if (classPath.getParent() != null && !Files.isDirectory(classPath.getParent())) {
                Files.createDirectories(classPath.getParent());
            }

            Files.write(classPath, json.getBytes(StandardCharsets.UTF_8));
        }
    }

    public VersionedMappingDataContainer read(Path base) throws IOException {
        DataInfo info = factory.create(DataInfo.class, indent).fromJson(base.resolve("info.json"));

        Collection<? extends MappingDataContainer.PackageData> packages = factory
            .<Collection<? extends MappingDataContainer.PackageData>>create(PACKAGE_COLLECTION_TYPE, indent)
            .fromJson(base.resolve("packages.json"));

        List<MappingDataContainer.ClassData> classes = new ArrayList<>();

        IOAdapter<MappingDataContainer.ClassData> classAdapter = factory.create(MappingDataContainer.ClassData.class, indent);
        Path classesBase = base.resolve("classes");
        Files.walkFileTree(classesBase, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Objects.requireNonNull(file);
                Objects.requireNonNull(attrs);

                classes.add(classAdapter.fromJson(file));

                return FileVisitResult.CONTINUE;
            }
        });

        return new VersionedMDCDelegate<>(info.version, new ImmutableMappingDataContainer(packages, classes));
    }

    static class DataInfo {
        public SimpleVersion version;
    }
}
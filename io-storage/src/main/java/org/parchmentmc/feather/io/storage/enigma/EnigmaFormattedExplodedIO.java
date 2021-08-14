package org.parchmentmc.feather.io.storage.enigma;

import com.google.common.base.CharMatcher;
import org.parchmentmc.feather.io.storage.MappingDataIO;
import org.parchmentmc.feather.io.storage.utils.Types;
import org.parchmentmc.feather.mapping.*;
import org.parchmentmc.feather.spi.IOAdapterFactory;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.parchmentmc.feather.mapping.MappingDataContainer.ClassData;

public class EnigmaFormattedExplodedIO implements MappingDataIO {
    static final CharMatcher DOLLAR_SIGN = CharMatcher.is('$');
    static final Comparator<String> CLASS_NAME_LENGTH_THEN_LEXICOGRAPHICALLY = Comparator
        .comparingInt(String::length)
        .thenComparing(Function.identity());

    static final String CLASS = "CLASS";
    static final String FIELD = "FIELD";
    static final String METHOD = "METHOD";
    static final String PARAM = "ARG";
    static final String COMMENT = "COMMENT";

    private static final ParameterizedType PACKAGE_COLLECTION_TYPE = new Types<MappingDataContainer.PackageData>() {}.subtypeCollection();

    private final IOAdapterFactory factory;
    private final String jsonIndent;
    private final String extension;

    public EnigmaFormattedExplodedIO(IOAdapterFactory factory) {
        this(factory, STANDARD_INDENT, "mapping");
    }

    public EnigmaFormattedExplodedIO(IOAdapterFactory factory, String jsonIndent, String extension) {
        this.factory = factory;
        this.jsonIndent = jsonIndent;
        this.extension = extension;
    }

    @Override
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
        factory.create(DataInfo.class, jsonIndent).toJson(base.resolve("info.json"), info);

        // Write out packages.json
        factory.create(PACKAGE_COLLECTION_TYPE, jsonIndent).toJson(base.resolve("packages.json"), data.getPackages());

        // Group classes by their outermost classes (via `$` matching)
        final Map<String, Set<String>> outerClassesToClasses = data.getClasses().stream()
            .map(ClassData::getName)
            .sorted()
            .collect(Collectors.groupingBy(EnigmaWriter::stripToOuter,
                Collectors.toCollection(() -> new TreeSet<>(EnigmaFormattedExplodedIO::compareClassNames))));

        Set<String> visited = new HashSet<>();

        // Write out classes
        for (Map.Entry<String, Set<String>> entry : outerClassesToClasses.entrySet()) {
            final String outerClass = entry.getKey();
            final Set<String> classes = entry.getValue();

            final Path mappingFile = base.resolve(outerClass + '.' + extension);

            if (mappingFile.getParent() != null) {
                Files.createDirectories(mappingFile.getParent());
            }

            try (Writer writer = Files.newBufferedWriter(mappingFile)) {
                visited.add(outerClass);

                ClassData outerClassData = data.getClass(outerClass);
                // If the data for the outer class is not there, substitute an empty one
                if (outerClassData == null) outerClassData = new ImmutableMappingDataContainer.ImmutableClassData(
                    outerClass, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
                );

                EnigmaWriter.writeClass(writer, 0, outerClass, outerClassData);

                for (String clz : classes) {
                    if (clz.contentEquals(outerClass)) continue; // Skip the outer class
                    visited.add(clz);

                    for (String component : EnigmaWriter.expandClass(clz)) {
                        if (visited.contains(component)) continue; // Skip if it's already been visited
                        visited.add(component);
                        if (component.contentEquals(clz)) break; // Skip if it's the class currently being written

                        EnigmaWriter.writeClass(writer, DOLLAR_SIGN.countIn(component), EnigmaWriter.stripToMostInner(component),
                            new ImmutableMappingDataContainer.ImmutableClassData(
                                component, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
                            ));
                    }

                    ClassData clzData = data.getClass(clz);
                    // If the data for the inner class is not there, substitute an empty one
                    if (clzData == null) clzData = new ImmutableMappingDataContainer.ImmutableClassData(
                        clz, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
                    );

                    EnigmaWriter.writeClass(writer, DOLLAR_SIGN.countIn(clz), EnigmaWriter.stripToMostInner(clz), clzData);
                }
            }
        }
    }

    @Override
    public VersionedMappingDataContainer read(Path base) throws IOException {
        DataInfo info = factory.create(DataInfo.class).fromJson(base.resolve("info.json"));
        if (info == null) throw new IOException("info.json did not deserialize");

        Collection<? extends MappingDataContainer.PackageData> packages = factory
            .<Collection<? extends MappingDataContainer.PackageData>>create(PACKAGE_COLLECTION_TYPE)
            .fromJson(base.resolve("packages.json"));
        if (packages == null) throw new IOException("packages.json did not deserialize");

        MappingDataBuilder builder = new MappingDataBuilder();

        Files.walkFileTree(base, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Objects.requireNonNull(file);
                Objects.requireNonNull(attrs);
                // Skip files not ending with the extension
                if (!file.toString().endsWith(extension)) return FileVisitResult.CONTINUE;

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    EnigmaReader.readFile(builder, reader);
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return new VersionedMDCDelegate<>(info.version, new ImmutableMappingDataContainer(packages, builder.getClasses()));
    }

    static class DataInfo {
        public SimpleVersion version;
    }

    static int compareClassNames(String a, String b) {
        final String[] aComponents = a.split(DOLLAR_SIGN.toString());
        final String[] bComponents = b.split(DOLLAR_SIGN.toString());

        int ret = 0;
        int minimum = Math.min(aComponents.length, bComponents.length);
        for (int i = 0; i < minimum; i++) {
            String aComp = aComponents[i];
            String bComp = bComponents[i];
            ret = CLASS_NAME_LENGTH_THEN_LEXICOGRAPHICALLY.compare(aComp, bComp);
            if (ret != 0) break;
        }

        if (ret == 0) {
            ret = CLASS_NAME_LENGTH_THEN_LEXICOGRAPHICALLY.compare(a, b);
        }

        return ret;
    }
}
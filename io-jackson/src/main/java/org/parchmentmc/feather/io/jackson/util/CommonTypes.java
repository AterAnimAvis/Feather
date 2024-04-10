package org.parchmentmc.feather.io.jackson.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class CommonTypes {

    private CommonTypes() {
        throw new IllegalStateException("Can not instantiate an instance of: CommonTypes. This is a utility class");
    }

    private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

    /**
     * {@code List<String>}
     */
    public static final JavaType LIST_STRING = TYPE_FACTORY.constructCollectionType(List.class, String.class);

    /**
     * {@code LinkedHashSet<Named>}
     */
    public static final JavaType SET_NAMED = TYPE_FACTORY.constructCollectionType(LinkedHashSet.class, Named.class);

    /**
     * {@code LinkedHashSet<ClassMetadata>}
     */
    public static final JavaType SET_CLASS_METADATA = TYPE_FACTORY.constructCollectionType(LinkedHashSet.class, ClassMetadata.class);

    /**
     * {@code LinkedHashSet<FieldMetadata>}
     */
    public static final JavaType SET_FIELD_METADATA = TYPE_FACTORY.constructCollectionType(LinkedHashSet.class, FieldMetadata.class);

    /**
     * {@code LinkedHashSet<MethodMetadata>}
     */
    public static final JavaType SET_METHOD_METADATA = TYPE_FACTORY.constructCollectionType(LinkedHashSet.class, MethodMetadata.class);

    /**
     * {@code LinkedHashSet<Reference>}
     */
    public static final JavaType SET_REFERENCE = TYPE_FACTORY.constructCollectionType(LinkedHashSet.class, Reference.class);

    /**
     * {@code LinkedHashSet<RecordMetadata>}
     */
    public static final JavaType SET_RECORD_METADATA = TYPE_FACTORY.constructCollectionType(LinkedHashSet.class, RecordMetadata.class);

    /**
     * {@code Collection<PackageData>}
     */
    public static final JavaType COLLECTION_PACKAGE_DATA = TYPE_FACTORY.constructCollectionLikeType(Collection.class, MappingDataContainer.PackageData.class);

    /**
     * {@code Collection<ClassData>}
     */
    public static final JavaType COLLECTION_CLASS_DATA = TYPE_FACTORY.constructCollectionLikeType(Collection.class, MappingDataContainer.ClassData.class);

    /**
     * {@code Collection<FieldData>}
     */
    public static final JavaType COLLECTION_FIELD_DATA = TYPE_FACTORY.constructCollectionLikeType(Collection.class, MappingDataContainer.FieldData.class);

    /**
     * {@code Collection<MethodData>}
     */
    public static final JavaType COLLECTION_METHOD_DATA = TYPE_FACTORY.constructCollectionLikeType(Collection.class, MappingDataContainer.MethodData.class);

    /**
     * {@code Collection<ParameterData>}
     */
    public static final JavaType COLLECTION_PARAMETER_DATA = TYPE_FACTORY.constructCollectionLikeType(Collection.class, MappingDataContainer.ParameterData.class);
    
}

package org.parchmentmc.feather.io.storage.utils;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.gson.spi.GsonAdapterFactory;
import org.parchmentmc.feather.io.moshi.spi.MoshiAdapterFactory;
import org.parchmentmc.feather.io.storage.StorageTest;
import org.parchmentmc.feather.manifests.MDCTestConstants;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.spi.IOAdapterFactory;

public class TypesTest extends StorageTest {

    private static final ParameterizedType type = new Types<MappingDataContainer.PackageData>() {}.subtypeCollection();

    @Test
    public void testGson() {
        test(new GsonAdapterFactory());
    }

    @Test
    public void testMoshi() {
        test(new MoshiAdapterFactory());
    }

    protected <T> void test(IOAdapterFactory factory) {
        test(factory.<Collection<? extends MappingDataContainer.PackageData>>create(type), MDCTestConstants.PACKAGES);
    }
}

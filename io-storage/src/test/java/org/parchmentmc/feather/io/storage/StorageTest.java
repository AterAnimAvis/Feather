package org.parchmentmc.feather.io.storage;

import org.parchmentmc.feather.spi.IOAdapter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class StorageTest {

    protected <T> void test(IOAdapter<T> adapter, T original) {
        final String originalJson = assertDoesNotThrow(() -> adapter.toJson(original));

        final T versionA = assertDoesNotThrow(() -> adapter.fromJson(originalJson));

        final String versionAJson = assertDoesNotThrow(() -> adapter.toJson(versionA));

        final T versionB = assertDoesNotThrow(() -> adapter.fromJson(versionAJson));

        assertEquals(original, versionA);
        assertEquals(versionA, versionB);

        assertNotSame(original, versionA);
        assertNotSame(versionA, versionB);

        assertEquals(originalJson, versionAJson);
    }

}

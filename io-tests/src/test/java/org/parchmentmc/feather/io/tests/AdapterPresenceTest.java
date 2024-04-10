package org.parchmentmc.feather.io.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.spi.IOAdapterFactory;

import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AdapterPresenceTest {
    
    private static final ServiceLoader<IOAdapterFactory> CONVERTERS = ServiceLoader.load(IOAdapterFactory.class);
    private static final String[] EXPECTED_ADAPTERS = new String[] { "gson", "moshi", "jackson" };
    
    @Test
    public void insureExpectedAdaptersAreActuallyPresent() {
        Assertions.assertLinesMatch(
                Stream.of(EXPECTED_ADAPTERS).sorted(),
                StreamSupport.stream(CONVERTERS.spliterator(), false)
                        .map(IOAdapterFactory::name)
                        .distinct()
                        .sorted()
        );
    }
    
}

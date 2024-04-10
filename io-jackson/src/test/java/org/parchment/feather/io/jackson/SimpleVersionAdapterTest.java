package org.parchment.feather.io.jackson;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.jackson.JacksonTest;
import org.parchmentmc.feather.io.jackson.modules.SimpleVersionModule;
import org.parchmentmc.feather.util.SimpleVersion;

public class SimpleVersionAdapterTest extends JacksonTest {
    public SimpleVersionAdapterTest() {
        super(b -> b.registerModule(new SimpleVersionModule()));
    }

    @Test
    public void testJackson() {
        test(SimpleVersion.class, SimpleVersion.of(1, 2, 3));
        test(SimpleVersion.class, SimpleVersion.of("1.4"));
    }
}

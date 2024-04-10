package org.parchment.feather.io.jackson;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.jackson.JacksonTest;
import org.parchmentmc.feather.io.jackson.modules.OffsetDateTimeModule;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeAdapterTest extends JacksonTest {
    public OffsetDateTimeAdapterTest() {
        super(b -> b.registerModule(new OffsetDateTimeModule()));
    }

    @Test
    public void testJackson() {
        test(OffsetDateTime.class, OffsetDateTime.MIN);
        test(OffsetDateTime.class, OffsetDateTime.MAX);
        test(OffsetDateTime.class, OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
    }
}

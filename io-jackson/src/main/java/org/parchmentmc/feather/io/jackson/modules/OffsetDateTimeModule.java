package org.parchmentmc.feather.io.jackson.modules;

import org.parchmentmc.feather.io.jackson.FeatherModule;
import org.parchmentmc.feather.io.jackson.modules.datetime.OffsetDateTimeDeserializer;
import org.parchmentmc.feather.io.jackson.modules.datetime.OffsetDateTimeSerializer;
import org.parchmentmc.feather.io.jackson.util.BaseModule;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeModule extends BaseModule {

    public OffsetDateTimeModule() {
        this(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * @param formatter formatter used to serialize and deserialize OffsetDateTime.
     */
    public OffsetDateTimeModule(DateTimeFormatter formatter) {
        super("Feather$OffsetDateTime", FeatherModule.VERSION);
        addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer(formatter));
        addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer(formatter));
    }

}

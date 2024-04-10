package org.parchmentmc.feather.io.jackson;

import com.fasterxml.jackson.core.Version;
import org.parchmentmc.feather.io.jackson.modules.MDCModule;
import org.parchmentmc.feather.io.jackson.modules.MetadataModule;
import org.parchmentmc.feather.io.jackson.modules.OffsetDateTimeModule;
import org.parchmentmc.feather.io.jackson.modules.SimpleVersionModule;
import org.parchmentmc.feather.io.jackson.util.BaseModule;

import java.time.format.DateTimeFormatter;

public class FeatherModule extends BaseModule {
    
    // TODO: Base it on Implementation-Version? We should probably be adding that in the META-INF
    public static final Version VERSION = Version.unknownVersion();

    public FeatherModule() {
        this(false, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    
    /**
     * @param ignoreNonDocumented whether this should ignore mapping data entries which have no javadocs.
     * @param formatter formatter used to serialize and deserialize OffsetDateTime.
     */
    public FeatherModule(boolean ignoreNonDocumented, DateTimeFormatter formatter) {
        super("FeatherModule", FeatherModule.VERSION);
        addDependency(new MDCModule(ignoreNonDocumented));
        addDependency(new MetadataModule());
        addDependency(new OffsetDateTimeModule(formatter));
        addDependency(new SimpleVersionModule());
    }
    
}

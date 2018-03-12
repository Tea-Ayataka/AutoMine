package net.ayataka.automine.config;

import lombok.Getter;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class GlobalConfig {
    @Getter
    public static Configuration configuration;

    // Properties (Client Side)
    public static Property isEnabledMining;
    public static Property isEnabledCutting;
    public static Property blacklistedTools;

    // Properties (Server Side)
    public static Property blockLimit;

    public GlobalConfig(File file) {
        configuration = new Configuration(file);
        configuration.load();

        // Client
        isEnabledMining = configuration.get(Configuration.CATEGORY_CLIENT, "isToggledMining", true);
        isEnabledCutting = configuration.get(Configuration.CATEGORY_CLIENT, "isToggledCutting", true);

        blacklistedTools = configuration.get(Configuration.CATEGORY_CLIENT, "Blacklisted_Tools", new String[]{"minecraft:useless_pickaxe", "minecraft:useless_axe"}, "Blacklisted Tools");

        // Server
        blockLimit = configuration.get(Configuration.CATEGORY_GENERAL, "BlockLimit", 360, "Maximum number of blocks to be vein mined");

        configuration.save();
    }

    public static void toggle(Property property) {
        property.set(!property.getBoolean());
        configuration.save();
    }
}

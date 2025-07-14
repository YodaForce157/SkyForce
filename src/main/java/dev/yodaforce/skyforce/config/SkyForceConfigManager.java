package dev.yodaforce.skyforce.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.yodaforce.skyforce.config.categories.GeneralCategory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Path;

public class SkyForceConfigManager {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("skyforce.json");
    private static final ConfigClassHandler<SkyForceConfig> CONFIG_HANDLER = ConfigClassHandler.createBuilder(SkyForceConfig.class)
            .serializer(c -> GsonConfigSerializerBuilder.create(c)
                    .setJson5(false)
                    .setPath(CONFIG_PATH)
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting).build())
            .build();

    public static void init() {
        CONFIG_HANDLER.load();
    }

    public static void save() {
        CONFIG_HANDLER.save();
    }

    public static SkyForceConfig getConfig() {
        return CONFIG_HANDLER.instance();
    }

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG_HANDLER, ((skyForceConfig, defaults, builder) ->
                builder.title(Text.literal("SkyForce"))
                        .category(GeneralCategory.create(getConfig()))
                        .save(SkyForceConfigManager::save)
        )).generateScreen(parent);
    }
}

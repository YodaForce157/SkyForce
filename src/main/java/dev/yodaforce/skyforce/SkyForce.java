package dev.yodaforce.skyforce;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import dev.yodaforce.skyforce.config.SkyForceConfigManager;
import dev.yodaforce.skyforce.events.ChatEvents;
import dev.yodaforce.skyforce.features.notifications.Notifications;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SkyForce implements ModInitializer {
    public static final String MOD_ID = "skyforce";
    public static final Logger LOGGER = LoggerFactory.getLogger(SkyForce.class);
    public static final Gson GSON = new Gson().newBuilder().create();
    public static final Version VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion();

    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[SkyForce] Initializing SkyForce version {}", VERSION.getFriendlyString());
        SkyForceConfigManager.init();

        init();
    }

    public void init() {
        Notifications.init();
    }
}

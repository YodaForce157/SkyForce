package dev.yodaforce.skyforce.config.categories;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.yodaforce.skyforce.config.SkyForceConfig;
import dev.yodaforce.skyforce.features.notifications.NotificationConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GeneralCategory {
    public static ConfigCategory create(SkyForceConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.literal("General"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Notifications Enabled"))
                        .binding(false, () -> config.enableNotifications, newVal -> config.enableNotifications = newVal)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(ButtonOption.createBuilder()
                        .name(Text.literal("Shortcuts"))
                        .text(Text.literal("§9Open"))
                        .action((a, b) -> MinecraftClient.getInstance().setScreen(new NotificationConfigScreen(a)))
                        .build()
                ).build();
    }
}

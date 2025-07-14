package dev.yodaforce.skyforce.features.notifications;

import com.google.gson.reflect.TypeToken;
import dev.yodaforce.skyforce.SkyForce;
import dev.yodaforce.skyforce.events.ChatEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Notifications {
    private static final Logger LOGGER = LoggerFactory.getLogger(Notifications.class);
    private static final Path NOTIFICATIONS_CONFIG = SkyForce.CONFIG_DIR.resolve("notifications.json");
    private static final Type notificationListType = new TypeToken<List<Notification>>(){}.getType();

    private static List<Notification> notifications = new ArrayList<>();

    public static List<Notification> getNotifications() {
        return notifications;
    }

    public static void clearNotifications() {
        notifications.clear();
    }

    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public static void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    public static void init() {
        loadNotifications();

        ChatEvents.ON_GAME_MESSAGE.register(Notifications::onMessage);
    }

    private static void onMessage(Text message) {
        if (message == null || message.getString().isEmpty()) {
            return;
        }

        for (Notification notification : notifications) {
            if (!notification.enabled()) continue;

            boolean matches = notification.regex() ? message.getString().matches(notification.matcher()) : message.getString().contains(notification.matcher());
            if (matches) sendAlert(notification.response());
        }
    }

    public static void loadNotifications() {
        try (BufferedReader reader = Files.newBufferedReader(NOTIFICATIONS_CONFIG)) {
            notifications = SkyForce.GSON.fromJson(reader, notificationListType);
            if (notifications == null) notifications = new ArrayList<>();

            LOGGER.info("[SkyForce] Loaded {} notifications from config", notifications.size());
        } catch (FileNotFoundException e) {
            LOGGER.warn("[SkyForce] Failed to load notifications config, file not found");
        } catch (IOException e) {
            LOGGER.error("[SkyForce] Failed to load notifications config", e);
        }
    }

    public static void saveNotifications() {
        File notificationsFile = NOTIFICATIONS_CONFIG.toFile();
        try {
            if (!notificationsFile.exists()) {
                notificationsFile.getParentFile().mkdirs();

                if (notificationsFile.createNewFile()) {
                    LOGGER.info("[SkyForce] Created notifications config file");
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(NOTIFICATIONS_CONFIG, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                SkyForce.GSON.toJson(notifications, notificationListType, writer);
            }

            LOGGER.info("[SkyForce] Saved notifications config");
        } catch (IOException e) {
            LOGGER.error("[SkyForce] Failed to save notifications config", e);
        }
    }

    private static void sendAlert(String message) {
        MinecraftClient.getInstance().inGameHud.setTitleTicks(0, 200, 0); // TODO possibly allow for configurable duration
        MinecraftClient.getInstance().inGameHud.setTitle(Text.literal(message));
    }
}

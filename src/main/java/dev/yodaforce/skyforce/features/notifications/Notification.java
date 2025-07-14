package dev.yodaforce.skyforce.features.notifications;

public record Notification(String matcher, String response, boolean regex, boolean enabled) {
}

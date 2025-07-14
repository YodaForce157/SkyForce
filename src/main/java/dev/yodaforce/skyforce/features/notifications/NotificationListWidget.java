package dev.yodaforce.skyforce.features.notifications;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.List;

public class NotificationListWidget extends ElementListWidget<NotificationListWidget.NotificationEntry> {
    public NotificationListWidget(MinecraftClient client, int w, int h, int y, int entryHeight) {
        super(client, w, h, y, entryHeight);

        Notifications.getNotifications().forEach(notif -> {
            addEntry(new NotificationEntry(notif.matcher(), notif.regex(), notif.response(), notif.enabled()));
        });
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 60;
    }

    @Override
    protected int getScrollbarX() {
        return super.getScrollbarX();
    }

    public void createNew() {
        //TODO make each field clearer on what it does
        children().add(new NotificationEntry("Message", false, "Title", true));
    }

    public void saveNotifications() {
        Notifications.clearNotifications();
        children().forEach(NotificationEntry::loadToNotifications);
        Notifications.saveNotifications();
    }

    public class NotificationEntry extends Entry<NotificationEntry> {
        private final List<ClickableWidget> widgets;
        private final TextFieldWidget matcherField;
        private final CheckboxWidget isRegexWidget;
        private final TextFieldWidget responseField;
        private final CheckboxWidget enabledWidget;
        private final ButtonWidget deleteButton;

        public NotificationEntry(String matcher, boolean isRegex, String response, boolean enabled) {
            matcherField = new TextFieldWidget(client.textRenderer,65, 20, Text.literal("Matcher"));
            matcherField.setText(matcher);

            isRegexWidget = CheckboxWidget.builder(Text.literal("Regex"), client.textRenderer)
                    .checked(isRegex).build();

            responseField = new TextFieldWidget(client.textRenderer,65, 20, Text.literal("Response"));
            responseField.setText(response);

            enabledWidget = CheckboxWidget.builder(Text.literal("Enabled"), client.textRenderer)
                    .checked(enabled).build();

            deleteButton = ButtonWidget.builder(Text.literal("\uD83D\uDDD1"), button -> {
                NotificationListWidget.this.removeEntry(this);
                Notifications.removeNotification(new Notification(this.matcherField.getText(), this.responseField.getText(), this.isRegexWidget.isChecked(), this.enabledWidget.isChecked())); //TODO codec
            }).size(16, 16).build();

            widgets = List.of(matcherField, isRegexWidget, responseField, enabledWidget, deleteButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return widgets;
        }

        @Override
        public List<? extends Element> children() {
            return widgets;
        }

        private void loadToNotifications() {
            if (matcherField.getText().isEmpty() || responseField.getText().isEmpty()) return;
            Notifications.addNotification(new Notification(matcherField.getText(), responseField.getText(), isRegexWidget.isChecked(), enabledWidget.isChecked()));
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
            matcherField.setPosition(x, y + (entryHeight - matcherField.getHeight()) / 2);
            isRegexWidget.setPosition(matcherField.getRight() + 5, y + (entryHeight - isRegexWidget.getHeight()) / 2);
            responseField.setPosition(isRegexWidget.getRight() + 5, y + (entryHeight - responseField.getHeight()) / 2);
            enabledWidget.setPosition(responseField.getRight() + 5, y + (entryHeight - enabledWidget.getHeight()) / 2);
            deleteButton.setPosition(enabledWidget.getRight() + 5, y + (entryHeight - deleteButton.getHeight()) / 2);

            for (ClickableWidget widget : widgets) {
                widget.render(context, mouseX, mouseY, tickProgress);
            }
        }
    }
}

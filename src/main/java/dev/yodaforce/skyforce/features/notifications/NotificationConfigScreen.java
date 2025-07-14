package dev.yodaforce.skyforce.features.notifications;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;

public class NotificationConfigScreen extends Screen {
    private boolean initialized = false;
    private final Screen parent;
    private NotificationListWidget notificationListWidget;

    public NotificationConfigScreen(Screen parent) {
        super(Text.literal("SkyForce Notifications Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        if (initialized) {
            notificationListWidget.setDimensions(width, height - 64);
        } else {
            notificationListWidget = new NotificationListWidget(client, this.width, this.height - 64, 32, 24);
            initialized = true;
        }
        addDrawableChild(notificationListWidget);

        GridWidget buttonGrid = new GridWidget();
        buttonGrid.getMainPositioner().margin(5, 2);
        GridWidget.Adder adder = buttonGrid.createAdder(2);

        ButtonWidget doneButton = ButtonWidget.builder(Text.literal("Done"), button -> {
            notificationListWidget.saveNotifications();
            client.setScreen(parent);
        }).size(100, 20).build();
        adder.add(doneButton);

        ButtonWidget cancelButton = ButtonWidget.builder(Text.literal("Cancel"), button -> {
            client.setScreen(parent);
        }).size(100, 20).build();
        adder.add(cancelButton);

        buttonGrid.refreshPositions();
        SimplePositioningWidget.setPos(buttonGrid, 0, this.height - 16, this.width, 0);
        buttonGrid.forEachChild(this::addDrawableChild);

        ButtonWidget newButton = ButtonWidget.builder(Text.literal("\u002B"), a -> {
            notificationListWidget.createNew();
        }).position(this.width / 2 + 80, 8).size(16, 16).build();

        this.addDrawableChild(newButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(parent);
    }
}

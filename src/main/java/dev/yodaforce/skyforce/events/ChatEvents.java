package dev.yodaforce.skyforce.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;

public class ChatEvents {
    public static final Event<GameMessageListener> ON_GAME_MESSAGE = EventFactory.createArrayBacked(GameMessageListener.class, listeners -> message -> {
        for (GameMessageListener listener : listeners) {
            listener.onMessage(message);
        }
    });

    public interface GameMessageListener {
        void onMessage(Text message);
    }
}
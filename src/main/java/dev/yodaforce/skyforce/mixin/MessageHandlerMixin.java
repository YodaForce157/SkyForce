package dev.yodaforce.skyforce.mixin;

import dev.yodaforce.skyforce.events.ChatEvents;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MessageHandler.class)
public class MessageHandlerMixin {
    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void skyforce$onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
        //TODO possibly split into onGameMessage and onGameOverlayMessage
        if (overlay) return;
        ChatEvents.ON_GAME_MESSAGE.invoker().onMessage(message);
    }
}

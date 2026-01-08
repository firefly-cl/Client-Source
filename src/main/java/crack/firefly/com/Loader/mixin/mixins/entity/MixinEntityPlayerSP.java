package crack.firefly.com.Loader.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crack.firefly.com.System.event.impl.EventMotionUpdate;
import crack.firefly.com.System.event.impl.EventSendChat;
import crack.firefly.com.System.event.impl.EventUpdate;
import crack.firefly.com.System.mods.impl.AnimationsMod; // Importar
import net.minecraft.client.entity.EntityPlayerSP;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

	@Inject(method = "onUpdate", at = @At("HEAD"))
	public void preOnUpdate(CallbackInfo ci) {
		new EventUpdate().call();
	}
	
	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void preSendChatMessage(String message, CallbackInfo ci) {
		EventSendChat event = new EventSendChat(message);
		event.call();
		if(event.isCancelled()) ci.cancel();
	}
	
	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
	private void preOnUpdateWalkingPlayer(CallbackInfo ci) {
        // Lógica do 1.7 Sneak (Hard Sneak)
        AnimationsMod mod = AnimationsMod.getInstance();
        if (mod.isToggled() && mod.getSneakSetting().isToggled()) {
             // O 1.7 Sneak é puramente visual em outros lugares, mas para garantir 
             // que a câmera não tenha o 'smooth' padrão, algumas clients alteram o eyeHeight aqui.
             // Se você já tem 'smoothSneak', certifique-se que o 1.7 tenha prioridade.
        }
		new EventMotionUpdate().call();
	}
}
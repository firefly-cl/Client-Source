package crack.firefly.com.Loader.mixin.mixins.gui;

import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import crack.firefly.com.System.mods.impl.TabEditorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay {
    
    /**
     * Limpado: Agora apenas desenha o nome no TAB normalmente, sem procurar por ícones
     */
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I", ordinal = 2))
    public int renderGlideIcon(FontRenderer instance, String text, float x, float y, int color) {
        return instance.drawStringWithShadow(text, x, y, color);
    }
    
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getPlayerEntityByUUID(Ljava/util/UUID;)Lnet/minecraft/entity/player/EntityPlayer;"))
    public EntityPlayer removePlayerHead(WorldClient instance, UUID uuid) {
        if(TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getHeadSetting().isToggled()) return null;
        return instance.getPlayerEntityByUUID(uuid);
    }

    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isIntegratedServerRunning()Z"))
    public boolean removePlayerHead(Minecraft instance) { 
        return instance.isIntegratedServerRunning() && showHeads(); 
    }

    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;getIsencrypted()Z"))
    public boolean removePlayerHead(NetworkManager instance) { 
        return instance.getIsencrypted() && showHeads(); 
    }

    @ModifyConstant(method = "renderPlayerlist", constant = @Constant(intValue = Integer.MIN_VALUE))
    public int removeBackground(int original) { 
        return (TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getBackgroundSetting().isToggled()) ? 0 : original; 
    }

    @ModifyConstant(method = "renderPlayerlist", constant = @Constant(intValue = 553648127))
    public int removeBackground2(int original) { 
        return (TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getBackgroundSetting().isToggled()) ? 0 : original; 
    }

    private boolean showHeads() { 
        return !(TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getHeadSetting().isToggled()); 
    }
}
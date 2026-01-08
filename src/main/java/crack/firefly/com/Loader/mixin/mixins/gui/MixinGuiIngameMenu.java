package crack.firefly.com.Loader.mixin.mixins.gui;

import crack.firefly.com.menus.GuiGameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngameMenu {

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void preInitGui(CallbackInfo ci) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiGameMenu());
        ci.cancel();
    }

}

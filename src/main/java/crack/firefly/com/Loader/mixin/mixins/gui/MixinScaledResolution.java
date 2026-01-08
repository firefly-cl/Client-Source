package crack.firefly.com.Loader.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import crack.firefly.com.System.mods.impl.InventoryMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

@Mixin(ScaledResolution.class)
public abstract class MixinScaledResolution {

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow private double scaledWidthD;
    @Shadow private double scaledHeightD;
    @Shadow private int scaleFactor;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(Minecraft mc, CallbackInfo ci) {
        InventoryMod mod = InventoryMod.getInstance();
        if (mod != null && mod.isToggled()) {
            float customFactor = mod.getGuiScaleSetting().getValueFloat();
            this.scaledWidth = mc.displayWidth;
            this.scaledHeight = mc.displayHeight;
            this.scaleFactor = 1;
            int i = mc.gameSettings.guiScale == 0 ? 1000 : mc.gameSettings.guiScale;

            while (this.scaleFactor < i && 
                   this.scaledWidth / (this.scaleFactor + 1) >= (int)(320 * customFactor) && 
                   this.scaledHeight / (this.scaleFactor + 1) >= (int)(240 * customFactor)) {
                ++this.scaleFactor;
            }

            if (mc.isUnicode() && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) --this.scaleFactor;

            this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
            this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
            this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
            this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
        }
    }
}
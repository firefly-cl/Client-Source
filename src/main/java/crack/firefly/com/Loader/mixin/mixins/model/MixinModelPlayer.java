package crack.firefly.com.Loader.mixin.mixins.model;

import crack.firefly.com.System.mods.impl.FemaleGenderMod;
import crack.firefly.com.System.emote.EmoteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer extends ModelBiped {

    private ModelRenderer boobs;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void boobsInit(float size, boolean z, CallbackInfo c) {
        boobs = new ModelRenderer(this, 16, 20);
        boobs.addBox(-4F, -1.5F, -5F, 8, 4, 4, size);
    }

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void applyEmoteAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        EmoteManager em = EmoteManager.getInstance();

        if (em.isPlaying() && entityIn == Minecraft.getMinecraft().thePlayer) {
            
            // ROTAÇÃO DO CORPO
            this.bipedBody.rotateAngleX += em.getRotation("body", "x");
            this.bipedBody.rotateAngleY += em.getRotation("body", "y");
            this.bipedBody.rotateAngleZ += em.getRotation("body", "z");

            // ROTAÇÃO DOS BRAÇOS
            this.bipedRightArm.rotateAngleX += em.getRotation("rightArm", "x");
            this.bipedLeftArm.rotateAngleX += em.getRotation("leftArm", "x");

            // ROTAÇÃO DAS PERNAS (Apenas no X para dobrar o joelho)
            float legBend = em.getRotation("rightLeg", "x");
            this.bipedRightLeg.rotateAngleX += legBend;
            this.bipedLeftLeg.rotateAngleX += legBend;

            // --- POSICIONAMENTO (SQUAT/AGACHADO) ---
            // Faz o boneco descer um pouco para o rebolado ficar real
            if (em.getRotation("body", "x") != 0) {
                this.bipedBody.rotationPointY = 2.0f;
                this.bipedRightLeg.rotationPointY = 12.0f + 2.0f;
                this.bipedLeftLeg.rotationPointY = 12.0f + 2.0f;
            }
        } else {
            // RESETA TUDO QUANDO PARAR
            this.bipedBody.rotationPointY = 0.0f;
            this.bipedRightLeg.rotationPointY = 12.0f;
            this.bipedLeftLeg.rotationPointY = 12.0f;
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void renderOverride(Entity e, float v, float w, float x, float y, float z, float scale, CallbackInfo c) {
        if (boobs != null && e == Minecraft.getMinecraft().thePlayer) {
            boobs.showModel = FemaleGenderMod.getInstance().isToggled();
            boobs.offsetY = e.isSneaking() ? .25F : 0F;
            boobs.offsetZ = e.isSneaking() ? .1F : 0F;
            boobs.rotateAngleX = 45;
            boobs.render(scale);
        }
    }
}
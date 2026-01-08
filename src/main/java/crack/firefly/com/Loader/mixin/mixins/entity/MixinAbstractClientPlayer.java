package crack.firefly.com.Loader.mixin.mixins.entity;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crack.firefly.com.Loader.Abstractions.IMixinEntityPlayer;
import crack.firefly.com.System.event.impl.EventFovUpdate;
import crack.firefly.com.System.event.impl.EventLocationCape;
import crack.firefly.com.System.event.impl.EventLocationSkin;
import crack.firefly.com.System.mods.impl.skin3d.render.CustomizableModelPart;
import crack.firefly.com.System.mods.impl.waveycapes.sim.StickSimulation;
import crack.firefly.com.System.skin.SkinManager; // IMPORT ADICIONADO
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer implements IMixinEntityPlayer {

    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Shadow
    private NetworkPlayerInfo playerInfo;

    private CustomizableModelPart headLayers;
    private CustomizableModelPart[] skinLayers;
    private final StickSimulation simulation = new StickSimulation();

    @Override
    public CustomizableModelPart getHeadLayers() {
        return headLayers;
    }

    @Override
    public void setupHeadLayers(CustomizableModelPart box) {
        this.headLayers = box;
    }

    @Override
    public CustomizableModelPart[] getSkinLayers() {
        return skinLayers;
    }

    @Override
    public void setupSkinLayers(CustomizableModelPart[] box) {
        this.skinLayers = box;
    }

    @Override
    public StickSimulation getSimulation() {
        return simulation;
    }

    /**
     * FORÇA O BRAÇO MAGRO OU GORDO (SLIM / DEFAULT)
     * Esse método é o que o Minecraft usa para decidir qual modelo de braço carregar.
     */
    @Inject(method = "getSkinType", at = @At("HEAD"), cancellable = true)
    public void getSkinType(CallbackInfoReturnable<String> cir) {
        SkinManager sm = SkinManager.getInstance();
        
        // Se o jogador estiver usando uma skin customizada do seu client
        if (sm.hasCustomSkin()) {
            // Se o botão "Slim" estiver ativo no menu, retorna "slim" (Alex), senão "default" (Steve)
            cir.setReturnValue(sm.isSlimModel() ? "slim" : "default");
        }
    }

    @Inject(method = "getFovModifier", at = @At("RETURN"), cancellable = true)
    public void getFovModifier(CallbackInfoReturnable<Float> cir) {
        EventFovUpdate event = new EventFovUpdate((AbstractClientPlayer) (Object)this, cir.getReturnValue());
        event.call();
        cir.setReturnValue(event.getFov());
    }
    
    @Inject(method = "getLocationSkin", at = @At("HEAD"), cancellable = true)
    public void onGetLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        EventLocationSkin event = new EventLocationSkin(playerInfo);
        event.call();
        
        if(event.isCancelled()) {
            cir.cancel();
            cir.setReturnValue(event.getSkin());
        }
    }
    
    @Inject(method = "getLocationCape", cancellable = true, at = @At("HEAD"))
    public void onGetLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        EventLocationCape event = new EventLocationCape(playerInfo);
        event.call();
        
        if(event.isCancelled()) {
            cir.cancel();
            cir.setReturnValue(event.getCape());
        }
    }
}
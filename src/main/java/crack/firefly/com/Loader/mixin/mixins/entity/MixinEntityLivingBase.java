package crack.firefly.com.Loader.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crack.firefly.com.Loader.Abstractions.IMixinEntityLivingBase;
import crack.firefly.com.System.event.impl.EventLivingUpdate;
import crack.firefly.com.System.mods.impl.SlowSwingMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity implements IMixinEntityLivingBase {

	@Shadow
	public abstract int getArmSwingAnimationEnd();
	
	public MixinEntityLivingBase(World worldIn) {
		super(worldIn);
	}

    @Inject(method = "onEntityUpdate", at = @At("TAIL"))
    public void onEntityUpdate(CallbackInfo ci) {
    	new EventLivingUpdate((EntityLivingBase) (Object) this).call();
    }
    
	@Inject(method = "getArmSwingAnimationEnd", at = @At("HEAD"), cancellable = true)
	public void changeSwingSpeed(CallbackInfoReturnable<Integer> cir) {
		
		SlowSwingMod mod = SlowSwingMod.getInstance();
		
		if(mod.isToggled()) {
			cir.setReturnValue(mod.getDelaySetting().getValueInt());
		}
	}
	
    // REMOVIDO O 'getLook' (Mouse Delay Fix) POIS ELE CAUSA "FLUTUAÇÃO" AO PULAR
    // Se quiser usar, precisa de uma implementação melhor, mas por enquanto tire isso para testar.
    
	@Override
	public int getArmSwingAnimation() {
		return getArmSwingAnimationEnd();
	}
}
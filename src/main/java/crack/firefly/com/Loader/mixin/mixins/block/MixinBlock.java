package crack.firefly.com.Loader.mixin.mixins.block;

import crack.firefly.com.System.mods.impl.FPSBoostMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void hideBlocks(CallbackInfoReturnable<Integer> cir) {
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod == null || !mod.isToggled()) return;

        Block block = (Block)(Object)this;
        if ((mod.hideTallGrass.isToggled() && block instanceof BlockTallGrass) ||
            (mod.hideFlowers.isToggled() && block instanceof BlockFlower) ||
            (mod.hideFences.isToggled() && block instanceof BlockFence)) {
            cir.setReturnValue(-1);
        }
    }
}
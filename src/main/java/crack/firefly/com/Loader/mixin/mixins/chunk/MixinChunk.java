package crack.firefly.com.Loader.mixin.mixins.chunk;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

@Mixin(Chunk.class)
public abstract class MixinChunk {
    
    @Shadow @Final private ClassInheritanceMultiMap<Entity>[] entityLists;
    @Shadow @Final private World worldObj;
    @Shadow @Final private ExtendedBlockStorage[] storageArrays;
    
    @Inject(method = "onChunkUnload", at = @At("HEAD"))
    public void chunkUpdateFix(CallbackInfo ci) {
        // Reduz picos de lag ao descarregar mundos
        if (worldObj.playerEntities.isEmpty()) return;

        for (ClassInheritanceMultiMap<Entity> map : entityLists) {
            if (!map.isEmpty()) {
                for (EntityPlayer player : map.getByClass(EntityPlayer.class)) {
                    worldObj.updateEntityWithOptionalForce(player, false);
                }
            }
        }
    }
    
    @ModifyArg(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;relightBlock(III)V", ordinal = 0), index = 1)
    private int subtractOneFromY(int y) {
        return y - 1;
    }

    /**
     * @author Firefly
     * @reason Otimização extrema de acesso a blocos (Nível Lunar/Optifine)
     */
    @Overwrite
    public IBlockState getBlockState(BlockPos pos) {
        int y = pos.getY();
        // Acesso direto via bit-shift (y >> 4) é muito mais rápido que métodos
        if (y >= 0 && (y >> 4) < storageArrays.length) {
            ExtendedBlockStorage storage = storageArrays[y >> 4];
            if (storage != null) {
                return storage.get(pos.getX() & 15, y & 15, pos.getZ() & 15);
            }
        }
        return Blocks.air.getDefaultState();
    }
}
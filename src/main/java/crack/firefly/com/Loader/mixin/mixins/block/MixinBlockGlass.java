package crack.firefly.com.Loader.mixin.mixins.block;

import org.spongepowered.asm.mixin.Mixin;
import crack.firefly.com.System.mods.impl.ClearGlassMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

@Mixin(BlockGlass.class)
public class MixinBlockGlass extends Block {

    protected MixinBlockGlass(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        ClearGlassMod clearGlass = ClearGlassMod.getInstance();

        // Verifica se o mod e a configuração "Normal" estão ativos
        if (clearGlass.isToggled() && clearGlass.getNormalSetting().isToggled()) {
            // Pega o bloco que está encostado nessa face
            Block neighborBlock = worldIn.getBlockState(pos).getBlock();

            // Se o vizinho for o mesmo bloco (Vidro), NÃO desenha a face (retorna false)
            if (neighborBlock == this) {
                return false;
            }
        }

        // Caso contrário, usa o comportamento padrão do Minecraft
        return super.shouldSideBeRendered(worldIn, pos, side);
    }
}
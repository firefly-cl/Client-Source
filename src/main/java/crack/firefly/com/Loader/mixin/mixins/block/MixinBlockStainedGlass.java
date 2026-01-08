package crack.firefly.com.Loader.mixin.mixins.block;

import org.spongepowered.asm.mixin.Mixin;
import crack.firefly.com.System.mods.impl.ClearGlassMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

@Mixin(BlockStainedGlass.class)
public class MixinBlockStainedGlass extends Block {

    protected MixinBlockStainedGlass(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        ClearGlassMod clearGlass = ClearGlassMod.getInstance();

        // Verifica se o mod e a configuração "Stained" estão ativos
        if (clearGlass.isToggled() && clearGlass.getStainedSetting().isToggled()) {
            Block neighborBlock = worldIn.getBlockState(pos).getBlock();

            // Se o vizinho for o mesmo bloco (Vidro Colorido), NÃO desenha a face
            // Nota: Isso conecta vidros de cores diferentes também. 
            // Se quiser conectar apenas cores iguais, precisaria checar o Metadata/Color.
            if (neighborBlock == this) {
                return false;
            }
        }

        return super.shouldSideBeRendered(worldIn, pos, side);
    }
}
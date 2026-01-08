package crack.firefly.com.Loader.mixin.mixins.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import crack.firefly.com.Loader.Abstractions.ICachedHashcode;
import net.minecraft.block.properties.PropertyInteger;

@Mixin(PropertyInteger.class)
public class MixinPropertyInteger {

    @Overwrite
    public int hashCode() {
        return ((ICachedHashcode)((Object)this)).getCachedHashcode();
    }
}
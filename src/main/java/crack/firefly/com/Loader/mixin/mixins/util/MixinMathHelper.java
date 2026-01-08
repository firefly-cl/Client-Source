package crack.firefly.com.Loader.mixin.mixins.util;

import crack.firefly.com.System.mods.impl.FPSBoostMod;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public class MixinMathHelper {

    private static final float[] SIN_TABLE = new float[65536];

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = (float) Math.sin((double) i * Math.PI * 2.0 / 65536.0);
        }
    }

    /** @author Firefly */
    @Overwrite
    public static float sin(float value) {
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod != null && mod.isToggled() && mod.optimizedMath.isToggled()) {
            return SIN_TABLE[(int) (value * 10430.378f) & 65535];
        }
        return SIN_TABLE[(int) (value * 10430.378f) & 65535]; 
    }

    /** @author Firefly */
    @Overwrite
    public static float cos(float value) {
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod != null && mod.isToggled() && mod.optimizedMath.isToggled()) {
            return SIN_TABLE[(int) (value * 10430.378f + 16384.0f) & 65535];
        }
        return SIN_TABLE[(int) (value * 10430.378f + 16384.0f) & 65535];
    }
}
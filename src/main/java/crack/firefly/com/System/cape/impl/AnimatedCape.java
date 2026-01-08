package crack.firefly.com.System.cape.impl;

import java.util.List;

import crack.firefly.com.System.cape.CapeCategory;
import net.minecraft.util.ResourceLocation;

public class AnimatedCape extends Cape {

    private final List<ResourceLocation> frames;
    private final int delay;
    
    public AnimatedCape(String name, List<ResourceLocation> frames, int delay, CapeCategory category) {
        // CORREÇÃO: Passamos 'null' como segundo argumento, pois a textura é dinâmica
        super(name, null, category);
        this.frames = frames;
        this.delay = delay;
    }

    @Override
    public ResourceLocation getCape() {
        if (frames == null || frames.isEmpty()) return null;
        
        long index = (System.currentTimeMillis() / delay) % frames.size();
        
        return frames.get((int) index);
    }
    
    // Sobrescrevemos o getSample para usar o frame atual no menus também
    public ResourceLocation getSample() {
        return getCape(); 
    }
}
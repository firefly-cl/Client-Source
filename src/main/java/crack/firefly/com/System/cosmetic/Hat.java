package crack.firefly.com.System.cosmetic;

import net.minecraft.util.ResourceLocation;

public class Hat {
    private final String name;
    private final ResourceLocation model, texture;

    public Hat(String name, String m, String t) {
        this.name = name;
        // CORREÇÃO: "minecraft" é o domínio, e o caminho aponta para sua pasta Firefly exata
        this.model = new ResourceLocation("minecraft", "Firefly/cosmetics/" + m);
        this.texture = new ResourceLocation("minecraft", "Firefly/cosmetics/" + t);
    }

    public String getName() { return name; }
    public ResourceLocation getModel() { return model; }
    public ResourceLocation getTexture() { return texture; }
}
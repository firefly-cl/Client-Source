package crack.firefly.com.System.cosmetic.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBandana extends ModelBase {
    public ModelRenderer box;

    public ModelBandana() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.box = new ModelRenderer(this, 0, 0);
        // addBox(xOffset, yOffset, zOffset, width, height, depth, scale)
        // Posicionado para envolver a cabeça do jogador
        this.box.addBox(-4.5F, -8.2F, -4.5F, 9, 2, 9, 0.1F);
    }

    public void render(float scale) {
        this.box.render(scale);
    }
}
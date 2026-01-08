package crack.firefly.com.Support;

import crack.firefly.com.Loader.Abstractions.IMixinEntityPlayer;
import crack.firefly.com.System.mods.impl.skin3d.opengl.NativeImage;
import crack.firefly.com.System.mods.impl.skin3d.render.CustomizableModelPart;
import crack.firefly.com.System.mods.impl.skin3d.render.SolidPixelWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SkinUtils {
	
    private static final Map<String, ResourceLocation> headCache = new HashMap<>();
    // Caminho exato que você pediu para o pirata offline
    private static final ResourceLocation SFACE = new ResourceLocation("minecraft", "Firefly/heads/sface.png");

    // --- LÓGICA DO MENU (AVATAR) ---
    public static ResourceLocation getAvatar(String username) {
        if (username == null || username.isEmpty()) return SFACE;
        
        // Se já estiver no cache, retorna direto
        if (headCache.containsKey(username)) return headCache.get(username);

        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        // Criamos um nome único para a textura no gerenciador do Minecraft
        ResourceLocation rl = new ResourceLocation("firefly/heads/" + username.toLowerCase());
        
        // Caminho da pasta onde a skin será salva no seu PC
        File cacheFile = new File("Firefly/cache/heads/" + username + ".png");

        // URLs: GitHub (Pirata) ou Minotar (Original)
        // Se não tiver no GitHub, o Minotar entrega a skin original. Se falhar, usa o sface.png
        String githubUrl = "https://raw.githubusercontent.com/SEU_USER/SEU_REPO/main/skins/" + username + ".png";
        String mojangUrl = "https://minotar.net/helm/" + username + "/64.png";

        // ThreadDownloadImageData faz o download sem travar o jogo
        ThreadDownloadImageData downloadThread = new ThreadDownloadImageData(cacheFile, githubUrl, SFACE, null);
        
        // Registra a textura no Minecraft
        textureManager.loadTexture(rl, downloadThread);
        headCache.put(username, rl);

        return rl;
    }

    // --- LÓGICA DO JOGO (CAMADAS 3D) - NECESSÁRIO PARA NÃO DAR ERRO DE COMPILAÇÃO ---
    public static boolean hasCustomSkin(AbstractClientPlayer player) {
        return !DefaultPlayerSkin.getDefaultSkin(player.getUniqueID()).equals(player.getLocationSkin());
    }

    private static NativeImage getSkinTexture(AbstractClientPlayer player) {
        return getTexture(player.getLocationSkin());
    }
    
    private static NativeImage getTexture(ResourceLocation resource) {
        NativeImage skin = new NativeImage(64, 64, false);
        ITextureObject abstractTexture = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
        if(abstractTexture == null) return null;
        GlStateManager.bindTexture(abstractTexture.getGlTextureId());
        skin.downloadTexture(0, false);
        return skin;
    }
    
    public static boolean setup3dLayers(AbstractClientPlayer abstractClientPlayerEntity, IMixinEntityPlayer settings, boolean thinArms, ModelPlayer model) {
        if(!hasCustomSkin(abstractClientPlayerEntity)) return false;
        NativeImage skin = getSkinTexture(abstractClientPlayerEntity);
        if(skin == null) return false;
        
        CustomizableModelPart[] layers = new CustomizableModelPart[5];
        layers[0] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 0, 48, true, 0f);
        layers[1] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 0, 32, true, 0f);
        if(thinArms) {
            layers[2] = SolidPixelWrapper.wrapBox(skin, 3, 12, 4, 48, 48, true, -2.5f);
            layers[3] = SolidPixelWrapper.wrapBox(skin, 3, 12, 4, 40, 32, true, -2.5f);
        } else {
            layers[2] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 48, 48, true, -2.5f);
            layers[3] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 40, 32, true, -2.5f);
        }
        layers[4] = SolidPixelWrapper.wrapBox(skin, 8, 12, 4, 16, 32, true, -0.8f);
        settings.setupSkinLayers(layers);
        settings.setupHeadLayers(SolidPixelWrapper.wrapBox(skin, 8, 8, 8, 32, 0, false, 0.6f));
        skin.close();
        return true;
    }
}
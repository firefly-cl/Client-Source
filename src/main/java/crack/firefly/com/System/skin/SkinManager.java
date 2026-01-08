package crack.firefly.com.System.skin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class SkinManager {

    private static final SkinManager instance = new SkinManager();
    private final File skinFolder = new File(Minecraft.getMinecraft().mcDataDir, "Firefly/skins");
    private ResourceLocation customSkinLocation = null;
    private String currentSkinName = "";
    private boolean slimModel = false; 

    public SkinManager() {
        if (!skinFolder.exists()) skinFolder.mkdirs();
    }

    public static SkinManager getInstance() { return instance; }

    public void loadLocalSkin(File file) {
        if (file == null || !file.exists()) return;
        try {
            BufferedImage image = ImageIO.read(file);
            DynamicTexture dynamicTexture = new DynamicTexture(image);
            this.customSkinLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("firefly_skin_" + file.getName(), dynamicTexture);
            this.currentSkinName = file.getName();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<File> getSkinFiles() {
        List<File> skins = new ArrayList<>();
        File[] files = skinFolder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.getName().toLowerCase().endsWith(".png")) skins.add(f);
            }
        }
        return skins;
    }

    public void resetSkin() {
        this.customSkinLocation = null;
        this.currentSkinName = "";
    }

    public ResourceLocation getCustomSkin() { return customSkinLocation; }
    public String getCurrentSkinName() { return currentSkinName; }
    public boolean hasCustomSkin() { return customSkinLocation != null; }
    public boolean isSlimModel() { return slimModel; }
    public void setSlimModel(boolean slim) { this.slimModel = slim; }
}
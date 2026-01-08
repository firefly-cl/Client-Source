package crack.firefly.com.System.cape;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import crack.firefly.com.Firefly;
import crack.firefly.com.Console.FireflyConsole;
import crack.firefly.com.System.cape.impl.AnimatedCape;
import crack.firefly.com.System.cape.impl.Cape;
import crack.firefly.com.System.cape.impl.CustomCape;
import crack.firefly.com.System.cape.impl.NormalCape;
import crack.firefly.com.System.file.FileManager;
import crack.firefly.com.System.mods.impl.InternalSettingsMod;
import crack.firefly.com.Support.ImageUtils;
import crack.firefly.com.Support.file.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class CapeManager {

    private Minecraft mc = Minecraft.getMinecraft();
    
    private ArrayList<Cape> capes = new ArrayList<Cape>();
    private Cape currentCape;
    
    public CapeManager() {
        
        Firefly instance = Firefly.getInstance();
        FileManager fileManager = instance.getFileManager();
        File customCapeDir = fileManager.getCustomCapeDir();
        File cacheDir = fileManager.getCapeCacheDir();
        
        // Opção "None" (Sem capa)
        capes.add(new NormalCape("None", null, null, CapeCategory.ALL));
        
        // --- NOVAS CAPAS ANIMADAS ---
        addMyticalFireflyAnimation();
        addJesusFlyAnimation();
        
        // --- CAPA MANTIDA (CAT COUPLE) ---
        addCatCoupleAnimation();

        // --- CAPAS CUSTOMIZADAS (PASTA DO USUÁRIO) ---
        if (customCapeDir.exists() && customCapeDir.listFiles() != null) {
            for(File f : customCapeDir.listFiles()) {
                if (f.isDirectory()) {
                    loadAnimatedCapeFromFolder(f);
                } else if(FileUtils.isImageFile(f)) {
                    loadStaticCustomCape(f, cacheDir);
                }
            }
        }

        currentCape = getCapeByName(InternalSettingsMod.getInstance().getCapeConfigName());
        loadSamples(instance);
    }

    /**
     * Capa: Mytical Firefly
     */
    private void addMyticalFireflyAnimation() {
        try {
            List<ResourceLocation> frames = new ArrayList<>();
            for (int i = 0; i <= 28; i++) {
                String numberPart = String.format("%05d", i);
                String path = "Firefly/capefirefly/capa base_" + numberPart + ".png";
                ResourceLocation loc = new ResourceLocation(path);
                
                // CORREÇÃO: Força o upload da textura para a GPU agora, e não durante o jogo
                mc.getTextureManager().bindTexture(loc); 
                
                frames.add(loc);
            }
            
            if (!frames.isEmpty()) {
                capes.add(new AnimatedCape("Mytical Firefly", frames, 50, CapeCategory.CARTOON));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Firefly] Erro ao carregar Mytical Firefly.");
        }
    }

    /**
     * Capa: Jesus fly
     */
    private void addJesusFlyAnimation() {
        try {
            List<ResourceLocation> frames = new ArrayList<>();
            for (int i = 0; i <= 28; i++) {
                String numberPart = String.format("%05d", i);
                String path = "Firefly/capejesusfly/capa base_" + numberPart + ".png";
                ResourceLocation loc = new ResourceLocation(path);
                
                // CORREÇÃO: Força o upload da textura
                mc.getTextureManager().bindTexture(loc);
                
                frames.add(loc);
            }
            
            if (!frames.isEmpty()) {
                capes.add(new AnimatedCape("Jesus fly", frames, 50, CapeCategory.CARTOON));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Firefly] Erro ao carregar Jesus fly.");
        }
    }

    /**
     * Capa: Cat Couple
     */
    private void addCatCoupleAnimation() {
        try {
            List<ResourceLocation> frames = new ArrayList<>();
            for (int i = 0; i <= 49; i++) {
                String numberPart = String.format("%05d", i);
                String path = "Firefly/Capesanimated/capa base (0-00-09-04)_" + numberPart + ".png";
                ResourceLocation loc = new ResourceLocation(path);
                
                // CORREÇÃO: Força o upload da textura
                mc.getTextureManager().bindTexture(loc);
                
                frames.add(loc);
            }
            
            if (!frames.isEmpty()) {
                capes.add(new AnimatedCape("Cat Couple", frames, 50, CapeCategory.CARTOON));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // --- LÓGICA DE CARREGAMENTO DE PASTA (USUÁRIO) ---
    private void loadAnimatedCapeFromFolder(File folder) {
        try {
            List<ResourceLocation> frames = new ArrayList<>();
            File[] images = folder.listFiles();
            
            if (images == null || images.length == 0) return;

            List<File> sortedImages = Arrays.asList(images);
            Collections.sort(sortedImages, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return extractNumber(o1.getName()) - extractNumber(o2.getName());
                }
                int extractNumber(String name) {
                    String num = name.replaceAll("\\D", "");
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            });

            for (File img : sortedImages) {
                if (FileUtils.isImageFile(img)) {
                    BufferedImage bImg = ImageIO.read(img);
                    DynamicTexture texture = new DynamicTexture(bImg);
                    String textureId = "animated_" + folder.getName() + "_" + img.getName();
                    ResourceLocation location = mc.getTextureManager().getDynamicTextureLocation(textureId, texture);
                    
                    // CORREÇÃO: Força a textura dinâmica a ser enviada para a GPU
                    // Isso pode deixar o jogo carregar um pouquinho mais lento no início, 
                    // mas remove o lag durante a partida.
                    mc.getTextureManager().bindTexture(location); 
                    
                    frames.add(location);
                }
            }

            if (!frames.isEmpty()) {
                capes.add(new AnimatedCape(folder.getName(), frames, 50, CapeCategory.CUSTOM));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStaticCustomCape(File f, File cacheDir) {
        File file = new File(cacheDir, f.getName() + ".png");
        if(!file.exists()) {
            try {
                BufferedImage image = ImageIO.read(f);
                int width = image.getWidth();
                int height = image.getHeight();
                BufferedImage outputImage = ImageUtils.scissor(image, (int) (width * 0.03125), (int) (height * 0.0625), (int) (width * 0.125), (int) (height * 0.46875));
                ImageIO.write(ImageUtils.resize(outputImage, 1000, 1700), "png", file);
            } catch (IOException e) {
                FireflyConsole.error("Failed to load image", e);
                return;
            }
        }
        
        if(file.exists()) {
            try {
                DynamicTexture cape = new DynamicTexture(ImageIO.read(f));
                ResourceLocation loc = mc.getTextureManager().getDynamicTextureLocation(String.valueOf(f.getName().hashCode()), cape);
                
                // Opcional: pre-load para capas estáticas customizadas também
                mc.getTextureManager().bindTexture(loc);
                
                addCustomCape(f.getName().replace("." + FileUtils.getExtension(f), ""), file,
                        loc, CapeCategory.CUSTOM);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadSamples(Firefly instance) {
        for(Cape c : capes) {
            if(c instanceof NormalCape) {
                NormalCape cape = (NormalCape) c;
                if(cape.getSample() != null) instance.getNanoVGManager().loadImage(cape.getSample());
            }
            else if(c instanceof CustomCape) {
                CustomCape cape = (CustomCape) c;
                if(cape.getSample() != null) instance.getNanoVGManager().loadImage(cape.getSample());
            }
        }
    }
    
    private void addCustomCape(String name, File sample, ResourceLocation cape, CapeCategory category) {
        capes.add(new CustomCape(name, sample, cape, category));
    }
    
    public ArrayList<Cape> getCapes() {
        return capes;
    }
    
    public Cape getCurrentCape() {
        return currentCape;
    }

    public void setCurrentCape(Cape currentCape) {
        this.currentCape = currentCape;
        InternalSettingsMod.getInstance().setCapeConfigName(currentCape.getName());
    }

    public Cape getCapeByName(String name) {
        for(Cape c : capes) {
            if(c.getName().equals(name)) {
                return c;
            }
        }
        return getCapeByName("None");
    }
}
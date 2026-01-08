package crack.firefly.com.menus;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import crack.firefly.com.Firefly;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.GlUtils;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.other.DecelerateAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class GuiSplashScreen {

    private Minecraft mc = Minecraft.getMinecraft();
    private Framebuffer framebuffer;
    private Animation fadeAnimation;
    
    // --- CONFIGURAÇÕES ---
    private static final int TOTAL_FRAMES = 177; 
    private static final int FRAME_DELAY = 30; 
    
    // Tempo extra para ficar parado na última imagem (em ms)
    // Se o vídeo tem 5310ms, com +2000 ele vai ficar 2s parado na última imagem antes de fechar.
    private static final int HOLD_TIME = 2000; 
    
    private final List<ResourceLocation> frames = new ArrayList<>();

    public GuiSplashScreen() {
        try {
            for (int i = 1; i <= TOTAL_FRAMES; i++) {
                frames.add(new ResourceLocation("Firefly/splash/" + i + ".jpeg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar frames da Splash Screen!");
        }
    }
    
    public void draw() {
        
        framebuffer = GlUtils.createFrameBuffer(framebuffer);
        
        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = sr.getScaleFactor();
        NanoVGManager nvg = new NanoVGManager();
        
        // Calculamos o tempo total do vídeo
        int videoDuration = TOTAL_FRAMES * FRAME_DELAY;
        
        // O tempo da animação será o vídeo + o tempo de espera na última imagem
        if(fadeAnimation == null) {
            fadeAnimation = new DecelerateAnimation(videoDuration + HOLD_TIME, 1);
            fadeAnimation.setDirection(Direction.FORWARDS);
            fadeAnimation.reset();
        }
        
        mc.updateDisplay();
        
        long startTime = System.currentTimeMillis();
        
        while (!fadeAnimation.isDone(Direction.FORWARDS)) {
            
            framebuffer.framebufferClear();
            framebuffer.bindFramebuffer(true);
            
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();

            GlStateManager.color(0, 0, 0, 0);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            nvg.setupAndDraw(() -> {
                nvg.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK);
                
                long elapsedTime = System.currentTimeMillis() - startTime;
                
                // --- MUDANÇA AQUI ---
                // Calcula o frame atual baseado no tempo
                int frameIndex = (int) (elapsedTime / FRAME_DELAY);
                
                // Se o índice passar do total, trava no último frame (frames.size() - 1)
                if (frameIndex >= frames.size()) {
                    frameIndex = frames.size() - 1;
                }
                
                // Desenha o frame calculado
                if (!frames.isEmpty()) {
                    ResourceLocation currentFrame = frames.get(frameIndex);
                    nvg.drawImage(currentFrame, 0, 0, sr.getScaledWidth(), sr.getScaledHeight());
                }
            });
            
            framebuffer.unbindFramebuffer();
            framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
            
            GlUtils.setAlphaLimit(1);
            
            mc.updateDisplay();
        }
        
        Firefly.getInstance().setNanoVGManager(nvg);
    }
}
package crack.firefly.com.Loader.mixin.mixins.gui;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.mouse.MouseUtils;
import crack.firefly.com.Support.mouse.Scroll;
import crack.firefly.com.Support.search.SearchBox;
import crack.firefly.com.Support.SearchUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Mixin(GuiScreenResourcePacks.class)
public abstract class MixinGuiScreenResourcePacks extends GuiScreen {

    @Unique private final SearchBox searchBox = new SearchBox();
    @Unique private final Scroll scrollLeft = new Scroll(), scrollRight = new Scroll();
    @Unique private List<ResourcePackRepository.Entry> available = new ArrayList<>(), active = new ArrayList<>();
    @Unique private final List<String> favoritePacks = new ArrayList<>();
    @Unique private final Map<ResourcePackRepository.Entry, String> cleanNames = new HashMap<>(), cleanDescs = new HashMap<>();

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        updateList();
        for (GuiButton b : this.buttonList) b.visible = false;
        searchBox.setPosition(0, 0, 110, 16);
    }

    private void updateList() {
        // Puxa as entradas atuais do repositório do Minecraft
        this.active = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntries());
        this.available = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntriesAll());
        this.available.removeAll(this.active);
        
        cleanNames.clear(); 
        cleanDescs.clear();
        for (ResourcePackRepository.Entry e : mc.getResourcePackRepository().getRepositoryEntriesAll()) {
            cleanNames.put(e, EnumChatFormatting.getTextWithoutFormattingCodes(e.getResourcePackName()));
            cleanDescs.put(e, EnumChatFormatting.getTextWithoutFormattingCodes(e.getTexturePackDescription()));
        }
    }

    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        if (nvg == null) return;

        float w = width * 0.88f, h = height * 0.92f;
        float x = (width - w) / 2, y = (height - h) / 2;

        if (MouseUtils.isInside(mouseX, mouseY, x, y, w/2, h)) scrollLeft.onScroll();
        else if (MouseUtils.isInside(mouseX, mouseY, x + w/2, y, w/2, h)) scrollRight.onScroll();
        scrollLeft.onAnimation(); scrollRight.onAnimation();

        nvg.setupAndDraw(() -> {
            nvg.drawRoundedRect(x, y, w, h, 6, new Color(0, 0, 0, 135)); 
            nvg.drawOutlineRoundedRect(x, y, w, h, 6, 1.0f, new Color(255, 255, 255, 35));
            nvg.drawText("Firefly Client", x + 12, y + 14, Color.WHITE, 12, Fonts.SEMIBOLD);

            float colY = y + 25, listH = h - 65, colW = (w / 2) - 15;
            nvg.drawText("Available", x + 15, colY + 10, Color.WHITE, 15, Fonts.SEMIBOLD);
            searchBox.setPosition(x + 85, colY + 2, 110, 16); searchBox.draw(mouseX, mouseY, partialTicks);

            nvg.save(); nvg.scissor(x + 10, colY + 22, colW, listH);
            renderPacks(nvg, x + 10, colY + 22 + scrollLeft.getValue(), colW, available, true, mouseX, mouseY);
            nvg.restore();

            nvg.drawText("Active", x + w/2 + 15, colY + 10, Color.WHITE, 15, Fonts.SEMIBOLD);
            nvg.save(); nvg.scissor(x + w/2 + 5, colY + 22, colW, listH);
            renderPacks(nvg, x + w/2 + 5, colY + 22 + scrollRight.getValue(), colW, active, false, mouseX, mouseY);
            nvg.restore();

            drawGlassBtn(nvg, x + w - 85, y + h - 25, 75, 18, "Done", mouseX, mouseY);
            drawGlassBtn(nvg, x + w - 215, y + h - 25, 120, 18, "Open Folder", mouseX, mouseY);
        });
    }

    private void renderPacks(NanoVGManager nvg, float x, float y, float w, List<ResourcePackRepository.Entry> entries, boolean isAvail, int mx, int my) {
        float currY = y;
        if (isAvail && searchBox.getText().isEmpty()) {
            drawF(nvg, x, currY, w, "resourcepacks >"); currY += 21;
            drawF(nvg, x, currY, w, "!Favorite Packs"); currY += 21;
        }
        for (ResourcePackRepository.Entry entry : entries) {
            String name = cleanNames.get(entry);
            if (isAvail && !searchBox.getText().isEmpty() && !SearchUtils.isSimillar(name, searchBox.getText())) continue;
            boolean hov = MouseUtils.isInside(mx, my, x, currY, w, 32);
            boolean isFav = favoritePacks.contains(name);
            nvg.drawRoundedRect(x, currY, w, 32, 4, hov ? new Color(255, 255, 255, 20) : new Color(0, 0, 0, 70));
            entry.bindTexturePackIcon(mc.getTextureManager());
            int texID = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            nvg.drawRoundedImage(texID, x + 4, currY + 4, 24, 24, 3);
            nvg.drawText(name.length() > 22 ? name.substring(0, 20) + ".." : name, x + 35, currY + 8, Color.WHITE, 12, Fonts.MEDIUM);
            nvg.drawText(isFav ? LegacyIcon.STAR_FILL : LegacyIcon.STAR, x + w - 18, currY + 12, isFav ? Color.YELLOW : new Color(255, 255, 255, 30), 11, Fonts.LEGACYICON);
            currY += 34;
        }
        if (isAvail) scrollLeft.setMaxScroll(Math.max(0, (currY - y) - (height * 0.45f)));
        else scrollRight.setMaxScroll(Math.max(0, (currY - y) - (height * 0.45f)));
    }

    private void drawF(NanoVGManager nvg, float x, float y, float w, String n) {
        nvg.drawRoundedRect(x, y, w, 18, 2, new Color(255, 255, 255, 10));
        nvg.drawText(LegacyIcon.FOLDER, x + 5, y + 6, Color.WHITE, 10, Fonts.LEGACYICON);
        nvg.drawText(n, x + 20, y + 6, Color.WHITE, 10, Fonts.REGULAR);
    }

    private void drawGlassBtn(NanoVGManager nvg, float x, float y, float w, float h, String t, int mx, int my) {
        boolean hov = MouseUtils.isInside(mx, my, x, y, w, h);
        nvg.drawRoundedRect(x, y, w, h, 3, hov ? new Color(255, 255, 255, 50) : new Color(255, 255, 255, 20));
        nvg.drawOutlineRoundedRect(x, y, w, h, 3, 1.0f, new Color(255, 255, 255, 45));
        nvg.drawCenteredText(t, x + w/2, y + h/2 - 4, Color.WHITE, 11, Fonts.REGULAR);
    }

    @Override
    protected void mouseClicked(int mx, int my, int b) throws IOException {
        searchBox.mouseClicked(mx, my, b);
        float w = width * 0.88f, h = height * 0.92f, x = (width - w) / 2, y = (height - h) / 2, colY = y + 47;
        
        if (handleC(x + 10, colY + scrollLeft.getValue(), (w/2)-15, available, mx, my, true)) return;
        if (handleC(x + w/2 + 5, colY + scrollRight.getValue(), (w/2)-15, active, mx, my, false)) return;
        
        // Botão Done
        if (MouseUtils.isInside(mx, my, x + w - 85, y + h - 25, 75, 18)) { 
            mc.gameSettings.saveOptions(); // Garante o salvamento ao sair
            mc.displayGuiScreen(null); 
            mc.refreshResources(); 
        }
        
        if (MouseUtils.isInside(mx, my, x + w - 215, y + h - 25, 120, 18)) 
            java.awt.Desktop.getDesktop().open(mc.getResourcePackRepository().getDirResourcepacks());
        
        super.mouseClicked(mx, my, b);
    }

    private boolean handleC(float x, float y, float w, List<ResourcePackRepository.Entry> list, int mx, int my, boolean avail) {
        float cY = y; if (avail && searchBox.getText().isEmpty()) cY += 42;
        for (ResourcePackRepository.Entry e : new ArrayList<>(list)) {
            String n = cleanNames.get(e);
            
            // Clique na estrela (favoritos)
            if (MouseUtils.isInside(mx, my, x + w - 25, cY, 25, 32)) { 
                if (!favoritePacks.remove(n)) favoritePacks.add(n); 
                return true; 
            }
            
            // Lógica de Seleção de Textura (Onde a mágica acontece)
            if (MouseUtils.isInside(mx, my, x, cY, w - 25, 32)) {
                List<ResourcePackRepository.Entry> cur = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntries());
                
                if (cur.contains(e)) {
                    cur.remove(e);
                } else {
                    cur.add(0, e);
                }

                // 1. Atualiza o repositório atual (Efeito imediato)
                mc.getResourcePackRepository().setRepositories(cur);
                
                // 2. Sincroniza com as GameSettings (Efeito permanente)
                mc.gameSettings.resourcePacks.clear();
                for (ResourcePackRepository.Entry entry : cur) {
                    mc.gameSettings.resourcePacks.add(entry.getResourcePackName());
                }
                
                // 3. Salva no options.txt agora mesmo
                mc.gameSettings.saveOptions();
                
                updateList(); 
                return true;
            }
            cY += 34;
        }
        return false;
    }

    @Override protected void keyTyped(char c, int k) throws IOException { 
        searchBox.keyTyped(c, k); 
        if (k == 1) {
            mc.gameSettings.saveOptions(); // Salva se apertar ESC
            mc.displayGuiScreen(null); 
        }
    }
}
package crack.firefly.com.System.gui;

import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.ResourcePackRepository;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiCustomPackScreen extends GuiScreen {

    private final GuiScreen parent;
    private GuiTextField searchField;
    private List<ResourcePackRepository.Entry> available;
    private List<ResourcePackRepository.Entry> active;

    public GuiCustomPackScreen(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        mc.getResourcePackRepository().updateRepositoryEntriesAll();
        refreshLists();

        searchField = new GuiTextField(0, mc.fontRendererObj, width / 2 - 190, 48, 140, 16);
        searchField.setMaxStringLength(32);
        
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 30, 95, 20, "Done"));
        this.buttonList.add(new GuiButton(2, width / 2 + 5, height - 30, 95, 20, "Open Folder"));
    }

    private void refreshLists() {
        this.active = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntries());
        this.available = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntriesAll());
        this.available.removeAll(this.active);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        // AJUSTE AQUI: Como você pega o seu NanoVGManager? 
        // Exemplo: NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        NanoVGManager nvg = null; // Substitua pelo seu getter real

        if (nvg == null) {
            mc.fontRendererObj.drawStringWithShadow("Erro: NanoVGManager nao encontrado!", 10, 10, -1);
            super.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }

        nvg.setupAndDraw(() -> {
            // Fundo principal (Estilo Badlion)
            nvg.drawRect(30, 30, width - 60, height - 75, new Color(20, 20, 25, 245));
            // Divisoria central
            nvg.drawRect(width / 2.0f - 1, 40, 2, height - 90, new Color(255, 255, 255, 15));

            // Titulos usando drawText do NanoVGManager
            nvg.drawText("Available", 45, 45, Color.WHITE, 18, Fonts.SEMIBOLD);
            nvg.drawText("Active", width / 2.0f + 15, 45, Color.WHITE, 18, Fonts.SEMIBOLD);
            
            // Icone de busca
            nvg.drawText(LegacyIcon.SEARCH, width / 2.0f - 210, 50, Color.GRAY, 16, Fonts.LEGACYICON);
            
            // Renderizar as listas
            List<ResourcePackRepository.Entry> filtered = available.stream()
                .filter(e -> e.getResourcePackName().toLowerCase().contains(searchField.getText().toLowerCase()))
                .collect(Collectors.toList());

            renderList(nvg, 45, 75, width / 2 - 55, filtered, mouseX, mouseY, true);
            renderList(nvg, width / 2 + 15, 75, width - 45, active, mouseX, mouseY, false);
        });

        searchField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderList(NanoVGManager nvg, int x, int y, int xEnd, List<ResourcePackRepository.Entry> list, int mx, int my, boolean isAvailable) {
        int currentY = y;
        for (ResourcePackRepository.Entry entry : list) {
            if (currentY + 40 > height - 60) break;

            boolean hovered = mx > x && mx < xEnd && my > currentY && my < currentY + 36;
            
            // Card com cantos arredondados
            nvg.drawRoundedRect(x, currentY, xEnd - x, 36, 4, hovered ? new Color(50, 50, 60) : new Color(35, 35, 40));
            
            // Icone da textura usando o drawRoundedImage do seu manager
            entry.bindTexturePackIcon(mc.getTextureManager());
            int texID = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            nvg.drawRoundedImage(texID, x + 4, currentY + 4, 28, 28, 2);

            // Nomes e descricoes
            String name = entry.getResourcePackName();
            if(name.length() > 20) name = name.substring(0, 18) + "..";
            nvg.drawText(name, x + 38, currentY + 6, Color.WHITE, 16, Fonts.MEDIUM);
            
            String desc = entry.getTexturePackDescription();
            if(desc.length() > 30) desc = desc.substring(0, 28) + "..";
            nvg.drawText(desc, x + 38, currentY + 20, Color.GRAY, 14, Fonts.REGULAR);

            if (hovered) {
                String icon = isAvailable ? LegacyIcon.PLAY : LegacyIcon.X;
                nvg.drawText(icon, xEnd - 22, currentY + 14, Color.WHITE, 16, Fonts.LEGACYICON);
            }
            currentY += 40;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        searchField.mouseClicked(mouseX, mouseY, mouseButton);
        handleListClick(45, 75, width / 2 - 55, available, true, mouseX, mouseY);
        handleListClick(width / 2 + 15, 75, width - 45, active, false, mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void handleListClick(int x, int y, int xEnd, List<ResourcePackRepository.Entry> list, boolean toActive, int mx, int my) {
        int currentY = y;
        for (ResourcePackRepository.Entry entry : new ArrayList<>(list)) {
            if (mx > x && mx < xEnd && my > currentY && my < currentY + 36) {
                List<ResourcePackRepository.Entry> current = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntries());
                if (toActive) current.add(0, entry); else current.remove(entry);
                mc.getResourcePackRepository().setRepositories(current);
                refreshLists();
                break;
            }
            currentY += 40;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (searchField.textboxKeyTyped(typedChar, keyCode)) return;
        if (keyCode == 1) mc.displayGuiScreen(parent);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) { mc.displayGuiScreen(parent); mc.refreshResources(); }
        if (button.id == 2) {
            try { Desktop.getDesktop().open(mc.getResourcePackRepository().getDirResourcepacks()); } catch (Exception ignored) {}
        }
    }
}
package crack.firefly.com.menus.mod.Family.Providers;

import java.awt.Color;
import java.io.File;

import crack.firefly.com.Firefly;
import org.lwjgl.input.Keyboard;

import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.menus.mod.Family.Category;
import crack.firefly.com.System.color.ColorManager;
import crack.firefly.com.System.color.palette.ColorPalette;
import crack.firefly.com.System.file.FileManager;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.ModManager;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.System.profile.Profile;
import crack.firefly.com.System.profile.ProfileIcon;
import crack.firefly.com.System.profile.ProfileManager;
import crack.firefly.com.System.profile.ProfileType;
import crack.firefly.com.ui.comp.Providers.field.CompTextBox;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.other.SmoothStepAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;

public class ProfileFamily extends Category {

    // Adicionado referência ao menus pai para pegar largura da sidebar
    private final GuiModMenu modMenu;
    
    private ProfileType currentType;
    private Animation profileAnimation;
    private boolean openProfile;
    private ProfileIcon currentIcon;
    private CompTextBox nameBox = new CompTextBox();
    private CompTextBox serverIpBox = new CompTextBox();
    
    // Cores locais
    private final Color GREEN = GuiModMenu.FIREFLY_GREEN;
    private final Color CARD_BG = new Color(0, 0, 0, 100);
    
    public ProfileFamily(GuiModMenu parent) {
        super(parent, TranslateText.PROFILE, LegacyIcon.EDIT, false, true);
        this.modMenu = parent; // Inicializa o modMenu
    }

    @Override
    public void initGui() {
        currentType = ProfileType.ALL;
        currentIcon = ProfileIcon.COMMAND;
        openProfile = false;
        profileAnimation = new SmoothStepAnimation(260, 1.0);
        profileAnimation.setValue(1.0);
    }
    
    @Override
    public void initCategory() {
        scroll.resetAll();
        openProfile = false;
        profileAnimation = new SmoothStepAnimation(260, 1.0);
        profileAnimation.setValue(1.0);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        ProfileManager profileManager = instance.getProfileManager();
        ColorManager colorManager = instance.getColorManager();
        ColorPalette palette = colorManager.getPalette();
        
        // CORREÇÃO: Calcular a área de conteúdo descontando a sidebar
        float sidebarWidth = modMenu.getSidebarWidth();
        float startX = this.getX() + sidebarWidth; 
        float contentWidth = this.getWidth() - sidebarWidth;
        
        int offsetX = 0;
        float offsetY = 13;
        int index = 1;
        
        profileAnimation.setDirection(openProfile ? Direction.BACKWARDS : Direction.FORWARDS);
        
        if(profileAnimation.isDone(Direction.FORWARDS)) {
            nameBox.setText("");
            serverIpBox.setText("");
            this.setCanClose(true);
        }
        
        // --- CENA PRINCIPAL (LISTA DE PERFIS) ---
        nvg.save();
        nvg.translate((float) -(600 - (profileAnimation.getValue() * 600)), 0);
        
        // 1. Abas de Tipo (All, Favorite)
        for(ProfileType t : ProfileType.values()) {
            
            float textWidth = nvg.getTextWidth(t.getName(), 9, Fonts.MEDIUM);
            boolean isCurrentCategory = t.equals(currentType);
            
            t.getBackgroundAnimation().setAnimation(isCurrentCategory ? 1.0F : 0.0F, 16);
            
            Color bgColor = ColorUtils.applyAlpha(GREEN, (int)(t.getBackgroundAnimation().getValue() * 255));
            Color textColor = t.getTextColorAnimation().getColor(isCurrentCategory ? Color.WHITE : new Color(180, 180, 180), 20);

            // Substituído this.getX() por startX
            if(isCurrentCategory) {
                nvg.drawRoundedRect(startX + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6, GREEN);
            } else {
                nvg.drawRoundedRect(startX + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6, new Color(255,255,255, 20));
            }
            
            nvg.drawText(t.getName(), startX + 15 + offsetX + ((textWidth + 20) - textWidth) / 2, this.getY() + offsetY + 1.5F, textColor, 9, Fonts.MEDIUM);
            
            offsetX += textWidth + 28;
        }
        
        offsetX = 0;
        offsetY = offsetY + 30; 
        
        // SCROLL START
        nvg.save();
        // Adicionando um Scissor para não vazar o texto por cima das abas
        nvg.scissor(startX, this.getY() + offsetY - 5, contentWidth, this.getHeight() - offsetY);
        nvg.translate(0, scroll.getValue());
        
        int itemsCount = 0;
        
        for(Profile p : profileManager.getProfiles()) {
            
            if(filter(p)) {
                continue;
            }
            
            // Substituído this.getX() por startX em todos os desenhos abaixo
            
            // Fundo do Card
            nvg.drawRoundedRect(startX + 15 + offsetX, this.getY() + offsetY, 123, 46, 6, CARD_BG);
            nvg.drawOutlineRoundedRect(startX + 15 + offsetX, this.getY() + offsetY, 123, 46, 6, 1f, new Color(255,255,255, 15));
            
            // Ícone do Perfil
            if(p.getIcon() != null) {
                nvg.drawRoundedImage(p.getIcon().getIcon(), startX + 15 + offsetX + 6, this.getY() + offsetY + 6, 34, 34, 6);
            }
            
            // Nome do Perfil
            if(p.getName() != "") {
                nvg.drawText(nvg.getLimitText(p.getName(), 10, Fonts.MEDIUM, 68), startX + 62 + offsetX, this.getY() + offsetY + 9, Color.WHITE, 10, Fonts.MEDIUM);
            }
            
            if(p.getId() == 999) {
                // Botão de Adicionar (+)
                nvg.drawCenteredText(LegacyIcon.PLUS, startX + offsetX + 14.5F + (123 / 2), this.getY() + offsetY + 13, GREEN, 20, Fonts.LEGACYICON);
            } else {
                // Botões de Ação
                Color starColor = p.getType().equals(ProfileType.FAVORITE) ? new Color(255, 215, 0) : new Color(150, 150, 150);
                nvg.drawText(p.getType().equals(ProfileType.FAVORITE) ? LegacyIcon.STAR_FILL : LegacyIcon.STAR, 
                        startX + 62 + offsetX, this.getY() + 29 + offsetY, starColor, 11, Fonts.LEGACYICON);
                
                nvg.drawText(LegacyIcon.TRASH, startX + 62 + 18 + offsetX, this.getY() + 29 + offsetY, new Color(255, 80, 80), 11, Fonts.LEGACYICON);
            }
            
            offsetX += 133;
            
            if(index % 3 == 0) {
                offsetX = 0;
                offsetY += 56;
            }
            
            index++;
            itemsCount++;
        }
        
        nvg.restore(); // SCROLL END
        nvg.restore(); // SCENE END
        
        int rows = (int) Math.ceil((double)itemsCount / 3.0);
        float totalHeight = rows * 56 + 60;
        scroll.setMaxScroll(Math.max(0, totalHeight - (this.getHeight() - 50)));
        
        // --- CENA DE ADICIONAR PERFIL ---
        
        nvg.save();
        nvg.translate((float) (profileAnimation.getValue() * 600), 0);
        
        offsetY = 15;
        offsetX = 0;
        
        // Fundo Escuro (Agora cobre apenas a área de conteúdo, respeitando a sidebar)
        nvg.drawRoundedRect(startX, this.getY(), contentWidth, this.getHeight(), 10, new Color(0,0,0, 220));
        
        // Ajuste de coordenadas para startX
        nvg.drawText(TranslateText.ADD_PROFILE.getText(), startX + 20, this.getY() + offsetY + 10, GREEN, 14, Fonts.SEMIBOLD);
        
        nvg.drawText(TranslateText.ICON.getText(), startX + 20, this.getY() + offsetY + 40, Color.GRAY, 11, Fonts.REGULAR);
        
        for(ProfileIcon icon : ProfileIcon.values()) {
            boolean isSelected = currentIcon.equals(icon);
            icon.getAnimation().setAnimation(isSelected ? 1.0F : 0.0F, 16);
            
            nvg.drawRoundedImage(icon.getIcon(), startX + 22 + offsetX, this.getY() + offsetY + 60, 32, 32, 6);
            
            if (isSelected) {
                nvg.drawOutlineRoundedRect(startX + 22 + offsetX, this.getY() + offsetY + 60, 32, 32, 6, 2f, GREEN);
            }
            
            offsetX += 45;
        }
        
        nvg.drawText(TranslateText.NAME.getText(), startX + 20, this.getY() + offsetY + 110, Color.GRAY, 11, Fonts.REGULAR);
        
        nameBox.setPosition(startX + 22, this.getY() + 145, 150, 22);
        nameBox.draw(mouseX, mouseY, partialTicks);
        
        nvg.drawText(TranslateText.SERVER_IP.getText() + " (Auto-Join)", startX + 200, this.getY() + offsetY + 110, Color.GRAY, 11, Fonts.REGULAR);
        
        serverIpBox.setPosition(startX + 202, this.getY() + 145, 150, 22);
        serverIpBox.draw(mouseX, mouseY, partialTicks);
        
        // Botão CREATE
        float btnW = 120;
        float btnH = 26;
        float btnX = startX + contentWidth - btnW - 20; // Alinhado à direita do conteúdo
        float btnY = this.getY() + this.getHeight() - btnH - 20;
        
        boolean hoverCreate = MouseUtils.isInside(mouseX, mouseY, btnX, btnY, btnW, btnH);
        
        nvg.drawRoundedRect(btnX, btnY, btnW, btnH, 6, hoverCreate ? GREEN.brighter() : GREEN);
        nvg.drawCenteredText(TranslateText.CREATE.getText(), btnX + btnW/2, btnY + 15, Color.WHITE, 11, Fonts.SEMIBOLD);
        
        if(MouseUtils.isInside(mouseX, mouseY, startX + 20, btnY, 60, 20)) {
            nvg.drawText("Cancel", startX + 20, btnY + 15, Color.WHITE, 11, Fonts.REGULAR);
        } else {
            nvg.drawText("Cancel", startX + 20, btnY + 15, Color.GRAY, 11, Fonts.REGULAR);
        }

        nvg.restore();
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        
        Firefly instance = Firefly.getInstance();
        ProfileManager profileManager = instance.getProfileManager();
        NanoVGManager nvg = instance.getNanoVGManager();
        ModManager modManager = instance.getModManager();
        FileManager fileManager = instance.getFileManager();
        
        // CORREÇÃO: Mesmas variáveis de deslocamento para o clique funcionar no lugar certo
        float sidebarWidth = modMenu.getSidebarWidth();
        float startX = this.getX() + sidebarWidth;
        float contentWidth = this.getWidth() - sidebarWidth;
        
        int offsetX = 0;
        float offsetY = 13;
        int index = 1;
        
        if(openProfile) {
            float dialogOffsetY = 15;
            offsetX = 0;

            for(ProfileIcon icon : ProfileIcon.values()) {
                if(MouseUtils.isInside(mouseX, mouseY, startX + 22 + offsetX, this.getY() + dialogOffsetY + 60, 32, 32) && mouseButton == 0) {
                    currentIcon = icon;
                }
                offsetX += 45;
            }
            
            nameBox.mouseClicked(mouseX, mouseY, mouseButton);
            serverIpBox.mouseClicked(mouseX, mouseY, mouseButton);
            
            float btnW = 120;
            float btnH = 26;
            float btnX = startX + contentWidth - btnW - 20;
            float btnY = this.getY() + this.getHeight() - btnH - 20;
            
            if(MouseUtils.isInside(mouseX, mouseY, btnX, btnY, btnW, btnH) && mouseButton == 0) {
                if(!nameBox.getText().isEmpty()) {
                    String serverIp = !serverIpBox.getText().isEmpty() ? serverIpBox.getText() : "";
                    profileManager.save(new File(fileManager.getProfileDir(), nameBox.getText() + ".json"), serverIp, ProfileType.ALL, currentIcon);
                    profileManager.loadProfiles(false);
                    openProfile = false;
                }
            }
            
            if(MouseUtils.isInside(mouseX, mouseY, startX + 20, btnY, 60, 20) && mouseButton == 0) {
                openProfile = false;
            }
            
        } else {
            
            // Abas
            for(ProfileType t : ProfileType.values()) {
                float textWidth = nvg.getTextWidth(t.getName(), 9, Fonts.MEDIUM);
                if(MouseUtils.isInside(mouseX, mouseY, startX + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16) && mouseButton == 0) {
                    currentType = t;
                }
                offsetX += textWidth + 28;
            }
            
            offsetX = 0;
            offsetY = offsetY + 30;
            
            for(Profile p : profileManager.getProfiles()) {
                
                if(filter(p)) continue;
                
                float realY = this.getY() + offsetY + scroll.getValue();
                
                if(realY > this.getY() && realY < this.getY() + this.getHeight()) {
                    
                    if(mouseButton == 0) {
                        // Ajustado para startX
                        boolean favorite = MouseUtils.isInside(mouseX, mouseY, startX + 61 + offsetX, realY + 29, 13, 13);
                        boolean delete = MouseUtils.isInside(mouseX, mouseY, startX + 62 + 18 + offsetX, realY + 29, 13, 13);
                        boolean inside = MouseUtils.isInside(mouseX, mouseY, startX + 15 + offsetX, realY, 123, 46);
                        
                        if(inside) {
                            if(p.getId() == 999) {
                                openProfile = true;
                                this.setCanClose(false);
                            } else if(!favorite && !delete) {
                                modManager.disableAll();
                                profileManager.load(p.getJsonFile());
                            }
                        }
                        
                        if(p.getId() != 999) {
                            if(favorite) {
                                p.setType(p.getType().equals(ProfileType.FAVORITE) ? ProfileType.ALL : ProfileType.FAVORITE);
                                profileManager.save(p.getJsonFile(), p.getServerIp(), p.getType(), p.getIcon());
                            }
                            if(delete) {
                                profileManager.delete(p);
                            }
                        }
                    }
                }
                
                offsetX += 133;
                if(index % 3 == 0) {
                    offsetX = 0;
                    offsetY += 56;
                }
                index++;
            }
        }
    }
    
    @Override
    public void keyTyped(char typedChar, int keyCode) {
        
        if(openProfile) {
            nameBox.keyTyped(typedChar, keyCode);
            serverIpBox.keyTyped(typedChar, keyCode);
            
            if(keyCode == Keyboard.KEY_ESCAPE) {
                openProfile = false;
            }
        } else {
            scroll.onKey(keyCode);
        }
    }
    
    private boolean filter(Profile p) {
        if(currentType.equals(ProfileType.FAVORITE) && !p.getType().equals(ProfileType.FAVORITE)) {
            return true;
        }
        return false;
    }
}
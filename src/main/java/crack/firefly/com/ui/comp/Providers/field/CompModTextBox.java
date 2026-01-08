package crack.firefly.com.ui.comp.Providers.field;

import java.awt.Color;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.mods.settings.impl.TextSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.Support.TimerUtils;
import net.minecraft.util.ResourceLocation;

public class CompModTextBox extends CompTextBoxBase {

    private TextSetting setting;
    private TimerUtils timer = new TimerUtils();

    // Textura de fundo (opcional, se quiser manter o visual original)
    private static final ResourceLocation SEARCH_BG =
            new ResourceLocation("Firefly", "modmenuicons/search.png");

    public CompModTextBox(float x, float y, float width, float height, TextSetting setting) {
        super(x, y, width, height);
        this.setting = setting;
        this.setText(setting.getText());
    }

    public CompModTextBox(TextSetting setting) {
        super(0, 0, 100, 16); // Definindo um tamanho padrão inicial
        this.setting = setting;
        this.setText(setting.getText());
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        float height = this.getHeight();
        int selectionEnd = this.getSelectionEnd();
        int cursorPosition = this.getCursorPosition();
        String text = this.getText();
        boolean focused = this.isFocused();

        float addX = 0;
        float halfHeight = 9f; // Tamanho de fonte fixo para melhor legibilidade

        // Lógica de Scroll do texto dentro da caixa
        String resultText = "";
        for (char c : text.toCharArray()) {
            resultText += c;
            if (nvg.getTextWidth(resultText, halfHeight, Fonts.REGULAR) + 10 > this.getWidth()) {
                addX = this.getWidth() - nvg.getTextWidth(resultText, halfHeight, Fonts.REGULAR) - 10;
            }
        }

        // Desenho do fundo (Preto levemente mais claro que o fundo do menu)
        nvg.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3, new Color(255, 255, 255, 15));
        nvg.drawOutlineRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3, 0.5f, focused ? Color.WHITE : new Color(255, 255, 255, 30));

        nvg.save();
        nvg.scissor(this.getX() + 2, this.getY(), this.getWidth() - 4, this.getHeight());

        // Desenha Seleção de Texto
        if (cursorPosition != selectionEnd) {
            int start = Math.min(selectionEnd, cursorPosition);
            int end = Math.max(selectionEnd, cursorPosition);
            float selectionWidth = nvg.getTextWidth(text.substring(start, end), halfHeight, Fonts.REGULAR);
            float offset = nvg.getTextWidth(text.substring(0, start), halfHeight, Fonts.REGULAR);
            
            nvg.drawRect(this.getX() + 5 + offset + addX, this.getY() + 3, selectionWidth, this.getHeight() - 6, new Color(0, 120, 215, 160));
        }

        // Desenha o Texto (Sempre Branco para fundo escuro)
        nvg.drawText(text, this.getX() + 5 + addX, this.getY() + (this.getHeight() / 2) - 4, Color.WHITE, halfHeight, Fonts.REGULAR);

        // Cursor Piscante
        if (focused && timer.delay(600)) {
            float position = nvg.getTextWidth(text.substring(0, cursorPosition), halfHeight, Fonts.REGULAR);
            nvg.drawRect(this.getX() + 5 + addX + position, this.getY() + 4, 0.7F, this.getHeight() - 8, Color.WHITE);
            if (timer.delay(1200)) timer.reset();
        }

        nvg.restore();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        // Atualiza a config no mesmo instante que digita
        if (this.isFocused()) {
            setting.setText(this.getText());
        }
    }
}
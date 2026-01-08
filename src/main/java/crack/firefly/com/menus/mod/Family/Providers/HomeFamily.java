package crack.firefly.com.menus.mod.Family.Providers;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.menus.mod.Family.Category;
import crack.firefly.com.System.color.ColorManager;
import crack.firefly.com.System.color.palette.ColorPalette;
import crack.firefly.com.System.color.palette.ColorType;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;

public class HomeFamily extends Category {

	// O Scroll e outras variáveis foram removidas, pois não são mais usadas
	// O construtor é mantido
	public HomeFamily(GuiModMenu parent) {
		super(parent, TranslateText.HOME, LegacyIcon.HOME, false, false);
	}
	
	// initGui mantido para compatibilidade, mas limpo
	@Override
	public void initGui() {
		// Apenas a verificação do Discord é mantida caso seja útil em outro lugar
		Firefly.getInstance().getDiscordStats().check();
	}

	// drawScreen agora só chama o método para desenhar o relógio
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Firefly instance = Firefly.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorManager colorManager = instance.getColorManager();
		ColorPalette palette = colorManager.getPalette();

		// Chamada para desenhar o relógio centralizado
		drawCentralTime(nvg, palette);
	}
	
	/**
	 * Desenha o relógio (horas e minutos) centralizado na área da categoria.
	 */
	private void drawCentralTime(NanoVGManager nvg, ColorPalette palette) {
		// 1. Obter a hora atual
		Calendar calendar = Calendar.getInstance();
		// Formato "HH:mm"
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String timeString = formatter.format(calendar.getTime());
		
		Color textColor = palette.getFontColor(ColorType.DARK);
		
		// 2. Definir a posição (Centralizada)
		
		// Centraliza o texto na horizontal: (Posição X do ModMenu + Largura do ModMenu) / 2
		float centerX = this.getX() + (this.getWidth() / 2.0F);
		// Centraliza o texto na vertical: (Posição Y do ModMenu + Altura do ModMenu) / 2
		float centerY = this.getY() + (this.getHeight() / 2.0F); 

		// 3. Desenhar o texto da hora
		// Usa drawCenteredText para centralizar o texto na coordenada (centerX, centerY)
		nvg.drawCenteredText(timeString, centerX, centerY, textColor, 50F, Fonts.SEMIBOLD); // Tamanho da fonte aumentado
		
		// 4. Desenhar o ícone de relógio acima (opcional)
		nvg.drawCenteredText(LegacyIcon.CLOCK, centerX, centerY - 60, textColor, 40F, Fonts.LEGACYICON);
	}


	// mouseClicked completamente limpo, pois não há botões ou links
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		// Não faz nada
	}
}
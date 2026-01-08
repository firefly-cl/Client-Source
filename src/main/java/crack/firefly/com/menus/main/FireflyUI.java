package crack.firefly.com.menus.main;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.main.Providers.Background;
import crack.firefly.com.menus.main.Providers.Discontinued;
import crack.firefly.com.menus.main.Providers.FireflyStage;
import crack.firefly.com.menus.main.Providers.Updater;

import crack.firefly.com.System.event.impl.EventRenderNotification;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.System.profile.mainmenu.impl.CustomBackground;
import crack.firefly.com.System.profile.mainmenu.impl.DefaultBackground;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.other.DecelerateAnimation;
import crack.firefly.com.System.color.Theme;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class FireflyUI extends GuiScreen {

	private LandingView currentScene;
	private ArrayList<LandingView> scenes = new ArrayList<>();
	private boolean soundPlayed = false;
	private Animation fadeIconAnimation, fadeBackgroundAnimation;

	public FireflyUI() {
		Firefly instance = Firefly.getInstance();

		scenes.add(new FireflyStage(this));
		scenes.add(new Background(this));
		scenes.add(new Updater(this));
		scenes.add(new Discontinued(this));
		
		if (instance.isFirstLogin()) {
			instance.getColorManager().setTheme(Theme.DARK);
			instance.createFirstLoginFile();
			currentScene = getSceneByClass(FireflyStage.class);
		} else if (instance.getSoar8Released()) {
			currentScene = getSceneByClass(Discontinued.class);
		} else if (instance.getUpdateNeeded()) {
			currentScene = getSceneByClass(Updater.class);
		} else {
			currentScene = getSceneByClass(FireflyStage.class);
		}
	}

	@Override
	public void initGui() {
		if (currentScene != null) currentScene.initGui();
	}

	@Override
	public void onGuiClosed() {
		if (currentScene != null) currentScene.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		Firefly instance = Firefly.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();

		// 1. DESENHAR O FUNDO (ESTÁTICO - SEM BUGS)
		nvg.setupAndDraw(() -> {
			drawBackground(sr, instance, nvg);
		});

		// 2. DESENHAR A CENA ATUAL (BOTÕES)
		if (currentScene != null) currentScene.drawScreen(mouseX, mouseY, partialTicks);

		// 3. INTRO / SPLASH SCREEN
		if (fadeBackgroundAnimation == null || !fadeBackgroundAnimation.isDone(Direction.FORWARDS)) {
			nvg.setupAndDraw(() -> drawSplashScreen(sr, nvg));
		}

		// 4. NOTIFICAÇÕES
		nvg.setupAndDraw(() -> new EventRenderNotification().call());

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawBackground(ScaledResolution sr, Firefly instance, NanoVGManager nvg) {
		float drawX = -21;
		float drawY = 0;
		float drawWidth = sr.getScaledWidth() + 21;
		float drawHeight = sr.getScaledHeight() + 20;

		// Lógica para usar a imagem de Natal como fundo fixo se estiver ativo
		if (FireflyStage.isChristmas) {
			// Usa a panorama_0 como imagem principal de fundo
			ResourceLocation natalImg = new ResourceLocation("soar/main/background.png");
			nvg.drawImage(natalImg, drawX, drawY, drawWidth, drawHeight);
		} else {
			// Lógica padrão do seu client
			crack.firefly.com.System.profile.mainmenu.impl.Background currentBackground = instance.getProfileManager().getBackgroundManager().getCurrentBackground();
			if (currentBackground instanceof DefaultBackground) {
				nvg.drawImage(((DefaultBackground) currentBackground).getImage(), drawX, drawY, drawWidth, drawHeight);
			} else if (currentBackground instanceof CustomBackground) {
				// Para CustomBackground, como o NanoVG usa ResourceLocation, a implementação depende de como você carrega o File
				// Se der erro aqui, mantenha apenas o DefaultBackground
			}
		}

		// Copyright e Versão
		String copyright = "Copyright Mojang AB. Do not distribute!";
		nvg.drawText(copyright, sr.getScaledWidth() - (nvg.getTextWidth(copyright, 9, Fonts.REGULAR)) - 4,
				sr.getScaledHeight() - 12, Color.WHITE, 9, Fonts.REGULAR);
		nvg.drawText("Firefly Client v0.0.1" + instance.getVersion(), 4, sr.getScaledHeight() - 12, Color.WHITE, 9, Fonts.REGULAR);
	}

	private void drawSplashScreen(ScaledResolution sr, NanoVGManager nvg) {
		if(fadeIconAnimation == null) {
			fadeIconAnimation = new DecelerateAnimation(100, 1);
			fadeIconAnimation.setDirection(Direction.FORWARDS);
			fadeIconAnimation.reset();
		}

		if(fadeIconAnimation != null) {
			if(fadeIconAnimation.isDone(Direction.FORWARDS) && fadeBackgroundAnimation == null) {
				fadeBackgroundAnimation = new DecelerateAnimation(500, 1);
				fadeBackgroundAnimation.setDirection(Direction.FORWARDS);
				fadeBackgroundAnimation.reset();
			}

			nvg.drawRect(0,0,sr.getScaledWidth(), sr.getScaledHeight(),
					new Color(0,0,0, fadeBackgroundAnimation != null ? (int)(255 - (fadeBackgroundAnimation.getValue()*255)) : 255));

			nvg.drawCenteredText(LegacyIcon.SOAR, sr.getScaledWidth()/2, sr.getScaledHeight()/2 - (nvg.getTextHeight(LegacyIcon.SOAR,130, Fonts.LEGACYICON)/2) - 1,
					new Color(255,255,255,(int)(255 - (fadeIconAnimation.getValue()*255))), 130, Fonts.LEGACYICON);
		}
	}

	// --- MÉTODOS OBRIGATÓRIOS PARA COMPILAR SEM ERROS ---

	public LandingView getSceneByClass(Class<? extends LandingView> clazz) {
		for(LandingView s : scenes) if(s.getClass().equals(clazz)) return s;
		return null;
	}

	public void setCurrentScene(LandingView currentScene) {
		if(this.currentScene != null) this.currentScene.onSceneClosed();
		this.currentScene = currentScene;
		if(this.currentScene != null) this.currentScene.initScene();
	}

	public Color getBackgroundColor() {
		return new Color(30, 30, 30, 200);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (currentScene != null) currentScene.mouseClicked(mouseX, mouseY, mouseButton);
		try { super.mouseClicked(mouseX, mouseY, mouseButton); } catch (IOException e) {}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (currentScene != null) currentScene.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (currentScene != null) currentScene.keyTyped(typedChar, keyCode);
	}
}
package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.Loader.Abstractions.IMixinMinecraft;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.event.impl.EventSwitchTexture;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.ColorUtils;
import net.minecraft.client.gui.Gui; // Importação necessária
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class PackDisplayMod extends HUDMod {

	private IResourcePack pack;
	private ResourceLocation currentPack;
	private final ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
	
	private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, true);
	private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
	private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);

	public PackDisplayMod() {
		super(TranslateText.PACK_DISPLAY, TranslateText.PACK_DISPLAY_DESCRIPTION);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		this.loadTexture();
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (pack == null) pack = this.getCurrentPack();
		NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
		float scale = getScale();

		String name = ColorUtils.removeColorCode(pack.getPackName()).replace(".zip", "");
		
		float iconSize = 12 * scale;
		float padding = 4 * scale;
		float textWidth = mc.fontRendererObj.getStringWidth(name) * scale;
		
		float w = iconSize + textWidth + (padding * 3);
		float h = iconSize + (padding * 2);
		
		this.setWidth((int) w);
		this.setHeight((int) h);

		// 1. Fundo NanoVG
		nvg.setupAndDraw(() -> {
			nvg.drawRoundedRect(getX(), getY(), w, h, rounded.isToggled() ? 4 : 0, bgColor.getColor());
		});

		// 2. Ícone da Textura (CORRIGIDO: Chamando via Gui.draw...)
		if (currentPack != null) {
			mc.getTextureManager().bindTexture(currentPack);
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.enableBlend();
			// Mudança aqui: Gui.drawModalRect...
			Gui.drawModalRectWithCustomSizedTexture((int)(getX() + padding), (int)(getY() + padding), 0, 0, (int)iconSize, (int)iconSize, iconSize, iconSize);
		}

		// 3. Texto Minecraft
		GL11.glPushMatrix();
		GL11.glTranslatef(getX() + iconSize + (padding * 1.5f), getY() + (h / 2f) - (4 * scale), 0);
		GL11.glScalef(scale, scale, 1);
		mc.fontRendererObj.drawStringWithShadow(name, 0, 0, textColor.getColor().getRGB());
		GL11.glPopMatrix();
	}

	@EventTarget
	public void onSwitchTexture(EventSwitchTexture event) {
		pack = this.getCurrentPack();
		this.loadTexture();
	}

	private void loadTexture() {
		DynamicTexture dynamicTexture;
		try {
			dynamicTexture = new DynamicTexture(getCurrentPack().getPackImage());
		} catch (Exception e) {
			try {
				dynamicTexture = new DynamicTexture(((IMixinMinecraft)mc).getMcDefaultResourcePack().getPackImage());
			} catch (IOException e1) {
				dynamicTexture = TextureUtil.missingTexture;
			}
		}
		this.currentPack = mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamicTexture);
	}

	private IResourcePack getCurrentPack() {
		List<ResourcePackRepository.Entry> entries = resourcePackRepository.getRepositoryEntries();
		if (!entries.isEmpty()) {
			return entries.get(entries.size() - 1).getResourcePack();
		}
		return ((IMixinMinecraft)mc).getMcDefaultResourcePack();
	}
}
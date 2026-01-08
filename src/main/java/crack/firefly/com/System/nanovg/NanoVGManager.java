package crack.firefly.com.System.nanovg;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.Console.FireflyConsole;
import crack.firefly.com.System.nanovg.asset.AssetManager;
import crack.firefly.com.System.nanovg.font.Font;
import crack.firefly.com.System.nanovg.font.FontManager;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class NanoVGManager {

	private Minecraft mc = Minecraft.getMinecraft();
	private HashMap<Integer, NVGColor> colorCache = new HashMap<Integer, NVGColor>();
	private long nvg;
	private FontManager fontManager;
	private AssetManager assetManager;
	
	public NanoVGManager() {
		nvg = NanoVGGL2.nvgCreate(NanoVGGL2.NVG_ANTIALIAS);
		if(nvg == 0) {
			FireflyConsole.error("Failed to create NanoVG context");
			mc.shutdown();
		}
		fontManager = new FontManager();
		fontManager.init(nvg);
		assetManager = new AssetManager();
	}
	
    // MÉTODO ORIGINAL (RESTAURADO)
    public void setupAndDraw(Runnable task, boolean scale) {
    	ScaledResolution sr = new ScaledResolution(mc);
        setupAndDraw(task, scale ? (float)sr.getScaleFactor() : 1.0f);
    }
    
    // MÉTODO PARA O MODMENU (ESCALA CUSTOMIZADA)
    public void setupAndDraw(Runnable task, float factor) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        NanoVG.nvgBeginFrame(nvg, mc.displayWidth, mc.displayHeight, 1);
        if(factor > 0) {
        	NanoVG.nvgScale(nvg, factor, factor);
        }
        task.run();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        NanoVG.nvgEndFrame(nvg);
        GL11.glPopAttrib();
    }
    
    public void setupAndDraw(Runnable task) {
    	setupAndDraw(task, true);
    }
    
    public void drawAlphaBar(float x, float y, float width, float height, float radius, Color color) {
        NVGPaint bg = NVGPaint.create();
        NanoVG.nvgBeginPath(nvg);
        NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
        NanoVG.nvgFillPaint(nvg, NanoVG.nvgLinearGradient(nvg, x, y, x + width, y, getColor(new Color(0,0,0,0)), getColor(color), bg));
        NanoVG.nvgFill(nvg);
    }
    
    public void drawHSBBox(float x, float y, float width, float height, float radius, Color color) {
        drawRoundedRect(x, y, width, height, radius, color);
        NVGPaint bg = NVGPaint.create();
        NanoVG.nvgBeginPath(nvg);
        NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
        NanoVG.nvgFillPaint(nvg, NanoVG.nvgLinearGradient(nvg, x + 8, y + 8, x + width, y, getColor(Color.WHITE), getColor(new Color(0,0,0,0)), bg));
        NanoVG.nvgFill(nvg);
        bg = NVGPaint.create();
        NanoVG.nvgFillPaint(nvg, NanoVG.nvgLinearGradient(nvg, x + 8, y + 8, x, y + height, getColor(new Color(0,0,0,0)), getColor(Color.BLACK), bg));
        NanoVG.nvgFill(nvg);
    }
    
	public void drawRect(float x, float y, float width, float height, Color color) {
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRect(nvg, x, y, width, height);
		NanoVG.nvgFillColor(nvg, getColor(color));
		NanoVG.nvgFill(nvg);
	}
	
	public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
		NanoVG.nvgFillColor(nvg, getColor(color));
		NanoVG.nvgFill(nvg);
	}
	
	public void drawRoundedRectVarying(float x, float y, float width, float height, float tL, float tR, float bL, float bR, Color color) {
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRectVarying(nvg, x, y, width, height, tL, tR, bR, bL);
		NanoVG.nvgFillColor(nvg, getColor(color));
		NanoVG.nvgFill(nvg);
	}

	public void drawHorizontalGradientRect(float x, float y, float width, float height, Color color1, Color color2) {
		NVGPaint bg = NVGPaint.create();
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRect(nvg, x, y, width, height);
		NanoVG.nvgFillPaint(nvg, NanoVG.nvgLinearGradient(nvg, x, y, x + width, y, getColor(color1), getColor(color2), bg));
		NanoVG.nvgFill(nvg);
	}

	public void drawGradientRoundedRect(float x, float y, float width, float height, float radius, Color color1, Color color2) {
		NVGPaint bg = NVGPaint.create();
		float tick = ((System.currentTimeMillis() % 3600) / 570F);
		float max = Math.max(width, height);
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
		NanoVG.nvgFillPaint(nvg, NanoVG.nvgLinearGradient(nvg, x + width / 2 - (max / 2) * MathUtils.cos(tick), y + height / 2 - (max / 2) * MathUtils.sin(tick), x + width / 2 + (max / 2) * MathUtils.cos(tick), y + height / 2 + (max + 2f) * MathUtils.sin(tick), getColor(color1), getColor(color2), bg));
		NanoVG.nvgFill(nvg);
	}
	
	public void drawOutlineRoundedRect(float x, float y, float width, float height, float radius, float strokeWidth, Color color) {
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
		NanoVG.nvgStrokeWidth(nvg, strokeWidth);
		NanoVG.nvgStrokeColor(nvg, getColor(color));
		NanoVG.nvgStroke(nvg);
	}
	
	public void drawGradientOutlineRoundedRect(float x, float y, float width, float height, float radius, float strokeWidth, Color color1, Color color2) {
		NVGPaint bg = NVGPaint.create();
		float tick = ((System.currentTimeMillis() % 3600) / 570F);
		float max = Math.max(width, height);
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
		NanoVG.nvgStrokeWidth(nvg, strokeWidth);
		NanoVG.nvgStrokePaint(nvg, NanoVG.nvgLinearGradient(nvg, x + width / 2 - (max / 2) * MathUtils.cos(tick), y + height / 2 - (max / 2) * MathUtils.sin(tick), x + width / 2 + (max / 2) * MathUtils.cos(tick), y + height / 2 + (max + 2f) * MathUtils.sin(tick), getColor(color1), getColor(color2), bg));
		NanoVG.nvgStroke(nvg);
	}
	
	public void drawShadow(float x, float y, float width, float height, float radius, int strength) {
		int alpha = 1;
		for(float f = strength; f > 0; f--) {
			drawOutlineRoundedRect(x - (f / 2), y - (f / 2), width + f, height + f, radius + 2, f, new Color(0, 0, 0, alpha));
			alpha+=2;
		}
	}
	
	public void drawShadow(float x, float y, float width, float height, float radius) {
		drawShadow(x, y, width, height, radius, 7);
	}
	
	public void drawGradientShadow(float x, float y, float width, float height, float radius, Color color1, Color color2) {
		int alpha = 1;
		for(float f = 10; f > 0; f--) {
			drawGradientOutlineRoundedRect(x - (f / 2), y - (f / 2), width + f, height + f, radius + 2, f, ColorUtils.applyAlpha(color1, alpha), ColorUtils.applyAlpha(color2, alpha));
			alpha+=3;
		}
	}

	public void drawCircle(float x, float y, float radius, Color color) {
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgCircle(nvg, x, y, radius);
		NanoVG.nvgFillColor(nvg, getColor(color));
		NanoVG.nvgFill(nvg);
	}
	
	public void drawArc(float x, float y, float radius, float startAngle, float endAngle, float strokeWidth, Color color) {
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgArc(nvg, x, y, radius, (float) Math.toRadians(startAngle), (float) Math.toRadians(endAngle), NanoVG.NVG_CW);
		NanoVG.nvgStrokeWidth(nvg, strokeWidth);
		NanoVG.nvgStrokeColor(nvg, getColor(color));
		NanoVG.nvgStroke(nvg);
	}

	public void fontBlur(float blur){ NanoVG.nvgFontBlur(nvg, blur); }

	public void drawText(String text, float x, float y, Color color, float size, Font font) {
		y+=size / 2;
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgFontSize(nvg, size);
		NanoVG.nvgFontFace(nvg, font.getName());
		NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);
		NanoVG.nvgFillColor(nvg, getColor(color));
		NanoVG.nvgText(nvg, x, y, text);
	}

	public void drawCenteredText(String text, float x, float y, Color color, float size, Font font) {
		int textWidth = (int) getTextWidth(text, size, font);
		drawText(text, x - (textWidth >> 1), y, color, size, font);
	}
	
	public float getTextWidth(String text, float size, Font font) {
	    float[] bounds = new float[4];
	    NanoVG.nvgFontSize(nvg, size);
	    NanoVG.nvgFontFace(nvg, font.getName());
	    NanoVG.nvgTextBounds(nvg, 0, 0, text, bounds);
	    return bounds[2] - bounds[0];
	}

	public float getTextHeight(String text, float size, Font font) {
	    float[] bounds = new float[4];
	    NanoVG.nvgFontSize(nvg, size);
	    NanoVG.nvgFontFace(nvg, font.getName());
	    NanoVG.nvgTextBounds(nvg, 0, 0, text, bounds);
	    return bounds[3] - bounds[1];
	}

	public String getLimitText(String inputText, float fontSize, Font font, float width) {
		String text = inputText;
		boolean isRemoved = false;
		while(getTextWidth(text, fontSize, font) > width && text.length() > 0) {
			text = text.substring(0, text.length() - 1);
			isRemoved = true;
		}
		return text + (isRemoved ? "..." : "");
	}
	
	public void scale(float x, float y, float scale) {
		NanoVG.nvgTranslate(nvg, x, y);
		NanoVG.nvgScale(nvg, scale, scale);
		NanoVG.nvgTranslate(nvg, -x, -y);
	}
	
	public void scale(float x, float y, float width, float height, float scale) {
		NanoVG.nvgTranslate(nvg, (x + (x + width)) / 2, (y + (y + height)) / 2);
		NanoVG.nvgScale(nvg, scale, scale);
		NanoVG.nvgTranslate(nvg, -(x + (x + width)) / 2, -(y + (y + height)) / 2);
	}
	
	public void translate(float x, float y) { NanoVG.nvgTranslate(nvg, x, y); }
	public void setAlpha(float alpha) { NanoVG.nvgGlobalAlpha(nvg, alpha); }
	public void scissor(float x, float y, float width, float height) { NanoVG.nvgScissor(nvg, x, y, width, height); }
	
	public void drawImage(ResourceLocation location, float x, float y, float width, float height) {
		if(assetManager.loadImage(nvg, location)) {
			NVGPaint imagePaint = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(location), 1, imagePaint);
			NanoVG.nvgRect(nvg, x, y, width, height);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			imagePaint.free();
		}
	}
	
	public void drawImage(File file, float x, float y, float width, float height) {
		if(assetManager.loadImage(nvg, file)) {
			NVGPaint imagePaint = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(file), 1, imagePaint);
			NanoVG.nvgRect(nvg, x, y, width, height);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			imagePaint.free();
		}
	}

	public void drawRoundedImage(ResourceLocation location, float x, float y, float width, float height, float radius) {
		if(assetManager.loadImage(nvg, location)) {
			NVGPaint img = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(location), 1, img);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, img);
			NanoVG.nvgFill(nvg);
			img.free();
		}
	}

	public void drawRoundedImage(int texture, float x, float y, float width, float height, float radius) {
		if(assetManager.loadImage(nvg, texture, (int)width, (int)height)) {
			NVGPaint img = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(texture), 1, img);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, img);
			NanoVG.nvgFill(nvg);
			img.free();
		}
	}

	public void drawRoundedImage(File file, float x, float y, float width, float height, float radius) {
		if(assetManager.loadImage(nvg, file)) {
			NVGPaint img = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(file), 1, img);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, img);
			NanoVG.nvgFill(nvg);
			img.free();
		}
	}

	public void drawRoundedImage(File file, float x, float y, float width, float height, float radius, float alpha) {
		if(assetManager.loadImage(nvg, file)) {
			NVGPaint img = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(file), alpha, img);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, img);
			NanoVG.nvgFill(nvg);
			img.free();
		}
	}
    
    public void drawRoundedImage(int texture, float x, float y, float width, float height, float radius, float alpha) {
		if(assetManager.loadImage(nvg, texture, (int)width, (int)height)) {
			NVGPaint img = NVGPaint.calloc();
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, assetManager.getImage(texture), alpha, img);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, img);
			NanoVG.nvgFill(nvg);
			img.free();
		}
	}

	public void drawPlayerHead(ResourceLocation location, float x, float y, float width, float height, float radius, float alpha) {
		if(location == null || mc.getTextureManager().getTexture(location) == null) return;
		int texture = mc.getTextureManager().getTexture(location).getGlTextureId();
		if(assetManager.loadImage(nvg, texture, (int)width, (int)height)) {
			NVGPaint p = NVGPaint.calloc();
	        float sM = 8;
	        NanoVG.nvgImagePattern(nvg, x - width / 4 * sM / 2, y - height / 4 * sM / 2, width * sM, height * sM, 0, assetManager.getImage(texture), alpha, p);
	        NanoVG.nvgBeginPath(nvg);
	        NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
	        NanoVG.nvgFillPaint(nvg, p);
	        NanoVG.nvgFill(nvg);
	        p.free();
		}
	}
	
	public void drawPlayerHead(ResourceLocation location, float x, float y, float width, float height, float radius) {
		drawPlayerHead(location, x, y, width, height, radius, 1.0F);
	}
	
	public void loadImage(File file) { assetManager.loadImage(nvg, file); }
	public void loadImage(ResourceLocation loc) { assetManager.loadImage(nvg, loc); }
	public AssetManager getAssetManager() { return assetManager; }
	public void save() { NanoVG.nvgSave(nvg); }
	public void restore() { NanoVG.nvgRestore(nvg); }
	public long getContext() { return nvg; }

	public NVGColor getColor(Color color) {
		if(color == null) color = Color.RED;
		if(colorCache.containsKey(color.getRGB())) return colorCache.get(color.getRGB());
		NVGColor nvgColor = NVGColor.create();
		NanoVG.nvgRGBA((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha(), nvgColor);
		colorCache.put(color.getRGB(), nvgColor);
		return nvgColor;
	}
}
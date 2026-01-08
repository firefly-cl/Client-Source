package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.Support.ServerUtils;
import net.minecraft.entity.Entity;

public class FPSBoostMod extends Mod {

    private static FPSBoostMod instance;

    // --- CONFIGURAÇÕES ORIGINAIS (MANTIDAS) ---
    public NumberSetting playerRenderDist = new NumberSetting(TranslateText.PLAYER_RENDER_DISTANCE, this, 1.0, 0.1, 5.0, false);
    public NumberSetting passiveRenderDist = new NumberSetting(TranslateText.PASSIVE_RENDER_DISTANCE, this, 1.0, 0.1, 5.0, false);
    public NumberSetting hostileRenderDist = new NumberSetting(TranslateText.HOSTILE_RENDER_DISTANCE, this, 1.0, 0.1, 5.0, false);
    
    public BooleanSetting hideTallGrass = new BooleanSetting(TranslateText.HIDE_TALL_GRASS, this, false);
    public BooleanSetting hideFlowers = new BooleanSetting(TranslateText.HIDE_FLOWERS, this, false);
    public BooleanSetting hideFences = new BooleanSetting(TranslateText.HIDE_FENCES, this, false);
    public BooleanSetting hideSkulls = new BooleanSetting(TranslateText.HIDE_SKULLS, this, false);
    public BooleanSetting hideArmorStands = new BooleanSetting(TranslateText.HIDE_ARMOR_STANDS, this, false);
    public BooleanSetting hideItemFrames = new BooleanSetting(TranslateText.HIDE_ITEM_FRAMES, this, false);
    public BooleanSetting hideGroundArrows = new BooleanSetting(TranslateText.HIDE_GROUND_ARROWS, this, false);
    
    // VARIÁVEL ADICIONADA PARA O MIXIN PLAYER
    public BooleanSetting hideStuckArrows = new BooleanSetting(TranslateText.HIDE_STUCK_ARROWS, this, false);
    
    public BooleanSetting fpsBoost = new BooleanSetting(TranslateText.FPS_BOOST, this, false);
    public BooleanSetting worldOptimization = new BooleanSetting(TranslateText.WORLD_OPTIMIZATION, this, false);
    public BooleanSetting optimizedMath = new BooleanSetting(TranslateText.FAST_MATH, this, true);
    public BooleanSetting smoothLighting = new BooleanSetting(TranslateText.SMOOTH_LIGHTING, this, true);
    public BooleanSetting lowGraphics = new BooleanSetting(TranslateText.LOW_GRAPHICS, this, false);

    public BooleanSetting removeBotSetting = new BooleanSetting(TranslateText.REMOVE_BOT, this, false);
    
    public BooleanSetting chunkDelaySetting = new BooleanSetting(TranslateText.CHUNK_DELAY, this, false);
    public NumberSetting delaySetting = new NumberSetting(TranslateText.DELAY, this, 5, 1, 12, true);

    // ADIÇÃO DO SISTEMA DE CULLING (Para performance em lobbies)
    public BooleanSetting entityCulling = new BooleanSetting(TranslateText.ENTITY_CULLING, this, true);
    public NumberSetting cullingDistance = new NumberSetting(TranslateText.DISTANCE, this, 45, 10, 150, true);
    public NumberSetting cullingDelay = new NumberSetting(TranslateText.DELAY, this, 2, 1, 3, true);

    public FPSBoostMod() {
        super(TranslateText.FPS_BOOST, TranslateText.FPS_BOOST_DESCRIPTION, ModCategory.OTHER);
        instance = this;
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if(mc.theWorld == null) return;

        if (lowGraphics.isToggled()) mc.gameSettings.fancyGraphics = false;
        mc.gameSettings.ambientOcclusion = smoothLighting.isToggled() ? 2 : 0;

        if (worldOptimization.isToggled()) {
            mc.gameSettings.particleSetting = 2;
        }

        if(removeBotSetting.isToggled()) {
            mc.theWorld.loadedEntityList.removeIf(entity -> 
                entity.isInvisible() && !ServerUtils.isInTablist(entity));
        }
    }
    
    public static FPSBoostMod getInstance() {
        return instance;
    }

    public BooleanSetting getChunkDelaySetting() {
        return chunkDelaySetting;
    }

    public NumberSetting getDelaySetting() {
        return delaySetting;
    }
}
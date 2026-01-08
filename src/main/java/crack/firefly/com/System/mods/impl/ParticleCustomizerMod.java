package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import crack.firefly.com.Loader.Abstractions.IMixinRenderManager;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventAttackEntity;
import crack.firefly.com.System.event.impl.EventLivingUpdate;
import crack.firefly.com.System.event.impl.EventLoadWorld;
import crack.firefly.com.System.event.impl.EventRender3D;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.Support.LocationUtils;
import crack.firefly.com.Support.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;

public class ParticleCustomizerMod extends Mod {

    // Configurações de Partículas de Hit
    private BooleanSetting alwaysSharpnessSetting = new BooleanSetting(TranslateText.ALWAYS_SHARPNESS, this, false);
    private BooleanSetting alwaysCriticalsSetting = new BooleanSetting(TranslateText.ALWAYS_CRITICALS, this, false);
    private BooleanSetting sharpnessSetting = new BooleanSetting(TranslateText.SHARPNESS, this, true);
    private BooleanSetting criticalsSetting = new BooleanSetting(TranslateText.CRITICALS, this, false);
    private NumberSetting sharpnessAmountSetting = new NumberSetting(TranslateText.SHARPNESS_AMOUNT, this, 2, 1, 10, true);
    private NumberSetting criticalsAmountSetting = new NumberSetting(TranslateText.CRITICALS_AMOUNT, this, 2, 1, 10, true);

    // Configuração de Partículas de Dano (Números)
    private BooleanSetting damageParticlesSetting = new BooleanSetting(TranslateText.DAMAGE_PARTICLES, this, true);

    // Lógica das Partículas de Dano
    private HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    private boolean canRemove;
    private Particle removeParticle;

    public ParticleCustomizerMod() {
        super(TranslateText.PARTICLE_CUSTOMIZER, TranslateText.PARTICLE_CUSTOMIZER_DESCRIPTION, ModCategory.RENDER);
    }

    // --- LÓGICA DE PARTÍCULAS DE HIT (SHARPNESS/CRIT) ---
    @EventTarget
    public void onAttackEntity(EventAttackEntity event) {
        EntityPlayer player = mc.thePlayer;
        int sMultiplier = sharpnessAmountSetting.getValueInt();
        int cMultiplier = criticalsAmountSetting.getValueInt();

        if (!(event.getEntity() instanceof EntityLivingBase)) {
            return;
        }

        boolean critical = criticalsSetting.isToggled() && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null;
        boolean alwaysSharpness = alwaysSharpnessSetting.isToggled();
        boolean sharpness = sharpnessSetting.isToggled() && EnchantmentHelper.getModifierForCreature(player.getHeldItem(), ((EntityLivingBase) event.getEntity()).getCreatureAttribute()) > 0;
        boolean alwaysCriticals = alwaysCriticalsSetting.isToggled();

        if (critical || alwaysCriticals) {
            for (int i = 0; i < cMultiplier - 1; i++) {
                mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.CRIT);
            }
        }

        if (alwaysSharpness || sharpness) {
            for (int i = 0; i < sMultiplier - 1; i++) {
                mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    // --- LÓGICA DE PARTÍCULAS DE DANO (NÚMEROS FLUTUANTES) ---
    @EventTarget
    public void onTick(EventTick event) {
        if (!damageParticlesSetting.isToggled()) return;

        if (canRemove) {
            particles.remove(removeParticle);
            canRemove = false;
        }

        particles.forEach(particle -> {
            particle.ticks++;
            if (particle.ticks <= 10) {
                particle.location.setY(particle.location.getY() + particle.ticks * 0.005);
            }
            if (particle.ticks > 20) {
                canRemove = true;
                removeParticle = particle;
            }
        });
    }

    @EventTarget
    public void onLivingUpdate(EventLivingUpdate event) {
        if (!damageParticlesSetting.isToggled()) return;

        EntityLivingBase entity = event.getEntity();
        if (entity == this.mc.thePlayer) return;

        if (!healthMap.containsKey(entity)) {
            healthMap.put(entity, entity.getHealth());
        }

        float before = healthMap.get(entity);
        float after = entity.getHealth();

        if (before != after) {
            String text;
            if ((before - after) < 0) {
                text = EnumChatFormatting.GREEN + "" + MathUtils.roundToPlace((before - after) * -1, 1);
            } else {
                text = EnumChatFormatting.YELLOW + "" + MathUtils.roundToPlace((before - after), 1);
            }

            LocationUtils location = new LocationUtils(entity);
            location.setY(entity.getEntityBoundingBox().minY + ((entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2));
            location.setX((location.getX() - 0.5) + (new Random(System.currentTimeMillis()).nextInt(5) * 0.1));
            location.setZ((location.getZ() - 0.5) + (new Random(System.currentTimeMillis() + 1).nextInt(5) * 0.1));

            particles.add(new Particle(location, text));
            healthMap.put(entity, entity.getHealth());
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (!damageParticlesSetting.isToggled() || particles.isEmpty()) return;

        for (Particle particle : this.particles) {
            double x = particle.location.getX() - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosX();
            double y = particle.location.getY() - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosY();
            double z = particle.location.getZ() - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosZ();

            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0F, -1500000.0F);

            GlStateManager.translate((float) x, (float) y, (float) z);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
            GlStateManager.rotate(mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
            
            double scale = 0.03;
            GlStateManager.scale(-scale, -scale, scale);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            GL11.glDepthMask(false);
            fr.drawStringWithShadow(particle.text, -(mc.fontRendererObj.getStringWidth(particle.text) / 2), -(mc.fontRendererObj.FONT_HEIGHT - 1), 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);

            GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }

    @EventTarget
    public void onLoadWorld(EventLoadWorld event) {
        this.particles.clear();
        this.healthMap.clear();
    }

    // Classe interna para as partículas de números
    private class Particle {
        public int ticks;
        public LocationUtils location;
        public String text;

        public Particle(LocationUtils location, String text) {
            this.location = location;
            this.text = text;
            this.ticks = 0;
        }
    }
}
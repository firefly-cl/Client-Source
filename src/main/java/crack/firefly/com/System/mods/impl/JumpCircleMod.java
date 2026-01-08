package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.List;

import crack.firefly.com.Firefly;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.Loader.Abstractions.IMixinMinecraft;
import crack.firefly.com.Loader.Abstractions.IMixinRenderManager;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventJump;
import crack.firefly.com.System.event.impl.EventRender3D;
import crack.firefly.com.System.event.impl.EventUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;

public class JumpCircleMod extends Mod {

	private List<JumpCircle> circles = new ArrayList<JumpCircle>();
	private boolean jumping;
	
	public JumpCircleMod() {
		super(TranslateText.JUMP_CIRCLE, TranslateText.JUMP_CIRCLE_DESCRIPTION, ModCategory.RENDER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if(jumping && mc.thePlayer.onGround) {
			jumping = false;
			circles.add(new JumpCircle(mc.thePlayer.getPositionVector()));
		}
		
		circles.removeIf(JumpCircle::update);
	}
	
	@EventTarget
	public void onJump(EventJump event) {
		jumping = true;
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		AccentColor currentColor = Firefly.getInstance().getColorManager().getCurrentColor();
		
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        
        for (JumpCircle circle : circles) {
            GL11.glBegin(8);
            for (int i = 0; i <= 360; i += 5) {
            	
                float red = (float) (currentColor.getInterpolateColor().getRGB() >> 16 & 255) / 255.0F;
                float green = (float) (currentColor.getInterpolateColor().getRGB() >> 8 & 255) / 255.0F;
                float blue = (float) (currentColor.getInterpolateColor().getRGB() & 255) / 255.0F;
                
                Vec3 pos = circle.pos();
                double x = Math.cos(Math.toRadians(i)) * createAnimation(1.0 - circle.getAnimation(((IMixinMinecraft)mc).getTimer().renderPartialTicks)) * 0.7;
                double z = Math.sin(Math.toRadians(i)) * createAnimation(1.0 - circle.getAnimation(((IMixinMinecraft)mc).getTimer().renderPartialTicks)) * 0.7;
                GL11.glColor4d(red, green, blue, 0.6 * circle.getAnimation(((IMixinMinecraft)mc).getTimer().renderPartialTicks));
                GL11.glVertex3d(pos.xCoord + x, pos.yCoord + (double)0.2f, pos.zCoord + z);
                GL11.glColor4d(red, green, blue, 0.2 * circle.getAnimation(((IMixinMinecraft)mc).getTimer().renderPartialTicks));
                GL11.glVertex3d(pos.xCoord + x * 1.4, pos.yCoord + (double)0.2f, pos.zCoord + z * 1.4);
            }
            GL11.glEnd();	
        }
        
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        GlStateManager.resetColor();
	}
	
    private static double createAnimation(double value) {
        return Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
    }
    
    private class JumpCircle {

    	private Minecraft mc = Minecraft.getMinecraft();
    	
    	private Vec3 vector;
    	private int tick;
    	private int prevTick;
    	
    	public JumpCircle(Vec3 vector) {
    		this.vector = vector;
    		this.prevTick = 20;
            this.prevTick = this.tick = 20;
    	}
    	
        public double getAnimation(float pt) {
            return ((float)this.prevTick + (float)(this.tick - this.prevTick) * pt) / 20.0f;
        }

        public boolean update() {
            this.prevTick = this.tick;
            return this.tick-- <= 0;
        }

        public Vec3 pos() {
            return new Vec3(this.vector.xCoord - ((IMixinRenderManager)mc.getRenderManager()).getRenderPosX(), this.vector.yCoord - ((IMixinRenderManager)mc.getRenderManager()).getRenderPosY(), this.vector.zCoord - ((IMixinRenderManager)mc.getRenderManager()).getRenderPosZ());
        }
    }
}
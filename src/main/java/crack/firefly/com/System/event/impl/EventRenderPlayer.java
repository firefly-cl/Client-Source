package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;
import net.minecraft.entity.Entity;

public class EventRenderPlayer extends Event {

	private Entity entity;
	private double x, y, z;
	private float partialTicks;
	
	public EventRenderPlayer(Entity entity, double x, double y, double z, float partialTicks) {
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.partialTicks = partialTicks;
	}

	public Entity getEntity() {
		return entity;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}

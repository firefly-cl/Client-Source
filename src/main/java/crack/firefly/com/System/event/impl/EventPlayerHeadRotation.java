package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventPlayerHeadRotation extends Event {
    private float yaw, pitch;

    public EventPlayerHeadRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() { return yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }

    public float getPitch() { return pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }
}
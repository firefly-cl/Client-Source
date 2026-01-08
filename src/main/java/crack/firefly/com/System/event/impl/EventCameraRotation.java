package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventCameraRotation extends Event {
    private float yaw, pitch, roll, thirdPersonDistance;

    public EventCameraRotation(float yaw, float pitch, float roll, float dist) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.thirdPersonDistance = dist;
    }

    public float getYaw() { return yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }

    public float getPitch() { return pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }

    public float getRoll() { return roll; }
    public void setRoll(float roll) { this.roll = roll; }

    public float getThirdPersonDistance() { return thirdPersonDistance; }
    public void setThirdPersonDistance(float dist) { this.thirdPersonDistance = dist; }
}
package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventHurtCamera extends Event {
    private float intensity = 1.0f;

    public float getIntensity() { return intensity; }
    public void setIntensity(float intensity) { this.intensity = intensity; }
}
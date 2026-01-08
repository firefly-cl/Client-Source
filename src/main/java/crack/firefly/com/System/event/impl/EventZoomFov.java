package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventZoomFov extends Event {
    private float fov;

    public EventZoomFov(float fov) {
        this.fov = fov;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }
}
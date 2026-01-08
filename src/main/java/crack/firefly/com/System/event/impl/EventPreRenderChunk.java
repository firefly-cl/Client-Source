package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class EventPreRenderChunk extends Event {

    public static int listeners = 0;

    private RenderChunk renderChunk;

    public EventPreRenderChunk(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }

    public RenderChunk getRenderChunk() {
        return renderChunk;
    }

    public void setRenderChunk(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }
}
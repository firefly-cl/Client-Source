package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;

public class EventRenderChunkPosition extends Event {
    public static int listeners = 0; // Controle de ativação
    private RenderChunk renderChunk;
    private BlockPos blockPos;

    public EventRenderChunkPosition(RenderChunk renderChunk, BlockPos blockPos) {
        this.renderChunk = renderChunk;
        this.blockPos = blockPos;
    }

    public void setRenderChunk(RenderChunk renderChunk) { this.renderChunk = renderChunk; }
    public void setBlockPos(BlockPos blockPos) { this.blockPos = blockPos; }
    public RenderChunk getRenderChunk() { return renderChunk; }
    public BlockPos getBlockPos() { return blockPos; }
}
package cn.exception.event.events;

import cn.exception.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventPreRenderBlock extends Event {
    private Block block = null;
    private BlockPos pos = null;

    public EventPreRenderBlock(Block block, BlockPos pos) {
        super(Type.POST);
        this.setBlock(block);
        this.setPos(pos);
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}

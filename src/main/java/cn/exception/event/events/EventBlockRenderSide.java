package cn.exception.event.events;

import cn.exception.event.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class EventBlockRenderSide extends Event {
    private final IBlockState state;
    private final IBlockAccess world;
    public final BlockPos pos;
    private final EnumFacing side;
    private boolean toRender;
    public double maxX;
    public double maxY;
    public double maxZ;
    public double minX;
    public double minY;
    public double minZ;

    public EventBlockRenderSide(IBlockAccess world, BlockPos pos, EnumFacing side, double maxX, double minX,
                                double maxY, double minY, double maxZ, double minZ) {
        super(Type.POST);
        this.state = Minecraft.getMinecraft().theWorld.getBlockState(pos);
        this.world = world;
        this.pos = pos;
        this.side = side;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
    }

    public IBlockState getState() {
        return state;
    }

    public IBlockAccess getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getSide() {
        return side;
    }

    public boolean isToRender() {
        return toRender;
    }

    public void setToRender(boolean toRender) {
        this.toRender = toRender;
    }
}

package cn.exception.event.events;

import cn.exception.event.Event;
import net.minecraft.client.Minecraft;

public class EventUpdate extends Event {

    public float yaw;
    public float pitch;
    private boolean onGround;

    public EventUpdate(Type type, float yaw, float pitch, boolean onGround) {
        super(type);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

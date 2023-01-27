package cn.exception.event.events;

import cn.exception.event.Event;

/**
 * @author: Thr1c0/s
 * @date: 2022/8/3 19:45
 */
public class EventRender3D extends Event {
    private float ticks;
    public EventRender3D(float ticks){
        super(Type.PRE);
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return this.ticks;
    }
}

package cn.exception.event.events;

import cn.exception.event.Event;

public class EventTick extends Event {
    public EventTick(){
        super(Type.POST);
    }
}

package cn.exception.module.impl.movement;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;

public class Scaffold extends Module {
    public Scaffold(){
        super("Scaffold", Category.Movement);
    }

    @EventTarget
    public void onEvent(EventUpdate e){

    }
}

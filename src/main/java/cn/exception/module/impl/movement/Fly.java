package cn.exception.module.impl.movement;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.utils.MoveUtil;

/**
 * @author MiLiBlue
 **/
public class Fly extends Module {
    public Fly(){
        super("Fly", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        mc.thePlayer.motionY = 0;
        MoveUtil.strafe(1);
    }
}

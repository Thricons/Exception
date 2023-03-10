package cn.exception.module.impl.movement;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.utils.MoveUtil;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class Sprint extends Module {
    public Sprint(){
        super("Sprint", Category.Movement);
        setEnable(true);
    }
    @EventTarget
    public void onUpdate(EventUpdate e){
        try{
            if(MoveUtil.isMoving()){
                mc.thePlayer.setSprinting(true);
            }
        }catch (Exception e1){}
    }
}

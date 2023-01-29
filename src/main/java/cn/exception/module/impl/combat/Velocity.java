package cn.exception.module.impl.combat;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventPacket;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.module.value.Mode;
import cn.exception.utils.PlayerUtil;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

/**
 * @author MiLiBlue
 **/
public class Velocity extends Module {
    private Mode mode = new Mode("Mode", new String[]{"Hypixel", "Cancel"}, "Hypixel");
    public Velocity(){
        super("Velocity", Category.Combat);
        addValues(mode);
    }

    @EventTarget
    public void onVelocity(EventUpdate e){
        if(mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime){
            if(mode.isCurrentMode("Cancel")){
                mc.thePlayer.motionY *= 1;
                mc.thePlayer.motionX *= 1;
                mc.thePlayer.motionZ *= 1;
            }
            if(mode.isCurrentMode("Hypixel")){
                mc.thePlayer.motionX *= 1;
                mc.thePlayer.motionZ *= 1;
            }
        }
    }
}

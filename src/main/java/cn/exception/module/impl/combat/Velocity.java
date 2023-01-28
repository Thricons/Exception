package cn.exception.module.impl.combat;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventPacket;
import cn.exception.module.Module;
import cn.exception.utils.PlayerUtil;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

/**
 * @author MiLiBlue
 **/
public class Velocity extends Module {
    public Velocity(){
        super("Velocity", Category.Combat);
    }

    @EventTarget
    public void onVelocity(EventPacket e){
        if(e.getPacket() instanceof S12PacketEntityVelocity){
            S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) e.getPacket();
            try{
                s12.getClass().getDeclaredField("motionX").set(s12, 0);
                s12.getClass().getDeclaredField("motionZ").set(s12, 0);
            }catch (Exception ex){
                try{
                    s12.getClass().getDeclaredField("field_149415_b").set(s12, 0);
                    s12.getClass().getDeclaredField("field_149414_d").set(s12, 0);
                }catch (Exception exp){
                    PlayerUtil.tellPlayer(exp.toString());
                }
            }
        }
    }
}

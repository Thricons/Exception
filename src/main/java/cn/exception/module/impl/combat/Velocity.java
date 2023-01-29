package cn.exception.module.impl.combat;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventPacket;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.module.value.Mode;
import cn.exception.utils.PlayerUtil;
import cn.exception.utils.RefUtil;
import net.minecraft.network.play.server.S02PacketChat;
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
    public void onVelocity(EventPacket e){
        if(e.getPacket() instanceof S12PacketEntityVelocity){
            if(mode.isCurrentMode("Cancel")){
                e.setCancelled(true);
            }
            if(mode.isCurrentMode("Hypixel")){
                S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) e.getPacket();
                RefUtil.set(s12, 0, "field_149415_b", "motionX", "field_149414_d", "motionZ");
            }
        }
    }
}

package cn.exception.module.impl.player;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventPacket;
import cn.exception.module.Module;
import net.minecraft.network.play.client.C0APacketAnimation;

/**
 * @author MiLiBlue
 **/
public class PacketFix extends Module {
    public PacketFix(){
        super("PacketFix", Category.Player);
    }

    @EventTarget
    public void onPacket(EventPacket e){
        if(e.getPacket() instanceof C0APacketAnimation){
            e.setCancelled(true);
        }
    }
}

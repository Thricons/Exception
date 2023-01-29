package cn.exception.module.impl.player;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author MiLiBlue
 **/
public class NoFall extends Module {
    public NoFall(){
        super("NoFall", Category.Player);
    }

    @EventTarget
    public void onSet(EventUpdate e){
        if(mc.thePlayer.fallDistance > 3){
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
        }
    }
}

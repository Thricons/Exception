package cn.exception.event;

import cn.exception.event.events.EventRender2D;
import cn.exception.event.events.EventUpdate;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author MiLiBlue
 **/
public class EventHook {//Low IQ so FML Event
    float oldYaw;
    float oldPitch;
    public EventHook(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text e){
        new EventRender2D().call();
    }

    @SubscribeEvent
    public void onIssus(TickEvent.PlayerTickEvent e){
        if(e.phase == TickEvent.Phase.START){
            EventUpdate eu = new EventUpdate(Event.Type.PRE,
                    Minecraft.getMinecraft().thePlayer.rotationYaw,
                    Minecraft.getMinecraft().thePlayer.rotationPitch,
                    Minecraft.getMinecraft().thePlayer.onGround);
            eu.call();
        }
    }
}

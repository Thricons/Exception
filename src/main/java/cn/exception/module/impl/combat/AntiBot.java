package cn.exception.module.impl.combat;

import cn.exception.Exception;
import cn.exception.event.EventTarget;
import cn.exception.event.events.EventPacket;
import cn.exception.event.events.EventWorldReload;
import cn.exception.module.Module;
import cn.exception.module.value.Mode;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MiLiBlue
 **/
public class AntiBot extends Module {
    static List<Integer> bots = new ArrayList<>();
    static Mode mode = new Mode("Mode", new String[]{"Hypixel", "Mineland"}, "Mineland");
    public AntiBot(){
        super("AntiBot", Category.Combat);
        addValues(mode);
    }

    public static boolean isBot(Entity entity){
        if(Exception.instance.moduleManager.getModuleByClass(AntiBot.class).isEnable() && mode.isCurrentMode("Hypixel")){
            if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                return false;
            }
            return true;
        }
        if(Exception.instance.moduleManager.getModuleByClass(AntiBot.class).isEnable() && mode.isCurrentMode("Mineland")){
            if(bots.contains(entity.getEntityId())){
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket){
        setSuffix(mode.getValue());
        if(eventPacket.getPacketType() == EventPacket.PacketType.Server){
            if(eventPacket.getPacket() instanceof S0CPacketSpawnPlayer){
                if(KillAura.curTarget != null && Exception.instance.moduleManager.getModuleByClass(KillAura.class).isEnable()){
                    bots.add(((S0CPacketSpawnPlayer) eventPacket.getPacket()).getEntityID());
                }
            }
        }
    }

    @Override
    public void onDisable(){
        bots.clear();
    }

    @EventTarget
    public void onReload(EventWorldReload e){
        bots.clear();
    }
}

package cn.exception.module.impl.player;

import cn.exception.Exception;
import cn.exception.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

/**
 * @author MiLiBlue
 **/
public class Teams extends Module {
    public Teams(){
        super("Teams", Category.Player);
    }

    public static boolean isOnSameTeam(Entity entity) {
        if(Exception.instance.moduleManager.getModuleByClass(Teams.class).isEnable()) {
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
                if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
                        || entity.getDisplayName().getUnformattedText().length() <= 2) {
                    return false;
                }
                if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
                    return true;
                }
            }
        }
        return false;
    }
}

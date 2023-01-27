package cn.exception.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class PlayerUtil {
    public static void tellPlayer(String message) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\247a[Exception] \247r" + message));
    }
    public static void tellPlayerIrc(String message) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\247b[Irc] \247r" + message));
    }
}

package cn.exception.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Code by MiLiBlue, At 2023/1/14
 **/
public class Wrapper {
    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        return (Unsafe) Unsafe.class.getClass().getDeclaredField("theUnsafe").get(null);
    }

    public static double renderPosX(){
        try{
            Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("renderPosX");
            f.setAccessible(true);
            return (double) f.get(Minecraft.getMinecraft().getRenderManager());
        }catch (Exception e){
            try {
                Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("field_78725_b");
                f.setAccessible(true);
                return (double) f.get(Minecraft.getMinecraft().getRenderManager());
            }catch (Exception e1){
                PlayerUtil.tellPlayer(e1.getMessage());
            }
        }
        return 0;
    }

    public static double renderPosY(){
        try{
            Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("renderPosY");
            f.setAccessible(true);
            return (double) f.get(Minecraft.getMinecraft().getRenderManager());
        }catch (Exception e){
            try {
                Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("field_78726_c");
                f.setAccessible(true);
                return (double) f.get(Minecraft.getMinecraft().getRenderManager());
            }catch (Exception e1){
                PlayerUtil.tellPlayer(e1.getMessage());
            }
        }
        return 0;
    }

    public static double renderPosZ(){
        try{
            Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("renderPosZ");
            f.setAccessible(true);
            return (double) f.get(Minecraft.getMinecraft().getRenderManager());
        }catch (Exception e){
            try {
                Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("field_78723_d");
                f.setAccessible(true);
                return (double) f.get(Minecraft.getMinecraft().getRenderManager());
            }catch (Exception e1){
                PlayerUtil.tellPlayer(e1.getMessage());
            }
        }
        return 0;
    }

    public static Timer getTimer(){
        try{
            Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("timer");
            f.setAccessible(true);
            return (Timer) f.get(Minecraft.getMinecraft());
        }catch (Exception e){
            try {
                Field f = Minecraft.getMinecraft().getRenderManager().getClass().getDeclaredField("field_71428_T");
                f.setAccessible(true);
                return (Timer) f.get(Minecraft.getMinecraft());
            }catch (Exception e1){
                PlayerUtil.tellPlayer(e1.getMessage());
            }
        }
        return null;
    }
}

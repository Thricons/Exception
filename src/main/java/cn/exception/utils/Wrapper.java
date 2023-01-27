package cn.exception.utils;

import sun.misc.Unsafe;

import java.lang.reflect.InvocationTargetException;

/**
 * Code by MiLiBlue, At 2023/1/14
 **/
public class Wrapper {
    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        return (Unsafe) Unsafe.class.getClass().getDeclaredField("theUnsafe").get(null);
    }
    public static void setRightClickDelay(int delay) {
        Class refClass = null;
        try {
           refClass = Class.forName("net/minecraft/client/Minecraft");
        } catch (ClassNotFoundException e) {
            try {
                refClass = Class.forName("bib");
            } catch (ClassNotFoundException ex) {
                PlayerUtil.tellPlayer("Could not find Minecraft Class. Did you use FML?");
            }
        }
        try {
            refClass.getDeclaredField("rightClickDelay").set(refClass, delay);
        } catch (NoSuchFieldException e) {
            try {
                refClass.getDeclaredField("rightClickDelay").set(refClass, delay);
            } catch (IllegalAccessException ex) {
                PlayerUtil.tellPlayer(ex.getMessage());
            } catch (NoSuchFieldException ex) {
                PlayerUtil.tellPlayer("Could not find rightClickDelay Field. Did you use FML?");
            }
        } catch (IllegalAccessException e) {
            PlayerUtil.tellPlayer(e.getMessage());
        }
    }

    public static void setSprinting(boolean is){
        Class refClass = null;
        try{
            refClass = Class.forName("net/minecraft/client/entity/EntityPlayerSP");
        }catch (ClassNotFoundException e){
            try {
                refClass = Class.forName("bud");
            } catch (ClassNotFoundException ex) {
                PlayerUtil.tellPlayer("Could not find EntityPlayerSP Class. Did you use FML?");
            }
        }

        try{
            refClass.getDeclaredMethod("setSprinting", refClass).invoke(refClass, is);
        } catch (Exception eee){
            try {
                refClass.getDeclaredMethod("func_70031_b", refClass).invoke(refClass, is);
            }catch (Exception ee){
                PlayerUtil.tellPlayer("Could not find setSprinting Method. Did you use FML?");
            }
        }
    }
}

package cn.exception;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
@Mod(modid = "exception", version = "Alpha")
public class ForgeMain {
    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        Exception.instance.start();
    }
}

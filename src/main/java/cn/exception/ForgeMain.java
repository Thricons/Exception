package cn.exception;

import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.mixinstranslator.MixinsTranslator;
import net.lenni0451.classtransform.utils.loader.InjectionClassLoader;
import net.lenni0451.classtransform.utils.tree.BasicClassProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import sun.misc.Launcher;

import java.lang.reflect.InvocationTargetException;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
@Mod(modid = "exception", version = "Alpha")
public class ForgeMain {
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        //Exception.instance.start();
        TransformerManager transformerManager = new TransformerManager(new BasicClassProvider());
        transformerManager.addTransformerPreprocessor(new MixinsTranslator());
        InjectionClassLoader classLoader = new InjectionClassLoader(transformerManager, this.getClass().getProtectionDomain().getCodeSource().getLocation());
        //classLoader.executeMain("cn.exception.Exception", "start");
        Thread.currentThread().setContextClassLoader(classLoader);
        Exception.instance.start();
    }
}

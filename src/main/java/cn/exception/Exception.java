package cn.exception;

import cn.exception.event.EventHook;
import cn.exception.event.EventManager;
import cn.exception.manager.ModuleManager;
import cn.exception.manager.PacketManager;
import cn.exception.ui.notification.Notifications;
import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.utils.tree.BasicClassProvider;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class Exception {
    public static Exception instance = new Exception();

    public boolean isFromAgent = false;

    public String name = "Exception";
    public String version = "114514 Build";

    public EventManager eventManager;
    public ModuleManager moduleManager;
    public EventHook eventHook;

    public void start() {
        //init Managers
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        moduleManager.loadModules();

        eventHook = new EventHook();
        PacketManager.instance = new PacketManager();
        System.out.printf("Done!");
    }
}

package cn.exception;

import cn.exception.event.EventHook;
import cn.exception.event.EventManager;
import cn.exception.manager.ModuleManager;
import cn.exception.manager.PacketManager;
import com.sun.tools.attach.VirtualMachine;
import sun.tools.attach.HotSpotVirtualMachine;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class Exception {
    public static Exception instance = new Exception();

    public String name = "Exception";
    public String version = "Dev Build";

    public EventManager eventManager;
    public ModuleManager moduleManager;
    public EventHook eventHook;
    public PacketManager packetManager;

    public void start(){
        //init Managers
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        packetManager = new PacketManager();
        moduleManager.loadModules();

        eventHook = new EventHook();
        System.out.printf("Done!");
    }
}

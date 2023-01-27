package cn.exception.module.impl.player;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.utils.Wrapper;
import net.minecraft.item.ItemBlock;
import org.lwjgl.input.Keyboard;


/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class FastPlace extends Module{
    public FastPlace(){
        super("FastPlace", Module.Category.World);
        setKey(Keyboard.KEY_G);
    }

    @EventTarget
    public void onEvent(EventUpdate e){
        //mc.rightClickDelayTimer = 0;
        if(mc.gameSettings.keyBindUseItem.isKeyDown() && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock){
            Wrapper.setRightClickDelay(0);
        }
    }
}

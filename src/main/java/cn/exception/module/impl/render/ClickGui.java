package cn.exception.module.impl.render;

import cn.exception.module.Module;
import cn.exception.ui.clickgui.ClickUi;
import org.lwjgl.input.Keyboard;

/**
 * Code by MiLiBlue, At 2022/12/28
 **/
public class ClickGui extends Module {
    public ClickGui(){
        super("ClickGui", Category.Render);
        setKey(Keyboard.KEY_RSHIFT);
    }
    @Override
    public void onEnable(){
        mc.displayGuiScreen(new ClickUi());
        setEnable(false);
    }
}

package cn.exception.module.impl.combat;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.module.value.Numbers;
import cn.exception.utils.MathUtil;
import cn.exception.utils.TimerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class AutoClicker extends Module{
    static Numbers cpsMax = new Numbers("MaxCPS", 13, 1, 20, 1);
    static Numbers cpsMin = new Numbers("MinCPS", 10, 1, 20, 1);
    public AutoClicker(){
        super("AutoClicker", Module.Category.Combat);
        addValues(cpsMax, cpsMin);
        setKey(Keyboard.KEY_R);
    }
    TimerUtil TimerUtil = new TimerUtil();
    @EventTarget
    public void onUpdate(EventUpdate e){
        setSuffix("CPSMax:" + cpsMax.getValue() + " CPSMin:" + cpsMin.getValue());
        if(TimerUtil.hasReached((1000.0/ MathUtil.randomDouble(cpsMin.intValue(), cpsMax.intValue()))) && mc.gameSettings.keyBindAttack.isKeyDown())
        {
            if(mc.gameSettings.keyBindAttack.isKeyDown()) {
                mc.thePlayer.swingItem();
                if(mc.objectMouseOver.entityHit != null){
                    mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                }
                if(mc.objectMouseOver.getBlockPos() != null){
                    BlockPos blockpos = mc.objectMouseOver.getBlockPos();

                    if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
                    {
                        mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit);
                    }
                }
                TimerUtil.reset();
            }
        }
    }
}

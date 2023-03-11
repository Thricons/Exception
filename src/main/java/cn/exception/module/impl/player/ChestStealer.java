package cn.exception.module.impl.player;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.module.value.Numbers;
import cn.exception.utils.TimerUtil;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class ChestStealer extends Module {
    private Numbers< Double > delay = new Numbers<>( "Delay", 30.0, 0.0, 1000.0, 10.0 );
    private TimerUtil timer = new TimerUtil();
    public ChestStealer(){
        super("ChestStealer", Category.Player);
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            int i = 0;
            while ( i < container.getLowerChestInventory().getSizeInventory() ) {
                if ( container.getLowerChestInventory().getStackInSlot( i ) != null
                        && this.timer.hasReached(this.delay.getValue())) {
                    mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                    this.timer.reset();
                }
                ++i;
            }
            if (this.isEmpty()) {
                mc.thePlayer.closeScreen();
            }
        }
    }

    private boolean isEmpty() {
        if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            int i = 0;
            while (i < container.getLowerChestInventory().getSizeInventory()) {
                ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() != null ) {
                    return false;
                }
                ++i;
            }
        }
        return true;
    }
}

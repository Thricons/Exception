package cn.exception.module.impl.render;

import cn.exception.Exception;
import cn.exception.event.EventTarget;
import cn.exception.event.events.EventRender2D;
import cn.exception.module.Module;
import cn.exception.module.value.Numbers;
import cn.exception.module.value.Option;
import cn.exception.ui.notification.NotificationRenderer;
import cn.exception.ui.notification.Notifications;
import cn.exception.utils.RenderUtil;
import cn.exception.utils.TimerUtil;
import cn.exception.utils.fonts.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;
import java.util.List;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class HUD extends Module {

    //    static Numbers r = new Numbers<>("R",  255, 0, 255, 1);
//    static Numbers g = new Numbers<>("G",  255, 0, 255, 1);
//    static Numbers b = new Numbers<>("B", 255, 0, 255, 1);
    static Numbers backgroundAlpha = new Numbers<>("backgroundAlpha", 120, 0, 255, 1);
    public static int MAIN = new Color(0, 128, 255).getRGB();
    public static final int SECONDARY = new Color(23, 23, 23, backgroundAlpha.intValue()).getRGB();
    static Option rainbow = new Option("Rainbow",  false);
    private TimerUtil timer = new TimerUtil();
    public HUD(){
        super("HUD", Category.Render);
        addValues(rainbow);
        setEnable(true);
    }

    @EventTarget
    public void onRender(EventRender2D eventRender2D){
        int rainbowTick = 0;
        Notifications.getManager().updateAndRender();
        FontLoaders.kiona18.drawStringWithShadow("Exception", 2, 2, -1);

        int yStart = 6;
        ScaledResolution sr = new ScaledResolution(mc);

        List<Module> mods = Exception.instance.moduleManager.getEnabledModList();
        mods.sort((o1, o2) ->FontLoaders.kiona17.getStringWidth(o2.getSuffix() == null ? o2.getName() : o2.getName() + "," + o2.getSuffix()) - FontLoaders.kiona17.getStringWidth(o1.getSuffix() == null ? o1.getName() : o1.getName() + "," + o1.getSuffix()));
        FontLoaders.kiona18.drawStringWithShadow("Build - " + "\247a230311\247f",
                sr.getScaledWidth() - 2 -FontLoaders.kiona18.getStringWidth("Build - 082022"),
                sr.getScaledHeight() - FontLoaders.kiona18.getHeight() - 1, -1);
        for (Module module : mods) {
            if(rainbow.getValue()) {
                MAIN = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f)).getRGB();
            }else {
                Color LOL = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
                MAIN = new Color(new Color(MAIN).getRed(), new Color(MAIN).getGreen(), new Color(MAIN).getBlue(), LOL.getRed()).getRGB();
            }

            int startX = sr.getScaledWidth() - FontLoaders.kiona17.getStringWidth(module.getSuffix() == null ? module.getName() : module.getName() + "," + module.getSuffix()) - 5;
            RenderUtil.drawRect(module.animX, module.animY - 1, sr.getScaledWidth() + 1, module.animY + 10, SECONDARY);
            RenderUtil.drawRect(sr.getScaledWidth() - 1, module.animY - 1, sr.getScaledWidth(), module.animY + 10, MAIN);
            if(module.animY == 0){
                module.animY = yStart;
            }
            FontLoaders.kiona17.drawStringWithShadow(module.getName(), module.animX + 3, module.getAnimY()+2, MAIN);
            if (module.getSuffix() != null) {
                FontLoaders.kiona17.drawStringWithShadow(module.getSuffix(), module.getAnimX() + 3 + FontLoaders.kiona17.getStringWidth(module.getName() + ","), module.animY+2, Color.WHITE.darker().getRGB());
            }
            if (++rainbowTick > 50) {
                rainbowTick = 0;
            }
            module.animY = (float) RenderUtil.getAnimationStateSmooth(yStart, module.animY, 8f / Minecraft.getDebugFPS());
            if(!module.preDisable) {
                module.animX = (float) RenderUtil.getAnimationStateSmooth(startX, module.animX, 8f / Minecraft.getDebugFPS());
            }else {
                if(!(module.animX >= sr.getScaledWidth()-1)){
                    module.animX = (float) RenderUtil.getAnimationStateSmooth(sr.getScaledWidth(), module.animX, 8f / Minecraft.getDebugFPS());
                }
                if (module.animX >= sr.getScaledWidth()-1){
                    module.preDisable = false;
                    module.enable = false;
                }
            }
            timer.reset();
            yStart += 11;
        }

        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1);
        drawPotionStatus(new ScaledResolution(mc));
    }
    private void drawPotionStatus(ScaledResolution sr) {
        int y = 0;
        for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
            int ychat;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName(), new Object[0]);
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = String.valueOf(PType) + " II";
                    break;
                }
                case 2: {
                    PType = String.valueOf(PType) + " III";
                    break;
                }
                case 3: {
                    PType = String.valueOf(PType) + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = String.valueOf(PType) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
            }
            int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 5 : -10;
            mc.fontRendererObj.drawStringWithShadow(PType, sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType) - 2, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT + y - 12 - ychat, potion.getLiquidColor());
            y -= 10;
        }
    }

}

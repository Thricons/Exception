package cn.exception.module.impl.render;

import cn.exception.event.EventTarget;
import cn.exception.event.events.EventRender2D;
import cn.exception.module.Module;
import cn.exception.module.impl.combat.KillAura;
import cn.exception.module.value.Mode;
import cn.exception.utils.AnimationUtil;
import cn.exception.utils.RenderUtil;
import cn.exception.utils.fonts.CFontRenderer;
import cn.exception.utils.fonts.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author MiLiBlue
 **/
public class TargetHUD extends Module {
    private double healthBarWidth;
    private double healthBarWidth2;
    private double hudHeight;
    private Mode mode = new Mode("Mode", new String[]{"Hanabi", "Simple"}, "Hanabi");
    private ScaledResolution sr = new ScaledResolution(mc);
    private EntityLivingBase target;
    public TargetHUD(){
        super("TargetHUD", Category.Render);
        addValues(mode);
    }

    @EventTarget
    public void onRender(EventRender2D e){
        target = KillAura.curTarget;
        if(mode.isCurrentMode("Hanabi")) {
            //Distance TH code by Mymylesaws
            int blackcolor = new Color(0, 0, 0, 180).getRGB();
            int blackcolor2 = new Color(200, 200, 200, 160).getRGB();
            ScaledResolution sr2 = new ScaledResolution(mc);
            float scaledWidth = sr2.getScaledWidth();
            float scaledHeight = sr2.getScaledHeight();
            CFontRenderer font1 = FontLoaders.kiona16;
            boolean nulltarget = false;

            nulltarget = target == null;

            float x = scaledWidth / 2.0f - 50;
            float y = scaledHeight / 2.0f + 32;
            float health;
            double hpPercentage;
            Color hurt;
            int healthColor;
            String healthStr;
            if (nulltarget) {
                health = 0;
                hpPercentage = health / 20;
                hurt = Color.getHSBColor(300f / 360f, ((float) 0 / 10f) * 0.37f, 1f);
                healthStr = String.valueOf((float) 0 / 2.0f);
                healthColor = getHealthColor(0, 20).getRGB();
            } else {
                health = target.getHealth();
                hpPercentage = health / target.getMaxHealth();
                hurt = Color.getHSBColor(310f / 360f, ((float) target.hurtTime / 10f), 1f);
                healthStr = String.valueOf((float) (int) (target.getHealth()) / 2.0f);
                healthColor = getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB();
            }
            hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
            double hpWidth = 140.0 * hpPercentage;

            if (nulltarget) {
                this.healthBarWidth2 = RenderUtil.getAnimationStateSmooth(0, this.healthBarWidth2, 6f / Minecraft.getDebugFPS());
                this.healthBarWidth = RenderUtil.getAnimationStateSmooth(0, this.healthBarWidth, 14f / Minecraft.getDebugFPS());

                this.hudHeight = RenderUtil.getAnimationStateSmooth(0.0, this.hudHeight, 8f / Minecraft.getDebugFPS());
            } else {
                this.healthBarWidth2 = AnimationUtil.moveUD((float) this.healthBarWidth2, (float) hpWidth, 6f / Minecraft.getDebugFPS(), 3f / Minecraft.getDebugFPS());
                this.healthBarWidth = RenderUtil.getAnimationStateSmooth(hpWidth, this.healthBarWidth, 14f / Minecraft.getDebugFPS());

                this.hudHeight = RenderUtil.getAnimationStateSmooth(40.0, this.hudHeight, 8f / Minecraft.getDebugFPS());
            }

            if (hudHeight == 0) {
                this.healthBarWidth2 = 140;
                this.healthBarWidth = 140;
            }

            GL11.glEnable(3089);
            RenderUtil.prepareScissorBox(x, (float) ((double) y + 40 - hudHeight), x + 140.0f, (float) ((double) y + 40));
            RenderUtil.drawRect(x, y, x + 140.0f, y + 40.0f, blackcolor);
            RenderUtil.drawRect(x, y + 37.0f, (x) + 140, y + 40f, new Color(0, 0, 0, 49).getRGB());

            RenderUtil.drawRect(x, y + 37.0f, (float) (x + this.healthBarWidth2), y + 40.0f, new Color(255, 0, 213, 220).getRGB());
            RenderUtil.drawGradientSideways(x, y + 37.0f, (x + this.healthBarWidth), y + 40.0f, new Color(0, 81, 179).getRGB(), healthColor);

            font1.drawStringWithShadow(healthStr, x + 40.0f + 85.0f - (float) font1.getStringWidth(healthStr) / 2.0f + mc.fontRendererObj.getStringWidth("\u2764") / 1.9f, y + 27.0f, blackcolor2);
            mc.fontRendererObj.drawStringWithShadow("\u2764", x + 40.0f + 85.0f - (float) font1.getStringWidth(healthStr) / 2.0f - mc.fontRendererObj.getStringWidth("\u2764") / 1.9f, y + 26.5f, hurt.getRGB());

            CFontRenderer font2 = FontLoaders.kiona14;
            if (nulltarget) {
                font2.drawStringWithShadow("XYZ:" + 0 + " " + 0 + " " + 0 + " | " + "Hurt: " + (false), x + 37f, y + 15f, -1);
                font1.drawStringWithShadow("(No target)", x + 36.0f, y + 5.0f, -1);
            } else {
                font2.drawStringWithShadow("XYZ:" + (int) target.posX + " " + (int) target.posY + " " + (int) target.posZ + " | " + "Hurt: " + (target.hurtTime > 0), x + 37f, y + 15f, -1);

                if ((target instanceof EntityPlayer)) {
                    font2.drawStringWithShadow("Block:" + " " + (((EntityPlayer) target).isBlocking() ? "True" : "False"), x + 37f, y + 25f, -1);
                }

                font1.drawStringWithShadow(target.getName(), x + 36f, y + 4.0f, -1);

                if ((target instanceof EntityPlayer)) {
                    GlStateManager.resetColor();
                    mc.getTextureManager().bindTexture(((AbstractClientPlayer) target).getLocationSkin());

                    GlStateManager.color(1, 1, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 3, (int) y + 3, 8.0F, 8.0F, 8, 8, 32, 32, 64, 64);
                }
            }
            GL11.glDisable(3089);
        }
        if(mode.isCurrentMode("Simple")){
            if(target != null) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                mc.fontRendererObj.drawStringWithShadow(KillAura.curTarget.getName(), sr.getScaledWidth() / 2.0f - mc.fontRendererObj.getStringWidth(KillAura.curTarget.getName()) / 2.0f, sr.getScaledHeight() / 2.0f - 33.0f, 16777215);
                RenderHelper.enableGUIStandardItemLighting();
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                for (int n = 0; n < KillAura.curTarget.getMaxHealth() / 2.0f; ++n) {
                    mc.ingameGUI.drawTexturedModalRect(sr.getScaledWidth() / 2 - KillAura.curTarget.getMaxHealth() / 2.0f * 10.0f / 2.0f + n * 10, (float) (sr.getScaledHeight() / 2 - 20), 16, 0, 9, 9);
                }
                for (int n2 = 0; n2 < KillAura.curTarget.getHealth() / 2.0f; ++n2) {
                    mc.ingameGUI.drawTexturedModalRect(sr.getScaledWidth() / 2 - KillAura.curTarget.getMaxHealth() / 2.0f * 10.0f / 2.0f + n2 * 10, (float) (sr.getScaledHeight() / 2 - 20), 52, 0, 9, 9);
                }
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                RenderHelper.disableStandardItemLighting();
            }
        }
    }

    public static Color getHealthColor(float health, float maxHealth) {
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(0, 81, 179), new Color(0, 153, 255), new Color(47, 154, 241)};
        float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter();
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indices = getFractionIndices(fractions, progress);
            float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    public static int[] getFractionIndices(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }
}

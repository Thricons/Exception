package cn.exception.ui.clickgui;

import cn.exception.utils.AnimationUtil;
import com.google.common.collect.Lists;
import cn.exception.module.Module;
import cn.exception.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class ClickUi extends GuiScreen
{
    public static ArrayList<Window> windows;
    public double opacity;
    public int scrollVelocity;
    public static boolean binding;
    private float animpos;
    public float imageX;
    private int imageNumber;
    public ClickUi() {
        this.opacity = 0.0;
        this.animpos = 75.0f;
        if (ClickUi.windows.isEmpty()) {
            int x = 5;
            for (final Module.Category c : Module.Category.values()) {
                ClickUi.windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.opacity = ((this.opacity + 10.0 < 200.0) ? (this.opacity += 10.0) : 200.0);
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(this.mc);
        final float scale = scaledRes.getScaleFactor() / (float)Math.pow(scaledRes.getScaleFactor(), 2.0);
        ClickUi.windows.forEach(w -> w.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            this.scrollVelocity = ((wheel < 0) ? -120 : ((wheel > 0) ? 120 : 0));
        }
        ClickUi.windows.forEach(w -> w.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        ClickUi.windows.forEach(w -> w.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && !ClickUi.binding) {
            this.mc.displayGuiScreen((GuiScreen)null);
            return;
        }
        ClickUi.windows.forEach(w -> w.key(typedChar, keyCode));
    }
    
    public void initGui() {
        imageNumber = new SecureRandom().nextInt(3);
        super.initGui();
    }
    
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        imageX = 0;
    }
    
    public synchronized void sendToFront(final Window window) {
        int panelIndex = 0;
        for (int i = 0; i < ClickUi.windows.size(); ++i) {
            if (ClickUi.windows.get(i) == window) {
                panelIndex = i;
                break;
            }
        }
        final Window t = ClickUi.windows.get(ClickUi.windows.size() - 1);
        ClickUi.windows.set(ClickUi.windows.size() - 1, ClickUi.windows.get(panelIndex));
        ClickUi.windows.set(panelIndex, t);
    }
    
    static {
        ClickUi.windows =  Lists.newArrayList();
        ClickUi.binding = false;
    }
}

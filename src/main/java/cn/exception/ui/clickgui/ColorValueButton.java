package cn.exception.ui.clickgui;

import cn.exception.module.Module;
import cn.exception.module.impl.render.HUD;
import cn.exception.utils.fonts.FontLoaders;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ColorValueButton extends ValueButton
{
    public Module.Category category;
    private float[] hue;
    private int position;
    private int color;
    
    public ColorValueButton(final Module.Category category, final int x, final int y) {
        super(category, null, x, y);
        this.hue = new float[]{10.0f};
        this.color = new Color(125, 125, 125).getRGB();
        this.category = category;
        this.custom = true;
        this.position = -1111;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        final float[] huee = { this.hue[0] };
        int i = this.x - 7;
        GL11.glEnable(3089);
        limitation.cut();
        FontLoaders.kiona14.drawString("Color", this.x - 7, this.y + 2, new Color(108, 108, 108).getRGB());
        while (i < this.x + 79) {
            final Color color = Color.getHSBColor(huee[0] / 255.0f, 0.7f, 1.0f);
            if (mouseX > i - 1 && mouseX < i + 1 && mouseY > this.y - 6 && mouseY < this.y + 12 && Mouse.isButtonDown(0)) {
                this.color = color.getRGB();
                this.position = i;
            }
            if (this.color == color.getRGB()) {
                this.position = i;
            }
            Gui.drawRect(i - 1, this.y, i, this.y + 8, color.getRGB());
            final float[] arrf = huee;
            arrf[0] += 4.0f;
            if (huee[0] > 255.0f) {
                huee[0] -= 255.0f;
            }
            ++i;
        }
        Gui.drawRect(this.position, this.y, this.position + 1, this.y + 8, -1);
        if (this.hue[0] > 0f) {
            this.hue[0] -= 5.0f;
        }
        GL11.glDisable(3089);
        HUD.MAIN = color;
    }
    
    @Override
    public void key(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
    }
}

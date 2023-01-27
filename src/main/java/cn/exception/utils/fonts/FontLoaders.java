package cn.exception.utils.fonts;

import cn.exception.utils.resources.Base64Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public abstract class FontLoaders
{
    public static CFontRenderer kiona14;
    public static CFontRenderer kiona16;
    public static CFontRenderer kiona17;
    public static CFontRenderer kiona18;
    public static CFontRenderer kiona20;
    public static CFontRenderer kiona22;
    public static CFontRenderer kiona24;
    public static CFontRenderer kiona26;
    public static CFontRenderer kiona28;

    private static Font getKiona(final int size) {
        Font font;
        try {
            final InputStream is = Base64Resources.getResources(Base64Resources.kionaBase64, "kiona.ttf", true);
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    static {
        FontLoaders.kiona14 = new CFontRenderer(getKiona(14), true, true);
        FontLoaders.kiona16 = new CFontRenderer(getKiona(16), true, true);
        FontLoaders.kiona17 = new CFontRenderer(getKiona(17), true, true);
        FontLoaders.kiona18 = new CFontRenderer(getKiona(18), true, true);
        FontLoaders.kiona20 = new CFontRenderer(getKiona(20), true, true);
        FontLoaders.kiona22 = new CFontRenderer(getKiona(22), true, true);
        FontLoaders.kiona24 = new CFontRenderer(getKiona(24), true, true);
        FontLoaders.kiona26 = new CFontRenderer(getKiona(26), true, true);
        FontLoaders.kiona28 = new CFontRenderer(getKiona(28), true, true);
    }
}

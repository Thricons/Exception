package cn.exception.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class RenderUtil {
    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    public static void drawCircle( float cx, float cy, float r, int num_segments, int c ) {
        GL11.glPushMatrix( );
        cx *= 2.0F;
        cy *= 2.0F;
        float f = ( c >> 24 & 0xFF ) / 255.0F;
        float f1 = ( c >> 16 & 0xFF ) / 255.0F;
        float f2 = ( c >> 8 & 0xFF ) / 255.0F;
        float f3 = ( c & 0xFF ) / 255.0F;
        float theta = ( float ) ( 6.2831852D / num_segments );
        float p = ( float ) Math.cos( theta );
        float s = ( float ) Math.sin( theta );
        float x = r *= 2.0F;
        float y = 0.0F;
        enableGL2D( );
        GL11.glScalef( 0.5F, 0.5F, 0.5F );
        GL11.glColor4f( f1, f2, f3, f );
        GL11.glBegin( 2 );
        int ii = 0;
        while ( ii < num_segments ) {
            GL11.glVertex2f( x + cx, y + cy );
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd( );
        GL11.glScalef( 2.0F, 2.0F, 2.0F );
        disableGL2D( );
        GlStateManager.color( 1, 1, 1, 1 );
        GL11.glPopMatrix( );
    }

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
    }

    public static void triangle( float x1, float y1, float x2, float y2, float x3, float y3, int fill ) {
        RenderUtil.enableGL2D( );
        GlStateManager.color( 0, 0, 0 );
        GL11.glColor4f( 0, 0, 0, 0 );

        float var11 = ( float ) ( fill >> 24 & 255 ) / 255.0F;
        float var6 = ( float ) ( fill >> 16 & 255 ) / 255.0F;
        float var7 = ( float ) ( fill >> 8 & 255 ) / 255.0F;
        float var8 = ( float ) ( fill & 255 ) / 255.0F;

        GlStateManager.enableBlend( );
        GlStateManager.disableTexture2D( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.color( var6, var7, var8, var11 );

        GL11.glBegin( GL11.GL_TRIANGLE_FAN );
        GL11.glVertex2f( x1, y1 );
        GL11.glVertex2f( x3, y3 );
        GL11.glVertex2f( x2, y2 );
        GL11.glVertex2f( x1, y1 );
        GL11.glEnd( );

        GlStateManager.enableTexture2D( );
        GlStateManager.disableBlend( );
        RenderUtil.disableGL2D( );
    }

    public static void drawRoundedRect( float x, float y, float x2, float y2, final float round, final int color ) {
        x += ( float ) ( round / 2.0f + 0.5 );
        y += ( float ) ( round / 2.0f + 0.5 );
        x2 -= ( float ) ( round / 2.0f + 0.5 );
        y2 -= ( float ) ( round / 2.0f + 0.5 );
        Gui.drawRect( ( int ) x, ( int ) y, ( int ) x2, ( int ) y2, color );
        circle( x2 - round / 2.0f, y + round / 2.0f, round, color );
        circle( x + round / 2.0f, y2 - round / 2.0f, round, color );
        circle( x + round / 2.0f, y + round / 2.0f, round, color );
        circle( x2 - round / 2.0f, y2 - round / 2.0f, round, color );
        Gui.drawRect( ( int ) ( x - round / 2.0f - 0.5f ), ( int ) ( y + round / 2.0f ), ( int ) x2, ( int ) ( y2 - round / 2.0f ), color );
        Gui.drawRect( ( int ) x, ( int ) ( y + round / 2.0f ), ( int ) ( x2 + round / 2.0f + 0.5f ), ( int ) ( y2 - round / 2.0f ), color );
        Gui.drawRect( ( int ) ( x + round / 2.0f ), ( int ) ( y - round / 2.0f - 0.5f ), ( int ) ( x2 - round / 2.0f ), ( int ) ( y2 - round / 2.0f ), color );
        Gui.drawRect( ( int ) ( x + round / 2.0f ), ( int ) y, ( int ) ( x2 - round / 2.0f ), ( int ) ( y2 + round / 2.0f + 0.5f ), color );
    }

    public static void circle( final float x, final float y, final float radius, final int fill ) {
        arc( x, y, 0.0f, 360.0f, radius, fill );
    }

    public static void circle( final float x, final float y, final float radius, final Color fill ) {
        arc( x, y, 0.0f, 360.0f, radius, fill );
    }

    public static void arc( final float x, final float y, final float start, final float end, final float radius, final int color ) {
        arcEllipse( x, y, start, end, radius, radius, color );
    }

    public static void arc( final float x, final float y, final float start, final float end, final float radius, final Color color ) {
        arcEllipse( x, y, start, end, radius, radius, color );
    }

    public static void arcEllipse( final float x, final float y, float start, float end, final float w, final float h, final Color color ) {
        GlStateManager.color( 0.0f, 0.0f, 0.0f );
        GL11.glColor4f( 0.0f, 0.0f, 0.0f, 0.0f );
        float temp = 0.0f;
        if ( start > end ) {
            temp = end;
            end = start;
            start = temp;
        }
        final Tessellator var9 = Tessellator.getInstance( );
        final WorldRenderer var10 = var9.getWorldRenderer( );
        GlStateManager.enableBlend( );
        GlStateManager.disableTexture2D( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.color( color.getRed( ) / 255.0f, color.getGreen( ) / 255.0f, color.getBlue( ) / 255.0f, color.getAlpha( ) / 255.0f );
        if ( color.getAlpha( ) > 0.5f ) {
            GL11.glEnable( 2848 );
            GL11.glLineWidth( 2.0f );
            GL11.glBegin( 3 );
            for ( float i = end; i >= start; i -= 4.0f ) {
                final float ldx = ( float ) Math.cos( i * 3.141592653589793 / 180.0 ) * w * 1.001f;
                final float ldy = ( float ) Math.sin( i * 3.141592653589793 / 180.0 ) * h * 1.001f;
                GL11.glVertex2f( x + ldx, y + ldy );
            }
            GL11.glEnd( );
            GL11.glDisable( 2848 );
        }
        GL11.glBegin( 6 );
        for ( float i = end; i >= start; i -= 4.0f ) {
            final float ldx = ( float ) Math.cos( i * 3.141592653589793 / 180.0 ) * w;
            final float ldy = ( float ) Math.sin( i * 3.141592653589793 / 180.0 ) * h;
            GL11.glVertex2f( x + ldx, y + ldy );
        }
        GL11.glEnd( );
        GlStateManager.enableTexture2D( );
        GlStateManager.disableBlend( );
    }


    public static void arcEllipse( final float x, final float y, float start, float end, final float w, final float h, final int color ) {
        GlStateManager.color( 0.0f, 0.0f, 0.0f );
        GL11.glColor4f( 0.0f, 0.0f, 0.0f, 0.0f );
        float temp = 0.0f;
        if ( start > end ) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = ( color >> 24 & 0xFF ) / 255.0f;
        final float var12 = ( color >> 16 & 0xFF ) / 255.0f;
        final float var13 = ( color >> 8 & 0xFF ) / 255.0f;
        final float var14 = ( color & 0xFF ) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance( );
        final WorldRenderer var16 = var15.getWorldRenderer( );
        GlStateManager.enableBlend( );
        GlStateManager.disableTexture2D( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.color( var12, var13, var14, var11 );
        if ( var11 > 0.5f ) {
            GL11.glEnable( 2848 );
            GL11.glLineWidth( 2.0f );
            GL11.glBegin( 3 );
            for ( float i = end; i >= start; i -= 4.0f ) {
                final float ldx = ( float ) Math.cos( i * 3.141592653589793 / 180.0 ) * w * 1.001f;
                final float ldy = ( float ) Math.sin( i * 3.141592653589793 / 180.0 ) * h * 1.001f;
                GL11.glVertex2f( x + ldx, y + ldy );
            }
            GL11.glEnd( );
            GL11.glDisable( 2848 );
        }
        GL11.glBegin( 6 );
        for ( float i = end; i >= start; i -= 4.0f ) {
            final float ldx = ( float ) Math.cos( i * 3.141592653589793 / 180.0 ) * w;
            final float ldy = ( float ) Math.sin( i * 3.141592653589793 / 180.0 ) * h;
            GL11.glVertex2f( x + ldx, y + ldy );
        }
        GL11.glEnd( );
        GlStateManager.enableTexture2D( );
        GlStateManager.disableBlend( );
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }
    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) ((int) (x * (float) factor)), (int) ((int) (((float) scale.getScaledHeight() - y2) * (float) factor)), (int) ((int) ((x2 - x) * (float) factor)), (int) ((int) ((y2 - y) * (float) factor)));
    }
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;

        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);

        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(255, 255, 255, 255);
    }

    public static double getAnimationStateSmooth(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            if (current + factor > target) {
                current = target;
            } else {
                current += factor;
            }
        } else {
            if (current - factor < target) {
                current = target;
            } else {
                current -= factor;
            }
        }
        return current;
    }
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawGradientRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        RenderUtil.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtil.glColor(topColor);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        glColor(bottomColor);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        RenderUtil.disableGL2D();
    }
    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}

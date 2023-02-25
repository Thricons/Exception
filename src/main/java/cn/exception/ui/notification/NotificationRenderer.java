package cn.exception.ui.notification;

import cn.exception.utils.Colors;
import cn.exception.utils.RenderUtil;
import cn.exception.utils.fonts.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class NotificationRenderer implements INotificationRenderer{
    private final ResourceLocation logo = new ResourceLocation("Distance/music.png");

    public void draw(List<Notification> notifications) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        float y = (float) scaledRes.getScaledHeight() - (float) (notifications.size() * 32 + 12 + (mc.currentScreen instanceof GuiChat ? 12 : 0));

        for (Iterator<?> var5 = notifications.iterator(); var5.hasNext(); y += 32.0F) {
            INotification notification = (INotification) var5.next();
            Notification not = (Notification) notification;
            not.translate.interpolate(not.getTarX(), y, 0.3F);
            float subHeaderWidth = FontLoaders.kiona16.getStringWidth(not.getSubtext());
            float headerWidth = FontLoaders.kiona20.getStringWidth(not.getHeader() + (not.getType().equals(Notifications.Type.MUSIC)? "( \u2022ω\u2022)\u266A~":""));
            float x = (float) (scaledRes.getScaledWidth() - 5 - 1 - 63) - (Math.max(headerWidth, subHeaderWidth));
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.prepareScissorBox((int) not.translate.getX(), not.translate.getY(), scaledRes.getScaledWidth(), not.translate.getY() + 30F);

            //GL11.glScissor((int) not.translate.getX() * s, (int) ((float) scaledRes.getScaledWidth() + 3 - not.translate.getY() * (float) s), scaledRes.getScaledWidth() * s,(int)((not.translate.getY() + 50.0F) * s));
            RenderUtil.drawRoundedRect(x - (not.getType().equals(Notifications.Type.MUSIC)?2:0), not.translate.getY(), scaledRes.getScaledWidth() - 5, not.translate.getY() + 30F, 2, new Color(10, 10, 10, 200).getRGB());
//            if (not.getType().equals(Notifications.Type.MUSIC)) {
//                if ( MusicManager.INSTANCE.circleLocations.containsKey(not.id)) {
//                    GL11.glPushMatrix();
//                    ResourceLocation icon = MusicManager.INSTANCE.circleLocations.get(not.id);
//                    RenderUtil.circle(x + 13.0F,not.translate.getY() + 14.5f,11,new Color(255, 76, 76));
//
//                    RenderUtil.drawImage(icon, (int)(x + 3.0F), (int)(not.translate.getY() + 10f), 20, 20);
//                    GL11.glPopMatrix();
//                } else {
//                    MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
//                }
//            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 10.0F, not.translate.getY() + 13.0F, 0.0F);
                GlStateManager.rotate(270.0F, 0.0F, 0.0F, 90.0F);
                GlStateManager.scale(1f, 1f, 1f);

                for (int i = 0; i < 11; ++i) {
                    RenderUtil.drawCircle(-4.0F, 0.0F + 3, (float) (8 - i), 3, this.getColor(not.getType()));
                }

                //RenderingUtil.drawCircle(0.0F, 0.0F, 11.0F, 3, Colors.getColor(0));
                GlStateManager.popMatrix();
                GlStateManager.scale(1f, 1f, 1f);
                RenderUtil.rectangle((double) x + 2 + 1 + 9.6D, not.translate.getY() + 5 + 6.0F, (double) x + 2 + 1 + 10.3D, not.translate.getY() + 4 + 13.0F, Colors.getColor(0));
                RenderUtil.rectangle((double) x + 2 + 1 + 9.6D, not.translate.getY() - 1 + 4 + 15.0F, (double) x + 2 + 1 + 10.3D, not.translate.getY() + 4 - 1 + 17.0F, Colors.getColor(0));

            //}
            FontLoaders.kiona20.drawStringWithShadow(not.getHeader() + (not.getType().equals(Notifications.Type.MUSIC)? ((System.currentTimeMillis() / 100L) % 30 == 0 ?"( - ω -)" : "( \u2022ω\u2022)") + "\u266A~":""), x + 2 + 24.0F, not.translate.getY() + 4 + 5F, -1);
            FontLoaders.kiona16.drawStringWithShadow(not.getSubtext(), x + 2 + 24.0F, not.translate.getY() + 4 + 14F, -1);
            RenderUtil.triangle(scaledRes.getScaledWidth() - 5, not.translate.getY() + 5 + 15, scaledRes.getScaledWidth() - 5, not.translate.getY() + 5 + 5, scaledRes.getScaledWidth(), not.translate.getY() + 5 + 10, new Color(10, 10, 10, 200).getRGB());

            GL11.glDisable(3089);
            GL11.glPopMatrix();
            if (not.checkTime() >= not.getDisplayTime() + not.getStart()) {
                if (!not.isplayed && not.getType().equals(Notifications.Type.MUSIC)){
                  //  new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.NowPlayingOut, -5);
                    not.isplayed = true;
                }
                not.setTarX(scaledRes.getScaledWidth());
                if (not.translate.getX() >= (float) scaledRes.getScaledWidth()) {
                    notifications.remove(notification);
                }
            }
        }
    }

    private int getColor(Notifications.Type type) {
        int color = 0;
        switch (type) {
            case INFO:
                color = Colors.getColor(64, 131, 214);
                break;
            case NOTIFY:
                color = Colors.getColor(242, 206, 87);
                break;
            case WARNING:
                color = Colors.getColor(226, 74, 74);
                break;
            case MUSIC:
                color = Colors.getColor(250, 10, 10);
        }

        return color;
    }
}

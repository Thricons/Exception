package cn.exception.manager;

import cn.exception.event.events.EventPacket;
import cn.exception.utils.PlayerUtil;
import cn.exception.utils.RefUtil;
import com.sun.jna.platform.win32.Kernel32;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * @author IDK But not me
 */
public class PacketManager {//Skid

    public static PacketManager instance = null;

    private float yaw;
    private float pitch;
    public PacketManager(){
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        NetworkManager netMgr;
        if (e == null) {
            netMgr = mc.thePlayer.sendQueue.getNetworkManager();
        } else
            netMgr = e.manager;
        if (netMgr != null)
            netMgr.channel().pipeline().addBefore("packet_handler", "SubMiLiBlue", new PacketListener());
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
class PacketListener extends ChannelDuplexHandler {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        EventPacket e = new EventPacket((Packet) msg, EventPacket.PacketType.Client);
        e.call();
        if(e.isCancelled()){
            return;
        }else {
            if(PacketManager.instance.getPitch() != 0){
                if(msg instanceof C03PacketPlayer){
                    C03PacketPlayer c03 = (C03PacketPlayer) msg;
                    RefUtil.set(c03, PacketManager.instance.getPitch(), "pitch", "field_149473_f");
                    e.setPacket(c03);
                    PacketManager.instance.setPitch(0);
                }
                if(msg instanceof C03PacketPlayer.C05PacketPlayerLook){
                    C03PacketPlayer.C05PacketPlayerLook c03 = (C03PacketPlayer.C05PacketPlayerLook) msg;
                    RefUtil.set(c03, PacketManager.instance.getPitch(), "pitch", "field_149473_f");
                    e.setPacket(c03);
                    PacketManager.instance.setPitch(0);
                }
                if(msg instanceof C03PacketPlayer.C06PacketPlayerPosLook){
                    C03PacketPlayer.C06PacketPlayerPosLook c03 = (C03PacketPlayer.C06PacketPlayerPosLook) msg;
                    RefUtil.set(c03, PacketManager.instance.getPitch(), "pitch", "field_149473_f");
                    e.setPacket(c03);
                    PacketManager.instance.setPitch(0);
                }
            }
            if(PacketManager.instance.getYaw() != 0){
                if(msg instanceof C03PacketPlayer){
                    C03PacketPlayer c03 = (C03PacketPlayer) msg;
                    RefUtil.set(c03, PacketManager.instance.getPitch(), "yaw", "field_149476_e");
                    e.setPacket(c03);
                    PacketManager.instance.setYaw(0);
                }
                if(msg instanceof C03PacketPlayer.C05PacketPlayerLook){
                    C03PacketPlayer.C05PacketPlayerLook c03 = (C03PacketPlayer.C05PacketPlayerLook) msg;
                    RefUtil.set(c03, PacketManager.instance.getPitch(), "yaw", "field_149476_e");
                    e.setPacket(c03);
                    PacketManager.instance.setPitch(0);
                }
                if(msg instanceof C03PacketPlayer.C06PacketPlayerPosLook){
                    C03PacketPlayer.C06PacketPlayerPosLook c03 = (C03PacketPlayer.C06PacketPlayerPosLook) msg;
                    RefUtil.set(c03, PacketManager.instance.getPitch(), "yaw", "field_149476_e");
                    e.setPacket(c03);
                    PacketManager.instance.setPitch(0);
                }
            }
            super.write(ctx, e.getPacket(), promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        EventPacket e = new EventPacket((Packet) msg, EventPacket.PacketType.Server);
        e.call();
        if(e.isCancelled()){
            return;
        }else {
            super.channelRead(ctx, e.getPacket());
        }
    }
}

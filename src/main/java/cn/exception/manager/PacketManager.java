package cn.exception.manager;

import cn.exception.event.events.EventPacket;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * @author IDK But not me
 */
public class PacketManager {//Skid

    public static PacketManager instance = new PacketManager();
    public NetworkManager networkManager;
    public PacketManager(){
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e){
        if(networkManager == null){
            this.networkManager = Minecraft.getMinecraft().getNetHandler().getNetworkManager();
        }else {
            this.networkManager = e.manager;
        }
        if(networkManager != null){
            networkManager.channel().pipeline().addBefore("packet_handler", "SubMiLiBlue", new PacketListener());
        }
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

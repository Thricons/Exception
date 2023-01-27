package cn.exception.event.events;

import cn.exception.event.Event;
import net.minecraft.network.Packet;

/**
 * @author: Thr1c0/s
 * @date: 2022/8/3 12:58
 */
public class EventPacket extends Event {
    private Packet packet;
    private PacketType packetType;

    public EventPacket(Packet packet, PacketType packetType) {
        super(Type.PRE);
        this.packet = packet;
        this.packetType = packetType;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }

    public static enum PacketType{
        Client, Server
    }
}

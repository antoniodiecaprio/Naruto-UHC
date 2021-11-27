package fr.lyneris.narutouhc.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {
    private final List<PacketListener> listeners;

    public PacketManager() {
        this.listeners = new ArrayList<>();
    }

    public void onJoin(final Player player) {
        final CraftPlayer nmsPlayer = (CraftPlayer) player;
        final Channel channel = nmsPlayer.getHandle().playerConnection.networkManager.channel;
        if (channel.pipeline().get("AtlantisPacketListener") != null) {
            return;
        }
        channel.pipeline().addAfter("decoder", "AtlantisPacketListener", new MessageToMessageDecoder<Packet<?>>() {
            protected void decode(final ChannelHandlerContext channel, final Packet<?> packet, final List<Object> arg) {
                arg.add(packet);
                listeners.forEach(packetListener -> packetListener.recievePacket(player, packet));
            }
        });
    }

    public void onQuit(final Player player) {
        final CraftPlayer nmsPlayer = (CraftPlayer) player;
        final Channel channel = nmsPlayer.getHandle().playerConnection.networkManager.channel;
        if (channel.pipeline().get("AtlantisPacketListener") != null) {
            channel.pipeline().remove("AtlantisPacketListener");
        }
    }

    public void addListener(final PacketListener listener) {
        this.listeners.add(listener);
    }
}

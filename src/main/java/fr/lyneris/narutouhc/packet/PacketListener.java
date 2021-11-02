package fr.lyneris.narutouhc.packet;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public interface PacketListener
{
    void recievePacket(final Player p0, final Packet<?> p1);
}

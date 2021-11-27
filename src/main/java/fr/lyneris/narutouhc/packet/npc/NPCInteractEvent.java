package fr.lyneris.narutouhc.packet.npc;

import org.bukkit.entity.Player;

public interface NPCInteractEvent {
    void onClick(final Player p0, final NPC p1);

    void onLeftClick(final Player p0, final NPC p1);

    void onRightClick(final Player p0, final NPC p1);
}

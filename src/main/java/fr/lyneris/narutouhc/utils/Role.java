package fr.lyneris.narutouhc.utils;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Role {

    public static Player findPlayer(String var1) {
        Player player = null;
        for (UUID uuid : UHC.getUhc().getGameManager().getPlayers()) {
            if(NarutoUHC.getNaruto().getRoleManager().getRole(uuid) != null && NarutoUHC.getNaruto().getRoleManager().getRole(uuid).getRoleName().equalsIgnoreCase(var1) && Bukkit.getPlayer(uuid) != null) {
                player = Bukkit.getPlayer(uuid);
            }
        }
        return player;
    }

    public static boolean isRole(Player var1, String var2) {
        if(NarutoUHC.getNaruto().getRoleManager().getRole(var1) == null) {
            return false;
        } else {
            return NarutoUHC.getNaruto().getRoleManager().getRole(var1).getRoleName().equalsIgnoreCase(var2);
        }
    }

    public static void knowsRole(Player player, String role) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(NarutoUHC.getNaruto(), () -> {
            if(findPlayer(role) == null) {
                player.sendMessage("§7▎ §fLe §a" + role + " §fn'est pas dans la composition de la partie");
            } else {
                player.sendMessage("§7▎ §fLe §a" + role + " §fde la partie est §a" + findPlayer(role).getName());
            }
        }, 20*3);

    }

}

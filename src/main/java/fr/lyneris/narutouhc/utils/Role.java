package fr.lyneris.narutouhc.utils;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Role {

    public static Player findPlayer(NarutoRoles var1) {
        Player player = null;
        for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
            try {
                if(NarutoUHC.getNaruto().getRoleManager().getRole(uuid) != null && NarutoUHC.getNaruto().getRoleManager().getRole(uuid).getRoleName().equals(var1.getNarutoRole().newInstance().getRoleName()) && Bukkit.getPlayer(uuid) != null) {
                    player = Bukkit.getPlayer(uuid);
                    break;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return player;
    }

    public static List<Player> getAliveOnlinePlayers() {
        List<Player> toReturn = new ArrayList<>();

        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .filter(player -> player.getGameMode() == GameMode.SPECTATOR)
                .forEach(toReturn::add);

        return toReturn;
    }

    public static boolean isRole(Player var1, NarutoRoles var2) {
        if(NarutoUHC.getNaruto().getRoleManager().getRole(var1) == null) {
            return false;
        } else {
            try {
                return NarutoUHC.getNaruto().getRoleManager().getRole(var1).getRoleName().equals(var2.getNarutoRole().newInstance().getRoleName());
            } catch (InstantiationException | IllegalAccessException ignored) {
                return false;
            }
        }
    }

    public static void knowsRole(Player player, NarutoRoles role) {
        Tasks.runAsyncLater(() -> {
            if(findPlayer(role) == null) {
                player.sendMessage(CC.prefix("§fLe §a" + role.getName() + " §fn'est pas dans la composition de la partie"));
            } else {
                player.sendMessage(CC.prefix("§fLe §a" + role.getName() + " §fde la partie est §a" + findPlayer(role).getName()));
            }
        }, 20*3);
    }

}

package fr.lyneris.narutouhc.utils;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Role {

    public static Player findPlayer(NarutoRoles var1) {
        Player player = null;
        for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
            if(NarutoUHC.getNaruto().getRoleManager().getRole(uuid) != null && NarutoUHC.getNaruto().getRoleManager().getRole(uuid).getClass().getName().equals(var1.getName()) && Bukkit.getPlayer(uuid) != null) {
                player = Bukkit.getPlayer(uuid);
                break;
            }
        }
        return player;
    }

    public static boolean isAlive(Player player) {
        return isAlive(player.getUniqueId());
    }

    public static boolean isAlive(UUID uuid) {
        return UHC.getUHC().getGameManager().getPlayers().contains(uuid);
    }

    public static List<Player> getAliveOnlinePlayers() {
        List<Player> toReturn = new ArrayList<>();

        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .filter(player -> player.getGameMode() != GameMode.SPECTATOR)
                .forEach(toReturn::add);

        return toReturn;
    }

    public static boolean isRole(Player var1, NarutoRoles var2) {
        if(NarutoUHC.getNaruto().getRoleManager().getRole(var1) == null) {
            return false;
            } else {
            return NarutoUHC.getNaruto().getRoleManager().getRole(var1).getClass().getName().equals(var2.getNarutoRole().getName());
        }
    }

    public static Camp getCamp(Player var1) {
        return NarutoUHC.getNaruto().getRoleManager().getCamp(var1);
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

    public static List<Player> getPlayersWithRole() {
        List<Player> toReturn = new ArrayList<>();

        getAliveOnlinePlayers().stream().filter(player -> NarutoUHC.getNaruto().getRoleManager().getRole(player) != null).forEach(toReturn::add);

        return toReturn;
    }

    public static NarutoRole getRole(Player var1) {
        return NarutoUHC.getNaruto().getRoleManager().getRole(var1);
    }

    public static List<Player> getPlayersInCamp(Camp camp) {
        List<Player> toReturn = new ArrayList<>();

        Role.getPlayersWithRole().stream().filter(p -> Role.getCamp(p) == camp).forEach(toReturn::add);

        return toReturn;
    }

    public static boolean won = false;
    public static void attemptWin() {
        if(won) return;
        HashMap<Camp, Integer> camp = new HashMap<>();
        for (Player player : getAliveOnlinePlayers()) {
            NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
            if(role != null && getCamp(player) != null) {
                camp.put(getCamp(player), camp.getOrDefault(getCamp(player), 0) + 1);
            }
        }
        if(camp.size() != 1) return;
        Camp winners = null;
        for (Camp camp1 : camp.keySet()) {
            winners = camp1;
            break;
        }
        won = true;
        String victoryTitle = CC.translate("&fVictoire du camp " + winners.getColor() + winners.getName());
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 10, 3*20, 10, "", victoryTitle));
        Bukkit.broadcastMessage(CC.CC_BAR);
        Bukkit.broadcastMessage(CC.prefix(victoryTitle));
        Bukkit.broadcastMessage(" ");
        getAliveOnlinePlayers().forEach(player -> {
            NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
            if(role != null && getCamp(player) != null) {
                Bukkit.broadcastMessage(CC.prefix("&6" + player.getName() + " &f&l» &e" + role.getRoleName()));
            }
        });
        Bukkit.broadcastMessage(CC.CC_BAR);
        Bukkit.broadcastMessage(CC.prefix("&cFermeture du serveur dans 60 secondes..."));


        Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Fermeture du serveur..."));
            Bukkit.getServer().shutdown();
        }, 60*20);
    }

}

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
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Role {

    public static boolean won = false;

    public static Player findPlayer(NarutoRoles var1) {
        Player player = null;
        for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(uuid) == null) continue;
            if (!(Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).getGameMode() != GameMode.SPECTATOR)) continue;
            if (!NarutoUHC.getNaruto().getRoleManager().getRole(uuid).getRole().equals(var1)) continue;
            player = Bukkit.getPlayer(uuid);
            break;
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

    public static boolean isRole(Entity var1, NarutoRoles var2) {
        if (NarutoUHC.getNaruto().getRoleManager().getRole(var1.getUniqueId()) == null) {
            return false;
        } else {
            return NarutoUHC.getNaruto().getRoleManager().getRole(var1.getUniqueId()).getRole().equals(var2);
        }
    }

    public static Camp getCamp(Player var1) {
        return NarutoUHC.getNaruto().getRoleManager().getCamp(var1);
    }

    public static void knowsRole(Player player, NarutoRoles role) {
        Tasks.runAsyncLater(() -> {
            if (findPlayer(role) == null) {
                player.sendMessage(CC.prefix("§fLe §a" + role.getName() + " §fn'est pas dans la composition de la partie"));
            } else {
                player.sendMessage(CC.prefix("§fLe §a" + role.getName() + " §fde la partie est §a" + findPlayer(role).getName()));
            }
        }, 20 * 3);
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

    public static int getItemAmount(Player player, Material material) {
        int toReturn = 0;
        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null && content.getType() == material) {
                toReturn += content.getAmount();
            }
        }

        return toReturn;
    }

    public static void removeItem(Player player, Material material, int remove) {
        if (player.getInventory().getItem(player.getInventory().first(material)).getAmount() <= remove) {
            player.getInventory().removeItem(player.getInventory().getItem(player.getInventory().first(material)));
            return;
        }
        player.getInventory().getItem(player.getInventory().first(material)).setAmount(player.getInventory().getItem(player.getInventory().first(material)).getAmount() - remove);
        if (remove > 64) {
            player.getInventory().getItem(player.getInventory().first(material)).setAmount(player.getInventory().getItem(player.getInventory().first(material)).getAmount() - (remove - 64));
        }
    }

    public static void attemptWin() {
        if (won) return;
        HashMap<Camp, Integer> camp = new HashMap<>();
        for (Player player : getAliveOnlinePlayers()) {
            NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
            if (role != null && getCamp(player) != null) {
                camp.put(getCamp(player), camp.getOrDefault(getCamp(player), 0) + 1);
            }
        }
        if (camp.size() != 1) return;
        Camp winners = null;
        for (Camp camp1 : camp.keySet()) {
            winners = camp1;
            break;
        }
        won = true;
        String victoryTitle = CC.translate("&fVictoire du camp " + winners.getColor() + winners.getName());
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 10, 3 * 20, 10, "", victoryTitle));
        Bukkit.broadcastMessage(CC.CC_BAR);
        Bukkit.broadcastMessage(CC.prefix(victoryTitle));
        Bukkit.broadcastMessage(" ");
        getAliveOnlinePlayers().forEach(player -> {
            NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
            if (role != null && getCamp(player) != null) {
                Bukkit.broadcastMessage(CC.prefix("&6" + player.getName() + " &f&l» &e" + role.getRoleName()));
            }
        });
        Bukkit.broadcastMessage(CC.CC_BAR);
        Bukkit.broadcastMessage(CC.prefix("&cFermeture du serveur dans 60 secondes..."));


        Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Fermeture du serveur..."));
            Bukkit.getServer().shutdown();
        }, 60 * 20);
    }

}

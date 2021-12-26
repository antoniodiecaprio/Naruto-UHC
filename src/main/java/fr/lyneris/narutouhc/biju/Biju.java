package fr.lyneris.narutouhc.biju;

import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class Biju {

    public abstract LivingEntity getLivingEntity();

    public abstract void setupBiju();

    public abstract String getName();

    public void spawnEntity() {
        Role.getAliveOnlinePlayers().forEach(player -> {
            if (Role.isRole(player, NarutoRoles.INO) || Role.isRole(player, NarutoRoles.OBITO)) {
                player.sendMessage(CC.prefix("&fVoici les coordonnées de " + getName() + " :"));
                player.sendMessage(CC.prefix("&a" + getSpawn().getBlockX() + "&f, &a" + getSpawn().getBlockY() + "&f, &a" + getSpawn().getBlockZ()));
            }
        });
        Player closest = null;
        for (Player player : Role.getAliveOnlinePlayers()) {
            if (closest == null) {
                closest = player;
            } else {
                if (player.getLocation().distance(getSpawn()) > closest.getLocation().distance(getSpawn())) {
                    closest = player;
                }
            }
        }
        assert closest != null;
        closest.sendMessage(CC.prefix("&fVous êtes le plus proche de " + getName()));
        closest.sendMessage(CC.prefix("&fVoici les coordonnées de " + getName() + " :"));
        closest.sendMessage(CC.prefix("&a" + getSpawn().getBlockX() + "&f, &a" + getSpawn().getBlockY() + "&f, &a" + getSpawn().getBlockZ()));
    }

    public abstract ItemStack getItem();

    public abstract void getItemInteraction(PlayerInteractEvent event, Player player);

    public UUID getMaster() {
        UUID toReturn = null;
        for (Player aliveOnlinePlayer : Role.getAliveOnlinePlayers()) {
            if (hasBiju(aliveOnlinePlayer) && getBiju(aliveOnlinePlayer) == this) {
                toReturn = aliveOnlinePlayer.getUniqueId();
                break;
            }
        }
        return toReturn;
    }

    public static boolean hasBiju(Player player) {
        return getBiju(player) != null;
    }

    public static Bijus getBijus(Player player) {
        Bijus toReturn = null;

        for (Bijus biju : Bijus.values()) {
            for (ItemStack content : player.getInventory().getContents()) {
                if (content.equals(biju.getBiju().getItem())) {
                    toReturn = biju;
                    break;
                }
            }
        }

        return toReturn;
    }


    public static Biju getBiju(Player player) {
        Biju toReturn = null;

        for (Bijus biju : Bijus.values()) {
            for (ItemStack content : player.getInventory().getContents()) {
                if (content.equals(biju.getBiju().getItem())) {
                    toReturn = biju.getBiju();
                    break;
                }
            }
        }

        return toReturn;
    }


    public abstract Location getSpawn();

}

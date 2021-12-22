package fr.lyneris.narutouhc.biju;

import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class Biju {

    private UUID master;

    public abstract void setupBiju();

    public abstract String getName();

    public void spawnEntity() {
        Role.getAliveOnlinePlayers().forEach(player -> {
            if(Role.isRole(player, NarutoRoles.INO) || Role.isRole(player, NarutoRoles.OBITO)) {
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

    public UUID getMaster() {
        return master;
    }

    public void setMaster(UUID master) {
        this.master = master;
    }

    public static boolean hasBiju(Player player) {
        return getBiju(player) != null;
    }

    public static Bijus getBiju(Player player) {
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

    public abstract Location getSpawn();

}

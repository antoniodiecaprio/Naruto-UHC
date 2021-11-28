package fr.lyneris.narutouhc.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class Armor {

    public static boolean hasArmor(Player player) {
        PlayerInventory inv = player.getInventory();
        return inv.getHelmet() != null || inv.getChestplate() != null || inv.getLeggings() != null || inv.getBoots() != null;
    }

}

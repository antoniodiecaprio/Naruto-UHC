package fr.lyneris.narutouhc.utils;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Item {

    public static String interactItem(String var1) {
        return "§7▎ §6§l" + var1 + " §8(§7Clic-droit§8)";
    }

    public static String specialItem(String var1) {
        return "§7▎ §6§l" + var1;
    }

    public static boolean interactItem(ItemStack var1, String var2) {
        if (!hasDisplayName(var1)) return false;
        return var1.getItemMeta().getDisplayName().equals("§7▎ §6§l" + var2 + " §8(§7Clic-droit§8)");
    }

    public static boolean interactItem(PlayerInteractEvent var1, String var2) {
        return interactItem(var1.getItem(), var2);
    }

    public static boolean specialItem(ItemStack var1, String var2) {
        if (!hasDisplayName(var1)) return false;
        return var1.getItemMeta().getDisplayName().equals("§7▎ §6§l" + var2);
    }

    public static boolean hasDisplayName(ItemStack var1) {
        return var1 != null && var1.hasItemMeta() && var1.getItemMeta().hasDisplayName();
    }

}

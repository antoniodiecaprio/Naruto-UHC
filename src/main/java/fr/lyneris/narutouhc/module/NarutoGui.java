package fr.lyneris.narutouhc.module;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.gui.config.ModuleMenu;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class NarutoGui implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onClick(InventoryClickEvent event) {

        Camp camp = null;
        ItemStack is = event.getCurrentItem();
        for (Camp value : Camp.values()) {
            if (event.getInventory().getName().equalsIgnoreCase(value.getName())) {
                camp = value;
                break;
            }
        }

        if (camp == null) return;

        if (is.hasItemMeta() && is.getItemMeta().getDisplayName().equalsIgnoreCase("§6§l« §eRetour")) {
            event.getWhoClicked().closeInventory();
            Tasks.runLater(() -> {
                UHC.getUHC().getGameManager().getGuiManager().open((Player) event.getWhoClicked(), ModuleMenu.class);
                ((Player) event.getWhoClicked()).updateInventory();
            }, 1);
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        boolean rightClick = event.getClick().isLeftClick();

        Camp finalCamp = camp;
        Arrays.stream(NarutoRoles.values())
                .filter(n -> n.getNarutoRole() != null)
                .filter(n -> n.getCamp().equals(finalCamp))
                .filter(n -> is.hasItemMeta())
                .filter(n -> is.getItemMeta().hasDisplayName())
                .filter(n -> n.getName().equals(is.getItemMeta().getDisplayName().substring(2)))
                .forEach(n -> {
                    if (rightClick) NarutoUHC.getNaruto().getRoleManager().addRole(n);
                    else NarutoUHC.getNaruto().getRoleManager().removeRole(n);
                    new CampSelector(player, finalCamp);
                });

    }

    public static class CampSelector {

        public CampSelector(Player player, Camp camp) {

            Inventory inv = Bukkit.createInventory(null, 9 * 5, camp.getName());

            for (int i : Utils.getGlassInInventory(5)) {
                inv.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            }

            int i = 10;

            for (NarutoRoles n : NarutoRoles.values()) {
                if (n.getNarutoRole() != null && n.getCamp().equals(camp)) {

                    int amount = 0;

                    for (NarutoRoles role : NarutoUHC.getNaruto().getRoleManager().getRoles()) {
                        if (role.getName().equals(n.getName())) amount++;
                    }

                    inv.setItem(i, new ItemBuilder(Material.INK_SACK).setAmount(amount).setDurability(NarutoUHC.getNaruto().getRoleManager().getRoles().contains(n) ? 12 : 8).setName("§6" + n.getName()).toItemStack());
                    switch (i) {
                        case 16:
                        case 25:
                        case 34:
                        case 43:
                        case 52:
                            i += 3;
                            break;
                        default:
                            i++;
                            break;
                    }

                    inv.setItem(40, new ItemBuilder(Material.ARROW).setName("§6§l« §eRetour").toItemStack());

                }
            }

            player.openInventory(inv);
        }

    }

}

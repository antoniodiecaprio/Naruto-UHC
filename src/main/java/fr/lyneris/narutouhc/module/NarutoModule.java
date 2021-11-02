package fr.lyneris.narutouhc.module;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.gui.config.ModuleMenu;
import fr.lyneris.uhc.module.Module;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.function.Supplier;

public class NarutoModule implements Module {

    int timer = 0;

    @Override
    public String getName() {
        return "Naruto UHC";
    }

    @Override
    public boolean hasRoles() {
        return true;
    }

    @Override
    public boolean hasGroups() {
        return true;
    }

    @Override
    public void onPlayerDeath(Player player, Player player1) {

    }

    @Override
    public boolean canRespawn() {
        return false;
    }

    @Override
    public void onRoleEnable() {

        for(UUID id : UHC.getUhc().getGameManager().getPlayers()) {
            if(Bukkit.getPlayer(id) == null) {
                UHC.getUhc().getGameManager().getPlayers().remove(id);
                Bukkit.broadcastMessage("§7▎ §c" + Bukkit.getOfflinePlayer(id) + " §fa été éliminé.");
            }
        }

        try {
            NarutoUHC.getNaruto().getManager().distributeRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                        NarutoUHC.getNaruto().getRoleManager().getRole(player).onMinute(timer, player);
                    }
                }
            }
        }.runTaskTimer(NarutoUHC.getNaruto(), 0, 60*20);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                        NarutoUHC.getNaruto().getRoleManager().getRole(player).onSecond(timer, player);
                    }
                }
            }
        }.runTaskTimer(NarutoUHC.getNaruto(), 0, 20);


    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDay() {
        NarutoUHC.getNaruto().getManager().setDay(true);
    }

    @Override
    public boolean hasEternalDay() {
        return false;
    }

    @Override
    public void onNight() {
        NarutoUHC.getNaruto().getManager().setDay(false);
    }

    @Override
    public void onNextEpisode() {

        for (UUID player : UHC.getUhc().getGameManager().getPlayers()) {
            if(Bukkit.getPlayer(player) != null && NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                NarutoUHC.getNaruto().getRoleManager().getRole(player).onNewEpisode(Bukkit.getPlayer(player));
            }
        }
        
    }

    @Override
    public int mainInventorySize() {
        return 5;
    }

    public Supplier<ItemStack[]> mainInventoryContent(Player player)  {

        ItemStack[] slots = new ItemStack[mainInventorySize()*9];

        int i = 0;

        for (NarutoRoles n : NarutoRoles.values()) {
            if (n.getNarutoRole() != null) {
                try {
                    slots[i] = new ItemBuilder(Material.INK_SACK).setDurability(NarutoUHC.getNaruto().getRoleManager().getRoles().contains(n) ? 12 : 8).setName("§6" + n.getNarutoRole().newInstance().getRoleName()).toItemStack();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }

        return () -> slots;
    }

    @Override
    public void onMainInventoryClick(Player player, Inventory inventory, ItemStack itemStack, int i) {

        for(NarutoRoles n : NarutoRoles.values()) {
            if(n.getNarutoRole() != null) {
                if(itemStack.getItemMeta().getDisplayName().replace("§6", "").equals(n.getNarutoRole().getName())) {
                    NarutoUHC.getNaruto().getRoleManager().addRole(n);
                    UHC.getUhc().getGameManager().getGuiManager().open(player, ModuleMenu.class);
                }
            }
        }

    }
}

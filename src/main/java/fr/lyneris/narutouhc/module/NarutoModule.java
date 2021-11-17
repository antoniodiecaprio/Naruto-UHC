package fr.lyneris.narutouhc.module;

import fr.lyneris.common.utils.Tasks;
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

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Supplier;

public class NarutoModule implements Module {

    int timer = 0;
    int second = 0;

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

        UHC.getUHC().getGameManager().getPlayers().stream().filter(uuid -> Bukkit.getPlayer(uuid) == null).forEach(uuid -> {
            UHC.getUHC().getGameManager().getPlayers().remove(uuid);
            Bukkit.broadcastMessage("§7▎ §c" + Bukkit.getOfflinePlayer(uuid) + " §fa été éliminé.");
        });

        NarutoUHC.getNaruto().getManager().distributeRoles();

        Tasks.runTimer(() -> {
            timer++;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                    NarutoUHC.getNaruto().getRoleManager().getRole(player).onMinute(timer, player);
                }
            }
        }, 0, 20*60);

        Tasks.runTimer(() -> {
            second++;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                    NarutoUHC.getNaruto().getRoleManager().getRole(player).onSecond(second, player);
                    NarutoUHC.getNaruto().getRoleManager().getRole(player).runnableTask();
                }
            }
        }, 0, 20);

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

        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .filter(uuid -> NarutoUHC.getNaruto().getRoleManager().getRole(uuid) != null)
                .map(Bukkit::getPlayer)
                .forEach(player -> NarutoUHC.getNaruto().getRoleManager().getRole(player).onNewEpisode(player));
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

                int amount = 0;

                for (NarutoRoles role : NarutoUHC.getNaruto().getRoleManager().getRoles()) {
                    if(role.getName().equals(n.getName())) amount++;
                }

                slots[i] = new ItemBuilder(Material.INK_SACK).setAmount(amount).setDurability(NarutoUHC.getNaruto().getRoleManager().getRoles().contains(n) ? 12 : 8).setName("§6" + n.getName()).toItemStack();
                i++;
            }
        }

        return () -> slots;
    }

    @Override
    public void onMainInventoryClick(Player player, Inventory inventory, ItemStack is, int i, boolean rightClick) {

        Arrays.stream(NarutoRoles.values())
                .filter(n -> n.getNarutoRole() != null)
                .filter(n -> is.hasItemMeta())
                .filter(n -> is.getItemMeta().hasDisplayName())
                .filter(n -> n.getName().equals(is.getItemMeta().getDisplayName().substring(2)))
                .forEach(n -> {
                    if(rightClick) NarutoUHC.getNaruto().getRoleManager().addRole(n); else NarutoUHC.getNaruto().getRoleManager().removeRole(n);
                    UHC.getUHC().getGameManager().getGuiManager().open(player, ModuleMenu.class);
                });
    }
}

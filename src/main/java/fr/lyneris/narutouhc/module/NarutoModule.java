package fr.lyneris.narutouhc.module;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Bijus;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.sankyodai.Gaara;
import fr.lyneris.narutouhc.roles.solo.Danzo;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.gui.config.ConfigurationMenu;
import fr.lyneris.uhc.module.Module;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
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
    public void onPlayerDeath(Player player, Player killer) {

        if(player.getUniqueId().equals(NarutoUHC.getNaruto().getHokage().getHokage())) {
            if(Role.isRole(killer, NarutoRoles.DANZO)) {
                Tasks.runLater(() -> NarutoUHC.getNaruto().getHokage().chooseHokage(killer), 5*20*60);
            } else {
                Tasks.runLater(() -> NarutoUHC.getNaruto().getHokage().chooseHokage(), 5*20*60);
            }
            Bukkit.broadcastMessage(CC.prefix("&cLe Hokage est mort. Un nouveau Hokage sera défini dans 5 minutes."));
        }

        if(Role.isRole(player, NarutoRoles.GAARA) && Gaara.narutoHit && Role.getCamp(player) != Camp.SHINOBI) {
            int x = new Random().nextInt(60);
            int z = new Random().nextInt(60);
            int y = Bukkit.getWorld("uhc_world").getHighestBlockYAt(x, z) + 1;
            player.teleport(new Location(Bukkit.getWorld("uhc_world"), x, y, z));
            NarutoUHC.getNaruto().getRoleManager().setCamp(player, Camp.SHINOBI);

            for(Player target : new Player[] { Role.findPlayer(NarutoRoles.TEMARI), Role.findPlayer(NarutoRoles.KANKURO) }) {
                if(target != null) {
                    target.sendMessage(CC.prefix("&aGaara &fa changé de camp. Vous rejoignez donc le camp des &aShinobi"));
                    NarutoUHC.getNaruto().getRoleManager().setCamp(target, Camp.SHINOBI);
                }
            }
            return;
        }

        if (Role.isRole(player, NarutoRoles.DANZO) && Danzo.lives > 0) {
            Tasks.runLater(() -> {
                Danzo.died++;
                player.spigot().respawn();
                int x = new Random().nextInt(60) - 30;
                int z = new Random().nextInt(60) - 30;
                int y = Bukkit.getWorld("uhc_world").getHighestBlockYAt(x, z) + 1;
                player.teleport(new Location(Bukkit.getWorld("uhc_world"), x, y, z));
                Danzo.lives--;
                player.setMaxHealth(10);
                player.setMaxHealth(player.getMaxHealth() - Danzo.died);
                player.sendMessage(CC.prefix("&fIl vous reste &a" + Danzo.lives + " &fvie(s)."));
            }, 5);
            return;
        }
        UHC.getUHC().getGameManager().getPlayers().remove(player.getUniqueId());
        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(is -> is.getType() != Material.AIR).forEach(is ->
                player.getWorld().dropItemNaturally(player.getLocation(), is)
        );
        Arrays.stream(player.getInventory().getArmorContents()).filter(Objects::nonNull).filter(is -> is.getType() != Material.AIR).forEach(is ->
                player.getWorld().dropItemNaturally(player.getLocation(), is)
        );
        Location loc = player.getLocation();
        Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> {
            UHC.getUHC().getGameManager().clearPlayer(player);
            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(loc);
        }, 1);
        Messages.sendDeathMessage(player);
        Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f));

        //Role.attemptWin();
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
                if (NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                    NarutoUHC.getNaruto().getRoleManager().getRole(player).onMinute(timer, player);
                }
            }
        }, 0, 20 * 60);

        Tasks.runTimer(() -> {
            second++;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
                    NarutoUHC.getNaruto().getRoleManager().getRole(player).onSecond(second, player);
                    NarutoUHC.getNaruto().getRoleManager().getRole(player).runnableTask();
                }
            }
            NarutoUHC.getNaruto().getBijuListener().runnableTask();
            NarutoUHC.getNaruto().getJubi().runnableTask();
        }, 0, 20);

    }

    @Override
    public void onStart() {
        Bijus.initBijus();
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

    public Supplier<ItemStack[]> mainInventoryContent(Player player) {

        ItemStack[] slots = new ItemStack[mainInventorySize() * 9];

        HashMap<Camp, Integer> roles = new HashMap<>();
        HashMap<Camp, Integer> enabledRoles = new HashMap<>();

        for (NarutoRoles value : NarutoRoles.values()) {
            if (value.getNarutoRole() != null) {
                roles.put(value.getCamp(), roles.getOrDefault(value.getCamp(), 0) + 1);
                if(NarutoUHC.getNaruto().getRoleManager().getRoles().contains(value)) {
                    enabledRoles.put(value.getCamp(), enabledRoles.getOrDefault(value.getCamp(), 0) + 1);
                }
            }
        }

        List<String> lore = new ArrayList<>();
        roles.keySet().forEach(camp -> {
            lore.add(CC.prefix(camp.getFormat() + "§8: §f" + enabledRoles.getOrDefault(camp, 0) + "/" + roles.getOrDefault(camp, 0)));
        });

        for (int i : Utils.getGlassInInventory(mainInventorySize())) {
            slots[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack();
        }
        slots[12] = new ItemBuilder(Material.INK_SACK).setDurability(10).setName(Camp.SHINOBI.getFormat()).toItemStack();
        slots[22] = new ItemBuilder(Material.WATCH).setName("§6Rôles activés").setLore(lore).toItemStack();
        slots[14] = new ItemBuilder(Material.INK_SACK).setDurability(1).setName(Camp.AKATSUKI.getFormat()).toItemStack();

        slots[21] = new ItemBuilder(Material.INK_SACK).setDurability(14).setName(Camp.OROCHIMARU.getFormat()).toItemStack();
        slots[13] = new ItemBuilder(Material.INK_SACK).setDurability(13).setName(Camp.TAKA.getFormat()).toItemStack();
        slots[23] = new ItemBuilder(Material.INK_SACK).setDurability(6).setName(Camp.MADARA_OBITO.getFormat()).toItemStack();

        slots[30] = new ItemBuilder(Material.INK_SACK).setDurability(11).setName(Camp.ZABUZA_HAKU.getFormat()).toItemStack();
        slots[31] = new ItemBuilder(Material.INK_SACK).setDurability(2).setName(Camp.SANKYODAI.getFormat()).toItemStack();
        slots[32] = new ItemBuilder(Material.INK_SACK).setDurability(7).setName(Camp.SOLO.getFormat()).toItemStack();

        slots[40] = new ItemBuilder(Material.ARROW).setName("§6§l« §eRetour").toItemStack();

        return () -> slots;
    }

    @Override
    public void onMainInventoryClick(Player player, Inventory inventory, ItemStack is, int slot, boolean rightClick) {

        if(is.getType() == Material.ARROW) {
            UHC.getUHC().getGameManager().getGuiManager().open(player, ConfigurationMenu.class);
            return;
        }

        Camp camp = null;

        for (Camp value : Camp.values()) {
            if (is.hasItemMeta() && is.getItemMeta().getDisplayName().equalsIgnoreCase(value.getFormat())) {
                camp = value;
            }
        }

        if (camp != null) {
            new NarutoGui.CampSelector(player, camp);
        }

    }


}

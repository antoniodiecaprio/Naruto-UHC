package fr.lyneris.narutouhc.crafter;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.manager.Manager;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.manager.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.List;

public abstract class NarutoRole {

    protected final NarutoUHC narutoUHC = NarutoUHC.getNaruto();
    protected final RoleManager roleManager = NarutoUHC.getNaruto().getRoleManager();
    protected final Manager manager = NarutoUHC.getNaruto().getManager();

    public abstract String getRoleName();
    public abstract List<String> getDescription();

    public abstract void onDistribute(Player player);
    public void onNewEpisode(Player player) {}
    public void onDay(Player player) {}
    public void onNight(Player player) {}

    public void resetCooldowns() {}
    public void startRunnableTask() {}

    public Chakra getChakra() {
        return Chakra.AUCUN;
    }
    public abstract Camp getCamp();

    public void onSubCommand(Player player, String[] args) {}

    public void onPlayerInteract(PlayerInteractEvent event, Player player) {}
    public void onPlayerDamage(EntityDamageEvent event, Player player) {}
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {}
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {}
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {}
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {}
    public void onPlayerEntityMountEvent(EntityMountEvent event, Player entity) {}
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {}
    public void onProjectileHitEvent(ProjectileHitEvent event, Player shooter) {}
    public void onPlayerMove(PlayerMoveEvent event, Player player) {}
    public void onPlayerInteractAtPlayer(PlayerInteractAtEntityEvent event, Player target) {}
    public void onPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {}

    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {}
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {}
    public void onAllPlayerEntityMountEvent(EntityMountEvent event, Player entity) {}
    public void onAllPlayerVehicleExitEvent(VehicleExitEvent event, Player exited) {}
    public void onAllPlayerPowerUse(Player player) {}
    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {}
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {}
    public void onAllPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {}
    public void onAllPlayerItemInteract(PlayerInteractEvent event, Player player) {}

    public void onMinute(int minute, Player player) {}
    public void onSecond(int timer, Player player) {}

}

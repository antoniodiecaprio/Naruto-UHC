package fr.lyneris.narutouhc.crafter;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.manager.Manager;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.manager.RoleManager;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.List;

public abstract class NarutoRole {

    protected final NarutoUHC narutoUHC = NarutoUHC.getNaruto();
    protected final RoleManager roleManager = NarutoUHC.getNaruto().getRoleManager();
    protected final Manager manager = NarutoUHC.getNaruto().getManager();

    public abstract String getRoleName();

    public abstract List<String> getDescription();

    public abstract NarutoRoles getRole();

    public abstract void onDistribute(Player player);

    public void onNewEpisode(Player player) {
    }

    public Player getPlayer() {
        return Role.findPlayer(this.getRole());
    }

    public void onDay(Player player) {
    }

    public void onNight(Player player) {
    }

    public void resetCooldowns() {
    }

    public void runnableTask() {
    }

    public Chakra getChakra() {
        return Chakra.AUCUN;
    }

    public abstract Camp getCamp();

    public void onSubCommand(Player player, String[] args) {
    }

    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
    }

    public void onPlayerDamage(EntityDamageEvent event, Player player) {
    }

    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
    }

    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
    }

    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
    }

    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
    }

    public void onPlayerEntityMountEvent(EntityMountEvent event, Player entity) {
    }

    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {
    }

    public void onProjectileHitEvent(ProjectileHitEvent event, Player shooter) {
    }

    public void onPlayerMove(PlayerMoveEvent event, Player player) {
    }

    public void onPlayerInteractAtPlayer(PlayerInteractAtEntityEvent event, Player target) {
    }

    public void onPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
    }

    public void onPlayerHealthRegain(EntityRegainHealthEvent event, Player player) {
    }

    public void onPlayerJoin(PlayerJoinEvent event, Player player) {
    }

    public void onPlayerChat(AsyncPlayerChatEvent event, Player player) {
    }

    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {
    }

    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
    }

    public void onAllPlayerEntityMountEvent(EntityMountEvent event, Player entity) {
    }

    public void onAllPlayerVehicleExitEvent(VehicleExitEvent event, Player exited) {
    }

    public void onAllPlayerPowerUse(Player player) {
    }

    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {
    }

    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
    }

    public void onAllPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
    }

    public void onAllPlayerItemInteract(PlayerInteractEvent event, Player player) {
    }

    public void onAllPlayerJoin(PlayerJoinEvent event, Player player) {
    }

    public void onMinute(int minute, Player player) {
    }

    public void onSecond(int timer, Player player) {
    }

    public void onArrowHitPlayerEvent(EntityDamageByEntityEvent event, Player player, Player shooter, Arrow arrow) {
    }

    public void onAllPlayerCampChange(Camp camp, Player player) {

    }

    public String prefix(String var1) {
        return CC.prefix(var1);
    }

}

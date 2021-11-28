package fr.lyneris.narutouhc.events;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.Damage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityMountEvent;

@SuppressWarnings("unused")
public class NarutoListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Damage.noDamage.keySet().stream()
                .filter(uuid -> event.getEntity().getUniqueId().equals(uuid))
                .filter(uuid -> event.getCause() == Damage.noDamage.get(uuid))
                .forEach(uuid -> event.setCancelled(true));

        if (NarutoUHC.getNaruto().getManager().getResistance().containsKey(event.getEntity().getUniqueId())) {
            int var1 = NarutoUHC.getNaruto().getManager().getResistance().get(event.getEntity().getUniqueId());
            if (var1 != 0) {
                int var2 = 1 - (var1 / 100);
                event.setDamage(event.getFinalDamage() * var2);
            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (NarutoUHC.getNaruto().getManager().isStuned(event.getPlayer())) {
            if (event.getFrom().getBlock() != event.getTo().getBlock()) {
                event.getPlayer().teleport(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onDamageOnEntity(EntityDamageByEntityEvent event) {
        if (NarutoUHC.getNaruto().getManager().getStrength().containsKey(event.getDamager().getUniqueId())) {
            int var1 = NarutoUHC.getNaruto().getManager().getStrength().get(event.getDamager().getUniqueId());
            if (var1 != 0) {
                int var2 = 1 + (var1 / 100);
                event.setDamage(event.getFinalDamage() * var2);
            }
        }
    }

    @EventHandler
    public void onRoleConsume(PlayerItemConsumeEvent event) {
        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()).onPlayerItemConsume(event, event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()).onPlayerJoin(event, event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()).onPlayerChat(event, event.getPlayer());
    }

    @EventHandler
    public void onRoleConsume(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity()).onPlayerHealthRegain(event, (Player) event.getEntity());
    }

    @EventHandler
    public void onDamage2(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!(((Arrow) event.getDamager()).getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();
        Player shooter = (Player) arrow.getShooter();

        NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(shooter);
        if (role == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(shooter).onArrowHitPlayerEvent(event, player, shooter, arrow);

    }

    @EventHandler
    public void onRoleInteract(PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()).onPlayerInteract(event, event.getPlayer());
    }

    @EventHandler
    public void onRoleDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity()).onPlayerDamage(event, (Player) event.getEntity());
    }

    @EventHandler
    public void onRoleDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getDamager()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getDamager()).onPlayerDamageOnEntity(event, (Player) event.getDamager());
    }

    @EventHandler
    public void onRoleClick(InventoryClickEvent event) {
        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getWhoClicked()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getWhoClicked()).onPlayerInventoryClick(event, (Player) event.getWhoClicked());
    }

    @EventHandler
    public void onRoleMove(PlayerMoveEvent event) {
        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()).onPlayerMove(event, event.getPlayer());
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() == null) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity().getShooter()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity().getShooter()).onProjectileLaunchEvent(event, (Player) event.getEntity().getShooter());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() == null) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity().getShooter()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity().getShooter()).onProjectileHitEvent(event, (Player) event.getEntity().getShooter());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerDeath(event, event.getEntity());
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerJoin(event, event.getPlayer());
        }
    }

    @EventHandler
    public void allPlayerRoleConsume(PlayerItemConsumeEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerItemConsume(event, event.getPlayer());
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerMove(event, event.getPlayer());
        }
    }

    @EventHandler
    public void onRoleInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;

        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getPlayer()).onPlayerInteractAtPlayer(event, (Player) event.getRightClicked());

    }

    @EventHandler
    public void onRoleRide(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole((Player) event.getEntity()).onPlayerEntityMountEvent(event, (Player) event.getEntity());
    }


    @EventHandler
    public void onRide(EntityMountEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerEntityMountEvent(event, (Player) event.getEntity());
        }
    }

    @EventHandler
    public void onAllPlayerInteract(PlayerInteractEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerItemInteract(event, event.getPlayer());
        }
    }

    @EventHandler
    public void onAllDamageOnEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerDamageOnEntity(event, (Player) event.getDamager());
        }
    }

    @EventHandler
    public void onAllDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerDamage(event, (Player) event.getEntity());
        }
    }

    @EventHandler
    public void onUnRide(VehicleExitEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer) == null) return;
            NarutoUHC.getNaruto().getRoleManager().getRole(onlinePlayer).onAllPlayerVehicleExitEvent(event, (Player) event.getExited());
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;

        NarutoUHC.getNaruto().getManager().getDeath().put(event.getEntity().getName(), event.getEntity().getKiller().getUniqueId());

        if (NarutoUHC.getNaruto().getRoleManager().getRole(event.getEntity().getKiller()) == null) return;
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getEntity().getKiller()).onPlayerKill(event, event.getEntity().getKiller());
    }

    @EventHandler
    public void onRoleDeath(PlayerDeathEvent event) {
        NarutoUHC.getNaruto().getRoleManager().getRole(event.getEntity()).onPlayerDeath(event, event.getEntity(), event.getEntity().getKiller());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equals("kamui")) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().equals("kamui")) event.setCancelled(true);
    }

    @EventHandler
    public void onKamuiDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (event.getEntity().getWorld().getName().equals("kamui")) event.setCancelled(true);
    }

    @EventHandler
    public void onWater(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().getWorld().getName().equals("kamui")) {
            Tasks.runLater(() -> event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR), 60 * 20);
        }
    }


}

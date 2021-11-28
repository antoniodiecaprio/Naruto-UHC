package fr.lyneris.narutouhc.packet;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class Reach implements Listener {

    public static HashMap<UUID, Double> lastDamage = new HashMap<>();
    public static HashMap<UUID, Boolean> canHit = new HashMap<>();
    public static List<UUID> reachPlayers = new ArrayList<>();

    public static void addReachPlayerTemp(UUID uuid, Integer seconds) {
        reachPlayers.add(uuid);
        Tasks.runAsyncLater(() -> reachPlayers.remove(uuid), seconds * 20);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        lastDamage.put(event.getDamager().getUniqueId(), event.getFinalDamage());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action.name().toLowerCase().contains("left")) return;
        if (!reachPlayers.contains(player.getUniqueId())) return;
        Bukkit.getOnlinePlayers().stream().filter(p -> p.getLocation().distance(player.getLocation()) <= 6).forEach(p -> {
            if (getLookingAt(player, p) && canHit.getOrDefault(player.getUniqueId(), true)) {
                p.damage(lastDamage.get(player.getUniqueId()));
                p.setVelocity(player.getLocation().getDirection().multiply(0.9).setY(0.3));
                canHit.put(player.getUniqueId(), false);
                Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> canHit.put(player.getUniqueId(), true), 60*20);
            }
        });
    }

    private boolean getLookingAt(Player player, Player player1) {
        Location eye = player.getEyeLocation();
        Vector toEntity = player1.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }

}

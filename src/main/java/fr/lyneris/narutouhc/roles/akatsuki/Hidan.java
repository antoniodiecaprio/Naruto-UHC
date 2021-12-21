package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Hidan extends NarutoRole {

    private final Set<Block> blocks = new HashSet<>();
    private UUID linked = null;
    private int hoeCooldown = 0;

    @Override
    public void resetCooldowns() {
        this.hoeCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (hoeCooldown > 0) hoeCooldown--;
    }

    public NarutoRoles getRole() {
        return NarutoRoles.HIDAN;
    }

    @Override
    public String getRoleName() {
        return "Hidan";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.KAKUZU);
        player.getInventory().addItem(new ItemBuilder(Material.REDSTONE_BLOCK).setName(Item.specialItem(Item.specialItem("Plateforme"))).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_HOE).setName(Item.specialItem(Item.specialItem("Houe"))).toItemStack());
    }

    @Override
    public void onPlayerBlockPlace(BlockPlaceEvent event, Player player) {
        if (Item.specialItem(event.getItemInHand(), "Plateforme")) {
            if (this.linked == null) {
                event.setCancelled(true);
                player.sendMessage(prefix("&cVous n'êtes lié à personne."));
                return;
            }

            if (this.hoeCooldown > 0) {
                Messages.getCooldown(hoeCooldown).queue(player);
                return;
            }

            sphereblock(event.getBlockPlaced().getLocation().clone().subtract(0, 1, 0), 2);
            this.hoeCooldown = 30 * 60;
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        if(this.linked != null) {
            Bukkit.getPlayer(linked).sendMessage(prefix("&cHidan &fest mort. De ce fait vos vies ne sont plus liés"));
            this.linked = null;
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(this.linked.equals(player.getUniqueId())) {
            getPlayer().sendMessage(prefix("&cVous n'êtes plus lié à " + Bukkit.getPlayer(linked).getName()));
            this.linked = null;
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if (this.linked == null) return;
        if (!isInPlateforme(player)) return;
        if(Bukkit.getPlayer(linked) == null) return;
        Player target = Bukkit.getPlayer(linked);
        if(target.getLocation().distance(player.getLocation()) >= 100) {
            player.sendMessage(prefix("&c" + target.getName() + " &fest plus de 100 blocks de vous. De ce fait vous prenez le dégât de ce coup."));
        } else {
            target.damage(event.getFinalDamage());
            event.setDamage(0);
        }
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (this.linked != null) return;
        if (!(event.getEntity() instanceof Player)) return;
        this.linked = event.getEntity().getUniqueId();
        player.sendMessage(prefix("&fVotre &avie &fa été lié avec &c" + event.getEntity().getName()));
        player.sendMessage(prefix("&fVotre &avie &fa été lié avec &cHidan"));
        Tasks.runLater(() -> {
            if(linked == null) return;
            if(Bukkit.getPlayer(linked) == null) return;
            if(getPlayer() == null) return;
            getPlayer().sendMessage(prefix("&cVous n'êtes plus lié avec " + Bukkit.getPlayer(linked)));
            Bukkit.getPlayer(linked).sendMessage(prefix("&cVous n'êtes plus lié avec Hidan"));
        }, 5*20*60);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        Player kakuzu = Role.findPlayer(NarutoRoles.KAKUZU);
        if (kakuzu == null) return;
        if (kakuzu.getLocation().distance(player.getLocation()) <= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 0, false, false));
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    public void sphereblock(final Location center, final double radius) {
        for (double x = -radius; x <= radius; x++)
            for (double z = -radius; z <= radius; z++)
                if (center.clone().add(x, 0, z).distance(center) <= radius)
                    this.blocks.add(center.clone().add(x, 0, z).getBlock());
    }

    private boolean isInPlateforme(Player damager) {
        return this.blocks.stream().anyMatch(block -> block.getLocation().distance(damager.getLocation()) < 2);
    }

}

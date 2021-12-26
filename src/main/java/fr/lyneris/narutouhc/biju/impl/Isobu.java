package fr.lyneris.narutouhc.biju.impl;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.narutouhc.utils.Item;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.Guard;
import java.util.UUID;

public class Isobu extends Biju implements Listener {

    private Guardian guardian;
    private Location spawn;

    @Override
    public LivingEntity getLivingEntity() {
        return guardian;
    }

    @Override
    public void setupBiju() {
        int value = (int) (Math.random() * 3);

        World world = Bukkit.getWorld("world");
        if (value == 0) {
            int x = NarutoUHC.getRandom().nextInt(150, 300);
            int z = NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        } else if (value == 1) {
            int x = -NarutoUHC.getRandom().nextInt(150, 300);
            int z = NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        } else if (value == 2) {
            int x = NarutoUHC.getRandom().nextInt(150, 300);
            int z = -NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        } else {
            int x = -NarutoUHC.getRandom().nextInt(150, 300);
            int z = -NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        }

        new IsobuRunnable().runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public String getName() {
        return "§aIsobu";
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if (NarutoUHC.getNaruto().getBijuListener().getIsobuCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getIsobuCooldown()).queue(player);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20 * 60, 0, false, false));
        final UUID uuid = player.getUniqueId();
        player.setMaxHealth(player.getMaxHealth() + 10);
        NarutoUHC.getNaruto().getBijuListener().setIsobuDamage(player.getUniqueId());
        Tasks.runLater(() -> {
            NarutoUHC.getNaruto().getBijuListener().setIsobuDamage(null);
            Bukkit.getPlayer(uuid).setMaxHealth(Bukkit.getPlayer(uuid).getMaxHealth() - 10);
        }, 5 * 20 * 60);
        NarutoUHC.getNaruto().getBijuListener().setIsobuCooldown(20 * 60);
    }

    @Override
    public void spawnEntity() {
        this.guardian = (Guardian) Bukkit.getWorld("world").spawnEntity(this.spawn, EntityType.GUARDIAN);
        guardian.setCustomName(this.getName());
        guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        guardian.setMaxHealth(2D * 100D);
        guardian.setElder(true);
        guardian.setHealth(guardian.getMaxHealth());
        guardian.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) guardian).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!guardian.isDead()) {
                    if (!Loc.getNearbyPlayers(guardian, 15).isEmpty()) {
                        Guardian little_guardian = (Guardian) guardian.getWorld().spawnEntity(guardian.getLocation(), EntityType.GUARDIAN);
                        little_guardian.setElder(false);
                        little_guardian.setMaxHealth(10);
                        little_guardian.setHealth(little_guardian.getMaxHealth());
                        little_guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));

                    }
                }
            }
        }.runTaskTimer(NarutoUHC.getNaruto(), 0, 10 * 20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (guardian != null && !guardian.isDead() && event.getEntity().getUniqueId().equals(guardian.getUniqueId())) {
            int random = (int) (Math.random() * 25);
            if (random == 0) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (this.guardian != null && event.getEntity().getUniqueId().equals(this.guardian.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.guardian = null;
            event.getDrops().clear();
            Tasks.runLater(() -> {
                if (this.getMaster() == null) {
                    spawnEntity();
                    Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient de réapparaître."));
                }
            }, 5 * 20 * 60);
        }
    }


    @Override
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Isobu");
    }

    public class IsobuRunnable extends BukkitRunnable {

        int timer = 0;
        int spawn = (int) (Math.random() * 1200);

        @Override
        public void run() {

            if (this.timer == (40 * 60 + spawn) - 30) {
                Bukkit.broadcastMessage(CC.prefix(getName() + " &fva apparaître dans &a30 &fsecondes."));
            }

            if (this.timer == (40 * 60 + spawn)) {
                spawnEntity();
                Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient d'apparaître."));
                cancel();
            }
        }
    }
}

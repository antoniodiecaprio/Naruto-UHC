package fr.lyneris.narutouhc.biju.impl;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.narutouhc.utils.Item;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Chomei extends Biju implements Listener {

    private Ghast ghast;
    private Location spawn;

    @Override
    public LivingEntity getLivingEntity() {
        return ghast;
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
        return "§aChômei";
    }

    @Override
    public void spawnEntity() {
        this.ghast = (Ghast) Bukkit.getWorld("world").spawnEntity(this.spawn, EntityType.GHAST);
        ghast.setCustomName(this.getName());
        ghast.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        ghast.setMaxHealth(2D * 100D);
        ghast.setHealth(ghast.getMaxHealth());
        ghast.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) ghast).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (this.ghast != null && event.getEntity().getUniqueId().equals(this.ghast.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.ghast = null;
            event.getDrops().clear();
            Tasks.runLater(() -> {
                if (this.getMaster() == null) {
                    spawnEntity();
                    Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient de réapparaître."));
                }
            }, 5 * 20 * 60);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().equals(this.getItem())) {
            this.setMaster(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void hitFireball(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Fireball) {
            Fireball f = (Fireball) event.getEntity();
            if (f.getShooter() instanceof Ghast) {
                Ghast ghast = (Ghast) f.getShooter();
                if (ghast == this.ghast) {
                    Location location = f.getLocation();
                    location.getWorld().spawn(location, Creeper.class);
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Chômei");
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if(NarutoUHC.getNaruto().getBijuListener().getChomeiCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getChomeiCooldown()).queue(player);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5*20*60, 0, false, false));
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 5*60);
        NarutoUHC.getNaruto().getBijuListener().setChomeiFire(player.getUniqueId());
        Tasks.runLater(() -> NarutoUHC.getNaruto().getBijuListener().setChomeiFire(null), 5*20*60);
        NarutoUHC.getNaruto().getBijuListener().setChomeiCooldown(20*60);
    }

    @Override
    public Location getSpawn() {
        return spawn;
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

package fr.lyneris.narutouhc.biju.impl;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Damage;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
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

        World world = Bukkit.getWorld("uhc_world");
        spawn = new Location(world, (Math.random() * 300), 100 + 2, (Math.random() * 300));
        new IsobuRunnable().runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public String getName() {
        return "§aChômei";
    }

    @Override
    public void spawnEntity() {
        this.ghast = (Ghast) Bukkit.getWorld("uhc_world").spawnEntity(this.spawn, EntityType.GHAST);
        ghast.setCustomName(this.getName());
        ghast.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        ghast.setMaxHealth(2D * 100D);
        ghast.setHealth(ghast.getMaxHealth());
        ghast.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) ghast).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        super.spawnEntity();
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if(NarutoUHC.getNaruto().getBijuListener().getChomeiCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getChomeiCooldown()).queue(player);
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
        Tasks.runLater(() -> {
            player.setAllowFlight(false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3*20*60, 1, false, false));
        }, 10*20);
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 10*60);
        NarutoUHC.getNaruto().getBijuListener().setChomeiCooldown(20*60);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (this.ghast != null && event.getEntity().getUniqueId().equals(this.ghast.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.ghast = null;
            event.getDrops().clear();
            Bukkit.broadcastMessage(CC.prefix("&a" + getName() + " &fa été tué."));

            Tasks.runLater(() -> {
                if (this.getMaster() == null) {
                    spawnEntity();
                    Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient de réapparaître."));
                }
            }, 5 * 20 * 60);
        }
    }


    @EventHandler
    public void hitFireball(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Fireball)) return;
        Fireball f = (Fireball) event.getEntity();
        if (!(f.getShooter() instanceof Ghast)) return;
        Ghast ghast = (Ghast) f.getShooter();
        if (ghast != this.ghast) return;
        Location location = f.getLocation();
        location.getWorld().spawn(location, Creeper.class);
    }

    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Chômei");
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    public class IsobuRunnable extends BukkitRunnable {

        int timer = 0;
        int spawn = (int) (Math.random() * 30);

        @Override
        public void run() {
            timer++;
            if (this.timer == (NarutoUHC.getNaruto().getBijuListener().getBijuStart() + spawn) - 30) {
                Bukkit.broadcastMessage(CC.prefix(getName() + " &fva apparaître dans &a30 &fsecondes."));
            }
            if (this.timer == (NarutoUHC.getNaruto().getBijuListener().getBijuStart() + spawn)) {
                spawnEntity();
                Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient d'apparaître."));
                cancel();
            }
        }
    }
}

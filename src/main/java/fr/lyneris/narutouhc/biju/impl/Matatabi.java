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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Matatabi extends Biju implements Listener {

    private Blaze blaze;
    private Location spawn;

    @Override
    public LivingEntity getLivingEntity() {
        return blaze;
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if (NarutoUHC.getNaruto().getBijuListener().getMatatabiCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getMatatabiCooldown()).queue(player);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20 * 60, 0, false, false));
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 5 * 60);
        NarutoUHC.getNaruto().getBijuListener().setMatatabiFire(player.getUniqueId());
        Tasks.runLater(() -> NarutoUHC.getNaruto().getBijuListener().setMatatabiFire(null), 5 * 20 * 60);
        NarutoUHC.getNaruto().getBijuListener().setMatatabiCooldown(20 * 60);
    }

    @Override
    public void setupBiju() {
        int value = (int) (Math.random() * 3);

        World world = Bukkit.getWorld("uhc_world");
        if (value == 0) {
            int x = NarutoUHC.getRandom().nextInt(150, 300);
            int z = NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("uhc_world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        } else if (value == 1) {
            int x = -NarutoUHC.getRandom().nextInt(150, 300);
            int z = NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("uhc_world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        } else if (value == 2) {
            int x = NarutoUHC.getRandom().nextInt(150, 300);
            int z = -NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("uhc_world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        } else {
            int x = -NarutoUHC.getRandom().nextInt(150, 300);
            int z = -NarutoUHC.getRandom().nextInt(150, 300);
            spawn = new Location(Bukkit.getWorld("uhc_world"), x, world.getHighestBlockYAt(x, z) + 2, z);
        }
        new MetatabiRunnable().runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public String getName() {
        return "§aMetatabi";
    }

    @Override
    public void spawnEntity() {
        this.blaze = (Blaze) Bukkit.getWorld("uhc_world").spawnEntity(this.spawn, EntityType.BLAZE);
        blaze.setCustomName(this.getName());
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        blaze.setMaxHealth(2D * 100D);
        blaze.setHealth(blaze.getMaxHealth());
        blaze.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) blaze).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);

        new BukkitRunnable() {
            int timeAttack = 60;

            @Override
            public void run() {
                if (!blaze.isDead()) {
                    if (timeAttack == 0) {
                        for (Entity entity : blaze.getNearbyEntities(15, 15, 15)) {
                            if (!(entity instanceof Player)) continue;
                            Player player = (Player) entity;
                            player.setFireTicks(20 * 16);
                            player.sendMessage(CC.prefix(getName() + " &fvient de vous mettre en &cfeu&f."));
                        }
                        timeAttack = 60;
                    }
                } else {
                    cancel();
                }
                timeAttack--;

            }
        }.runTaskTimer(NarutoUHC.getNaruto(), 20, 20);
        super.spawnEntity();

    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (this.blaze != null && event.getEntity().getUniqueId().equals(this.blaze.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.blaze = null;
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


    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Matatabi");
    }

    public class MetatabiRunnable extends BukkitRunnable {

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

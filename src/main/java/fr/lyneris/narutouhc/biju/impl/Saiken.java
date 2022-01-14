package fr.lyneris.narutouhc.biju.impl;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Saiken extends Biju implements Listener {

    private Slime slime;
    private Location spawn;

    @Override
    public LivingEntity getLivingEntity() {
        return slime;
    }

    @Override
    public void setupBiju() {
        World world = Bukkit.getWorld("uhc_world");
        spawn = new Location(world, (Math.random() * 300), 100 + 2, (Math.random() * 300));
        new SaikenRunnable().runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public String getName() {
        return "§aSaiken";
    }

    @Override
    public void spawnEntity() {
        this.slime = (Slime) Bukkit.getWorld("uhc_world").spawnEntity(this.spawn, EntityType.SLIME);
        slime.setCustomName(this.getName());
        slime.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        slime.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        slime.setMaxHealth(2D * 100D);
        slime.setSize(8);
        slime.setHealth(slime.getMaxHealth());
        slime.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) slime).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        super.spawnEntity();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (this.slime != null && event.getEntity().getUniqueId().equals(this.slime.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.slime = null;
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
    public void onDamageBiju(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Slime && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Slime slime = (Slime) event.getDamager();
            if (slime != null && slime.getUniqueId().equals(this.slime.getUniqueId())) {
                int value = (int) (Math.random() * 100);
                if (value <= 25) {
                    slime.setHealth(Math.min(slime.getHealth() + 2D, slime.getHealth()));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 0));
                    player.getWorld().createExplosion(player.getLocation(), 1.5F);
                    player.sendMessage(CC.prefix(getName() + " &fvous a touché..."));
                }
            }
        }
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Saiken");
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if (NarutoUHC.getNaruto().getBijuListener().getSaikenCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getSaikenCooldown()).queue(player);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
        NarutoUHC.getNaruto().getBijuListener().setSaikenUser(player.getUniqueId());
        Tasks.runLater(() -> NarutoUHC.getNaruto().getBijuListener().setSaikenUser(null), 5 * 20 * 60);
        NarutoUHC.getNaruto().getBijuListener().setSaikenCooldown(15 * 60);
    }


    public class SaikenRunnable extends BukkitRunnable {

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

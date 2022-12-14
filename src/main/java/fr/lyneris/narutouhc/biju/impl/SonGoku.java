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
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SonGoku extends Biju implements Listener {

    private MagmaCube magma_cube;
    private Location spawn;

    @Override
    public LivingEntity getLivingEntity() {
        return magma_cube;
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if (NarutoUHC.getNaruto().getBijuListener().getSonGokuCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getSonGokuCooldown()).queue(player);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20 * 60, 3, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20 * 60, 0, false, false));
        NarutoUHC.getNaruto().getBijuListener().setSonGokuUser(player.getUniqueId());
        Tasks.runLater(() -> NarutoUHC.getNaruto().getBijuListener().setSonGokuUser(null), 5 * 20 * 60);
        NarutoUHC.getNaruto().getBijuListener().setSonGokuCooldown(50 * 60);
    }


    @Override
    public void setupBiju() {
        World world = Bukkit.getWorld("uhc_world");
        spawn = new Location(world, (Math.random() * 300), 100 + 2, (Math.random() * 300));
        new SonGokuRunnable().runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public String getName() {
        return "??aSon Gok??";
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public void spawnEntity() {
        this.magma_cube = (MagmaCube) Bukkit.getWorld("uhc_world").spawnEntity(this.spawn, EntityType.MAGMA_CUBE);
        magma_cube.setCustomName(this.getName());
        magma_cube.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        magma_cube.setMaxHealth(2D * 100D);
        magma_cube.setHealth(magma_cube.getMaxHealth());
        magma_cube.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) magma_cube).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        super.spawnEntity();
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if(this.magma_cube != null && !this.magma_cube.isDead() && this.magma_cube.getLocation().distance(event.getPlayer().getLocation()) <= 30) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.prefix("??cVous ne pouvez pas utiliser votre lave ou votre seau d'eau ?? c??t?? de Son Gok??."));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if(this.magma_cube != null && event.getEntity().getUniqueId().equals(this.magma_cube.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.magma_cube = null;
            event.getDrops().clear();
            Bukkit.broadcastMessage(CC.prefix("&a" + getName() + " &fa ??t?? tu??."));

            Tasks.runLater(() -> {
                if(this.getMaster() == null) {
                    spawnEntity();
                    Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient de r??appara??tre."));
                }
            }, 5*20*60);
        }
    }


    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Son Gok??");
    }

    public class SonGokuRunnable extends BukkitRunnable {

        int timer = 0;
        int spawn = (int) (Math.random() * 30);

        @Override
        public void run() {
            timer++;
            if (this.timer == (NarutoUHC.getNaruto().getBijuListener().getBijuStart() + spawn) - 30) {
                Bukkit.broadcastMessage(CC.prefix(getName() + " &fva appara??tre dans &a30 &fsecondes."));
            }

            if (this.timer == (NarutoUHC.getNaruto().getBijuListener().getBijuStart() + spawn)) {
                spawnEntity();
                Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient d'appara??tre."));
                cancel();
            }
        }
    }
}

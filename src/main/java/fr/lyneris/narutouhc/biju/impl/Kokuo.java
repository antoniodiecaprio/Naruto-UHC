package fr.lyneris.narutouhc.biju.impl;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.packet.KokuoInvoker;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kokuo extends Biju implements Listener {

    private Horse horse;
    private Location spawn;

    @Override
    public LivingEntity getLivingEntity() {
        return horse;
    }

    @Override
    public void getItemInteraction(PlayerInteractEvent event, Player player) {
        if (NarutoUHC.getNaruto().getBijuListener().getKokuoCooldown() > 0) {
            Messages.getCooldown(NarutoUHC.getNaruto().getBijuListener().getKokuoCooldown()).queue(player);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
        NarutoUHC.getNaruto().getBijuListener().setKokuoUser(player.getUniqueId());
        Tasks.runLater(() -> NarutoUHC.getNaruto().getBijuListener().setKokuoUser(null), 5 * 20 * 60);
        NarutoUHC.getNaruto().getBijuListener().setKokuoCooldown(20 * 60);
    }

    @Override
    public void setupBiju() {
        World world = Bukkit.getWorld("uhc_world");
        spawn = new Location(world, (Math.random() * 300), 100 + 2, (Math.random() * 300));
        new KokuoRunnable().runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public String getName() {
        return "§aKokuô";
    }

    @Override
    public void spawnEntity() {
        this.horse = KokuoInvoker.invokeKokuo(horse, this.spawn, getName());
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(this.horse == null) return;
        if(!event.getDamager().getUniqueId().equals(this.horse.getUniqueId())) return;
        int random = (int) (Math.random() * 5);
        if(random == 2) {
            horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8*20, 0, false, false));
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8*20, 0, false, false));
            event.getEntity().sendMessage(CC.prefix("&fVous n'avez pas eu de chance et avez reçu l'effet &7Blindness &fet &7Slowness &fpendant 8 secondes."));
        }

        super.spawnEntity();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if(this.horse != null && event.getEntity().getUniqueId().equals(this.horse.getUniqueId())) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), getItem());
            this.horse = null;
            event.getDrops().clear();
            Bukkit.broadcastMessage(CC.prefix("&a" + getName() + " &fa été tué."));

            Tasks.runLater(() -> {
                if(this.getMaster() == null) {
                    spawnEntity();
                    Bukkit.broadcastMessage(CC.prefix(getName() + " &fvient de réapparaître."));
                }
            }, 5*20*60);
        }
    }


    @Override
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public ItemStack getItem() {
        return Item.getInteractItem("Kokuô");
    }

    public class KokuoRunnable extends BukkitRunnable {

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

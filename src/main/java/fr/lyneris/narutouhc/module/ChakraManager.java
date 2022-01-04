package fr.lyneris.narutouhc.module;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class ChakraManager implements Listener {

    private final NarutoUHC narutoUHC;
    private final ArrayList<UUID> under50;

    public ChakraManager(NarutoUHC narutoUHC) {
        this.narutoUHC = narutoUHC;
        this.under50 = new ArrayList<>();
    }

    public void setupChakras() {
        new ChakraTask().runTaskTimer(narutoUHC, 0, 10);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if(Role.getRole((Player) event.getDamager()) == null) return;
        if (Role.getRole((Player) event.getDamager()).getChakra().equals(Chakra.KATON)) {
            if (!Item.isSword(((Player) event.getEntity()).getItemInHand())) return;
            int random = (int) (Math.random() * 50);
            if (random != 10) return;
            event.getEntity().setFireTicks(100);
        }

        if (Role.getRole((Player) event.getDamager()).getChakra().equals(Chakra.RAITON)) {
            if (!Item.isSword(((Player) event.getEntity()).getItemInHand())) return;
            int random = (int) (Math.random() * 100);
            if (random != 10) return;
            event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if(Role.getRole((Player) event.getEntity()) == null) return;
        if (!Role.getRole((Player) event.getEntity()).getChakra().equals(Chakra.FUTON))
            return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        event.setDamage(event.getDamage() * 0.8);
    }

    private class ChakraTask extends BukkitRunnable {

        @Override
        public void run() {
            Role.getAliveOnlinePlayers().forEach(player -> {
                if(Role.getRole(player) == null) return;
                switch (Role.getRole(player).getChakra()) {
                    case DOTON: {
                        if (player.getLocation().getBlockY() <= 50) {
                            if (!under50.contains(player.getUniqueId())) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false));
                                player.setWalkSpeed(player.getWalkSpeed() + 0.01F);
                                under50.add(player.getUniqueId());
                            }
                        } else {
                            if (under50.contains(player.getUniqueId())) {
                                player.setWalkSpeed(player.getWalkSpeed() - 0.01F);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false));
                                under50.remove(player.getUniqueId());
                            }
                        }
                        break;
                    }
                    case SUITON: {
                        if (player.getLocation().getBlock().isLiquid()) {
                            if (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType() == Material.AIR)
                                return;
                            if (player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER)) return;
                            player.getInventory().setBoots(new ItemBuilder(player.getInventory().getBoots()).addEnchant(Enchantment.DEPTH_STRIDER, 1).toItemStack());
                        } else {
                            if (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType() == Material.AIR)
                                return;
                            if (!player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER))
                                return;
                            if (player.getInventory().getBoots().getItemMeta().getEnchants().get(Enchantment.DEPTH_STRIDER) > 1)
                                return;
                            player.getInventory().getBoots().removeEnchantment(Enchantment.DEPTH_STRIDER);
                        }
                        break;
                    }
                }
            });
        }

    }

}

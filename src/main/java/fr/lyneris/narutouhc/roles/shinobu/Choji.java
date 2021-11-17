package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.DoubleCircleEffect;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Choji extends NarutoRole {

    public int decuplementCooldown = 0;
    public int bouletCooldown = 0;

    @Override
    public void resetCooldowns() {
        bouletCooldown = 0;
        decuplementCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(bouletCooldown > 0) {
            bouletCooldown--;
        }

        if(decuplementCooldown > 0) {
            decuplementCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Choji";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.INO);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Décuplement Partiel")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Boulet Humain")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.COOKED_BEEF, 3).setName(Item.interactItem("Akimichi")).toItemStack());
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {

        if(Item.interactItem(event.getItem(), "Akimichi")) {
            new DoubleCircleEffect(20 * 60, EnumParticle.WATER_SPLASH).start(player);

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30*20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30*20, 0    , false, false));
            Tasks.runLater(() -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60*20, 0, false, false));
                player.sendMessage(CC.prefix("§cL'indigestion commence à se faire ressentir..."));
            }, 31*20);
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event.getItem(), "Décuplement Partiel")) {

            if(decuplementCooldown > 0) {
                player.sendMessage(Messages.cooldown(decuplementCooldown));
                return;
            }

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20, 1, false, false));
            Tasks.runLater(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false)), 21*20);

            decuplementCooldown = 10*60;
        }

        if(Item.interactItem(event.getItem(), "Boulet Humain")) {

            if(bouletCooldown > 0) {
                player.sendMessage(Messages.cooldown(bouletCooldown));
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 1, false, false));

            bouletCooldown = 15*60;
        }

    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if(player.getFoodLevel() <= 3) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        } else {
            if(!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            }
        }

        if(player.getFoodLevel() <= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 5*20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 0, false, false));
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }
}

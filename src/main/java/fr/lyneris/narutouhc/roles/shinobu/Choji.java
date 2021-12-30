package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.DoubleCircleEffect;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Choji extends NarutoRole {

    public int decuplementCooldown = 0;
    public int bouletCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.CHOJI;
    }

    @Override
    public void resetCooldowns() {
        bouletCooldown = 0;
        decuplementCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (bouletCooldown > 0) {
            bouletCooldown--;
        }

        if (decuplementCooldown > 0) {
            decuplementCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Choji";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aChôji\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item “§rDécuplement Partiel§7\", celui-ci permet de recevoir l’effet §cForce 2§7 pendant 20 secondes, il possède un délai de 10 minutes.\n" +
                "    \n" +
                "§7• Il dispose également de l’item “§rBoulet Humain§7”, celui-ci permet de recevoir l’effet §bVitesse 2§7 pendant 1 minute, il possède un délai de 15 minutes.\n" +
                "    \n" +
                "§7• Il dispose de 3 steaks nommés “§rAkimichi§7”, lorsqu’il les mange, ils lui permettent de recevoir l’effet §cForce 2§7 et §9Résistance 1§7 pendant 30 secondes, des particules d'eau forment des ailes lorsqu’il mange ses steaks, après avoir utilisé ce pouvoir, il reçoit l’effet §8Lenteur 1§7 pendant 1 minute.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’effet §cForce 1§7 permanent.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il est à moins de 1,5 sur sa barre de faim, il perd son effet de §cForce§7.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il est à la moitié de sa barre de faim, il obtient les effets §2Hunger 1§7 et §8Lenteur 1§7. \n" +
                "§e §f\n" +
                "§7• Il connaît l’identité de §aIno§7.\n" +
                "      \n" +
                "§7• Il dispose de la nature de Chakra : §6Doton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
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

        if (Item.interactItem(event.getItem(), "Akimichi")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            new DoubleCircleEffect(20 * 60, EnumParticle.WATER_SPLASH).start(player);

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20, 0, false, false));
            Tasks.runLater(() -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 * 20, 0, false, false));
                player.sendMessage(CC.prefix("§cL'indigestion commence à se faire ressentir..."));

            }, 31 * 20);
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Décuplement Partiel")) {

            if (decuplementCooldown > 0) {
                player.sendMessage(Messages.cooldown(decuplementCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 1, false, false));
            Tasks.runLater(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false)), 21 * 20);

            decuplementCooldown = 10 * 60;
        }

        if (Item.interactItem(event.getItem(), "Boulet Humain")) {

            if (bouletCooldown > 0) {
                player.sendMessage(Messages.cooldown(bouletCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 1, false, false));

            bouletCooldown = 15 * 60;
        }

    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if (player.getFoodLevel() <= 3) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        } else {
            if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            }
        }

        if (player.getFoodLevel() <= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 5 * 20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0, false, false));
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

package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.DoubleCircleEffect;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class RockLee extends NarutoRole {

    public boolean usedEight = false;
    public boolean usedNight = false;

    public NarutoRoles getRole() {
        return NarutoRoles.ROCK_LEE;
    }

    @Override
    public String getRoleName() {
        return "Rock Lee";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aRock Lee\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item \"§rTrois Portes§7\", lorsqu’il clique sur l’item il obtient l'effet §bVitesse 1§7 pendant 2 minutes mais perdra aussi §c1 cœur§7. \n" +
                "      \n" +
                "§7• Il dispose de l'item \"§rSix Portes§7\", lorsqu’il clique sur l’item, il obtient les effets §cForce 1§7 et §bVitesse 1§7 pendant 5 minutes cependant il perd §c1 cœur§7 permanent et des particules vertes émaneront de §aRock Lee§7. \n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il connaît l'identité de §aGaï Maito§7. \n" +
                "§e §f\n" +
                "§7• Si §aGaï Maito§7 viens à mourir, alors §aRock Lee§7 recevra l’item §rHuit Portes§7 (voir fiche §aGaï Maito§7). \n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.GAI_MAITO);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Trois Portes")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Six Portes")).toItemStack());
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        Player rockLee = Role.findPlayer(NarutoRoles.ROCK_LEE);

        if (rockLee == null) return;

        if (Role.isRole(player, NarutoRoles.GAI_MAITO)) {
            rockLee.sendMessage(CC.prefix("§cGaï Maito §fest mort, vous obtenez donc son item §aHuit Portes§f."));
            rockLee.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Huit Portes")).toItemStack());
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Trois Portes")) {
            if (usedEight) {
                player.sendMessage(CC.prefix("§cVous ne pouvez plus utiliser vos pouvoirs car vous avez utilisé l'item Huit Portes."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20 * 60, 0, false, false));
            player.setMaxHealth(player.getMaxHealth() - 2);
            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aTrois Portes§f."));
        }

        if (Item.interactItem(event.getItem(), "Six Portes")) {
            if (usedEight) {
                player.sendMessage(CC.prefix("§cVous ne pouvez plus utiliser vos pouvoirs car vous avez utilisé l'item Huit Portes."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
            player.setMaxHealth(player.getMaxHealth() - 2);
            new DoubleCircleEffect(20 * 2 * 60, EnumParticle.VILLAGER_HAPPY).start(player);
            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aSix Portes§f."));
        }

        if (Item.interactItem(event.getItem(), "Huit Portes")) {

            usedEight = true;
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20 * 60, 0, false, false));
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20 * 60, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2 * 20 * 60, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2 * 20 * 60, 0, false, false));
            player.setMaxHealth(player.getMaxHealth() + 10);
            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aHuit Portes§f."));
            new DoubleCircleEffect(20 * 2 * 60, EnumParticle.FLAME).start(player);

            player.setItemInHand(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Gaï de la Nuit")).toItemStack());

            Tasks.runLater(() -> {
                player.sendMessage(CC.prefix("§fLes deux minutes sont écoulés, vous avez désormais §c6 coeurs§f permanent ainsi que §8Faiblesse §fpendant 15 minutes."));
                player.setMaxHealth(16);
            }, 2 * 20 * 60);
        }

        if (Item.interactItem(event.getItem(), "Gaï de la Nuit")) {
            if (usedNight) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            usedNight = true;

            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aGaï de la Nuit§f."));

            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2, true, true));
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 2, true, true));
            new DoubleCircleEffect(20 * 10, EnumParticle.REDSTONE).start(player);

            Tasks.runLater(() -> player.setHealth(0), 10 * 20);
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }
}

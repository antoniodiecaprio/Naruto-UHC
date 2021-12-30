package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Neji extends NarutoRole {

    public int byakuganCooldown = 0;
    public int hakkeCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.NEJI;
    }

    @Override
    public void resetCooldowns() {
        byakuganCooldown = 0;
        hakkeCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (byakuganCooldown > 0) {
            byakuganCooldown--;
        }

        if (hakkeCooldown > 0) {
            hakkeCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Neji";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aNeji\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item nommé \"§rByakugan§7\", celui-ci lui envoie un message de la position approximative de tous les joueurs se situant dans un rayon de 60 blocs, il possède un délai de 20 minutes. \n" +
                "§3Exemple : SteLeague : Position : Nord | Distance :  10 blocs\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item \"§rHakke§7\", celui-ci lui permet d’infliger §c3 cœurs§7 à tous les joueurs situés dans un rayon de 10 blocs autour de lui, ces joueurs recevront aussi les effets §0Cécité 1§7 et §8Lenteur 4§7 pendant 3 secondes, il possède un délai de 20 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Lorsqu’un joueur utilise sa technique dans un rayon de 20 blocs autour de lui, un message lui est envoyé pour l’en informer. Le message apparaîtra sous la forme suivante : [rôle] vient d’utiliser son pouvoir.\n" +
                "      \n" +
                "§7• Si §aHinata§7 vient à mourir alors il obtient l'effet §cForce 1§7 de façon permanente. \n" +
                "      \n" +
                "§7• Il connaît l'identité de §aHinata§7. \n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §6Doton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.HINATA);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Byakugan")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Hakke")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Byakugan")) {
            if (byakuganCooldown > 0) {
                player.sendMessage(Messages.cooldown(byakuganCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aByakugan"));

            Loc.getNearbyPlayers(player, 60).forEach(target -> {
                String position = Loc.getCardinalDirection(target);
                int distance = (int) target.getLocation().distance(player.getLocation());

                player.sendMessage(CC.prefix("§9" + target.getName() + " §f§l» §fPosition: §e" + position + "§f, Distance: §e" + distance));

            });

            byakuganCooldown = 20 * 60;

        }

        if (Item.interactItem(event.getItem(), "Hakke")) {

            if (hakkeCooldown > 0) {
                player.sendMessage(Messages.cooldown(byakuganCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aHakke"));

            Loc.getNearbyPlayers(player, 10).forEach(target -> {
                target.damage(0.1);
                target.setHealth(target.getHealth() - 6);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, false));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 0, false, false));
                target.sendMessage(CC.prefix("§aNeji §fa utilisé son pouvoir sur vous, vous venez de perdre §c3 coeurs §fet reçu §8Blindness §fet §7Slowness §fpour 4 secondes."));
            });

            hakkeCooldown = 20 * 60;

        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        Player neji = Role.findPlayer(NarutoRoles.NEJI);
        if (neji == null) return;
        if (Role.isRole(player, NarutoRoles.HINATA)) {
            neji.sendMessage(CC.prefix("§aHinata §fest §cmort§f. De ce fait, vous obtenez §cForce I §fde façon permanente."));
            neji.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onAllPlayerPowerUse(Player player) {
        Player neji = Role.findPlayer(NarutoRoles.NEJI);
        if (neji == null) return;
        if (player.getLocation().distance(neji.getLocation()) <= 20) {
            neji.sendMessage(CC.prefix("§a" + roleManager.getRole(player).getRoleName() + " §fvient d'utiliser son pouvoir autour de vous."));
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

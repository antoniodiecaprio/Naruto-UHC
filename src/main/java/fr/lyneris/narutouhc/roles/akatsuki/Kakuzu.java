package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kakuzu extends NarutoRole {

    private Chakra chakra = Chakra.DOTON;
    private int corpsCooldown = 0;
    private int healthUse = 0;
    private boolean healthCooldown = false;

    public NarutoRoles getRole() {
        return NarutoRoles.KAKUZU;
    }

    @Override
    public void resetCooldowns() {
        healthCooldown = true;
        corpsCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (corpsCooldown > 0) {
            corpsCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kakuzu";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §cKakuzu\n" +
                "§7▎ Objectif: §rSon but est de gagner avec l'§cAkatsuki\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rCorps rapiécé§7\", celui-ci lui permet d’immobiliser tous les joueurs ne faisant pas partie de son camp dans un rayon de 20 blocs pendant 5 secondes, il possède un délai de 10 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités : \n" +
                "§e §f\n" +
                "§7• Il dispose d’une particularité qui lui permet, lorsqu'il tombe à §c1 demi-coeur§7, de recevoir les effets §eAbsorption 2§7 pendant 5 secondes, §dRégénération 2§7 pendant 40 secondes et §6Résistance au feu 2§7 pendant 40 secondes, cette particularité peut avoir lieu seulement 4 fois, tant qu'il a cette particularité, il ne peut pas mourir. Cependant une fois après avoir utilisé son pouvoir, il sera vulnérable pendant 5 minutes, cela veut dire qu'il peut mourir.\n" +
                "§e §f\n" +
                "§7• Il connaît l’identité d’§cHidan§7 et obtient l’effet §cForce 1§7 lorsqu'il est dans un rayon de 10 blocs proches d’§cHidan§7, si celui-ci vient à mourir, alors §cKakuzu§7 recevra l'effet §cForce 1§7 de manière permanente.\n" +
                "§e §f\n" +
                "§7• Il possède une nature de chakra, cependant à chaque fois qu’il meurt, avec le pouvoir cité ci-dessus, il change de nature de Chakra, cela se fait dans cette ordre : §6Doton§7 → §eRaiton§7 → §aFûton§7 → §cKaton§7 → §9Suiton§7\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Corps rapiécé")).toItemStack());
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(Role.isRole(player, NarutoRoles.HIDAN)) {
            getPlayer().sendMessage(prefix("&cHidan &fest mort. Vous obtenez &cForce 1 &fpermanent."));
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if (healthUse >= 4) return;
        if (healthCooldown) return;
        if (player.getHealth() - event.getDamage() <= 1) {
            event.setDamage(0.01);
            if (chakra == Chakra.DOTON) {
                chakra = Chakra.RAITON;
            } else if (chakra == Chakra.RAITON) {
                chakra = Chakra.FUTON;
            } else if (chakra == Chakra.FUTON) {
                chakra = Chakra.KATON;
            } else if (chakra == Chakra.KATON) {
                chakra = Chakra.SUITON;
            }
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40 * 20, 1, false, false));
            healthCooldown = true;
            Tasks.runLater(() -> healthCooldown = false, 5 * 20 * 60);
            healthUse++;
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Corps rapiécé")) {
            if (corpsCooldown > 0) {
                player.sendMessage(Messages.cooldown(corpsCooldown));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            Loc.getNearbyPlayers(player, 20).forEach(player1 -> {
                if (roleManager.getRole(player1) != null) {
                    if (roleManager.getCamp(player1) != roleManager.getCamp(player)) {
                        manager.setStuned(player1, true, 5);
                        player.sendMessage(CC.prefix("§fVous avez immobilisé §c" + player1.getName() + "§f."));
                    }
                }
            });

            corpsCooldown = 10 * 60;

        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }
}

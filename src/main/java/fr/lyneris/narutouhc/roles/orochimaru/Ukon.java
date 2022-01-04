package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static fr.lyneris.narutouhc.roles.orochimaru.Sakon.senpoCooldown;
import static fr.lyneris.narutouhc.roles.orochimaru.Sakon.usedBarrier;

public class Ukon extends NarutoRole {

    public static int senpoTimer = 0;
    public static int barrierTimer = 0;
    public static boolean died = false;

    public NarutoRoles getRole() {
        return NarutoRoles.UKON;
    }

    @Override
    public String getRoleName() {
        return "Ukon";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §5Ukon\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp d'§5Orochimaru\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rSenpô§7”, il doit l’utiliser en même temps que §5Sakon§7 avec un délai de 15 secondes à son utilisation pour que l’item puisse être utilisé, à l’utilisation, lui et §5Sakon§7 reçoivent les effets §cForce 1§7 et §5Vitesse 1§7 pendant 5 minutes, il perd §c1 cœur§7 permanent à la fin de l’utilisation et il possède un délai de 10 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rBarrière protectrice§7”, celui-ci lui permet de faire apparaître une barrière violette qui est indestructible, celle-ci est en forme de carré et se forme selon la position de §5Sakon§7 et de §5Ukon§7, cette barrière est donc utilisable de la façon suivante: §5Ukon§7 et §5Sakon§7 doivent utiliser leur item en même temps avec un délai de 15 secondes et être à 40 blocs maximum de distance l’un de l’autre. L’item ne peut être utilisé qu’une seule fois et la barrière reste active pendant 5 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Lorsqu’il se trouve dans un rayon de 15 blocs de §5Sakon§7, il reçoit l’effet §bVitesse 1§7.\n" +
                "§e §f\n" +
                "§7• Si §5Ukon§7 ou bien §5Sakon§7 vient à mourir dans un rayon de 15 blocs proche de l'autre, alors il mourra à son tour, cependant dans le cas contraire, si l’un meurt dans un rayon supérieur à 15 blocs de l’autre alors il ne mourra pas et ressuscitera 5 minutes après sa mort à l’emplacement de celui qui est toujours en vie mais si l’autre meurt dans les 5 minutes suivant la mort du premier alors tout deux mourront définitivement et ne pourront pas être ressusciter par n’importe quel autre rôle.\n" +
                "§e §f\n" +
                "§7• Il possède un chat permettant de communiquer avec §5Sakon§7, il lui suffit simplement d’écrire dans le chat avec l’aide du préfixe \"!\" pour pouvoir communiquer avec lui.\n" +
                "§e §f\n" +
                "§7• Il connaît l'identité de §5Sakon§7.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.SAKON);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senpô")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Barrière protectrice")).toItemStack());
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);
        if(sakon.getLocation().distance(player.getLocation()) > 15) {
            if(!(event.getDamage() >= player.getHealth())) return;
            if(Sakon.died) {
                sakon.setGameMode(GameMode.SURVIVAL);
                sakon.setHealth(0);
                player.setHealth(0);
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            player.setGameMode(GameMode.SPECTATOR);
            died = true;
        }
    }



    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Senpô")) {

            if (senpoCooldown > 0) {
                player.sendMessage(Messages.cooldown(senpoCooldown));
                return;
            }

            if (Sakon.senpoTimer > 0) {
                Sakon.useSenpo();
                return;
            }

            if (senpoTimer > 0) {
                player.sendMessage(CC.prefix("§cCet item est déjà en cours d'utilisation."));
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            senpoTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aSakon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));
        }

        if (Item.interactItem(event.getItem(), "Barrière protectrice")) {

            if (usedBarrier) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player ukon = Role.findPlayer(NarutoRoles.SAKON);
            if (ukon == null) {
                player.sendMessage(CC.prefix("§cSakon n'est pas en vie."));
                return;
            }

            if (ukon.getLocation().distance(player.getLocation()) > 40) {
                player.sendMessage(CC.prefix("§cSakon doit être au maximum à 40 blocks de vous."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            if (Sakon.barrierTimer > 0) {
                Sakon.useBarrier();
                return;
            }

            if (barrierTimer > 0) {
                player.sendMessage(CC.prefix("§cCet item est déjà en cours d'utilisation."));
            }

            barrierTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aSakon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));

        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);

        if (sakon != null) {
            if (sakon.getLocation().distance(player.getLocation()) <= 15) {
                sakon.setHealth(0);
                sakon.sendMessage(CC.prefix("§cUkon est mort et vous a emporté avec lui..."));
            } else {
                died = true;
            }
        }
    }

    @Override
    public void onSecond(int timer, Player player) {
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);

        if (ukon == null || sakon == null) return;

        if (ukon.getLocation().distance(sakon.getLocation()) <= 15) {
            ukon.removePotionEffect(PotionEffectType.SPEED);
            ukon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 0, false, false));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

}

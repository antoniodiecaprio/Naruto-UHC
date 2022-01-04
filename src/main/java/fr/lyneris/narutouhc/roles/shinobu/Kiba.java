package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Kiba extends NarutoRole {

    public boolean usedKiba = false;
    public boolean usedAkamaru = false;

    public NarutoRoles getRole() {
        return NarutoRoles.KIBA;
    }

    @Override
    public String getRoleName() {
        return "Kiba";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aKiba\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d'un item nommé \"§rAkamaru§7\", il lui permet de faire apparaître un chien, il lui permet aussi de recevoir l'effet §cForce 1§7, cependant, l'effet ne dure que 10 minutes, après quoi, son chien disparaît et il perd son effet de §cForce§7.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r➜ /ns sniff <Joueur>§7, celle-ci lui permet d’avoir la position d’un joueur indiqué au dessus de sa barre de raccourcis à l’aide d’une flèche. Le joueur ciblé doit être situé dans un rayon de 20 blocs autour de §aKiba§7. La commande est utilisable une seule fois.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l'effet §bVitesse 1§7 de manière permanente. \n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §6Doton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Akamaru")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Akamaru")) {

            if (usedAkamaru) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usedAkamaru = true;

            Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
            wolf.setTamed(true);
            wolf.setOwner(player);
            wolf.setAngry(false);
            wolf.setSitting(false);

            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aAkamaru§f."));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20 * 60, 0, false, false));

            Tasks.runLater(() -> {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                wolf.setHealth(0);
                player.sendMessage(CC.prefix("§cVous avez perdu votre effet de Force et votre chien."));
            }, 10 * 20 * 60);

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

    @Override
    public void onSubCommand(Player player, String[] args) {

        if (args[0].equalsIgnoreCase("sniff")) {

            if (usedKiba) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns sniff <player>"));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (target.getLocation().distance(player.getLocation()) > 20) {
                player.sendMessage(CC.prefix("§c" + target.getName() + " n'est pas dans un rayon de 20 blocks autour de vous."));
                return;
            }
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            usedKiba = true;

            final String oldName;
            oldName = player.getName();

            final String targetOldName;
            targetOldName = target.getName();

            new BukkitRunnable() {
                @Override
                public void run() {
                    final Player newPlayer = Bukkit.getPlayer(oldName);
                    final Player newTarget = Bukkit.getPlayer(targetOldName);

                    if (newPlayer != null && newTarget != null) {
                        Title.sendActionBar(newPlayer, Loc.getDirectionMate(newPlayer, newTarget));
                    }
                }
            }.runTaskTimer(narutoUHC, 0, 20);

        }

    }
}

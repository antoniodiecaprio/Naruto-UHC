package fr.lyneris.narutouhc.roles.taka;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Karin extends NarutoRole {

    public final HashMap<UUID, Integer> avancement = new HashMap<>();
    public UUID kagurashinganTarget = null;
    public int kagurashinganCooldown = 0;
    public boolean usedOnceInEpisode = false;
    public boolean usedTwiceInEpisode = false;

    public NarutoRoles getRole() {
        return NarutoRoles.KARIN;
    }

    @Override
    public void resetCooldowns() {
        kagurashinganCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (kagurashinganCooldown > 0) {
            kagurashinganCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Karin";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §6Karin\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp de §6Taka\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Elle dispose d’un livre lui permettant de voir les pseudos des joueurs se trouvant dans un rayon de 100 blocs autour d’elle.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns kagurashingan <Joueur>§7, cette commande lui permet de voir la position d’un joueur avec l’aide d’une flèche au dessus de sa barre de raccourcis, elle peut l’utiliser uniquement si le joueur ciblé se situe dans un rayon de 100 blocs autour d’elle. La position du joueur est inconnu s’il se trouve à plus de 200 blocs de §6Karin§7. La commande possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§r→ /ns morsure <Joueur>§7, celle-ci lui permet de régénérer instantanément le joueur ciblé, elle peut le faire sur elle-même. Elle ne possède aucun délai, cependant si elle l’utilise plus d’une fois par épisode, à chaque utilisation elle perd §c1 cœur§7 permanent. Elle doit se trouver dans un rayon de 10 blocs du joueur ciblé.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Elle dispose de l’effet §cForce 1§7 permanent.\n" +
                "§e §f\n" +
                "§7• Elle dispose d’une particularité qui lui permet de connaître le rôle exacte d’un joueur en restant à côté de lui à environ 20 blocs de rayon du joueur pendant 10 minutes, cela fonctionne sur tout le monde, cependant pour les membres de son camp, il lui faudra attendre 2 minutes proche de l’un d’entre eux.\n" +
                "§e §f\n" +
                "§7• Son camp se retrouvera souvent allié avec un autre camp, c’est pour cela qu’au début de la partie, son camp fait partie du camp d’§5Orochimaru§7. Si §5Orochimaru§7 vient à mourir alors son camp sera sans allié, suite à ça, si §cItachi§7 vient à mourir, son camp s’alliera avec l’§cAkatsuki§7 et donc il connaîtra  l’identité de §cNagato§7. Si §dTobi§7 et §dMadara§7 rassemble les 9 biju pour former Jûbi, il s’alliera au camp §aShinobi§7.\n" +
                "§e §f\n" +
                "§7• Elle connaît l’identité d’§5Orochimaru§7.\n" +
                "§e §f\n" +
                "§7• A la mort d’§5Orochimaru§7, elle obtient l’identité de §6Sasuke§7.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.BOOK).setName(Item.interactItem("Karin")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        Player karin = Role.findPlayer(NarutoRoles.ITACHI);
        if (karin == null) return;

        if (Role.isRole(player, NarutoRoles.OROCHIMARU)) {
            karin.sendMessage(CC.prefix("§cOrochimaru §fest mort. Voici l'identité de &aSasuke&f:"));
            Role.knowsRole(karin, NarutoRoles.SASUKE);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event, "Karin")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.sendMessage(" ");
            player.sendMessage(prefix("&aVoici les joueurs autour de vous :"));
            Loc.getNearbyPlayers(player, 100).forEach(target -> player.sendMessage(" &8- &c" + target.getName()));
            player.sendMessage(" ");
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("kagurashingan")) {
            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (target.getLocation().distance(player.getLocation()) > 100) {
                player.sendMessage(prefix("&cCe joueur n'est pas dans un rayon de 100 blocks autour de vous."));
                return;
            }

            if (kagurashinganCooldown > 0) {
                player.sendMessage(Messages.cooldown(kagurashinganCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            this.kagurashinganTarget = target.getUniqueId();
            this.kagurashinganCooldown = 5 * 60;
        }

        if (args[0].equalsIgnoreCase("morsure")) {

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (target.getLocation().distance(player.getLocation()) > 10) {
                player.sendMessage(prefix("&cCe joueur n'est pas dans un rayon de 10 blocks autour de vous."));
                return;
            }

            if (usedOnceInEpisode) {
                usedTwiceInEpisode = true;
                player.sendMessage(prefix("&cVous avez utilisé cette commande deux fois en un épisode. De ce fait à chaque utilisation vous perdrez 1 coeur permanent."));
            }

            if (usedTwiceInEpisode) {
                target.setMaxHealth(player.getMaxHealth() - 2);
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            target.setHealth(player.getMaxHealth());

            usedOnceInEpisode = true;

        }

    }

    @Override
    public void onNewEpisode(Player player) {
        usedOnceInEpisode = false;
    }

    @Override
    public void onSecond(int timer, Player player) {
        if (Bukkit.getPlayer(this.kagurashinganTarget) != null) {
            Title.sendActionBar(player, Loc.getDirectionMate(player, Bukkit.getPlayer(this.kagurashinganTarget), 200));
        }

        Loc.getNearbyPlayers(player, 20).forEach(target -> {
            avancement.put(target.getUniqueId(), avancement.getOrDefault(target.getUniqueId(), 0) + 1);
            //CHECK ROLE OF target
            NarutoRole role = roleManager.getRole(target.getUniqueId());
            if (true) {
                if (avancement.get(target.getUniqueId()) == 2 * 60) {
                    player.sendMessage(prefix("&fL'avancement sur &a" + target.getName() + " &fvient de terminer. Il est &a" + role.getRoleName() + "&f."));
                }
            } else {
                if (avancement.get(target.getUniqueId()) == 10 * 60) {
                    player.sendMessage(prefix("&fL'avancement sur &a" + target.getName() + " &fvient de terminer. Il est &a" + role.getRoleName() + "&f."));
                }
            }
        });


    }

    @Override
    public Camp getCamp() {
        return Camp.TAKA;
    }
}

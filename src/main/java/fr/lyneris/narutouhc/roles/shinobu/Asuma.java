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
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Asuma extends NarutoRole {

    public int nueesCooldown = 0;
    public int avancement = 0;
    public Player latest = null;

    public NarutoRoles getRole() {
        return NarutoRoles.ASUMA;
    }

    @Override
    public void resetCooldowns() {
        nueesCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (nueesCooldown > 0) {
            nueesCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Asuma";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §rAsuma\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "• Il dispose d’une épée en fer Tranchant 5.\n" +
                "§e §f\n" +
                "• Il dispose d’un item nommé \"§rNuées Ardentes§7\", celui-ci lui permet de créer une explosion 3 mètres face à lui et cela 3 secondes après l’avoir utilisé, il possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "• Lorsqu’il se trouve proche de §aIno§7, §aShikamaru§7 ou de §aChôji§7, une progression invisible commence, celle-ci a pour but de donner l’identité de l’un d’entre eux, cela ne donne pas le rôle mais seulement le pseudo, la progression se réalise de la manière suivante :\n" +
                "§e §f\n" +
                "§7- 20 blocs = 1pt/s\n" +
                "§7- 10 blocs = 5pts/s\n" +
                "§7- 5 blocs = 10pts/s\n" +
                "§e §f\n" +
                "§7La limite de cette progression est de 2500, une fois cela atteint, un message affichant l’identité du joueur auquel la progression est terminée, lui est affiché.\n" +
                "§e §f\n" +
                "§7• Il possède l'effet §cForce 1§7 et §bVitesse 1§7 permanent. \n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §aFûton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).setName("§7▎ §6Epée").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Nuées Ardentes")).toItemStack());
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {

        if (avancement >= 2500) {
            Player target = latest;
            if (target != null) {
                player.sendMessage(CC.prefix("§fProgression §aterminée§f. Voici l’identité du joueur auquel la progression est terminée : §a§l" + latest.getName()));
                latest = null;
            }
        } else {
            Player ino = Role.findPlayer(NarutoRoles.INO);
            if (ino != null) {
                int distance = (int) ino.getLocation().distance(player.getLocation());
                if (distance <= 5) {
                    avancement += 10;
                    latest = ino;
                } else if (distance <= 10) {
                    avancement += 5;
                    latest = ino;
                } else if (distance <= 20) {
                    avancement += 1;
                    latest = ino;
                }
            }

            Player shikamaru = Role.findPlayer(NarutoRoles.SHIKAMARU);
            if (shikamaru != null) {
                int distance = (int) shikamaru.getLocation().distance(player.getLocation());
                if (distance <= 5) {
                    avancement += 10;
                    latest = shikamaru;
                } else if (distance <= 10) {
                    avancement += 5;
                    latest = shikamaru;
                } else if (distance <= 20) {
                    avancement += 1;
                    latest = shikamaru;
                }
            }

            Player choji = Role.findPlayer(NarutoRoles.CHOJI);
            if (choji != null) {
                int distance = (int) choji.getLocation().distance(player.getLocation());
                if (distance <= 5) {
                    avancement += 10;
                    latest = choji;
                } else if (distance <= 10) {
                    avancement += 5;
                    latest = choji;
                } else if (distance <= 20) {
                    avancement += 1;
                    latest = choji;
                }
            }
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Nuées Ardentes")) {

            if (nueesCooldown > 0) {
                player.sendMessage(Messages.cooldown(nueesCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            final Location front;
            front = player.getLocation();
            Tasks.runLater(() -> front.getWorld().createExplosion(front, 3.0f), 3 * 20);

            player.sendMessage(CC.prefix("§fUne explosion va retentir dans §a3 secondes§f."));

            nueesCooldown = 5 * 60;

        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }
}

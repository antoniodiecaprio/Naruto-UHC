package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class KillerBee extends NarutoRole {

    public int gyukiCooldown = 0;
    public int timer = 0;
    public boolean usedDeath = false;

    public NarutoRoles getRole() {
        return NarutoRoles.KILLER_BEE;
    }

    @Override
    public void resetCooldowns() {
        gyukiCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (gyukiCooldown > 0) {
            gyukiCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Killer Bee";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aKiller Bee\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rGyûki§7\", lorsqu’il clique dessus, il obtient les effets §bVitesse 1§7 et §rSaut Amélioré 4§7 pendant 5 minutes, il ne peut utiliser cet item qu'une fois toutes les 20 minutes. \n" +
                "§e §f\n" +
                "§7• Il dispose d'une épée en fer Tranchant 5. \n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r➜ /ns death§7, celle-ci permet de faire croire à tout les joueurs de la partie que §aKiller Bee§7 est mort avec l'aide d'un message dans le chat, suite à cela §aKiller Bee§7 sera invisible aux yeux de tous, le pouvoir dure 5 minutes et il pourra se rendre visible avec l'aide d'un item nommé \"§rrevive§7\".\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l'effet §cForce 1§7 permanent. \n" +
                "§e §f\n" +
                "§7• Il n’est pas affecté par les pouvoirs de §rSamehada§7 et de §rTsukuyomi§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §eRaiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).setName(Item.specialItem("Epee")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Gyûki")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Gyûki")) {

            if (gyukiCooldown > 0) {
                player.sendMessage(Messages.cooldown(gyukiCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20 * 60, 3, false, false));

            gyukiCooldown = 20 * 60;

        }

        if (Item.interactItem(event.getItem(), "Revive")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.getInventory().removeItem(player.getItemInHand());
            timer = 99;
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("death")) {
            if (usedDeath) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            Messages.sendDeathMessage(player);
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.hidePlayer(player));
            player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Revive")).toItemStack());

            usedDeath = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    timer++;
                    Bukkit.getOnlinePlayers().forEach(player1 -> player1.hidePlayer(player));
                    if (timer >= 60) {
                        Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(player));
                        player.sendMessage(CC.prefix("§fVous êtes désormais §avisible§f."));
                        cancel();
                    }
                }
            }.runTaskTimer(narutoUHC, 0, 100);

        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.RAITON;
    }
}

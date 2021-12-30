package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public class YondaimeRaikage extends NarutoRole {

    public boolean usingArmor = false;
    public int armorTime = 20 * 60;

    public NarutoRoles getRole() {
        return NarutoRoles.YONDAIME_RAIKAGE;
    }

    @Override
    public String getRoleName() {
        return "Yondaime Raikage";
    }


    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aYondaime Raikage\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rArmure de foudre§7“, lorsqu’il l’utilise il possède l’effet §9Résistance 1§7 pendant 20 minutes. En cliquant à nouveau sur son item, cela annule son armure et donc son effet. Les 20 minutes sont le total de temps qu’il peut porter l’armure. A chaque fois, qu’il remet l’armure, le temps restant diminue en fonction de son utilisation.\n" +
                "§7Exemple : Si l’armure est utilisée 5 minutes puis désactivée, alors l’utilisation suivant, il ne pourra l’utiliser que 15 minutes maximum et ainsi de suite.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l'effet §bVitesse 2§7 permanent. \n" +
                "§e §f\n" +
                "§7• Il connaît l’identité de §aKiller Bee§7, Lorsque celui-ci vient à mourir, il obtient §c2 cœurs§7 supplémentaires et l’identité de la personne ayant tué ce dernier.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §eRaiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Armure de foudre")).toItemStack());
        Role.knowsRole(player, NarutoRoles.KILLER_BEE);
    }

    @Override
    public void onMinute(int minute, Player player) {
        if(minute == 70) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();

            Objective objective = board.registerNewObjective("showhealth", "health");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(" §c❤");

            for(Player online : Bukkit.getOnlinePlayers()){
                online.setScoreboard(board);
                online.setHealth(online.getHealth());
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Armure de foudre")) {

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            if (usingArmor) {
                usingArmor = false;
                player.sendMessage(CC.prefix("§fVous avez désactivé votre §c  Armure§f."));
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            } else {
                usingArmor = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, armorTime * 20, 0, false, false));

                player.sendMessage(CC.prefix("§fVous avez activé votre §aArmure§f."));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (usingArmor) {
                            armorTime--;
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(narutoUHC, 0, 20);

            }
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        Player yondaime = Role.findPlayer(NarutoRoles.YONDAIME_RAIKAGE);
        if (yondaime == null) return;

        if (Role.isRole(player, NarutoRoles.KILLER_BEE)) {
            if (event.getEntity().getKiller() == null) {
                yondaime.sendMessage(CC.prefix("§aKiller Bee §fest mort de §cPVE§f."));
            } else {
                yondaime.sendMessage(CC.prefix("§aKiller Bee §fs'est fait tué par §c" + event.getEntity().getKiller().getName()));
            }
            yondaime.setMaxHealth(player.getMaxHealth() + 4);
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

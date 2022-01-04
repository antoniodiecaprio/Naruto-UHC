package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Shino extends NarutoRole {

    public int kikaichuUses = 0;
    public List<UUID> kikaichuTargets = new ArrayList<>();

    public NarutoRoles getRole() {
        return NarutoRoles.SHINO;
    }

    @Override
    public String getRoleName() {
        return "Shino";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aShino\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rkikaichû§7\", lorsqu’il effectue un clique droit en visant joueur avec celui-ci, il recevra toutes les informations de ce joueur, les informations dites sont les dégâts infligés (à qui ou à quoi), les dégâts subis (par qui ou par quoi), s’il mange des pommes en or, s’il meurt par qui il se fait tuer et s’il tue quelqu’un. Son item possède 5 utilisations maximum. \n" +
                "§e §f\n" +
                "Commandes :\n" +
                "§e §f\n" +
                "§r➜ /ns tracking <Joueur>§7, celle-ci lui permet d’avoir la position d’un joueur indiqué au dessus de sa barre de raccourci à l’aide d’une flèche. Pour que sa commande fonctionne il lui faut avoir utilisé son pouvoir ci-dessus sur la personne ciblé, cependant après avoir utilisé son pouvoir, il ne pourra plus accéder aux informations du joueur dans son chat..\n" +
                "§e §f\n" +
                "§r➜ /ns feed <Joueur>§7, celle-ci lui permet de voler §c1 cœur§7 permanent au joueur ciblé, la personne perdra 1 cœur de manière permanent puis Shino obtient 1 cœur supplémentaire. Pour que sa commande fonctionne il lui faut avoir utilisé son item ci-dessus sur la personne ciblé, cependant après avoir utilisé son pouvoir, il ne pourra plus accéder aux informations du joueur dans son chat.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kikaichû")).toItemStack());
    }

    @Override
    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {
        Player shino = Role.findPlayer(NarutoRoles.SHINO);
        if (shino == null) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;
        if (kikaichuTargets.contains(entity.getUniqueId())) {
            shino.sendMessage(CC.prefix("§8(§c!§8) §a" + entity.getName() + " §fa prit un dégât de §c" + event.getCause().name().toLowerCase()));
        }
    }

    @Override
    public void onAllPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
        Player shino = Role.findPlayer(NarutoRoles.SHINO);
        if (shino == null) return;
        if (kikaichuTargets.contains(player.getUniqueId())) {
            shino.sendMessage(CC.prefix("§8(§c!§8) §a" + player.getName() + " §fa mangé une §ePomme d'Or§f."));
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        Player shino = Role.findPlayer(NarutoRoles.SHINO);
        if (shino == null) return;
        if (event.getEntity().getKiller() != null && kikaichuTargets.contains(event.getEntity().getKiller().getUniqueId())) {
            shino.sendMessage(CC.prefix("§8(§c!§8) §a" + player.getName() + " §fa tué §c" + event.getEntity().getName()));
        }

        if (kikaichuTargets.contains(event.getEntity().getUniqueId())) {
            shino.sendMessage(CC.prefix("§8(§c!§8) §a" + player.getName() + " §fs'est fait tuer"));
        }

    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        Player shino = Role.findPlayer(NarutoRoles.SHINO);
        if (shino == null) return;

        if (!(event.getEntity() instanceof Player)) return;

        if (kikaichuTargets.contains(damager.getUniqueId())) {
            shino.sendMessage(CC.prefix("§8(§c!§8) §c" + damager.getName() + " §fa mis un dégât à §a" + event.getEntity().getName()));
        }

        if (kikaichuTargets.contains(event.getEntity().getUniqueId())) {
            shino.sendMessage(CC.prefix("§8(§c!§8) §a" + event.getEntity().getName() + " §fs'est prit un dégât de §a" + damager.getName()));
        }

    }

    @Override
    public void onPlayerInteractAtPlayer(PlayerInteractAtEntityEvent event, Player target) {

        Player player = event.getPlayer();

        if (Item.interactItem(player.getItemInHand(), "Kikaichû")) {

            if (kikaichuUses >= 5) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 5 fois."));
                return;
            }

            if (kikaichuTargets.contains(target.getUniqueId())) {
                player.sendMessage(CC.prefix("§cCe joueur est déjà sous l'effet de votre pouvoir."));
                return;
            }

            kikaichuUses++;

            kikaichuTargets.add(target.getUniqueId());
            player.sendMessage(CC.prefix("§fVous avez utilisé votre §aKikaichû §fsur §c" + target.getName() + "§f. Vous avez utilisé votre pouvoir §c" + kikaichuUses + "§f fois."));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("tracking")) {

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns tracking <player>"));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (!kikaichuTargets.contains(target.getUniqueId())) {
                player.sendMessage(CC.prefix("§cPour utiliser cette commande sur ce joueur, vous devez avoir utilisé votre Kikaichû sur lui."));
                return;
            }

            final String oldName;
            oldName = player.getName();

            final String targetOldName;
            targetOldName = target.getName();

            player.sendMessage(CC.prefix("§fVous avez utilisé votre §aTrack §fsur §c" + target.getName()));

            Tasks.runTimer(() -> {
                Player newPlayer = Bukkit.getPlayer(oldName);
                Player newTarget = Bukkit.getPlayer(targetOldName);

                if (newPlayer != null && newTarget != null) {
                    Title.sendActionBar(newPlayer, Loc.getDirectionMate(newPlayer, newTarget));
                }
            }, 0, 20);
            new BukkitRunnable() {
                @Override
                public void run() {

                }
            }.runTaskTimer(narutoUHC, 0, 20);

            kikaichuTargets.remove(target.getUniqueId());

        }

        if (args[0].equalsIgnoreCase("feed")) {
            {

                if (args.length != 2) {
                    player.sendMessage(Messages.syntax("/ns feed <player>"));
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(Messages.offline(args[1]));
                    return;
                }

                if (!kikaichuTargets.contains(target.getUniqueId())) {
                    player.sendMessage(CC.prefix("§cPour utiliser cette commande sur ce joueur, vous devez avoir utilisé votre Kikaichû sur lui."));
                    return;
                }

                if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                player.sendMessage(CC.prefix("§fVous avez utilisé votre §aFeed §fsur §c" + target.getName()));
                target.setMaxHealth(target.getMaxHealth() - 2);
                player.setMaxHealth(player.getMaxHealth() + 2);
                target.sendMessage(CC.prefix("§cShino §fa utilisé son §aFeed §fsur vous. De ce fait, vous perdez §c1 coeur §fpermanent."));

                kikaichuTargets.remove(target.getUniqueId());

            }
        }

    }
}

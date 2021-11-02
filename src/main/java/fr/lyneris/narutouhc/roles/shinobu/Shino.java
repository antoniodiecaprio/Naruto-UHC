package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
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

    @Override
    public String getRoleName() {
        return "Shino";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
            player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kikaichû")).toItemStack());
    }

    @Override
    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {
        Player shino = Role.findPlayer("Shino");
        if(shino == null) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.CUSTOM || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if(kikaichuTargets.contains(entity.getUniqueId())) {
            shino.sendMessage("§7▎ §8(§c!§8) §a" + entity.getName() + " §fa prit un dégât de §c" + event.getCause().name().toLowerCase());
        }
    }

    @Override
    public void onAllPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
        Player shino = Role.findPlayer("Shino");
        if(shino == null) return;
        if(kikaichuTargets.contains(player.getUniqueId())) {
            shino.sendMessage("§7▎ §8(§c!§8) §a" + player.getName() + " §fa mangé une §ePomme d'Or§f.");
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        Player shino = Role.findPlayer("Shino");
        if(shino == null) return;
        if(event.getEntity().getKiller() != null && kikaichuTargets.contains(event.getEntity().getKiller().getUniqueId())) {
            shino.sendMessage("§7▎ §8(§c!§8) §a" + player.getName() + " §fa tué §c" + event.getEntity().getName());
        }

        if(kikaichuTargets.contains(event.getEntity().getUniqueId())) {
            shino.sendMessage("§7▎ §8(§c!§8) §a" + player.getName() + " §fs'est fait tuer");
        }

    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        Player shino = Role.findPlayer("Shino");
        if(shino == null) return;

        if(!(event.getEntity() instanceof Player)) return;

        if(kikaichuTargets.contains(damager.getUniqueId())) {
            shino.sendMessage("§7▎ §8(§c!§8) §c" + damager.getName() + " §fa mis un dégât à §a" + event.getEntity().getName());
        }

        if(kikaichuTargets.contains(event.getEntity().getUniqueId())) {
            shino.sendMessage("§7▎ §8(§c!§8) §a" + event.getEntity().getName() + " §fs'est prit un dégât de §a" + damager.getName());
        }

    }

    @Override
    public void onPlayerInteractAtPlayer(PlayerInteractAtEntityEvent event, Player target) {

        Player player = event.getPlayer();

        if(Item.interactItem(player.getItemInHand(), "Kikaichû")) {

            if(kikaichuUses >= 5) {
                player.sendMessage("§7▎ §cVous avez déjà utilisé ce pouvoir 5 fois.");
                return;
            }

            if(kikaichuTargets.contains(target.getUniqueId())) {
                player.sendMessage("§7▎ §cCe joueur est déjà sous l'effet de votre pouvoir.");
                return;
            }

            kikaichuUses++;

            kikaichuTargets.add(target.getUniqueId());
            player.sendMessage("§7▎ §fVous avez utilisé votre §aKikaichû §fsur §c" + target.getName() + "§f. Vous avez utilisé votre pouvoir §c" + kikaichuUses + "§f fois.");
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if(args[0].equalsIgnoreCase("tracking")) {

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns tracking <player>"));
                return;
            }


            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (!kikaichuTargets.contains(target.getUniqueId())) {
                player.sendMessage("§7▎ §cPour utiliser cette commande sur ce joueur, vous devez avoir utilisé votre Kikaichû sur lui.");
                return;
            }

            final String oldName;
            oldName = player.getName();

            final String targetOldName;
            targetOldName = target.getName();

            player.sendMessage("§7▎ §fVous avez utilisé votre §aTrack §fsur §c" + target.getName());

            new BukkitRunnable() {
                @Override
                public void run() {
                    final Player newPlayer = Bukkit.getPlayer(oldName);
                    final Player newTarget = Bukkit.getPlayer(targetOldName);

                    if(newPlayer != null && newTarget != null) {
                        Title.sendActionBar(newPlayer, Loc.getDirectionMate(newPlayer, newTarget));
                    }
                }
            }.runTaskTimer(narutoUHC, 0, 20);

            kikaichuTargets.remove(target.getUniqueId());

        }

        if(args[0].equalsIgnoreCase("feed")) {
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
                    player.sendMessage("§7▎ §cPour utiliser cette commande sur ce joueur, vous devez avoir utilisé votre Kikaichû sur lui.");
                    return;
                }

                player.sendMessage("§7▎ §fVous avez utilisé votre §aFeed §fsur §c" + target.getName());
                target.setMaxHealth(target.getMaxHealth()-2);
                player.setMaxHealth(player.getMaxHealth()+2);
                target.sendMessage("§7▎ §cShino §fa utilisé son §aFeed §fsur vous. De ce fait, vous perdez §c1 coeur §fpermanent.");

                kikaichuTargets.remove(target.getUniqueId());

            }
        }

    }
}

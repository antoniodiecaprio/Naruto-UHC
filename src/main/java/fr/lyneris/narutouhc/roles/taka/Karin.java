package fr.lyneris.narutouhc.roles.taka;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
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
    public List<String> getDescription() {
        return new ArrayList<>();
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

package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
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
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.UKON);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senpô")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Barrière protectrice")).toItemStack());
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);
        if(sakon.getLocation().distance(player.getLocation()) > 15) {
            if(!(event.getFinalDamage() >= player.getHealth())) return;
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

            senpoTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aSakon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));
        }

        if (Item.interactItem(event.getItem(), "Barrière protectrice")) {

            if (usedBarrier) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player ukon = Role.findPlayer(NarutoRoles.UKON);
            if (ukon == null) {
                player.sendMessage(CC.prefix("§cUkon n'est pas en vie."));
                return;
            }

            if (ukon.getLocation().distance(player.getLocation()) > 40) {
                player.sendMessage(CC.prefix("§cUkon doit être au maximum à 40 blocks de vous."));
                return;
            }

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
            sakon.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            sakon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 0, false, false));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

}

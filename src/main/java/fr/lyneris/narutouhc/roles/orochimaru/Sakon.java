package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Sakon extends NarutoRole {

    public static int senpoTimer = 0;
    public static int barrierTimer = 0;
    public static boolean usedBarrier = false;
    public static int senpoCooldown = 0;

    @Override
    public void resetCooldowns() {
        senpoCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(senpoTimer > 0) {
            senpoTimer--;
        }

        if(senpoCooldown > 0) {
            senpoCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Sakon";
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
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event.getItem(), "Senpô")) {

            if(senpoCooldown > 0) {
                player.sendMessage(Messages.cooldown(senpoCooldown));
                return;
            }

            if(senpoTimer > 0) {
                player.sendMessage(CC.prefix("§cCet item est déjà en cours d'utilisation."));
            }

            senpoTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aUkon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));

            senpoCooldown = 10*60;

        }

        if(Item.interactItem(event.getItem(), "Barrière protectrice")) {

            if(usedBarrier) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player ukon = Role.findPlayer(NarutoRoles.UKON);
            if(ukon == null) {
                player.sendMessage(CC.prefix("§cUkon n'est pas en vie."));
                return;
            }

            if(ukon.getLocation().distance(player.getLocation()) > 40) {
                player.sendMessage(CC.prefix("§cUkon doit être au maximum à 40 blocks de vous."));
                return;
            }

            if(barrierTimer > 0) {
                player.sendMessage(CC.prefix("§cCet item est déjà en cours d'utilisation."));
            }

            barrierTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aUkon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));

        }
    }

    public static void useBarrier() {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        if(ukon == null) return;
        if(sakon == null) return;

        ukon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aBarrière protectrice§f."));
        sakon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aBarrière protectrice§f."));

        usedBarrier = true;

        //TODO BUILD LE TRUC DE MERDE
    }

    public static void useSenpo() {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        if(ukon == null) return;
        if(sakon == null) return;

        ukon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aSenpô§f."));
        sakon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aSenpô§f."));

        sakon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
        ukon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
        sakon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 0, false, false));
        ukon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 0, false, false));

        Tasks.runLater(() -> {
            ukon.setMaxHealth(ukon.getMaxHealth()-2);
            sakon.setMaxHealth(sakon.getMaxHealth()-2);
        }, 5*20*60);

        senpoCooldown = 10*60;

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        Player ukon = Role.findPlayer(NarutoRoles.UKON);

        if(ukon != null) {
            if(ukon.getLocation().distance(player.getLocation()) <= 15) {
                ukon.setHealth(0);
                ukon.sendMessage(CC.prefix("§cSakon est mort et vous a emporté avec lui..."));
            }
        }
    }

    @Override
    public void onSecond(int timer, Player player) {
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);

        if(ukon == null || sakon == null) return;

        if(ukon.getLocation().distance(sakon.getLocation()) <= 15) {
            sakon.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            sakon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 0, false, false));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

}

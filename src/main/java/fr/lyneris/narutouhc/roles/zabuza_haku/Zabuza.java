package fr.lyneris.narutouhc.roles.zabuza_haku;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.gui.NarutoGui;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Zabuza extends NarutoRole {

    private int camouflageCooldown = 0;
    private boolean invisible = false;

    @Override
    public void resetCooldowns() {
        camouflageCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(camouflageCooldown > 0) camouflageCooldown--;
    }

    @Override
    public String getRoleName() {
        return "Zabuza";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §bZabuza\n" +
                "§7▎ Objectif: §rSon but est de gagner avec §bHaku\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’une épée en diamant Tranchant 4 nommée “§rKubikiribôchô§7”, elle possède 10% de chance de régénérer celui-ci du même nombre de dégâts qu’il a infligé.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item nommé “§rCamouflage§7”, il lui permet d’être entièrement invisible même s’il porte une armure, lorsqu’il inflige ou reçoit un coup, il est à nouveau visible, cet item possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §bVitesse 1§7 et §cForce 1§7.\n" +
                "§e §f\n" +
                "§7• Lorsqu’un joueur utilise un item ou une commande, il est mit au courant que le joueur vient d’utiliser une technique avec le nom de celle-ci.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’identité de §bHaku§7, à la mort de celui-ci, il reçoit l’effet §9Résistance 1§7 de manière permanente ainsi que l’effet §bVitesse 2§7 pendant 10 minutes.\n" +
                "     \n" +
                "§7• Il possède la particularité de parler avec §bHaku§7 dans le chat, il lui suffit simplement d’écrire dans le chat avec l’aide du préfixe \"!\" pour pouvoir communiquer avec lui.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.ZABUZA;
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.HAKU);
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.specialItem("Kubikiribôchô")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Camouflage")).toItemStack());
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(Item.specialItem(player.getItemInHand(), Item.specialItem("Kubikiribôchô"))) {
            int random = (int) (Math.random() * 10);
            if(random == 2) {
                player.setHealth(player.getHealth() + event.getFinalDamage());
                player.sendMessage(prefix("&fVous avez régénéré les dégâts que vous avez infligez à &a" + event.getEntity().getName()));
            }
        }
    }

    @Override
    public void onAllPlayerPowerUse(Player player) {
        getPlayer().sendMessage(prefix("&a" + player.getName() + "&f a utilisé son pouvoir. Il est " + Role.getRole(player).getRoleName()));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event, "Camouflage")) {
            if(camouflageCooldown > 0) {
                player.sendMessage(Messages.cooldown(camouflageCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            camouflageCooldown = 5*60;
            invisible = true;


            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if(invisible) {
            invisible = false;
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(player));
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if(invisible) {
            invisible = false;
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(damager));
            damager.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(getPlayer() != null && Role.isRole(player, NarutoRoles.HAKU)) {
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            getPlayer().removePotionEffect(PotionEffectType.SPEED);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20*60, 1, false, false));
            getPlayer().sendMessage(prefix("&aHaku &fest morte. De ce fait vous obtenez &7Résistance &fpermanent ainsi que &bSpeed 2 &fpendant 10 minutes."));
        }
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event, Player player) {

        if(!event.getMessage().startsWith("!")) return;
        event.setCancelled(true);

        Player haku = Role.findPlayer(NarutoRoles.HAKU);
        if(haku == null) {
            player.sendMessage(prefix("&cHaku n'est pas dans la partie."));
            return;
        }

        String message = event.getMessage().substring(1);
        haku.sendMessage(prefix("&6&lZabuza&8: &7" + message));
        player.sendMessage(prefix("&6&lZabuza&8: &7" + message));
    }

    @Override
    public Camp getCamp() {
        return Camp.ZABUZA_HAKU;
    }
}

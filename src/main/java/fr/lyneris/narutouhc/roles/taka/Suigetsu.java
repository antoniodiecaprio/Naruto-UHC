package fr.lyneris.narutouhc.roles.taka;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Armor;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Suigetsu extends NarutoRole {

    public boolean orochimaruDied = false;

    public NarutoRoles getRole() {
        return NarutoRoles.SUIGETSU;
    }

    @Override
    public String getRoleName() {
        return "Suigetsu";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §6Suigetsu\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp de §6Taka\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un livre Depth Strider 3. \n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’effet §9Résistance 1§7 permanent.\n" +
                "§e §f\n" +
                "§7• Il dispose de 10% de chance qu’un coup ne lui mette pas de dégâts.\n" +
                "§e §f\n" +
                "§7• Dans l’eau, il dispose de l’effet §9Résistance 2§7, il peut respirer sous l’eau et s’il enlève son armure, il peut se mettre en invisible et une fois invisible, il ne peut recevoir de dégâts venant d’un joueur et il ne peut infliger de dégâts.\n" +
                "§e §f\n" +
                "§7• Son camp se retrouvera souvent allié avec un autre camp, c’est pour cela qu’au début de la partie, son camp fait partie du camp d’§5Orochimaru§7. Si §5Orochimaru§7 vient à mourir alors son camp sera sans allié, suite à ça, si §cItachi§7 vient à mourir, son camp s’alliera avec l’§cAkatsuki§7 et donc il connaîtra  l’identité de §cNagato§7. Si §dTobi§7 et §dMadara§7 rassemble les 9 biju pour former Jûbi, il s’alliera au camp §aShinobi§7.\n" +
                "§e §f\n" +
                "§7• Il connaît l’identité d’§5Orochimaru§7.\n" +
                "§e §f\n" +
                "§7• A la mort d’§5Orochimaru§7, il obtient l’identité de §6Sasuke§7.\n" +
                "§e §f\n" +
                "§7• A la mort de §bZabuza§7, il obtient l’épée de celui-ci nommée “§rKubikiribôchô§7”.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §9Suiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);
        player.getInventory().addItem(new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchantment(Enchantment.DEPTH_STRIDER, 3).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }


    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if (event.getTo().getBlock().isLiquid()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2*20, 1, false, false));
        }

        Tasks.runLater(() -> {
            Player realPlayer = Bukkit.getPlayer(player.getName());
            if(!Armor.hasArmor(realPlayer) && realPlayer.getLocation().getBlock().isLiquid()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2, false, false));
            } else {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }, 5);

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        Tasks.runLater(() -> {
            Player realPlayer = Bukkit.getPlayer(player.getName());
            if(!Armor.hasArmor(realPlayer) && realPlayer.getLocation().getBlock().isLiquid()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2, false, false));
            } else {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }, 5);
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) event.setCancelled(true);
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {

        if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) event.setCancelled(true);
        if(event.getCause() == EntityDamageEvent.DamageCause.DROWNING) event.setCancelled(true);


        int random = (int) (Math.random() * 10);
        if (random == 2) {
            event.setDamage(0.001);
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        if(Role.isRole(player, NarutoRoles.ZABUZA)) {
            //TODO RECUPERER LITEM DE MERDE “Kubikiribôchô”
        }

        if(Role.isRole(player, NarutoRoles.OROCHIMARU)) {
            Player suigetsu = Role.findPlayer(NarutoRoles.SUIGETSU);
            if(suigetsu == null) return;

            suigetsu.sendMessage(prefix("&cOrochimaru &fest mort. De ce fait vous désormais &6seul&f."));
            roleManager.setCamp(suigetsu.getUniqueId(), Camp.SOLO);
            orochimaruDied = true;
            Role.knowsRole(suigetsu, NarutoRoles.SASUKE);
        }

        if(Role.isRole(player, NarutoRoles.ITACHI) && orochimaruDied) {
            Player suigetsu = Role.findPlayer(NarutoRoles.SUIGETSU);
            if(suigetsu == null) return;

            Role.knowsRole(suigetsu, NarutoRoles.NAGATO);
            suigetsu.sendMessage(prefix("&cItachi &fest mort. Vous devez désormais gagner avec l'&cAkatsuki&f."));
            roleManager.setCamp(suigetsu.getUniqueId(), Camp.AKATSUKI);
            orochimaruDied = false;
        }

    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }
}

package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Blocked;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kisame extends NarutoRole {

    public int heartDamage = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.KISAME;
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (event.getEntity() instanceof Player) {
            heartDamage += event.getDamage();
        }

        if (heartDamage >= 50) {
            heartDamage = 0;
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40 * 20, 1, false, false));
            player.sendMessage(CC.prefix("§fVous avez infligé §c50 coeurs §fde dégâts, vous obtenez donc §eAbsorption§f, §dRégénération §fet §6Résistance au Feu§f."));
        }

    }

    @Override
    public String getRoleName() {
        return "Kisame";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §cKisame\n" +
                "§7▎ Objectif: §rSon but est de gagner avec l'§cAkatsuki\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un livre Agilité aquatique 3. \n" +
                "§e §f\n" +
                "§7• Il dispose d’une épée en diamant nommée \"§rSamehada§7\" enchantée Tranchant 4, elle permet de retirer les pouvoirs des joueurs ayant été touché par celle-ci, l'effet dure 1 minute et est utilisable par tous les joueurs de la partie. \n" +
                "§e §f\n" +
                "§7§l▎ Particularités : \n" +
                "§e §f\n" +
                "§7• Il dispose des effets §cForce 1§7 et §9Résistance 1§7.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il inflige §c50 cœurs§7 de dommages, il reçoit les effets §eAbsorption 2§7 pendant 5 secondes, §dRégénération 2§7 pendant 40 secondes et §6Résistance au feu 2§7 pendant 40 secondes, ce pouvoir se répète à chaque fois qu’il inflige les §c50 cœurs§7 de dommages. \n" +
                "      \n" +
                "§7• Il possède la capacité de respirer sous l’eau.\n" +
                "§e §f\n" +
                "§7• Il connaît l’identité d’§cItachi§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §9Suiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------\n";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.ITACHI);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 100, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).setName(Item.specialItem("Samehada")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchantment(Enchantment.DEPTH_STRIDER, 3).toItemStack());
    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if (Item.specialItem(damager.getItemInHand(), "Samehada")) {
            if (fr.lyneris.narutouhc.utils.Blocked.blocked.contains(event.getEntity().getName())) return;
            fr.lyneris.narutouhc.utils.Blocked.blocked.add(event.getEntity().getName());
            event.getEntity().sendMessage(CC.prefix("§cSamehada §fa utilisé son épée sur vous, vous ne pouvez plus utiliser vos pouvoirs pendant §c1 minute§f."));
            damager.sendMessage(prefix("&fVous avez utilisé votre épée sur &a" + event.getEntity().getName()));
            Tasks.runLater(() -> Blocked.blocked.remove(event.getEntity().getName()), 60 * 20);
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}

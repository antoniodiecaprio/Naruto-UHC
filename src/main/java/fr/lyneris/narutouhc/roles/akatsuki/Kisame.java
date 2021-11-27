package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
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

    public static List<String> blocked = new ArrayList<>();
    public int heartDamage = 0;

    public static boolean isBlocked(Player player) {
        return blocked.contains(player.getName());
    }

    public NarutoRoles getRole() {
        return NarutoRoles.KISAME;
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (event.getEntity() instanceof Player) {
            heartDamage += event.getFinalDamage();
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
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.ITACHI);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 100, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).setName(Item.specialItem("Samehada")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchantment(Enchantment.DEPTH_STRIDER, 3).toItemStack());
    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if (Item.specialItem(damager.getItemInHand(), "Samehada")) {
            if (blocked.contains(event.getEntity().getName())) return;
            blocked.add(event.getEntity().getName());
            event.getEntity().sendMessage(CC.prefix("§cSamehada §fa utilisé son épée sur vous, vous ne pouvez plus utiliser vos pouvoirs pendant §c1 minute§f."));
            Tasks.runLater(() -> blocked.remove(event.getEntity().getName()), 60 * 20);
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

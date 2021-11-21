package fr.lyneris.narutouhc.roles.taka;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Suigetsu extends NarutoRole {
    @Override
    public String getRoleName() {
        return "Suigetsu";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchantment(Enchantment.DEPTH_STRIDER, 3).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if(event.getTo().getBlock().getType() == Material.WATER || event.getTo().getBlock().getType() == Material.STATIONARY_WATER) {

        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        int random = (int) (Math.random() * 10);
        if(random == 2) {
            event.setDamage(0.001);
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.TAKA;
    }
}

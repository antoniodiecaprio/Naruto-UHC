package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Hidan extends NarutoRole {
    @Override
    public String getRoleName() {
        return "Hidan";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, "Kakuzu");
        player.getInventory().addItem(new ItemBuilder(Material.REDSTONE_BLOCK).setName(Item.specialItem(Item.specialItem("Redstone"))).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_HOE).setName(Item.specialItem(Item.specialItem("Houe"))).toItemStack());
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        Player kakuzu = Role.findPlayer("Kakuzu");
        if(kakuzu == null) return;
        if(kakuzu.getLocation().distance(player.getLocation()) <= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 0, false, false));
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }
}

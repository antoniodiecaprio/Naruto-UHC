package fr.lyneris.narutouhc.roles.solo;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Danzo extends NarutoRole {

    public static int lives = 1;
    public static int died = 0;
    public boolean usedIzanagi = false;

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(Item.specialItem(player.getItemInHand(), "Epee")) {
            int random = (int) (Math.random() * 20);
            if(random == 1) {
                player.sendMessage(prefix("&fVous avez mis &a25% &fde dégâts en plus."));
                event.setDamage(event.getFinalDamage()*1.25);
            }
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        lives++;
        killer.sendMessage(prefix("&fVous avez désormais &a" + lives + " &fvie(s)."));
    }

    @Override
    public String getRoleName() {
        return "Danzo";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.DANZO;
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.specialItem("Epée")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("izanagi")) {

            if (usedIzanagi) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aIzanagi§f."));

            player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).toItemStack());
            player.setHealth(player.getMaxHealth());
            player.setMaxHealth(player.getMaxHealth() - 2);
            usedIzanagi = true;
        }

    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    @Override
    public Camp getCamp() {
        return Camp.SOLO;
    }
}

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
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);
        player.getInventory().addItem(new ItemBuilder(Material.BOOK).addStoredEnchantment(Enchantment.DEPTH_STRIDER, 3).toItemStack());
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

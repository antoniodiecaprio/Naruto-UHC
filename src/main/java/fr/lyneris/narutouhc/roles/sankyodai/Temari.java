package fr.lyneris.narutouhc.roles.sankyodai;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Temari extends NarutoRole {

    private boolean usedKamatari = false;
    private int eventailCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.TEMARI;
    }

    @Override
    public void resetCooldowns() {
        eventailCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (eventailCooldown > 0) {
            eventailCooldown--;
        }

    }

    @Override
    public String getRoleName() {
        return "Temari";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kamatari")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).setName(Item.specialItem("Éventail")).toItemStack());
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if (Item.specialItem(event.getPlayer().getItemInHand(), "Éventail")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2 * 20, 0, false, false));
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.specialItem(event.getItem(), "Éventail")) {

            if (eventailCooldown > 0) {
                player.sendMessage(Messages.cooldown(eventailCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);
            for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                Location initialLocation = player.getLocation().clone();
                Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(initialLocation.toVector());
                fromPlayerToTarget.multiply(4); //6
                fromPlayerToTarget.setY(1); // 2
                entity.setVelocity(fromPlayerToTarget);
            }
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aÉventail§f."));

            eventailCooldown = 20 * 60;

        }

        if (Item.interactItem(event.getItem(), "Kamatari")) {

            if (usedKamatari) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);
            Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
            wolf.setTamed(true);
            wolf.setOwner(player);
            wolf.setSitting(false);
            wolf.setCollarColor(DyeColor.BLUE);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false));

            usedKamatari = true;

            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aKamatari§f."));
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SANKYODAI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }
}

package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.Damage;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Konohamaru extends NarutoRole {

    public boolean isUsingRasengan = false;
    public int rasenganCooldown = 0;

    @Override
    public void resetCooldowns() {
        rasenganCooldown = 0;
    }

    @Override
    public void startRunnableTask() {
        if(rasenganCooldown > 0) {
            rasenganCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Konohamaru";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!isUsingRasengan) return;


        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 5);
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 5);

        Bukkit.getScheduler().runTaskLater(NarutoUHC.getNaruto(), () -> player.getWorld().createExplosion(event.getEntity().getLocation(), 3.0f), 3);

        isUsingRasengan = false;

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(player.getItemInHand(), "Rasengan")) {
            if (rasenganCooldown > 0) {
                player.sendMessage(Messages.cooldown(rasenganCooldown));
                return;
            }
            player.sendMessage("§7▎ §fVous avez utilisé votre item §aRasengan§f.");

            rasenganCooldown = 5 * 60;
            isUsingRasengan = true;

        }
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Rasengan")).toItemStack());
    }   

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }
}

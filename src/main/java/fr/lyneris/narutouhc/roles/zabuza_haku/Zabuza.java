package fr.lyneris.narutouhc.roles.zabuza_haku;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
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
    public List<String> getDescription() {
        return new ArrayList<>();
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
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event, "Camouflage")) {
            if(camouflageCooldown > 0) {
                player.sendMessage(Messages.cooldown(camouflageCooldown));
                return;
            }

            camouflageCooldown = 5*60;
            invisible = true;
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.hidePlayer(player));
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

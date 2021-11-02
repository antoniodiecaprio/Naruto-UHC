package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Hinata extends NarutoRole {

    public int byakuganCooldown = 0;
    public int hakkeCooldown = 0;

    @Override
    public void resetCooldowns() {
        byakuganCooldown = 0;
        hakkeCooldown = 0;
    }

    @Override
    public void startRunnableTask() {
        if(byakuganCooldown > 0) {
            byakuganCooldown--;
        }

        if(hakkeCooldown > 0) {
            hakkeCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Hinata";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, "Naruto");
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Byakugan")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Hakke")).toItemStack());

    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        Player hinata = Role.findPlayer("Hinata");

        if(hinata == null) return;

        if(Role.isRole(player, "Neji")) {
            hinata.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            hinata.sendMessage("§7▎ §cNeji §fest mort. Vous obtenez donc §cForce 1 §fpermanent.");
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.interactItem(event.getItem(), "Byakugan")) {
            if(byakuganCooldown > 0) {
                player.sendMessage(Messages.cooldown(byakuganCooldown));
                return;
            }


            player.sendMessage("§7▎ §fVous avez utilisé votre item §aByakugan");

            player.getNearbyEntities(60, 60, 60).stream().filter(e -> e instanceof Player).map(e -> (Player)e).forEach(target -> {
                String position = Loc.getCardinalDirection(target);
                int distance = (int) target.getLocation().distance(player.getLocation());

                player.sendMessage("§7▎ §9" + target.getName() + " §f§l» §fPosition: §e" + position + "§f, Distance: §e" + distance);

            });

            byakuganCooldown = 20*60;

        }

        if(Item.interactItem(event.getItem(), "Hakke")) {

            if(hakkeCooldown > 0) {
                player.sendMessage(Messages.cooldown(byakuganCooldown));
                return;
            }

            player.sendMessage("§7▎ §fVous avez utilisé votre item §aHakke");

            player.getNearbyEntities(10, 10, 10).stream().filter(e -> e instanceof Player).map(e -> (Player)e).forEach(target -> {
                target.damage(0.1);
                target.setHealth(target.getHealth()-6);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 0, false, false));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3*20, 0, false, false));
                target.sendMessage("§7▎ §aHinata §fa utilisé son pouvoir sur vous, vous venez de perdre §c3 coeurs §fet reçu §8Blindness §fet §7Slowness §fpour 4 secondes.");
            });

            hakkeCooldown = 20*60;

        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {

        Player naruto = Role.findPlayer("Naruto");

        if(naruto == null) return;

        if(naruto.getLocation().distance(player.getLocation()) <= 15) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 0, false, false));
        }

    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {

        Player naruto = Role.findPlayer("Naruto");

        if(naruto == null) return;

        if(naruto.getLocation().distance(player.getLocation()) <= 15) {
            event.setDamage(event.getFinalDamage()*1.1);
        }

    }

    @Override
    public void onAllPlayerPowerUse(Player player) {
        Player hinata = Role.findPlayer("Hinata");
        if(hinata == null) return;
        if(player.getLocation().distance(hinata.getLocation()) <= 20) {
            hinata.sendMessage("§7▎ §a" + roleManager.getRole(player).getRoleName() + " §fvient d'utiliser son pouvoir autour de vous.");
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }
}

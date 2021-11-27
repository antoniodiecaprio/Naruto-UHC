package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Jirobo extends NarutoRole {

    public boolean hunger = false;
    public int marqueCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.JIROBO;
    }

    @Override
    public void resetCooldowns() {
        marqueCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (marqueCooldown > 0) {
            marqueCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Jirobo";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marque Maudite")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event, "Marque Maudite")) {

            if (marqueCooldown > 0) {
                player.sendMessage(Messages.cooldown(marqueCooldown));
                return;
            }

            hunger = true;
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(CC.prefix("Vous avez utilisé votre pouvoir &aMarque Mandite&f."));
            Tasks.runLater(() -> {
                player.removePotionEffect(PotionEffectType.HUNGER);
                hunger = false;
                player.sendMessage(CC.prefix("Vous avez perdu votre effet de &aHunger&f ainsi que &c2 coeurs &fpendant 15 minutes."));
                player.setMaxHealth(player.getMaxHealth() - 4);
            }, 5 * 20 * 60);
            Tasks.runLater(() -> {
                player.sendMessage(CC.prefix("Vous avez récupéré vos &a2 coeurs&f."));
                player.setMaxHealth(player.getMaxHealth() + 4);
            }, 20 * 20 * 60);

            marqueCooldown = 30 * 60;
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if (killer.hasPotionEffect(PotionEffectType.HUNGER)) {
            if (hunger) {
                killer.removePotionEffect(PotionEffectType.HUNGER);
                killer.setFoodLevel(20);
                killer.sendMessage(CC.prefix("Vous avez récupéré toute votre barre de nourriture ainsi que votre effet de &aHunger&f pendant 1 minute."));
                Tasks.runLater(() -> {
                    Player newPlayer = Bukkit.getPlayer(killer.getUniqueId());
                    if (hunger) {
                        newPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 60 * 20, 0, false, false));
                    }
                }, 20 * 60);
            }
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }
}


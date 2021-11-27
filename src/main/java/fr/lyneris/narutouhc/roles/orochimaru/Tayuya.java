package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Tayuya extends NarutoRole {

    private int nbGolemSpawn = 3;
    private int nbEndermiteSpawn = 5;
    private int marqueCooldown = 0;
    private int fluteCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.TAYUYA;
    }

    @Override
    public void resetCooldowns() {
        marqueCooldown = 0;
        fluteCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (marqueCooldown > 0) {
            marqueCooldown--;
        }
        if (fluteCooldown > 0) {
            fluteCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Tayuya";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marque Maudite")).toItemStack());
        player.setWalkSpeed(0.22F);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event, "Marque Maudite")) {

            if (marqueCooldown > 0) {
                player.sendMessage(Messages.cooldown(marqueCooldown));
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20 * 60, 0, false, false));
            player.sendMessage(CC.prefix("Vous avez utilisé votre pouvoir &aMarque Maudite&f."));
            Tasks.runLater(() -> {
                player.setMaxHealth(player.getMaxHealth() - 2);
                player.sendMessage(CC.prefix("Vous avez perdu &c1 coeur &fpermanent."));
            }, 5 * 20 * 60);

            marqueCooldown = 10 * 60;

        }

        if (Item.interactItem(event, "Flûte démoniaque")) {

            if (fluteCooldown > 0) {
                player.sendMessage(Messages.cooldown(fluteCooldown));
                return;
            }

            Player nearestPlayer = null;
            double nearestDistance = Integer.MAX_VALUE;
            for (Player players : Role.getAliveOnlinePlayers()) {
                if (player.getLocation().distance(players.getLocation()) < nearestDistance) {
                    nearestPlayer = players;
                    nearestDistance = player.getLocation().distance(players.getLocation());
                }
            }
            for (int i = 0; i < nbGolemSpawn; i++) {
                IronGolem golem = player.getLocation().getWorld().spawn(player.getLocation(), IronGolem.class);
                golem.setCustomName("§eServant de Tayuya");
                golem.setPlayerCreated(false);
                if (nearestPlayer != null)
                    golem.setTarget(nearestPlayer);
            }
            for (int i = 0; i < nbEndermiteSpawn; i++) {
                Endermite endermite = player.getLocation().getWorld().spawn(player.getLocation(), Endermite.class);
                endermite.setCustomName("§eServant de Tayuya");
                if (nearestPlayer != null)
                    endermite.setTarget(nearestPlayer);

            }

            fluteCooldown = 30 * 60;

            Tasks.runAsyncLater(() -> {
                nbGolemSpawn = 3;
                nbEndermiteSpawn = 3;
            }, 20 * 20 * 60);
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }
}

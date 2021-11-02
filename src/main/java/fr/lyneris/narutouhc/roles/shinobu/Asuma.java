package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Asuma extends NarutoRole {

    public int nueesCooldown = 0;
    public int avancement = 0;
    public Player latest = null;

    @Override
    public void resetCooldowns() {
        nueesCooldown = 0;
    }

    @Override
    public void startRunnableTask() {
        if(nueesCooldown > 0) {
            nueesCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Asuma";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).setName("§7▎ §6Epée").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Nuées Ardentes")).toItemStack());
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {

        if(avancement >= 2500) {
            Player target = latest;
            if(target != null) {
                player.sendMessage("§7▎ §fProgression §aterminée§f. Voici l’identité du joueur auquel la progression est terminée : §a§l" + latest.getName());
                latest = null;
            }
        } else {
            Player ino = Role.findPlayer("Ino");
            if(ino != null) {
                int distance = (int) ino.getLocation().distance(player.getLocation());
                if(distance <= 5) {
                    avancement += 10;
                    latest = ino;
                } else if(distance <= 10) {
                    avancement += 5;
                    latest = ino;
                } else if(distance <= 20) {
                    avancement += 1;
                    latest = ino;
                }
            }

            Player shikamaru = Role.findPlayer("Shikamaru");
            if(shikamaru != null) {
                int distance = (int) shikamaru.getLocation().distance(player.getLocation());
                if(distance <= 5) {
                    avancement += 10;
                    latest = shikamaru;
                } else if(distance <= 10) {
                    avancement += 5;
                    latest = shikamaru;
                } else if(distance <= 20) {
                    avancement += 1;
                    latest = shikamaru;
                }
            }

            Player choji = Role.findPlayer("Choji");
            if(choji != null) {
                int distance = (int) choji.getLocation().distance(player.getLocation());
                if(distance <= 5) {
                    avancement += 10;
                    latest = choji;
                } else if(distance <= 10) {
                    avancement += 5;
                    latest = choji;
                } else if(distance <= 20) {
                    avancement += 1;
                    latest = choji;
                }
            }
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event.getItem(), "Nuées Ardentes")) {

            if(nueesCooldown > 0) {
                player.sendMessage(Messages.cooldown(nueesCooldown));
                return;
            }

            final Location front;
            front = player.getLocation();
            Bukkit.getScheduler().runTaskLater(narutoUHC, () -> front.getWorld().createExplosion(front, 3.0f), 3*20);

            player.sendMessage("§7▎ §fUne explosion va retentir dans §a3 secondes§f.");

            nueesCooldown = 5*60;

        }
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

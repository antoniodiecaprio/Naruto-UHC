package fr.lyneris.narutouhc.roles.shinobu;

import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;
import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hiruzen extends NarutoRole {

    public int enmaCooldown = 0;
    public int avancement = 0;
    public UUID avancementTarget = null;
    public boolean usedShiki = false;

    @Override
    public void resetCooldowns() {
        enmaCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (enmaCooldown > 0) enmaCooldown--;
    }

    public NarutoRoles getRole() {
        return NarutoRoles.HIRUZEN;
    }

    @Override
    public String getRoleName() {
        return "Hiruzen";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Enma")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onSecond(int timer, Player player) {
        player.removePotionEffect(PotionEffectType.POISON);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.WITHER);

        if (avancementTarget != null) {

            Player target = Bukkit.getPlayer(avancementTarget);
            if (target == null) return;

            if (avancement >= 120) {
                target.setHealth(0);
                target.sendMessage(prefix("&cHiruzen &fa utilisé son &aShiki Fûjin &fsur vous et est resté plus de 2 minutes à côté de vous."));
                target.sendMessage(prefix("&fDe ce fait, vous êtes &cmort&f."));
                player.sendMessage(prefix("&fVous êtes resté plus de &a2 minutes &fà côté de &c" + target.getName()));
                player.sendMessage(prefix("&fDe ce fait, il est &cmort&f mais vous perdez &c5 coeurs &fpermanent."));
                player.setMaxHealth(player.getMaxHealth() - 10);

                avancement = 0;
                avancementTarget = null;
            }

            if (player.getLocation().distance(target.getLocation()) <= 15) {
                avancement++;
            }
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(this.avancementTarget != null && player.getUniqueId().equals(avancementTarget)) {
            Player hiruzen = Role.findPlayer(NarutoRoles.HIRUZEN);
            if(hiruzen != null) {
                if(!Role.isAlive(hiruzen)) return;
                hiruzen.sendMessage(prefix("&cVotre cible est mort, et vous emporte avec lui."));
                hiruzen.setHealth(0);
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        if(avancementTarget != null) {
            if(!Role.isAlive(avancementTarget)) return;
            Player avancementTarget = Bukkit.getPlayer(this.avancementTarget);
            if(avancementTarget == null) return;
            avancementTarget.sendMessage(prefix("&aHiruzen &fest mort alors qu'il utilisait son &cShiki Fûjin &fsur vous."));
            int random = (int) (Math.random() * 3);
            if(random == 0) {
                //TODO DISABLE POWERS AVANCEMENTTARGET FOR 20 MINUTES
                avancementTarget.sendMessage(prefix("&cVous ne pouvez plus utiliser de pouvoirs pendant 20 minutes."));
            } else if(random == 1) {
                avancementTarget.sendMessage(prefix("&cVous obtenez Slowness pour toute la game."));
                avancementTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
            } else {
                avancementTarget.sendMessage(prefix("&cVous obtenez blindness 5 secondes toutes les minutes."));
                Tasks.runTimer(() -> {
                    Player real = Bukkit.getPlayer(this.avancementTarget);
                    if(real != null) real.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 0, false, false));
                }, 0, 60*20);
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event, "Enma")) {

            if (enmaCooldown > 0) {
                player.sendMessage(Messages.cooldown(enmaCooldown));
                return;
            }

            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20 * 60, 3, false, false));
            Tasks.runLater(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false)), 5 * 20 * 60 + 1);
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.specialItem("Kongounyai")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
            Reach.addReachPlayerTemp(player.getUniqueId(), 5 * 60);
        }

        if (Item.interactItem(event, "Shiki Fûjin")) {
            if (usedShiki) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            int i = 9;
            int nearbyPlayer = 0;
            for (Player ignored : Loc.getNearbyPlayers(player, 20)) {
                nearbyPlayer++;
            }
            if (nearbyPlayer > 7 && nearbyPlayer <= 16) {
                i = 18;
            } else if (nearbyPlayer > 16) {
                i = 27;
            }
            Inventory inv = Bukkit.createInventory(null, i, "Shiki Fûjin");
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            int j = 1;
            for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                j++;
            }
            player.openInventory(inv);
        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equalsIgnoreCase("Shiki Fûjin")) {
            event.setCancelled(true);
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            avancement = 0;
            avancementTarget = target.getUniqueId();
            usedShiki = true;

        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.ALL;
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }
}

package fr.lyneris.narutouhc.roles.taka;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Sasuke extends NarutoRole {

    private boolean usingSusano = false;
    private int susanoCooldown = 0;
    private int rinneganCooldown = 0;
    private int bowCooldown = 0;
    private int amaterasuCooldown = 0;
    private boolean usedIzanagi = false;

    @Override
    public void resetCooldowns() {
        susanoCooldown = 0;
        bowCooldown = 0;
        rinneganCooldown = 0;
        amaterasuCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (susanoCooldown > 0) susanoCooldown--;
        if (bowCooldown > 0) bowCooldown--;
        if (rinneganCooldown > 0) rinneganCooldown--;
        if (amaterasuCooldown > 0) amaterasuCooldown--;
    }

    public NarutoRoles getRole() {
        return NarutoRoles.SASUKE;
    }

    @Override
    public String getRoleName() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 10);
        player.getInventory().addItem(Item.getInteractItem("Susano"));
        player.getInventory().addItem(Item.getInteractItem("Rinnegan"));
        player.getInventory().addItem(Item.getInteractItem("Amaterasu"));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event, "Amaterasu")) {
            if(this.amaterasuCooldown > 0) {
                Messages.getCooldown(amaterasuCooldown).queue(player);
                return;
            }

            for (Player players : Loc.getNearbyPlayers(player, 50)) {
                if (Reach.getLookingAt(player, players) && Role.isAlive(players)) {
                    UUID uuid = players.getUniqueId();
                    players.sendMessage(prefix("&cSasuke &fa utilisé son pouvoir sur vous. De ce fait vous serez enflammé pendant &c1&f minute."));
                    player.sendMessage(prefix("&fVous avez utilisé votre &aAmaterasu &fsur &c" + players.getName()));
                    player.setMaxHealth(player.getMaxHealth() - 2);
                    new BukkitRunnable() {
                        int timer = 60;
                        @Override
                        public void run() {
                            Player players = Bukkit.getPlayer(uuid);
                            if(timer <= 0) {
                                players.setFireTicks(0);
                                cancel();
                            }
                            timer--;
                            players.setFireTicks(40);
                        }
                    }.runTaskTimer(narutoUHC, 0, 20);
                    this.amaterasuCooldown = 15*60;
                    break;
                }
            }
        }

        if (Item.interactItem(event, "Rinnegan")) {
            if (this.rinneganCooldown > 0) {
                player.sendMessage(Messages.cooldown(rinneganCooldown));
                return;
            }
            for (Player players : Loc.getNearbyPlayers(player, 50)) {
                if (Reach.getLookingAt(player, players) && Role.isAlive(players)) {
                    Location loc1 = player.getLocation();
                    Location loc2 = players.getLocation();
                    player.teleport(loc2);
                    players.teleport(loc1);
                    players.sendMessage(prefix("§cSasuke &fvient d'échanger sa position avec vous."));
                    player.sendMessage(prefix("&fVous avez utilisé votre &aRinnegan"));
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2 * 60, 1, false, false));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                                player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                        }
                    }.runTaskLater(narutoUHC, 20 * 2 * 60);

                    this.rinneganCooldown = 5 * 60;
                    break;
                }
            }
        }

        if (Item.interactItem(event, "Susano")) {
            if (this.susanoCooldown > 0) {
                player.sendMessage(Messages.cooldown(susanoCooldown));
                return;
            }

            player.sendMessage(prefix("&fVous avez utilisé votre &aSusano&f."));
            player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 7).setName(Item.specialItem("Arc")).toItemStack());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20 * 60, 0, false, false));
            usingSusano = true;
            Tasks.runLater(() -> {
                usingSusano = false;
                ItemStack is = Arrays.stream(getPlayer().getInventory().getContents()).filter(itemStack -> Item.specialItem(itemStack, "Arc")).findFirst().orElse(null);
                player.getInventory().removeItem(is);
            }, 5 * 20 * 60);
            susanoCooldown = 20 * 60;
        }
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
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {
        if (Item.specialItem(shooter.getItemInHand(), "Arc")) {
            if (!usingSusano) {
                shooter.getInventory().removeItem(shooter.getItemInHand());
                return;
            }
            if (bowCooldown > 0) {
                shooter.sendMessage(Messages.cooldown(bowCooldown));
                return;
            }

            bowCooldown = 20;
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if (Role.isRole(event.getEntity(), NarutoRoles.ITACHI)) {
            killer.setMaxHealth(killer.getMaxHealth() + 6);
            killer.sendMessage(prefix("Vous avez tué &cItachi&f. De ce fait vous obtenez &c3 coeurs&f permanent ainsi "));
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if (Role.isRole(player, NarutoRoles.OROCHIMARU)) {
            Player sasuke = getPlayer();
            if (sasuke == null) return;
            sasuke.sendMessage(CC.prefix("&aOrochimaru &fest mort..."));
            Role.knowsRole(sasuke, NarutoRoles.KARIN);
            Role.knowsRole(sasuke, NarutoRoles.SUIGETSU);
            Role.knowsRole(sasuke, NarutoRoles.JUGO);
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }
}

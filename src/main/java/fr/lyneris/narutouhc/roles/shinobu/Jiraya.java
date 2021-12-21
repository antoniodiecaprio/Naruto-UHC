package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Jiraya extends NarutoRole {

    int senjutsuCooldown = 0;
    int dasshuCooldown = 0;
    int isanCooldown = 0;
    int kisuoiriCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.JIRAYA;
    }

    @Override
    public void resetCooldowns() {
        senjutsuCooldown = 0;
        dasshuCooldown = 0;
        isanCooldown = 0;
        kisuoiriCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (senjutsuCooldown > 0) {
            senjutsuCooldown--;
        }
        if (dasshuCooldown > 0) {
            dasshuCooldown--;
        }
        if (isanCooldown > 0) {
            isanCooldown--;
        }
        if (kisuoiriCooldown > 0) {
            kisuoiriCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Jiraya";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senjutsu")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Gamabunta")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Senjutsu")) {

            if (senjutsuCooldown > 0) {
                player.sendMessage(Messages.cooldown(senjutsuCooldown));
                return;
            }

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 1, false, false));
            Tasks.runLater(() -> {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            }, 5 * 20 * 60);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));

            Player naruto = Role.findPlayer(NarutoRoles.NARUTO);

            if (naruto != null) {
                int x = player.getLocation().getBlockX();
                int y = player.getLocation().getBlockY();
                int z = player.getLocation().getBlockZ();
                naruto.sendMessage(CC.prefix("§aJiraya §fa utilisé son §aSenjutsu§f. Voici ses coordonnées:"));
                naruto.sendMessage(" §f§l» §a" + x + "§f, §a" + y + "§f, §a" + z);
            }

            player.sendMessage(CC.prefix("§fVous avez utilisé votre §aSenjutsu§f. Attention, vos §ccoordonnées§f ont été envoyées à §aNaruto§f."));

            senjutsuCooldown = 30 * 60;
        }

        if(Item.interactItem(event, "Gamabunta")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Gamabunta");
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Dasshu").setLore(
                    "§7Ce pouvoir permet à Jiraya de faire un",
                    "§7grand saut dans la direction qu’il regarde,",
                    "§7il saute d’environ 30 blocs, celui-ci",
                    "§7possède un délai de 5 minutes."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Isan").setLore(
                    "§7Ce pouvoir permet à Jiraya de téléporter",
                    "§75 joueurs proches de lui, dans un rayon de",
                    "§730 blocs, dans le suc gastrique de Gamabunta",
                    "§7dans cette zone les joueurs ne peuvent",
                    "§7utilisés leurs pouvoirs, seuls les effets",
                    "§7restent actifs et perdent 1 cœur non",
                    "§7permanent toutes les 30 secondes (hormis",
                    "§7Jiraya), ce pouvoir dure 3 minutes et",
                    "§7possède un délai de 20 minutes."
            ).toItemStack());
        }

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if(event.getInventory().getName().equalsIgnoreCase("Gamabunta")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                if(dasshuCooldown > 0) {
                    player.sendMessage(Messages.cooldown(dasshuCooldown));
                    return;
                }

                player.sendMessage(prefix("Vous avez utilisé votre &aDasshu&f."));
                player.setVelocity(player.getLocation().getDirection().multiply(10));
                dasshuCooldown = 5*60;
            }

            if (event.getSlot() == 2) {
                if(isanCooldown > 0) {
                    player.sendMessage(Messages.cooldown(isanCooldown));
                    return;
                }

                List<Player> players = new ArrayList<>();
                int i = 0;
                for (Player player1 : Loc.getNearbyPlayers(player, 30)) {
                    if (i < 5) {
                        players.add(player1);
                        i++;
                    }
                }
                player.sendMessage(prefix("Vous avez utilisé votre &a&f."));
                //TODO TELEPORTER DANS LA MAP QUI EST PAS ARRIVEE

                isanCooldown = 20*60;

            }

            if(event.getSlot() == 3) {
                if(this.kisuoiriCooldown > 0) {
                    Messages.getCooldown(kisuoiriCooldown).queue(player);
                    return;
                }
                List<Entity> burning = new ArrayList<>();
                new BukkitRunnable() {
                    int timer = 40;

                    @Override
                    public void run() {
                        if (timer == 0) {
                            burning.clear();
                            cancel();
                            return;
                        }
                        char c = Loc.getCharCardinalDirection(player);
                        Cuboid cuboid;
                        if (c == 'W') {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() + 10);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 0.5, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() + 1);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ());
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        } else if (c == 'E') {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() - 10);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 0.5, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() - 1);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX() + (i + 0.1), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ());
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        } else if (c == 'N') {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() - 10, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() + 0.5);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 1, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() - 0.5);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ() + (i + 0.1));
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ() + (i + 0.1));
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        } else {
                            Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 10, player.getLocation().getBlockY() + 1.5, player.getLocation().getBlockZ() + 0.5);
                            Location two = new Location(player.getWorld(), player.getLocation().getBlockX() + 1, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() - 0.5);
                            cuboid = new Cuboid(one, two);
                            cuboid.getBlocks().forEach(block -> {
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ() + (i + 0.1));
                                    player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                }
                                for (double i = 0.0; i < 1; i += 0.1) {
                                    for (double j = 0.2; j < 1.2; j += 0.2) {
                                        Location three = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + j, block.getLocation().getBlockZ() + (i + 0.1));
                                        player.getWorld().spigot().playEffect(three, Effect.FLAME, 0, 1, 23, 23, 23, 0, 0, 64);
                                    }
                                }
                            });
                        }
                        player.getNearbyEntities(20, 20, 20).forEach(entity -> {
                            for (Block block : cuboid.getBlocks()) {
                                if (block.getLocation().distance(entity.getLocation()) <= 3 && entity.getUniqueId() != player.getUniqueId() ) {
                                    entity.setFireTicks(20);
                                    burning.add(entity);
                                    break;
                                }
                            }
                        });
                        timer--;
                        burning.forEach(entity -> entity.setFireTicks(20));
                    }
                }.runTaskTimer(narutoUHC, 0, 5);

                kisuoiriCooldown = 10*60;
            }

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

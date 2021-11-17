package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.DoubleCircleEffect;
import fr.lyneris.narutouhc.particle.ProgressBar;
import fr.lyneris.narutouhc.particle.WorldUtils;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Deidara extends NarutoRole {

    private BowState state;
    private int c1Cooldown = 0;
    private int c2Cooldown = 0;
    private int c3Cooldown = 0;

    @Override
    public void resetCooldowns() {
        c1Cooldown = 0;
        c2Cooldown = 0;
        c3Cooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(c1Cooldown > 0) {
            c1Cooldown--;
        }

        if(c2Cooldown > 0) {
            c2Cooldown--;
        }

        if(c3Cooldown > 0) {
            c3Cooldown--;
        }
    }

    List<Arrow> arrows = new ArrayList<>();

    private enum BowState {
        C1, C3
    }

    @Override
    public String getRoleName() {
        return "Deidara";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
        Role.knowsRole(player, NarutoRoles.SASORI);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Bakuton")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).setName(Item.specialItem("Deidara")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event.getItem(), "Bakuton")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Bakuton");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(7).toItemStack());
            inv.setItem(1, new ItemBuilder(Material.ARROW).setName("§6C1" + (state == BowState.C1 ? " §f(§aSéléctionné§f)" : "")).setLore(
                    "§7Lorsqu’il l’utilise, son arc effectue",
                    "§7des explosions moyennes environ de la",
                    "§7puissance de 2 à 3 dynamites (tnt), il",
                    "§7possède un délai de 15 secondes entre",
                    "§7chaque utilisation."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.BONE).setName("§6C2").setLore(
                    "§7Lorsqu’il l’utilise, il dispose d’une",
                    "§7liste de tous les joueurs autour de lui",
                    "§7dans un rayon de 20 blocs, après avoir",
                    "§7cliqué sur l’un des joueurs, un chien",
                    "§7apparaît et fonce sur le joueur ciblé, une",
                    "§7fois la cible atteinte, le chien explose,",
                    "§7ce pouvoir possède un délai de 5 minutes."
            ).toItemStack());
            inv.setItem(3, new ItemBuilder(Material.SULPHUR).setName("§6C3" + (state == BowState.C3 ? " §f(§aSéléctionné§f)" : "")).setLore(
                    "§7Lorsqu’il l’utilise, son arc vient à",
                    "§7changer, à l'emplacement où atterrit sa",
                    "§7flèche, une pluie de dynamites (tnt) a",
                    "§7lieu (environ une vingtaine de dynamites),",
                    "§7il possède un délai de 5 minutes entre",
                    "§7chaque utilisation."
            ).toItemStack());
            inv.setItem(4, new ItemBuilder(Material.TNT).setName("§6L'Art Ultime").setLore(
                    "§7Lorsqu’il l’utilise, pendant 10 secondes",
                    "§7des particules noires apparaîtront sur lui,",
                    "§7un bruit a lieu, suite à ses 10 secondes,",
                    "§7une énorme explosion a lieu à sa position,",
                    "§7aussi puissante que la météorite de Madara,",
                    "§7cependant il ne survivra pas à l’explosion,",
                    "§7s’il vient à mourir pendant ces 10 secondes,",
                    "§7l’explosion s’annule."
            ).toItemStack());

            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if(event.getInventory().getName().equals("Bakuton")) {
            event.setCancelled(true);
            if(event.getSlot() == 1) {
                state = BowState.C1;
                player.sendMessage(CC.prefix("§fVotre arc est désormais sous l'effet §cC1§f."));
                player.closeInventory();
            }

            if(event.getSlot() == 2) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    nearbyPlayer++;
                }
                if(nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if(nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "C2");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if(event.getSlot() == 3) {
                state = BowState.C3;
                player.sendMessage(CC.prefix("§fVotre arc est désormais sous l'effet §cC3§f."));
                player.closeInventory();
            }

            if(event.getSlot() == 4) {
                player.sendMessage(CC.prefix("§fVous venez de déclancher votre §aArt Ultime§f."));

                new DoubleCircleEffect(20*10, EnumParticle.SMOKE_NORMAL).start(player);


                player.closeInventory();
                player.setItemInHand(null);
                for(Player players : Bukkit.getOnlinePlayers()) {
                    if(players.getWorld() == player.getWorld()) {
                        if(players.getLocation().distance(player.getLocation()) <= 100) {
                            players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
                        }
                    }
                }

                //TODO IMMOBILISER joueur 10s

                new BukkitRunnable() {

                    int timer = 10*20;

                    @Override
                    public void run() {
                        Title.sendActionBar(player, "§7Explosion §f§l» " + ProgressBar.getProgressBar(timer, 10*20, 120, '▎', ChatColor.GREEN, ChatColor.WHITE));

                        if(timer == 0) {
                            if(Bukkit.getPlayer(player.getName()) != null) {
                                if(UHC.getUHC().getGameManager().getPlayers().contains(player.getUniqueId())) {
                                    player.damage(100D);
                                    if(player.getWorld() != Bukkit.getWorld("kamui")) {
                                        WorldUtils.createBeautyExplosion(player.getLocation(), 30, false);
                                    }
                                    for(Player players : Bukkit.getOnlinePlayers()) {
                                        if(players.getWorld() == player.getWorld()) {
                                            if(players.getGameMode() != GameMode.SPECTATOR && UHC.getUHC().getGameManager().getPlayers().contains(players.getUniqueId())) {
                                                if(players.getLocation().distance(player.getLocation()) <= 30) {
                                                    players.damage(100D);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(narutoUHC, 0, 1);
            }

        }

        if(event.getInventory().getName().equals("C2")) {
            event.setCancelled(true);
            if(event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            if(!event.getCurrentItem().hasItemMeta()) return;

            if(c2Cooldown > 0) {
                player.sendMessage(Messages.cooldown(c2Cooldown));
                return;
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if(target == null) {
                player.sendMessage(Messages.offline(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "")));
                return;
            }

            player.closeInventory();

            Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
            wolf.setTamed(true);
            wolf.setOwner(player);
            wolf.setSitting(false);
            wolf.setAdult();
            wolf.setAngry(true);
            wolf.setTarget(target);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));

            c2Cooldown = 15;

            new BukkitRunnable() {
                int timer = 15*2;
                @Override
                public void run() {
                    try {
                        if(wolf.getLocation().distance(target.getLocation()) <= 1) {
                            wolf.getWorld().createExplosion(wolf.getLocation(), 3.0f);
                            cancel();
                        }
                    } catch (Exception e) {
                        cancel();
                    }
                    timer--;

                    if(timer <= 0) {
                        cancel();
                        wolf.setHealth(0);
                    }

                }
            }.runTaskTimer(narutoUHC, 0, 10);

        }

    }

    @Override
    public void onProjectileHitEvent(ProjectileHitEvent event, Player shooter) {
        if(!(event.getEntity() instanceof Arrow)) return;
        if(!arrows.contains((Arrow) event.getEntity())) return;

        if(state == BowState.C1) {
            World world = event.getEntity().getWorld();
            Location explosion = event.getEntity().getLocation();
            world.createExplosion(explosion, 3.0f);
        }

        if(state == BowState.C3) {
            World world = event.getEntity().getWorld();
            Location explosion = event.getEntity().getLocation();
            for (int x = -20; x <= 20; x += 5) {
                for (int z = -20; z <= 20; z += 5) {
                    world.spawn(new Location(world, explosion.getBlockX() + x, world.getHighestBlockYAt(explosion) + 64, explosion.getBlockZ() + z), TNTPrimed.class);
                }
            }
            this.arrows.remove((Arrow) event.getEntity());
        }

    }

    @Override
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event, Player shooter) {
        if(!(event.getEntity() instanceof Arrow)) return;
        if(!Item.specialItem(shooter.getItemInHand(), "Deidara")) return;

        if(state == BowState.C1) {
            if(c1Cooldown > 0) {
                event.setCancelled(true);
                shooter.getInventory().addItem(new ItemStack(Material.ARROW));
                shooter.sendMessage(Messages.cooldown(c1Cooldown));
                return;
            }
            arrows.add((Arrow) event.getEntity());

            c1Cooldown = 15;
        }

        if(state == BowState.C3) {
            if(c3Cooldown > 0) {
                event.setCancelled(true);
                shooter.getInventory().addItem(new ItemStack(Material.ARROW));
                shooter.sendMessage(Messages.cooldown(c3Cooldown));
                return;
            }

            arrows.add((Arrow) event.getEntity());

            c3Cooldown = 5*60;
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }
}

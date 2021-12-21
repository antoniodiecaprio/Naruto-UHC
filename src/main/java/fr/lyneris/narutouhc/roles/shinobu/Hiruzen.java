package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Hiruzen extends NarutoRole implements Listener {

    public int enmaCooldown = 0;
    public int avancement = 0;
    public UUID avancementTarget = null;
    public boolean usedShiki = false;
    private int parcheminCooldown = 0;
    private int power = 0;
    private int karyuuCooldown = 0;
    private int doryuuCooldown = 0;
    private int deihekiCooldown = 0;
    private int kazegafukiCooldown = 0;
    public boolean using = false;

    @Override
    public void resetCooldowns() {
        enmaCooldown = 0;
        parcheminCooldown = 0;
        karyuuCooldown = 0;
        doryuuCooldown = 0;
        deihekiCooldown = 0;
        kazegafukiCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (enmaCooldown > 0) enmaCooldown--;
        if (parcheminCooldown > 0) parcheminCooldown--;
        if (karyuuCooldown > 0) karyuuCooldown--;
        if (doryuuCooldown > 0) doryuuCooldown--;
        if (deihekiCooldown > 0) deihekiCooldown--;
        if (kazegafukiCooldown > 0) kazegafukiCooldown--;
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
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Parchemin Interdit")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {
        if(player.getLocation().distance(getPlayer().getLocation()) <= 30) {
            if(!using) return;
            if(!player.getLocation().getBlock().isLiquid()) return;
            if(!player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) return;
            if(player.getName().equals(getPlayer().getName())) return;
            player.setVelocity(player.getLocation().getDirection().multiply(3).setY(2));
        }
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
        if (this.avancementTarget != null && player.getUniqueId().equals(avancementTarget)) {
            Player hiruzen = Role.findPlayer(NarutoRoles.HIRUZEN);
            if (hiruzen != null) {
                if (!Role.isAlive(hiruzen)) return;
                hiruzen.sendMessage(prefix("&cVotre cible est mort, et vous emporte avec lui."));
                hiruzen.setHealth(0);
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        if (avancementTarget != null) {
            if (!Role.isAlive(avancementTarget)) return;
            Player avancementTarget = Bukkit.getPlayer(this.avancementTarget);
            if (avancementTarget == null) return;
            avancementTarget.sendMessage(prefix("&aHiruzen &fest mort alors qu'il utilisait son &cShiki Fûjin &fsur vous."));
            int random = (int) (Math.random() * 3);
            if (random == 0) {
                //TODO DISABLE POWERS avancementTarget FOR 20 MINUTES
                avancementTarget.sendMessage(prefix("&cVous ne pouvez plus utiliser de pouvoirs pendant 20 minutes."));
            } else if (random == 1) {
                avancementTarget.sendMessage(prefix("&cVous obtenez Slowness pour toute la game."));
                avancementTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
            } else {
                avancementTarget.sendMessage(prefix("&cVous obtenez blindness 5 secondes toutes les minutes."));
                Tasks.runTimer(() -> {
                    Player real = Bukkit.getPlayer(this.avancementTarget);
                    if (real != null)
                        real.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0, false, false));
                }, 0, 60 * 20);
            }
        }
    }


    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (!using) return;
        int id = event.getBlock().getTypeId();
        if (id == 8 || id == 9) {
            event.setCancelled(true);
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

        if (Item.interactItem(event, "Parchemin Interdit")) {
            if (this.power != 0) {
                if (parcheminCooldown > 0) {
                    Messages.getCooldown(parcheminCooldown).queue(player);
                    return;
                }

                if (this.power == 1) {
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
                    this.power = 0;
                    this.parcheminCooldown = 60;
                    this.karyuuCooldown = 6 * 60;
                }
                if (this.power == 2) {
                    char c = Loc.getCharCardinalDirection(player);
                    this.using = true;
                    Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                    Location loc2 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                    Location one;
                    Location two;
                    if (c == 'W' || c == 'E') {
                        one = loc.add(20, 20, 0);
                        two = loc2.add(-20, 0, 0);
                        new BukkitRunnable() {
                            int timer = 32;

                            @Override
                            public void run() {
                                if (timer == 0) {
                                    Cuboid old = new Cuboid(one, two);
                                    old.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.AIR));
                                    using = false;
                                    cancel();
                                    return;
                                }
                                if (c == 'W') {
                                    Cuboid old = new Cuboid(one, two);
                                    Cuboid cuboid = new Cuboid(one.add(0, 0, 1), two.add(0, 0, 1));
                                    old.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.AIR));
                                    cuboid.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.STATIONARY_WATER));
                                } else {
                                    Cuboid old = new Cuboid(one, two);
                                    Cuboid cuboid = new Cuboid(one.add(0, 0, -1), two.add(0, 0, -1));
                                    old.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.AIR));
                                    cuboid.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.STATIONARY_WATER));
                                }
                                timer--;
                            }
                        }.runTaskTimer(narutoUHC, 0, 5);
                    } else {
                        one = loc.add(0, 20, 20);
                        two = loc2.add(0, 0, -20);
                        new BukkitRunnable() {
                            int timer = 32;

                            @Override
                            public void run() {
                                if (timer == 0) {
                                    Cuboid old = new Cuboid(one, two);
                                    old.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.AIR));
                                    using = false;
                                    cancel();
                                    return;
                                }
                                if (c == 'S') {
                                    Cuboid old = new Cuboid(one, two);
                                    Cuboid cuboid = new Cuboid(one.add(1, 0, 0), two.add(1, 0, 0));
                                    old.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.AIR));
                                    cuboid.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.STATIONARY_WATER));
                                } else {
                                    Cuboid old = new Cuboid(one, two);
                                    Cuboid cuboid = new Cuboid(one.add(-1, 0, 0), two.add(-1, 0, 0));
                                    old.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.AIR));
                                    cuboid.getBlocks().stream().filter(Objects::nonNull).forEach(block -> block.setType(Material.STATIONARY_WATER));
                                }
                                timer--;
                            }
                        }.runTaskTimer(narutoUHC, 0, 5);
                    }

                    this.power = 0;
                    this.parcheminCooldown = 60;
                    this.doryuuCooldown = 6 * 60;
                }

                if (this.power == 3) {
                    char c = Loc.getCharCardinalDirection(player);
                    Cuboid cuboid;
                    if (c == 'W') {
                        Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 2, player.getLocation().getBlockY() + 2, player.getLocation().getBlockZ() + 4);
                        Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 2, player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ() + 4);
                        cuboid = new Cuboid(one, two);
                    } else if (c == 'E') {
                        Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 2, player.getLocation().getBlockY() + 2, player.getLocation().getBlockZ() - 4);
                        Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 2, player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ() - 4);
                        cuboid = new Cuboid(one, two);
                    } else if (c == 'N') {
                        Location one = new Location(player.getWorld(), player.getLocation().getBlockX() - 4, player.getLocation().getBlockY() + 2, player.getLocation().getBlockZ() + 2);
                        Location two = new Location(player.getWorld(), player.getLocation().getBlockX() - 4, player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ() - 2);
                        cuboid = new Cuboid(one, two);
                    } else {
                        Location one = new Location(player.getWorld(), player.getLocation().getBlockX() + 4, player.getLocation().getBlockY() + 2, player.getLocation().getBlockZ() + 2);
                        Location two = new Location(player.getWorld(), player.getLocation().getBlockX() + 4, player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ() - 2);
                        cuboid = new Cuboid(one, two);
                    }
                    cuboid.getBlocks().forEach(block -> block.setType(Material.COBBLESTONE));
                    this.power = 0;
                    this.parcheminCooldown = 60;
                    this.deihekiCooldown = 6 * 60;
                }
                if (this.power == 4) {
                    for (Player entity : Loc.getNearbyPlayers(player, 15)) {
                        Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
                        entity.setVelocity(new Vector(0, 1.3, 0));
                        fromPlayerToTarget.multiply(50);
                        fromPlayerToTarget.setY(2);
                        entity.setVelocity(fromPlayerToTarget.normalize());
                    }
                    this.power = 0;
                    this.parcheminCooldown = 60;
                    this.kazegafukiCooldown = 6 * 60;
                }
            } else {
                if (parcheminCooldown > 0) {
                    Messages.getCooldown(parcheminCooldown).queue(player);
                    return;
                }
                Inventory inv = Bukkit.createInventory(null, 9, "Karyuu Endan");

                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Karyuu Endan").setLore(
                        "§7Ce pouvoir permet à Hiruzen de créer un",
                        "§7surpuissant lance-flamme aura pour effet",
                        "§7de cramer toutes choses face à lui,",
                        "§7lorsqu’un joueur est enflammé par ce",
                        "§7pouvoir il ne pourra s’éteindre et ce",
                        "§7pendant 10 secondes."
                ).toItemStack());
                inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Doryuu Heki").setLore(
                        "§7Ce pouvoir permet à Hiruzen de créer une",
                        "§7puissante vague d’eau qui permet d’éjecter",
                        "§7les joueurs et les créatures hostiles face",
                        "§7à lui."
                ).toItemStack());
                inv.setItem(3, new ItemBuilder(Material.NETHER_STAR).setName("§6Deiheki").setLore(
                        "§7Ce pouvoir permet à Hiruzen de créer un",
                        "§7gigantesque mur de pierre face à lui."
                ).toItemStack());
                inv.setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§6Kazegafuki").setLore(
                        "§7Ce pouvoir permet à Hiruzen d’éjecter tout",
                        "§7les joueurs proches de lui à environ 15",
                        "§7blocs de lui."
                ).toItemStack());

                player.openInventory(inv);
            }
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

        if (event.getInventory().getName().equalsIgnoreCase("Karyuu Endan")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                if (this.karyuuCooldown > 0) {
                    Messages.getCooldown(karyuuCooldown).queue(player);
                    return;
                }

                player.closeInventory();
                player.sendMessage(prefix("&fVous avez sélectionné le pouvoir &aKaryuu Endan&f."));
                this.power = 1;
            }

            if (event.getSlot() == 2) {
                if (this.doryuuCooldown > 0) {
                    Messages.getCooldown(doryuuCooldown).queue(player);
                    return;
                }

                player.closeInventory();
                player.sendMessage(prefix("&fVous avez sélectionné le pouvoir &aDoryuu Heki&f."));
                this.power = 2;
            }


            if (event.getSlot() == 3) {
                if (this.deihekiCooldown > 0) {
                    Messages.getCooldown(deihekiCooldown).queue(player);
                    return;
                }

                player.closeInventory();
                player.sendMessage(prefix("&fVous avez sélectionné le pouvoir &aDeiheki&f."));
                this.power = 3;
            }

            if (event.getSlot() == 3) {
                if (this.kazegafukiCooldown > 0) {
                    Messages.getCooldown(kazegafukiCooldown).queue(player);
                    return;
                }

                player.closeInventory();
                player.sendMessage(prefix("&fVous avez sélectionné le pouvoir &aKazegafuki&f."));
                this.power = 4;
            }
        }

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

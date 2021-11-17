package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Kakashi extends NarutoRole {

    public UUID kakashiTarget = null;
    public int avancement = 0;
    public HashMap<String, List<PotionEffectType>> copies = new HashMap<>();
    public int pakkunCooldown = 0;
    public Wolf wolf = null;
    private int arimasuCooldown = 0;
    private int sonohokaCooldown = 0;
    public HashMap<UUID, Location> oldLocation = new HashMap<>();

    @Override
    public void resetCooldowns() {
        pakkunCooldown = 0;
        arimasuCooldown = 0;
        sonohokaCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(pakkunCooldown > 0) {
            pakkunCooldown--;
        }

        if(arimasuCooldown > 0) {
            arimasuCooldown--;
        }

        if(sonohokaCooldown > 0) {
            sonohokaCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kakashi";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Sharingan")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kamui")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Pakkun")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.interactItem(event.getItem(), "Sharingan")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Selection Kakashi");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Copie").setLore(
                    "§7Celui-ci lui permet de voir tous les",
                    "§7joueurs dans un rayon de 20 blocs, lorsqu’il",
                    "§7clique sur l’un des joueurs, une progression",
                    "§7commence, ce pouvoir permet de copier le",
                    "§7pouvoir ou les pouvoirs du joueur ciblé",
                    "§7Celle-ci §7possède certaines conditions, en",
                    "§7effet il faut être proche du joueur ciblé",
                    "§7pour que celle-ci avance, la progression",
                    "§7se fait de la manière suivante:",
                    "§f§l» 20 blocs = 2pts/s",
                    "§f§l» 10 blocs = 5pts/s",
                    "§f§l» 5 blocs = 10pts/s"
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Techniques").setLore(
                    "§7Ce menu montre une liste de tout les pouvoirs",
                    "§7qu’il a copié, cependant il faut réussir la",
                    "§7progression de son pouvoir."
            ).toItemStack());

            player.openInventory(inv);
        }

        if(Item.interactItem(event.getItem(), "Kamui")) {

            Inventory inv = Bukkit.createInventory(null, 9, "Kamui");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());

            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Arimasu").toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Sonohoka").toItemStack());

            player.openInventory(inv);

        }

        if(Item.interactItem(event.getItem(), "Pakkun")) {

            if(pakkunCooldown > 0) {
                player.sendMessage(Messages.cooldown(pakkunCooldown));
                return;
            }

            wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
            wolf.setAngry(false);
            wolf.setSitting(false);
            wolf.setTamed(true);
            wolf.setOwner(player);


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
            Inventory inv = Bukkit.createInventory(null, i, "Pakkun");
            int j = 1;
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            for (Player entity : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                j++;
            }
            player.openInventory(inv);

            pakkunCooldown = 30*60;
        }


    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(player.getUniqueId().equals(kakashiTarget)) {
            kakashiTarget = null;
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        if(event.getInventory().getName().equals("Selection Kakashi")) {
            event.setCancelled(true);
            if(event.getSlot() == 1) {
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
                Inventory inv = Bukkit.createInventory(null, i, "Copie");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if(event.getSlot() == 2) {
                Inventory inv;
                if(copies.size() < 8) {
                    inv = Bukkit.createInventory(null, 9, "Techniques");
                } else {
                    inv = Bukkit.createInventory(null, 18, "Techniques");
                }

                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (String s : copies.keySet()) {
                    List<String> lore = new ArrayList<>();
                    if(copies.get(s).size() == 0) lore.add("§cAucun effet");
                    copies.get(s).forEach(str -> lore.add("§f§l» " + str.getName().toLowerCase().replace("_", " ")));
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setLore(lore).setName("§6" + s).setSkullOwner(s).toItemStack());
                    j++;
                }

                player.openInventory(inv);
            }
        }

        if(event.getInventory().getName().equals("Techniques")) {
            event.setCancelled(true);
        }

        if(event.getInventory().getName().equals("Copie")) {

            if(!event.getCurrentItem().hasItemMeta()) return;
            if(event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            event.setCancelled(true);

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if(target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            if(kakashiTarget != null) {
                player.sendMessage(CC.prefix("§cVous utilisez déjà ce pouvoir sur quelqu'un"));
                return;
            }

            kakashiTarget = target.getUniqueId();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(kakashiTarget == null) {
                        cancel();
                        return;
                    }

                    Player kakashi = Role.findPlayer(NarutoRoles.KAKASHI);
                    Player newTarget = Bukkit.getPlayer(kakashiTarget);
                    if(avancement >= 2500) {

                        if(newTarget != null && kakashi != null) {
                            player.sendMessage(CC.prefix("§aVous avez terminé la progression sur " + newTarget.getName()));
                            List<PotionEffectType> map = new ArrayList<>();
                            for (PotionEffect activePotionEffect : newTarget.getActivePotionEffects()) {
                                if(activePotionEffect.getDuration() > 30*20*60) {
                                    kakashi.addPotionEffect(activePotionEffect);
                                    kakashi.sendMessage(CC.prefix("§fVous venez de recevoir l'effet §a" + activePotionEffect.getType().getName()));
                                    map.add(activePotionEffect.getType());
                                }
                            }
                            copies.put(newTarget.getName(), map);
                            cancel();
                        }
                    } else {
                        if(newTarget != null && kakashi != null) {
                            int distance = (int) kakashi.getLocation().distance(newTarget.getLocation());
                            if(distance <= 5) {
                                avancement += 10;
                                Title.sendActionBar(kakashi, "§7▎ §fAvancement §a" + newTarget.getName() + " §f§l» §a" + avancement + "§8/§a2500");
                            } else if(distance <= 10) {
                                avancement += 5;
                                Title.sendActionBar(kakashi, "§7▎ §fAvancement §a" + newTarget.getName() + " §f§l» §a" + avancement + "§8/§a2500");
                            } else if(distance <= 20) {
                                avancement += 2;
                                Title.sendActionBar(kakashi, "§7▎ §fAvancement §a" + newTarget.getName() + " §f§l» §a" + avancement + "§8/§a2500");
                            }

                        }
                    }

                }
            }.runTaskTimer(narutoUHC, 0, 20);

            player.closeInventory();

        }

        if(event.getInventory().getName().equals("Pakkun")) {

            event.setCancelled(true);
            if(!event.getCurrentItem().hasItemMeta()) return;
            if(!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if(target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            player.sendMessage(CC.prefix("§cVotre chien traquera désormais " + target.getName()));

            wolf.setAngry(true);
            wolf.setTarget(target);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(wolf != null && Bukkit.getPlayer(target.getName()) != null) {
                        wolf.setAngry(true);
                        wolf.setTarget(Bukkit.getPlayer(target.getName()));
                    }
                    if(wolf == null) {
                        cancel();
                    }
                }
            }.runTaskTimer(narutoUHC, 0, 20);

        }

        if(event.getInventory().getName().equals("Kamui")) {

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            if(event.getSlot() == 1) {
                final Location oldLocation;
                oldLocation = player.getLocation();

                if(arimasuCooldown > 0) {
                    player.sendMessage(Messages.cooldown(sonohokaCooldown));
                    return;
                }

                player.teleport(manager.getKamuiSpawn());
                this.oldLocation.put(player.getUniqueId(), oldLocation);
                player.sendMessage(CC.prefix("§fVous serez retéléporté à votre ancienne position dans §a10 minutes§f."));

                Tasks.runLater(() -> {
                    player.teleport(oldLocation);
                    player.sendMessage(CC.prefix("§fVous avez été téléporté à votre ancienne position."));
                }, 10*20*60);

                arimasuCooldown = 15*60;

            }

            if(event.getSlot() == 2) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player entity : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    nearbyPlayer++;
                }
                if(nearbyPlayer > 7 && nearbyPlayer <= 16) {
                    i = 18;
                } else if(nearbyPlayer > 16) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Arimasu");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20, 20, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

        if(event.getInventory().getName().equals("Arimasu")) {

            final Location oldLocation;
            oldLocation = player.getLocation();

            if(sonohokaCooldown > 0) {
                player.sendMessage(Messages.cooldown(arimasuCooldown));
                return;
            }

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if(target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            player.closeInventory();

            player.sendMessage(CC.prefix("§fVous avez téléporté §a" + player.getName() + " §fdans le monde lié de Kamui."));
            target.teleport(manager.getKamuiSpawn());
            this.oldLocation.put(target.getUniqueId(), oldLocation);
            target.sendMessage(CC.prefix("§cKakashi §fvous a téléporté au monde lié de Kamui."));
            target.sendMessage(CC.prefix("§fVous serez retéléporté à votre ancienne position dans §a5 minutes§f."));

            Tasks.runLater(() -> {
                target.teleport(oldLocation);
                target.sendMessage(CC.prefix("§fVous avez été téléporté à votre ancienne position."));
            }, 5*20*60);

            sonohokaCooldown = 30*60;
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {

        if(!args[0].equalsIgnoreCase("yameru")) return;

        for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
            if(Bukkit.getPlayer(uuid) != null) {
                Player target = Bukkit.getPlayer(uuid);
                if(target.getWorld().getName().equals("kamui") && oldLocation.get(uuid) != null) {
                    target.teleport(oldLocation.get(uuid));
                    target.sendMessage(CC.prefix("§aKakashi §fa décidé de vous téléporter sur le monde normal."));
                    player.sendMessage(CC.prefix("§fVous avez téléporté §a" + target.getName() + " §fdans le monde normal."));

                }
            }
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.RAITON;
    }
}

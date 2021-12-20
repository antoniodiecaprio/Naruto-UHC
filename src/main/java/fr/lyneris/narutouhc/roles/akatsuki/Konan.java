package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.particle.ProgressBar;
import fr.lyneris.narutouhc.particle.WingsEffect;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class Konan extends NarutoRole {

    public int yariCooldown = 0;
    public int batafuraiCooldown = 0;
    public int kamiTokuCooldown = 0;
    public int chissokuCooldown = 0;
    public int bakuhatsuCooldown = 0;
    public Location oldLocation = null;
    public boolean usingKamiNoShisha = false;
    private int kamiNoShishaCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.KONAN;
    }

    @Override
    public void resetCooldowns() {
        yariCooldown = 0;
        batafuraiCooldown = 0;
        kamiTokuCooldown = 0;
        chissokuCooldown = 0;
        bakuhatsuCooldown = 0;
        kamiNoShishaCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (yariCooldown > 0) {
            yariCooldown--;
        }

        if (batafuraiCooldown > 0) {
            batafuraiCooldown--;
        }

        if (kamiNoShishaCooldown > 0) {
            kamiNoShishaCooldown--;
        }

        if (kamiTokuCooldown > 0) {
            kamiTokuCooldown--;
        }

        if (chissokuCooldown > 0) {
            chissokuCooldown--;
        }

        if (bakuhatsuCooldown > 0) {
            bakuhatsuCooldown--;
        }

    }

    @Override
    public String getRoleName() {
        return "Konan";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Shikigami no Mai")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kami no Shisha")).toItemStack());

        List<String> list = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(e -> Bukkit.getPlayer(e) != null)
                .filter(uuid -> roleManager.getCamp(uuid) == Camp.AKATSUKI)
                .map(e -> Bukkit.getPlayer(e).getName())
                .forEach(list::add);
        if (Role.findPlayer(NarutoRoles.OBITO) != null) list.add(Role.findPlayer(NarutoRoles.OBITO).getName());


        player.sendMessage(CC.prefix("§cListe des Akatsuki:"));
        list.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");
    }


    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event, "Kami no Shisha")) {
            if (this.usingKamiNoShisha) {
                player.sendMessage(prefix("&cVous êtes déjà en train d'utiliser ce pouvoir"));
                return;
            }

            if (this.kamiNoShishaCooldown > 0) {
                Messages.getCooldown(kamiNoShishaCooldown).queue(player);
                return;
            }

            this.kamiNoShishaCooldown = 20 * 60;
            this.usingKamiNoShisha = true;
        }

        if (Item.interactItem(event.getItem(), "Shikigami no Mai")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Shikigami no Mai");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Yari").setLore(
                    "§7Lorsqu’elle l’utilise, une épée en diamant",
                    "§7nommé \"Yari\" lui est donné dans son",
                    "§7inventaire, celle-ci est Tranchant 7 mais",
                    "§7ne possède qu’une seule utilisation, ce",
                    "§7pouvoir possède un délai de 5 minutes."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Batafurai").setLore(
                    "§7Lorsqu’elle l’utilise, celle-ci lui",
                    "§7permet de se mettre en mode spectateur",
                    "§7et d’espionner les joueurs aux alentours",
                    "§7d’elle, le pouvoir dure 45 secondes, lors",
                    "§7de l’activation de son pouvoir elle n’est",
                    "§7plus visible dans la tablist, elle ne peut",
                    "§7s’éloigner de plus de 60 blocs de la position",
                    "§7à laquelle elle a utilisé son pouvoir, son",
                    "§7pouvoir possède un délai de 10 minutes."
            ).toItemStack());
            inv.setItem(3, new ItemBuilder(Material.NETHER_STAR).setName("§6Kami Toku").setLore(
                    "§7Lorsqu’elle l’utilise, ce pouvoir lui",
                    "§7permet de s'envoler pendant 8 secondes,",
                    "§7lorsqu’elle utilise ce pouvoir des",
                    "§7particules en forme d’ailes d’ange apparaît",
                    "§7sur son dos et celui-ci possède un délai",
                    "§7d'utilisation de 10 minutes."
            ).toItemStack());
            inv.setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§6Chissoku").setLore(
                    "§7Lorsqu’elle l’utilise, un menu de tous",
                    "§7les joueurs dans un rayon de 20 blocs",
                    "§7autour d’elle est affiché, lorsqu’elle",
                    "§7clique sur l’un d’entre eux, le joueur",
                    "§7ciblé sera immobilisé et recevra l’effet",
                    "§7poison 1 pendant 5 secondes, ce pouvoir",
                    "§7possède un délai de 20 minutes."
            ).toItemStack());


            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Shikigami no Mai")) {
            event.setCancelled(true);

            if (event.getSlot() == 1) {
                if (yariCooldown > 0) {
                    player.sendMessage(Messages.cooldown(yariCooldown));
                    return;
                }
                player.closeInventory();
                player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 7).setName(Item.specialItem("Yari")).toItemStack());

                player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aYari§f."));

                yariCooldown = 5 * 60;

            }

            if (event.getSlot() == 2) {
                if (batafuraiCooldown > 0) {
                    player.sendMessage(Messages.cooldown(batafuraiCooldown));
                    return;
                }

                player.closeInventory();

                player.setGameMode(GameMode.SPECTATOR);
                this.oldLocation = player.getLocation();

                player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aBatafurai§f."));

                Bukkit.getOnlinePlayers().forEach(player1 -> player.hidePlayer(player));

                Tasks.runLater(() -> {
                    player.teleport(this.oldLocation);
                    player.setGameMode(GameMode.SURVIVAL);
                    oldLocation = null;
                    Bukkit.getOnlinePlayers().forEach(player1 -> player.showPlayer(player));
                }, 45 * 20);

                batafuraiCooldown = 10 * 60;

            }

            if (event.getSlot() == 3) {
                if (kamiTokuCooldown > 0) {
                    player.sendMessage(Messages.cooldown(kamiTokuCooldown));
                    return;
                }

                player.closeInventory();

                player.setAllowFlight(true);
                player.setFlying(true);
                new WingsEffect(8 * 20, EnumParticle.FLAME).start(player);
                player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aKami Toku§f."));

                Tasks.runLater(() -> player.setAllowFlight(false), 8 * 20);

                kamiTokuCooldown = 10 * 60;

            }

            if (event.getSlot() == 4) {
                if (yariCooldown > 0) {
                    player.sendMessage(Messages.cooldown(chissokuCooldown));
                    return;
                }
                player.closeInventory();

                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 20)) {
                    nearbyPlayer++;
                }
                if (nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if (nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Chissoku");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

        if (event.getInventory().getName().equals("Chissoku")) {
            event.setCancelled(true);

            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aChissoku Toku§f."));

            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0, false, false));
            manager.setStuned(target, true, 5);

            chissokuCooldown = 20 * 60;
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("bakuhatsu")) {

            if (bakuhatsuCooldown > 0) {
                player.sendMessage(Messages.cooldown(bakuhatsuCooldown));
                return;
            }

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aBakuhatsu§f."));
            new BukkitRunnable() {
                int timer = 60 * 20;

                @Override
                public void run() {
                    if (!UHC.getUHC().getGameManager().getPlayers().contains(player.getUniqueId()) || player.getGameMode() == GameMode.SPECTATOR) {
                        cancel();
                    }
                    Title.sendActionBar(player, "§7Bakuhatsu §f§l» " + ProgressBar.getProgressBar(timer, 60 * 20, 100, '▎', ChatColor.GREEN, ChatColor.WHITE));

                    if (timer % 20 == 0) {
                        List<Location> blockLocations = getCircle(player.getLocation(), 10);
                        for (Location blockLocation : blockLocations) {
                            blockLocation.getWorld().createExplosion(blockLocation, 1.0f);
                        }
                    }

                    if (timer == 0) {
                        cancel();
                    }
                    timer--;
                }
            }.runTaskTimer(narutoUHC, 0, 1);

            bakuhatsuCooldown = 20 * 60;

        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if (oldLocation != null) {
            if (player.getLocation().distance(oldLocation) >= 60) {
                player.teleport(this.oldLocation);
                player.sendMessage(CC.prefix("§cVous ne pouvez pas vous déplacer à plus de 60 blocks de votre position initiale."));
            }
        }
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (Item.specialItem(player.getItemInHand(), "Yari")) {
            player.getInventory().removeItem(player.getItemInHand());
        }

        if (this.usingKamiNoShisha) {
            Location loc = event.getEntity().getLocation();
            Location one = new Location(Bukkit.getWorld("world"), loc.getBlockX() + 15, loc.getBlockY(), loc.getBlockZ() + 4);
            Location two = new Location(Bukkit.getWorld("world"), loc.getBlockX() - 15, loc.getBlockY(), loc.getBlockZ() - 4);
            Cuboid cuboid = new Cuboid(one, two);
            HashMap<Location, Material> map = new HashMap<>();
            cuboid.getBlocks().stream().filter(Objects::nonNull).filter(block -> block.getType() != Material.AIR).forEach(block -> {
                map.put(block.getLocation(), block.getType());
                block.setType(Material.AIR);
            });
            Tasks.runLater(() -> map.keySet().forEach(location -> location.getBlock().setType(map.get(location))), 10*20*60);
            this.usingKamiNoShisha = false;
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    public List<Location> getCircle(Location center, double radius) {
        List<Location> locs = new ArrayList<>();
        int points = 10;

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            Location point = center.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
            locs.add(point);
        }
        return locs;
    }

}

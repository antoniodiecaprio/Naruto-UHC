package fr.lyneris.narutouhc.roles.jubi;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.WorldUtils;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Madara extends NarutoRole implements Listener {

    public boolean usingSusano, usedMeteorite;
    private UUID detectTarget = null;
    private int detectUses = 0, susanoCooldown = 0, swordCooldown = 0, banshoCooldown = 0, banshoUses = 0, shinraUses = 0, shinraCooldown = 0;
    private boolean meteoresExplode = false;

    public NarutoRoles getRole() {
        return NarutoRoles.MADARA;
    }

    @Override
    public void resetCooldowns() {
        susanoCooldown = 0;
        swordCooldown = 0;
        banshoCooldown = 0;
        shinraCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (susanoCooldown > 0) susanoCooldown--;
        if (swordCooldown > 0) swordCooldown--;
        if (banshoCooldown > 0) banshoCooldown--;
        if (shinraCooldown > 0) shinraCooldown--;
    }

    @Override
    public void onSecond(int timer, Player player) {
        Player madara = Role.findPlayer(NarutoRoles.MADARA);
        Player target = Bukkit.getPlayer(detectTarget);
        if (madara == null || target == null) return;
        Title.sendActionBar(madara, Loc.getDirectionMate(madara, target));
    }

    @Override
    public String getRoleName() {
        return "Madara";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §dMadara\n" +
                "§7▎ Objectif: §rSon but est de gagner avec §dObito\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rSusano§7\" qui lui permet d'enflammer un ennemi dès qu'il le frappe, il gagne également l’effet §9Résistance 1§7, il reçoit aussi une épée Tranchant 7, il peut frappé avec son épée une fois toutes les 20 secondes, ce pouvoir dure 5 minutes et possède un délai d'utilisation de 20 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item en son nom, lorsqu’il l’utilise, il lui confère les effets §bVitesse 2§7 et §cForce 1§7, il est infini et ne possède aucun délai.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item “§rChibaku Tensei§7”, à son utilisation, il est dirigé vers un menu dans lequel il a trois choix :\n" +
                "§e §f\n" +
                "§7→ Banshô Ten’in : Lorsqu’il l’utilise, il dispose d’une liste de tous les joueurs dans un rayon de 30 blocs autour de lui, en cliquant sur l’un des joueurs celui-ci sera téléporté instantanément à sa position, son pouvoir possède un délai de 5 minutes, il peut le faire 2 fois dans la partie.\n" +
                "§e §f\n" +
                "§7→ Shinra Tensei : Lorsqu’il l’utilise, tous les joueurs et projectiles dans un rayon de 20 blocs seront propulsés à l’opposé de sa position, ils sont propulsés d’environ 10 blocs, son pouvoir possède un délai de 5 minutes, il peut le faire 2 fois dans la partie.\n" +
                "§e §f\n" +
                "§7→ Tengai Shinsei : Lorsqu’il l’utilise, un bruit de wither se fait retentir dans un rayon de 50 blocs autour de lui, suite à cela une météorite s’écrase à la position à laquelle il a utilisé le pouvoir, elle dispose d’une force destructrice, il est très conseillé de fuir celle-ci.\n" +
                "§e §f\n" +
                "§7• Il dispose d’une épée en diamant Tranchant 4 nommé “§rGunbai§7”\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns detect§7, celle-ci lui permet de voir tous les joueurs dans un rayon de 200 blocs, lorsqu’il clique sur l’un des joueurs écrit dans son chat, il peut alors traquer la personne pendant 5 minutes, son pouvoir est utilisable seulement 2 fois dans la partie.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §6Résistance au Feu§7 et §c2 cœurs§7 supplémentaires.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’identité d’§dObito§7.\n" +
                "§e §f\n" +
                "§7• Il possède la particularité de parler avec §dObito§7 dans le chat, il lui suffit simplement d’écrire dans le chat avec l’aide du préfixe \"!\" pour pouvoir communiquer avec lui.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §cKaton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.OBITO);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Susano")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Madara")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Chibaku Tensei")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).setName(Item.specialItem("Gunbai")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event, "Susano")) {
            if (susanoCooldown > 0) {
                player.sendMessage(Messages.cooldown(susanoCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usingSusano = true;
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20 * 60, 0, false, false));
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.specialItem("Epee")).addEnchant(Enchantment.DAMAGE_ALL, 7).toItemStack());

            susanoCooldown = 20 * 60;
            Tasks.runAsyncLater(() -> usingSusano = false, 5 * 20 * 60);
        }

        if (Item.interactItem(event, "Madara")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            if (player.hasPotionEffect(PotionEffectType.SPEED) && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            }
            player.sendMessage(prefix("&fVous avez utilisé votre &aMadara&f."));
        }

        if (Item.interactItem(event, "Chibaku Tensei")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Chibaku Tensei");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Banshô Ten’in").setLore(
                    "§7Lorsqu’il l’utilise, il dispose d’une",
                    "§7liste de tous les joueurs dans un rayon",
                    "§7de 30 blocs autour de lui, en cliquant",
                    "§7sur l’un des joueurs celui-ci sera téléporté",
                    "§7instantanément à sa position, son pouvoir",
                    "§7possède un délai de 5 minutes, il peut le",
                    "§7faire 2 fois dans la partie."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Shinra Tensei").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs et",
                    "§7projectiles dans un rayon de 20 blocs",
                    "§7seront propulsés à l’opposé de sa position,",
                    "§7ils sont propulsés d’environ 10 blocs, son",
                    "§7pouvoir possède un délai de 5 minutes, il",
                    "§7peut le faire 2 fois dans la partie."
            ).toItemStack());
            if (!usedMeteorite) {
                inv.setItem(3, new ItemBuilder(Material.NETHER_STAR).setName("§6Tengai Shinsei").setLore(
                        "§7Lorsqu’il l’utilise, un bruit de wither",
                        "§7se fait retentir dans un rayon de 50 blocs",
                        "§7autour de lui, suite à cela une météorite",
                        "§7s’écrase à la position à laquelle il a",
                        "§7utilisé le pouvoir, elle dispose d’une force",
                        "§7destructrice, il est très conseillé de fuir",
                        "§7celle-ci."
                ).toItemStack());
            }

            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equalsIgnoreCase("Chibaku Tensei")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {

                if (banshoUses >= 2) {
                    player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
                    return;
                }

                if (banshoCooldown > 0) {
                    player.sendMessage(Messages.cooldown(banshoCooldown));
                    return;
                }

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
                Inventory inv = Bukkit.createInventory(null, i, "Banshô Ten’in");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if (event.getSlot() == 2) {

                if (shinraUses >= 2) {
                    player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
                    return;
                }

                if (shinraCooldown > 0) {
                    player.sendMessage(Messages.cooldown(shinraCooldown));
                    return;
                }

                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                    Location initialLocation = player.getLocation().clone();
                    Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(initialLocation.toVector());
                    fromPlayerToTarget.multiply(4); //6
                    fromPlayerToTarget.setY(1); // 2
                    entity.setVelocity(fromPlayerToTarget);
                }

                shinraUses++;
                shinraCooldown = 5 * 60;

            }

            if (event.getSlot() == 3) {
                if (usedMeteorite) {
                    return;
                }

                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                createMeteorite(player);
            }
        }

        if (event.getInventory().getName().equalsIgnoreCase("Banshô Ten’in")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            if (banshoUses >= 2) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
                return;
            }

            if (banshoCooldown > 0) {
                player.sendMessage(Messages.cooldown(banshoCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            target.teleport(player);
            target.sendMessage(prefix("&cMadara &fvous a téléporté à sa position."));
            player.sendMessage(prefix("&fVous avez téléporté &a" + target.getName() + " &fà votre position."));

            banshoUses++;
            banshoCooldown = 5 * 60;
        }
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (usingSusano) {
            if (event.getEntity().getFireTicks() <= 0) {
                event.getEntity().setFireTicks(60);
            }
        }

        if (Item.specialItem(player.getItemInHand(), "Epee")) {
            if (usingSusano) {
                if (swordCooldown > 0) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.cooldown(swordCooldown));
                    return;
                }
                swordCooldown = 20;
            } else {
                player.getInventory().removeItem(player.getItemInHand());
            }
        }

    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("detect")) {

            if (detectUses >= 2) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
                return;
            }

            if (Loc.getNearbyPlayers(player, 200).size() == 0) {
                player.sendMessage(prefix("&cIl n'y a personne autour de vous."));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(prefix("&fVoici la liste des joueurs autour de vous:"));
            for (Player nearbyPlayer : Loc.getNearbyPlayers(player, 200)) {
                TextComponent text = new TextComponent(CC.translate("&8- &c" + nearbyPlayer.getName()));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ns detecttraque " + nearbyPlayer.getName()));
                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("&aCliquez-ici pour traquer " + nearbyPlayer.getName())}));
                player.spigot().sendMessage(text);
            }

            detectUses++;

        }

        if (args[0].equalsIgnoreCase("detecttraque")) {
            Player target = Bukkit.getPlayer(args[1]);

            if (detectUses > 2) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
                return;
            }

            if (detectTarget != null) {
                player.sendMessage(prefix("&cVous êtes déjà en train d'utiliser ce pouvoir sur quelqu'un."));
                return;
            }

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (target.getLocation().distance(player.getLocation()) > 200) {
                player.sendMessage(prefix("&cVous êtes devez être à moins de 200 blocks de ce joueur pour utiliser ce pouvoir."));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            detectTarget = target.getUniqueId();
            player.sendMessage(prefix("&fVous traquez désormais &a" + target.getName()));
            Tasks.runAsyncLater(() -> {
                detectTarget = null;
                player.sendMessage(prefix("&cVous ne traquez plus " + target.getName()));
            }, 5 * 20 * 60);
            if (detectUses == 2) detectUses++;
        }

    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event, Player player) {
        if (event.getMessage().startsWith("!")) {
            String message = event.getMessage().substring(1);
            Player obito = Role.findPlayer(NarutoRoles.OBITO);
            if (obito != null) {
                for (Player sendMessage : new Player[]{player, obito}) {
                    sendMessage.sendMessage("&f(&c!&f) &cMadara&8: &f" + message);
                }
            } else {
                player.sendMessage("&f(&c!&f) &cMadara&8: &f" + message);
            }
        }
    }

    public void createMeteorite(Player player) {
        player.sendMessage(CC.translate("&fVous avez fait spawn votre &cMétéorite&f."));
        usedMeteorite = true;
        playSoundDistanceVolume(player.getLocation(), "atlantis.tengaishinsei", 10);
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.getWorld() == player.getWorld()) {
                if (players.getLocation().distance(player.getLocation()) <= 50) {
                    players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
                }
            }
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                MeteoresGenerator.spawnMeteore(player.getLocation(), 5, 100);
            }
        }.runTaskLater(narutoUHC, 20 * 6);

        player.closeInventory();
    }

    public void playSoundDistanceVolume(Location location, String sound, int volume) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(location, sound, volume, 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockUpdate(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if (fallingBlock.getCustomName() != null) {
                if (fallingBlock.getCustomName().equals(Madara.MeteoresGenerator.METEORE_KEY)) {
                    block.setType(Material.AIR);
                    if (!meteoresExplode) {
                        if (fallingBlock.getWorld() != Bukkit.getWorld("kamui"))
                            WorldUtils.createBeautyExplosion(fallingBlock.getLocation(), 30);
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (players.getWorld() == fallingBlock.getWorld()) {
                                if (players.getGameMode() != GameMode.SPECTATOR && Role.isAlive(players)) {
                                    if (!(Role.isRole(players, NarutoRoles.MADARA))) {
                                        if (players.getLocation().distance(fallingBlock.getLocation()) <= 29) {
                                            players.damage(100D);
                                        }

                                    }
                                }
                            }
                        }
                        meteoresExplode = true;
                    }
                }
            }
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.MADARA_OBITO;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    public static class MeteoresGenerator {

        public static final String METEORE_KEY = "MeteoreFallingBlock";


        public static void spawnMeteore(Location loc, int radius, int distanceSound) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (WorldUtils.getDistanceBetweenTwoLocations(loc, players.getLocation()) <= distanceSound) {
                    players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1, 1);
                }
            }

            Location center = loc.clone().add(0, 50, 0);

            List<Location> blockLocations = WorldUtils.generateSphere(center, radius, false);

            for (Location blockLocation : blockLocations) {
                FallingBlock fallingBlock = center.getWorld().spawnFallingBlock(blockLocation, Material.STONE, (byte) 0);
                fallingBlock.setDropItem(false);
                fallingBlock.setHurtEntities(true);
                fallingBlock.setCustomName(METEORE_KEY);
            }
        }

    }
}

package fr.lyneris.narutouhc.roles.jubi;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.biju.Bijus;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Obito extends NarutoRole {

    public HashMap<UUID, Location> oldLocation = new HashMap<>();
    public boolean invisible = false;
    public int attaqueCooldown = 0;
    public List<UUID> cannotMove = new ArrayList<>();
    public int tsukuyomiUses = 0;
    public boolean izanagi = false;
    private int arimasuCooldown = 0;
    private int sonohokaCooldown = 0;
    private int ninjutsuCooldown = 0;
    private int swordCooldown = 0;
    private boolean usingSusano = false;
    private int susanoCooldown = 0;
    private Bijus bijus = null;

    public NarutoRoles getRole() {
        return NarutoRoles.OBITO;
    }

    @Override
    public void resetCooldowns() {
        arimasuCooldown = 0;
        sonohokaCooldown = 0;
        attaqueCooldown = 0;
        ninjutsuCooldown = 0;
        susanoCooldown = 0;
        swordCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (arimasuCooldown > 0) arimasuCooldown--;
        if (sonohokaCooldown > 0) sonohokaCooldown--;
        if (attaqueCooldown > 0) attaqueCooldown--;
        if (ninjutsuCooldown > 0) ninjutsuCooldown--;
        if (susanoCooldown > 0) susanoCooldown--;
        if (swordCooldown > 0) swordCooldown--;
    }

    @Override
    public String getRoleName() {
        return "Obito";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §dObito\n" +
                "§7▎ Objectif: §rSon but est de gagner avec §dMadara\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d'un item nommé \"§rNinjutsu Spatio-Temporel§7\", il lui permet de devenir invisible même en portant son armure. Son pouvoir dure 1 minute, il peut le désactiver à tout moment, il possède un délai de 5 minutes, cependant lorsqu’il utilise son pouvoir, on pourra voir ses traces de pas (particules de redstone).\n" +
                "§e §f\n" +
                "§7• Il dispose d’un autre item nommé “§rKamui§7”, celui-ci lui affiche un menu dans lequel il a deux choix, le premier est nommé “§rArimasu§7”, celui-ci lui permet de le téléporter dans le monde lié à §rKamui§7 et ceci pendant 10 minutes. Le deuxième est nommé “§rSonohoka§7”, il lui permet de téléporter un joueur dans un rayon de 20 blocs autour de lui, dans le monde lié à §rKamui§7 et cela pendant 5 minutes. “§rArimasu§7” possède un délai de 5 minutes, “§rSonohoka§7” possède un délai de 15 minutes. Dans le monde de §rKamui§7 on ne peut pas poser de blocs, sauf les seaux de laves et d'eaux qui s'enlèvent automatiquement au bout d'une minute. On ne peut pas prendre de dégâts de chutes. Il peut utiliser la commande §r/ns yameru§7 pour téléporter lui et le joueur (s’il en a téléporté un) dans le monde normal, suite à sa commande ou si le temps est écoulé, ils seront téléportés à l’endroit exact où ils se situaient avant qu’Obito utilise son pouvoir.\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rGenjutsu§7\", Lorsqu’il l’utilise, un menu s’affiche avec 3 pouvoirs différents (voir fiche §rGenjutsu§7)\n" +
                "§e §f\n" +
                "§7• Il dispose d’une boussole nommée \"§rBiju§7\", Lorsqu’il clique sur celle-ci, un menu s’ouvre avec affiché les 6 §6Biju§7, lorsqu’il clique sur l’un d’entre eux, il commence à traquer celui-ci ou le réceptacle (joueur possédant le §6Biju§7), il obtient aussi dans son chat à quel moment va apparaître le §6Biju§7 ainsi que ces coordonnées.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns izanagi§7, celle-ci lui permet de recevoir 5 pommes d’or et d’être entièrement régénéré, cependant il perd §c1 cœur§7 permanent, et il ne pourra plus utiliser le Susano, s'il avait réussi à ramasser l'item auparavant, il peut l’utiliser qu’une seule fois dans la partie.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de la liste de tous les membres de l'§cAkatsuki§7 et de l’identité de §dMadara§7.\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §bVitesse 1§7 et §6Résistance au feu 1§7.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il se trouve avec un nombre de vie plus bas, il obtient un certain pourcentage de résistance, il obtient 2% de résistance par demi-cœur en moins, donc s’il se trouve à la moitié de sa vie, il possède 20% de résistance (l’équivalent de l’effet Résistance 1), il peut aussi aller jusqu’à 38% de résistance maximum (1 demi-cœur).\n" +
                "§e §f\n" +
                "§7• Il possède la particularité de parler avec §dMadara§7 dans le chat, il lui suffit simplement d’écrire dans le chat avec l’aide du préfixe \"!\" pour pouvoir communiquer avec lui.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il tue un joueur, il régénère de §c3 cœurs§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §cKaton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onAllPlayerDamage(EntityDamageEvent event, Player player) {
        int health = (int) player.getHealth();
        double resistance = ((player.getMaxHealth() - health) * 2);
        event.setDamage(event.getFinalDamage() * (1 - (resistance / 100)));
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));

        List<String> list = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(e -> Bukkit.getPlayer(e) != null)
                .filter(uuid -> roleManager.getCamp(uuid) == Camp.AKATSUKI)
                .map(e -> Bukkit.getPlayer(e).getName())
                .forEach(list::add);

        player.sendMessage(CC.prefix("§cListe des Akatsuki:"));
        list.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");

        Role.knowsRole(player, NarutoRoles.MADARA);

        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Ninjutsu Spatio-Temporel")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Kamui")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Genjutsu")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Biju")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.specialItem("Traqueur")).toItemStack());

    }

    @Override
    public void onAllPlayerJoin(PlayerJoinEvent event, Player player) {
        if (invisible) {
            Player obito = Role.findPlayer(NarutoRoles.OBITO);
            if (obito != null) {
                player.hidePlayer(obito);
            }
        }
    }


    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {

        if (invisible) event.setCancelled(true);

        if (usingSusano) {
            if (event.getEntity().getFireTicks() <= 0) {
                event.getEntity().setFireTicks(60);
            }
        }

        if (Item.specialItem(player.getItemInHand(), "Epee")) {
            if (usingSusano && !izanagi) {
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
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.specialItem(event.getItem(), "Traqueur")) {
            if(bijus == null) {
                player.sendMessage(prefix("&cVous n'avez selectionné aucun Biju."));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            if(bijus.getBiju().getMaster() == null) {
                if(bijus.getBiju().getMaster() == null) {
                    player.sendMessage(prefix("&fVous traquez désormais " + bijus.getBiju().getName() + " &fqui se situe en :"));
                    Location loc = bijus.getBiju().getLivingEntity().getLocation();
                    player.sendMessage(CC.prefix("&a" + loc.getBlockX() + "&f, &a" + loc.getBlockY() + "&f, &a" + loc.getBlockZ()));
                    player.setCompassTarget(loc);
                } else {
                    player.sendMessage(prefix("&fVous traquez désormais " + Bukkit.getPlayer(bijus.getBiju().getMaster()).getName() + " &fqui se situe en :"));
                    Location loc = Bukkit.getPlayer(bijus.getBiju().getMaster()).getLocation();
                    player.sendMessage(CC.prefix("&a" + loc.getBlockX() + "&f, &a" + loc.getBlockY() + "&f, &a" + loc.getBlockZ()));
                    player.setCompassTarget(loc);
                }
            }
        }

        if(Item.interactItem(event, "Biju")) {
            if(!Bijus.start) {
                player.sendMessage(CC.prefix("&cLes bijus ne sont toujours pas apparu."));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            Inventory inventory = Bukkit.createInventory(null, 9, "Biju");
            inventory.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            int i = 1;
            for (Bijus value : Bijus.values()) {
                inventory.setItem(i, new ItemBuilder(value.getMaterial()).setName(value.getBiju().getName()).toItemStack());
                i++;
            }

            player.openInventory(inventory);
        }

        if (Item.interactItem(event, "Susano")) {
            if (susanoCooldown > 0) {
                player.sendMessage(Messages.cooldown(susanoCooldown));
                return;
            }
            if (izanagi) {
                player.sendMessage(prefix("&cVous avez utilisé votre Izanagi, vous ne pouvez pas utiliser le Susano."));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usingSusano = true;
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20 * 60, 0, false, false));
            player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).setName(Item.specialItem("Epee")).addEnchant(Enchantment.DAMAGE_ALL, 7).toItemStack());

            susanoCooldown = 20 * 60;
            Tasks.runAsyncLater(() -> usingSusano = false, 5 * 20 * 60);
        }

        if (Item.interactItem(event.getItem(), "Genjutsu")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Genjutsu");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Tsukuyomi").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs se",
                    "§7trouvant dans un rayon de 20 blocs autour",
                    "§7de lui seront immobilisés pendant 8 secondes,",
                    "§7ce pouvoir est utilisable 2 fois dans la",
                    "§7partie, il ne peut pas taper les personnes",
                    "§7figées."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Attaque").setLore(
                    "§7Lorsqu’il l’utilise, il a une liste de tous",
                    "§7les joueurs se trouvant dans un rayon de",
                    "§720 blocs autour de lui, lorsqu’il choisit",
                    "§7un joueur, il sera téléporté derrière sa",
                    "§7cible, il possède un délai de 5 minutes."
            ).toItemStack());

            player.openInventory(inv);
        }

        if (Item.interactItem(event.getItem(), "Kamui")) {

            Inventory inv = Bukkit.createInventory(null, 9, "Kamui");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());

            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Arimasu").toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Sonohoka").toItemStack());

            player.openInventory(inv);

        }

        if (Item.interactItem(event, "Ninjutsu Spatio-Temporel")) {

            if (this.ninjutsuCooldown > 0) {
                player.sendMessage(Messages.cooldown(ninjutsuCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            ninjutsuCooldown = 5 * 60;
            invisible = true;
            player.sendMessage(prefix("&fVous êtes désormais &aInvisible&f."));
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.hidePlayer(player));
            Tasks.runLater(() -> {
                Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(player));
                invisible = false;
                player.sendMessage(prefix("&fVous êtes désormais &cVisible&f."));
            }, 20 * 60);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {

        if(event.getInventory().getName().equals("Biju")) {
            event.setCancelled(true);
            if(event.getSlot() != 0) {
                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                Biju biju = null;
                for (Bijus value : Bijus.values()) {
                    if(value.getBiju().getName().equals(event.getCurrentItem().getItemMeta().getDisplayName())) {
                        biju = value.getBiju();
                        break;
                    }
                }
                assert biju != null;
                if(biju.getMaster() == null) {
                    player.sendMessage(prefix("&fVous traquez désormais " + biju.getName() + " &fqui se situe en :"));
                    Location loc = biju.getLivingEntity().getLocation();
                    player.sendMessage(CC.prefix("&a" + loc.getBlockX() + "&f, &a" + loc.getBlockY() + "&f, &a" + loc.getBlockZ()));
                    player.setCompassTarget(loc);
                } else {
                    player.sendMessage(prefix("&fVous traquez désormais " + Bukkit.getPlayer(biju.getMaster()).getName() + " &fqui se situe en :"));
                    Location loc = Bukkit.getPlayer(biju.getMaster()).getLocation();
                    player.sendMessage(CC.prefix("&a" + loc.getBlockX() + "&f, &a" + loc.getBlockY() + "&f, &a" + loc.getBlockZ()));
                    player.setCompassTarget(loc);
                }
                player.sendMessage(prefix("&fFaites un clic &adroit &favec votre boussoule pour actualiser les coordonnées."));
                Bijus bijus = null;
                for (Bijus value : Bijus.values()) {
                    if(value.getBiju().getName().equals(biju.getName())) {
                        bijus = value;
                        break;
                    }
                }
                this.bijus = bijus;
            }
        }

        if (event.getInventory().getName().equals("Genjutsu")) {
            event.setCancelled(true);

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            if (event.getSlot() == 1) {
                if (Loc.getNearbyPlayers(player, 20).size() == 0) {
                    player.sendMessage(CC.prefix("§cIl n'y a personne autour de vous."));
                    return;
                }

                if (tsukuyomiUses >= 2) {
                    player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 2 fois."));
                    return;
                }

                tsukuyomiUses++;

                Loc.getNearbyPlayers(player, 20).forEach(target -> {
                    cannotMove.add(target.getUniqueId());
                    manager.setStuned(target, true, 8);
                    player.sendMessage(CC.prefix("§fVous avez immobilisé §c" + target.getName()));
                });
                Tasks.runLater(() -> cannotMove.clear(), 8 * 20);
                player.closeInventory();
            }

            if (event.getSlot() == 2) {
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
                Inventory inv = Bukkit.createInventory(null, i, "Attaque");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }
        }

        if (event.getInventory().getName().equals("Attaque")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            if (attaqueCooldown > 0) {
                player.sendMessage(Messages.cooldown(attaqueCooldown));
                return;
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            Location playerLocation = player.getLocation();
            Location damagerLocation = target.getLocation();

            Vector swap = damagerLocation.toVector()
                    .subtract(playerLocation.toVector()).normalize();
            Location targetLocation = damagerLocation.clone().add(swap);
            targetLocation.setY(target.getLocation().getY());

            player.teleport(targetLocation);

            player.sendMessage(CC.prefix("§fVous vous êtes téléporté derrière §a" + target.getName()));
            attaqueCooldown = 5 * 60;

        }

        if (event.getInventory().getName().equals("Kamui")) {

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            if (event.getSlot() == 1) {
                final Location oldLocation;
                oldLocation = player.getLocation();

                if (arimasuCooldown > 0) {
                    player.sendMessage(Messages.cooldown(sonohokaCooldown));
                    return;
                }

                if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                player.teleport(manager.getKamuiSpawn());
                this.oldLocation.put(player.getUniqueId(), oldLocation);
                player.sendMessage(CC.prefix("§fVous serez retéléporté à votre ancienne position dans §a10 minutes§f."));

                Tasks.runLater(() -> {
                    player.teleport(oldLocation);
                    player.sendMessage(CC.prefix("§fVous avez été téléporté à votre ancienne position."));
                }, 10 * 20 * 60);

                arimasuCooldown = 15 * 60;

            }

            if (event.getSlot() == 2) {
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
                Inventory inv = Bukkit.createInventory(null, i, "Arimasu");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

        }

        if (event.getInventory().getName().equals("Arimasu")) {
            event.setCancelled(true);

            final Location oldLocation;
            oldLocation = player.getLocation();

            if (sonohokaCooldown > 0) {
                player.sendMessage(Messages.cooldown(arimasuCooldown));
                return;
            }

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));
            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.closeInventory();

            player.sendMessage(CC.prefix("§fVous avez téléporté §a" + player.getName() + " §fdans le monde lié de Kamui."));
            target.teleport(manager.getKamuiSpawn());
            this.oldLocation.put(target.getUniqueId(), oldLocation);
            target.sendMessage(CC.prefix("§cObito §fvous a téléporté au monde lié de Kamui."));
            target.sendMessage(CC.prefix("§fVous serez retéléporté à votre ancienne position dans §a5 minutes§f."));

            Tasks.runLater(() -> {
                target.teleport(oldLocation);
                target.sendMessage(CC.prefix("§fVous avez été téléporté à votre ancienne position."));
            }, 5 * 20 * 60);

            sonohokaCooldown = 30 * 60;
        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;
        if (!invisible) return;

        player.getWorld().spigot().playEffect(player.getLocation(), Effect.COLOURED_DUST, 0, 1, 23, 23, 23, 1, 0, 64);
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if (invisible) event.setCancelled(true);
    }


    @Override
    public Camp getCamp() {
        return Camp.MADARA_OBITO;
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if (killer.getHealth() + 3 > killer.getMaxHealth()) {
            killer.setHealth(killer.getMaxHealth());
        } else {
            killer.setHealth(killer.getMaxHealth() + 3);
        }
        killer.sendMessage(prefix("&fVous avez tué quelqu'un. Vous régénérez &c3 coeurs&f."));
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event, Player player) {
        if (event.getMessage().startsWith("!")) {
            String message = event.getMessage().substring(1);
            Player madara = Role.findPlayer(NarutoRoles.OBITO);
            if (madara != null) {
                for (Player sendMessage : new Player[]{player, madara}) {
                    sendMessage.sendMessage("&f(&c!&f) &cObito&8: &f" + message);
                }
            } else {
                player.sendMessage("&f(&c!&f) &cObito&8: &f" + message);
            }
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("izanagi")) {
            if (izanagi) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            izanagi = true;
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
            player.setHealth(player.getMaxHealth());
            player.setMaxHealth(player.getMaxHealth() - 2);
        }

        if (args[0].equalsIgnoreCase("yameru")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player target = Bukkit.getPlayer(uuid);
                    if (target.getWorld().getName().equals("kamui") && oldLocation.get(uuid) != null) {
                        target.teleport(oldLocation.get(uuid));
                        target.sendMessage(CC.prefix("§aObito §fa décidé de vous téléporter sur le monde normal."));
                        player.sendMessage(CC.prefix("§fVous avez téléporté §a" + target.getName() + " §fdans le monde normal."));

                    }
                }
            }
        }

    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }
}

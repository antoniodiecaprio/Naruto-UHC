package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

import static fr.lyneris.narutouhc.roles.akatsuki.Itachi.IzanamiPlayer.*;
import static fr.lyneris.narutouhc.roles.akatsuki.Itachi.IzanamiTarget.*;

public class Itachi extends NarutoRole {

    public List<UUID> cannotMove = new ArrayList<>();
    public int tsukuyomiUses = 0;
    public int attaqueCooldown = 0;
    public boolean usingSusano = false;
    public int swordCooldown = 0;
    public int susanoCooldown = 0;
    public int sharinganUses = 0;
    public boolean usedIzanagi = false;
    public boolean usedIzanami = false;
    public UUID cible;
    boolean cibleManaged;
    int playerAvancement;

    public int hits = 0;
    public boolean receivedHit;
    public boolean giveGoldenApple;
    public boolean receivedGoldenApple;
    public boolean stayedFiveMinutes;
    public boolean placedLava;

    public int kilometers;
    public boolean killedSomeone;
    public int goldenApplesAte;
    public boolean managed = false;
    private IzanamiPlayer playerIzanami;
    private IzanamiPlayer playerIzanami2;
    private IzanamiTarget targetIzanami;

    public NarutoRoles getRole() {
        return NarutoRoles.ITACHI;
    }

    @Override
    public void resetCooldowns() {
        attaqueCooldown = 0;
        swordCooldown = 0;
        susanoCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (attaqueCooldown > 0) {
            attaqueCooldown--;
        }

        if (swordCooldown > 0) {
            swordCooldown--;
        }

        if (susanoCooldown > 0) {
            susanoCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Itachi";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §cItachi\n" +
                "§7▎ Objectif: §rSon but est de gagner avec l'§cAkatsuki\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rGenjutsu§7\", lorsqu’il l’utilise, un menu s’affiche avec 3 pouvoirs différents (voir fiche Genjutsu)\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé \"§rSusano§7\", celui-ci lui permet d'enflammer un ennemi dès qu'il le frappe, il gagne également l’effet §9Résistance 1§7, il reçoit aussi une épée Tranchant 7, il peut frapper avec son épée une fois toutes les 20 secondes, ce pouvoir dure 5 minutes et possède un délai d'utilisation de 20 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns sharingan <Joueur>§7, celle-ci lui permet d’avoir toutes les informations possible à propos du joueur : Son rôle, s’il a tué des joueurs, si oui le nombre et qui il a tué et combien de pomme d’or il lui reste, il peut l'utiliser seulement 2 fois dans la partie.\n" +
                "§e §f\n" +
                "§r→ /ns Izanagi§7, celle-ci lui permet de recevoir 5 pommes d’or et d’être entièrement régénéré, cependant il perd §c1 cœur§7 permanent, et il ne pourra plus utiliser le §rSusano§7, il peut l’utiliser qu’une seule fois.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• §dMadara§7, §dTobi§7 et §6Sasuke§7 peuvent utiliser le \"§rGenjutsu§7\" s’ils le ramassent. \n" +
                "§e §f\n" +
                "§7• S’il se fait ressusciter par §5Orochimaru§7 ou §5Kabuto§7, lorsque §aNaruto§7 sera dans un rayon de 5 blocs autour de lui, un message apparaîtra dans son chat et il passera du côté des §aShinobi§7. \n" +
                "§e §f\n" +
                "§7• Il dispose des effets §bVitesse 1§7, §6Résistance au feu 1§7 et §c2 cœurs§7 supplémentaires. \n" +
                "      \n" +
                "§7• À la mort de §6Sasuke§7 il obtient l’effet §lFaiblesse 1§7 et perd §c2 cœurs§7 permanents. \n" +
                "§e §f\n" +
                "§7• Il connaît l’identité de §cKisame§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §cKaton\n" +
                "§e §f\n" +
                "§7§l▎ Genjutsu :\n" +
                "§e §f\n" +
                "§7Tsukuyomi : Lorsqu’il l’utilise, tous les joueurs se trouvant dans un rayon de 20 blocs autour de lui seront immobilisés pendant 8 secondes, ce pouvoir est utilisable 2 fois dans la partie, il ne peut pas taper les personnes figées.\n" +
                "§e §f\n" +
                "§7Attaque : Lorsqu’il l’utilise, il a une liste de tous les joueurs se trouvant dans un rayon de 20 blocs autour de lui, lorsqu’il choisit un joueur, il sera téléporté derrière sa cible, il possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§7Izanami : Lorsqu'il l’utilise, il a une liste de tous les joueurs se trouvant dans un rayon de 20 blocs autour de lui, lorsqu'il choisit un joueur, il devra accomplir 2 objectifs sur le joueur ciblé et celui-ci devra accomplir 1 objectif dont il ne sera pas au courant (A, B et C), les objectifs sont choisit de manière aléatoire sur deux listes différentes qui se trouve ci-dessous, et à la suite de ces objectifs il rejoindra le camp du joueur qui lui a infligé §rIzanami§7 qu’on va appeler le joueur Izanami. Voici les objectifs qui concernent lui et le joueur ciblé :\n" +
                "§7- Infliger un total de 15 coups d’épée au joueur ciblé\n" +
                "§7- Recevoir un coup d’épée de la part du joueur ciblé\n" +
                "§7- Donner une pomme dorée au joueur ciblé\n" +
                "§7- Forcer le joueur ciblé à lui donner une pomme dorée\n" +
                "§7- Rester à côté du joueur ciblé pendant un total de 5 minutes\n" +
                "§7- Poser un seau de lave sous les pieds du joueur ciblé\n" +
                "§7Ensuite voici les objectifs qui concernent uniquement le joueur ciblé à son égard :\n" +
                "§7- Marcher 1 kilomètre\n" +
                "§7- Tuer un joueur\n" +
                "§7- Manger 5 pommes d’orées\n" +
                "§7Lorsqu’il clique à nouveau sur §rIzanami§7 après l’avoir utilisé une fois, il peut voir l’évolution des objectifs.\n" +
                "§7Une fois §rIzanami§7 terminer, le joueur ciblé rejoindra le camp du joueur Izanami et lui recevra l’effet §0Cécité 1§7 pendant 5 secondes toutes les minutes pendant tout le reste de la partie.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.KISAME);
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Genjutsu")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Susano")).toItemStack());
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {


        if (cible != null && event.getEntity().getUniqueId().equals(cible)) {
            hits++;
            if (hits == 15 && (playerIzanami == FIFTEEN_HIT || playerIzanami2 == FIFTEEN_HIT)) {
                playerAvancement++;
            }

            check();
        }

        if (usingSusano) {
            if (event.getEntity().getFireTicks() <= 0) {
                event.getEntity().setFireTicks(100);
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
                event.setCancelled(true);
                player.getInventory().removeItem(player.getItemInHand());
            }
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Susano")) {

            if (usedIzanagi) {
                player.sendMessage(CC.prefix("§cVous ne pouvez pas utiliser cet item si vous avez utilisé le pouvoir Izanagi."));
                return;
            }

            if (susanoCooldown > 0) {
                player.sendMessage(Messages.cooldown(susanoCooldown));
                return;
            }

            if (Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usingSusano = true;
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 7).setName(Item.specialItem("Epee")).toItemStack());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20 * 60, 0, false, false));
            Tasks.runLater(() -> {
                for (ItemStack content : player.getInventory().getContents()) {
                    if (content != null && content.getType() == Material.DIAMOND_SWORD && content.hasItemMeta() && content.getItemMeta().getDisplayName().equals(Item.specialItem("Epee"))) {
                        player.getInventory().removeItem(content);
                    }
                }
                usingSusano = false;
            }, 5 * 20 * 60);

            susanoCooldown = 20 * 60;

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
            inv.setItem(3, new ItemBuilder(Material.NETHER_STAR).setName("§6Izanami").setLore(
                    "§7Lorsqu'il l’utilise, il a une liste de",
                    "§7tous les joueurs se trouvant dans un rayon",
                    "§7de 20 blocs autour de lui, lorsqu'il choisit",
                    "§7un joueur, il devra accomplir 2 objectifs",
                    "§7sur le joueur ciblé et celui-ci devra accomplir",
                    "§71 objectif dont il ne sera pas au courant",
                    "§7(A, B et C), les objectifs sont choisit",
                    "§7de manière aléatoire sur deux listes",
                    "§7différentes qui se trouve cidessous, et",
                    "§7à la suite de ces objectifs il rejoindra",
                    "§7le camp du joueur qui lui a infligé Izanami",
                    "§7qu’on va appeler le joueur Izanami. Voici",
                    "§7les objectifs qui concernent lui et le",
                    "§7joueur ciblé :",
                    "  §8- §7Infliger un total de 15 coups d’épée",
                    "§7au joueur ciblé",
                    "  §8- §7- Recevoir un coup d’épée de la part",
                    "§7du joueur ciblé",
                    "  §8- §7Donner une pomme dorée au joueur ciblé",
                    "  §8- §7Forcer le joueur ciblé à lui donner",
                    "§7une pomme dorée",
                    "  §8- §7Rester à côté du joueur ciblé pendant",
                    "§7un total de 5 minutes",
                    "  §8- §7Poser un seau de lave sous les pieds du",
                    "§7joueur ciblé",
                    "§7Ensuite voici les objectifs qui concernent",
                    "§7uniquement le joueur ciblé à son égard :",
                    " §8- §7Marcher 1 kilomètre",
                    " §8- §7Tuer un joueur",
                    " §8- §7Manger 5 pommes d’orées",
                    "§7Lorsqu’il clique à nouveau sur Izanami",
                    "§7après l’avoir utilisé une fois, il peut",
                    "§7voir l’évolution des objectifs.",
                    "§7Une fois Izanami terminé, le joueur ciblé",
                    "§7rejoindra le camp du joueur Izanami et",
                    "§7lui recevra §7l’effet Cécité 1 pendant",
                    "§7 5 secondes toutes les minutes pendant",
                    "§7 tout le reste de la partie."
            ).toItemStack());

            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Genjutsu")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                if (Loc.getNearbyPlayers(player, 20).size() == 0) {
                    player.sendMessage(CC.prefix("§cIl n'y a personne autour de vous."));
                    return;
                }

                if (tsukuyomiUses >= 2) {
                    player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 2 fois."));
                    return;
                }

                if (Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

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

            if (event.getSlot() == 3) {
                if (cible != null) {

                    Inventory inv = Bukkit.createInventory(null, 9, "Avancement Izanami");

                    if (Bukkit.getPlayer(cible) == null) return;
                    Player target = Bukkit.getPlayer(cible);

                    List<String> lore = new ArrayList<>();

                    if (this.playerIzanami == FIFTEEN_HIT || playerIzanami2 == FIFTEEN_HIT)
                        lore.add("§715 Coups: §f" + (hits >= 15 ? "§a✔" : hits + "/15"));
                    if (this.playerIzanami == RECEIVE_HIT || playerIzanami2 == RECEIVE_HIT)
                        lore.add("§7Recevoir un coup: §f" + (receivedHit ? "§a✔" : "§c×"));
                    if (this.playerIzanami == GIVE_APPLE || playerIzanami2 == GIVE_APPLE)
                        lore.add("§7Donner une pomme d'or: §f" + (giveGoldenApple ? "§a✔" : "§c×"));
                    if (this.playerIzanami == RECEIVE_APPLE || playerIzanami2 == RECEIVE_APPLE)
                        lore.add("§7Recevoir une pomme d'or: §f" + (receivedGoldenApple ? "§a✔" : "§c×"));
                    if (this.playerIzanami == STAY_FIVE || playerIzanami2 == STAY_FIVE)
                        lore.add("§7Rester 5 minutes: §f" + "§c×");
                    if (this.playerIzanami == LAVA_BUCKET || playerIzanami2 == LAVA_BUCKET)
                        lore.add("§7Poser de la lave: §f" + (placedLava ? "§a✔" : "§c×"));
                    lore.add(" ");
                    if (targetIzanami == WALK_1000)
                        lore.add("§7Marcher 1km: §f" + (kilometers >= 1000 ? "§a✔" : kilometers + "/1000"));
                    if (targetIzanami == KILL)
                        lore.add("§7Tuer un joueur: §f" + (killedSomeone ? "§a✔" : "§c×"));
                    if (targetIzanami == EAT_FIVE_APPLE)
                        lore.add("§7Manger 5 pommes d'or: §f" + (kilometers >= 1000 ? "§a✔" : goldenApplesAte + "/5"));

                    inv.setItem(0, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setLore(lore).setName("§6" + target.getName()).toItemStack());

                    player.openInventory(inv);

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
                Inventory inv = Bukkit.createInventory(null, i, "Izanami");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }
        }


        if (event.getInventory().getName().equals("Izanami")) {
            event.setCancelled(true);

            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            if (usedIzanami) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir"));
                return;
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }


            if (Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usedIzanami = true;
            cible = target.getUniqueId();

            List<IzanamiPlayer> list1 = Arrays.asList(IzanamiPlayer.values());
            Collections.shuffle(list1);
            this.playerIzanami = list1.get(0);
            player.sendMessage(prefix(list1.get(0).getMessage()));
            this.playerIzanami2 = list1.get(1);
            player.sendMessage(prefix(list1.get(1).getMessage()));

            List<IzanamiTarget> list2 = Arrays.asList(IzanamiTarget.values());
            Collections.shuffle(list2);
            this.targetIzanami = list2.get(0);
            player.sendMessage(prefix(list2.get(0).getMessage()));


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


            if (Kisame.isBlocked(player)) {
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

    }

    @Override
    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {
        if (cannotMove.contains(entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if (cannotMove.contains(damager.getUniqueId())) {
            event.setCancelled(true);
        }

        if (cible != null && cible.equals(event.getDamager().getUniqueId()) && event.getEntity().getUniqueId().equals(getPlayer().getUniqueId())) {
            this.receivedHit = true;
            if (playerIzanami == RECEIVE_HIT || playerIzanami2 == RECEIVE_HIT)
                this.playerAvancement++;
            check();
        }
    }

    @Override
    public void onAllPlayerPickupItem(PlayerPickupItemEvent event, Player player) {
        if (cible != null && cible.equals(player.getUniqueId())) {
            giveGoldenApple = true;
            if (playerIzanami == GIVE_APPLE || playerIzanami2 == GIVE_APPLE)
                this.playerAvancement++;
            check();
        }
    }

    @Override
    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            kilometers++;
            if (kilometers == 1000) {
                if (targetIzanami == WALK_1000)
                    cibleManaged = true;
                check();
            }
        }
    }

    @Override
    public void onAllPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
        if (cible != null && cible.equals(player.getUniqueId())) {
            if (event.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE) {
                goldenApplesAte++;
                if (goldenApplesAte == 5) {
                    if (targetIzanami == EAT_FIVE_APPLE)
                        cibleManaged = true;
                    check();
                }
            }
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        if (event.getEntity().getKiller() != null && cible != null && cible.equals(event.getEntity().getKiller().getUniqueId())) {
            killedSomeone = true;
            if (targetIzanami == KILL)
                cibleManaged = true;
            check();
        }

        Player itachi = Role.findPlayer(NarutoRoles.ITACHI);
        if (itachi == null) return;

        if (Role.isRole(player, NarutoRoles.SASUKE)) {
            itachi.sendMessage(CC.prefix("§cSasuke §fest mort. Vous obtenez un effet de §7Faiblesse 1 §fet vous perdez §c2 coeurs §fpermanent."));
            itachi.setMaxHealth(itachi.getMaxHealth() - 4);
            itachi.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    @Override
    public void onSubCommand(Player player, String[] args) {

        if (args[0].equalsIgnoreCase("izanagi")) {

            if (usedIzanagi) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            if (Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aIzanagi§f."));

            player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).toItemStack());
            player.setHealth(player.getMaxHealth());
            player.setMaxHealth(player.getMaxHealth() - 2);
            usedIzanagi = true;
        }

        if (args[0].equalsIgnoreCase("sharingan")) {
            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns sharingan <player>"));
                return;
            }

            if (sharinganUses >= 2) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 2 fois."));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if (Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(CC.CC_BAR);
            if (manager.getDeath().keySet().stream().noneMatch(s -> manager.getDeath().get(s).equals(target.getUniqueId()))) {
                player.sendMessage(CC.prefix("§c" + target.getName() + " §fn'a tué §cpersonne§f."));
            }
            manager.getDeath().keySet().forEach(s -> {
                if (manager.getDeath().get(s).equals(target.getUniqueId())) {
                    player.sendMessage(CC.prefix("§c" + target.getName() + " §fa tué §a" + s));
                }
            });
            if (roleManager.getRole(target) == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'a pas de rôle"));
            } else {
                player.sendMessage(CC.prefix("§a" + target.getName() + " §fest §a" + roleManager.getRole(target).getRoleName()));
            }

            int apple = 0;

            for (ItemStack content : target.getInventory().getContents()) {
                if (content != null && content.getType() == Material.GOLDEN_APPLE) {
                    apple += content.getAmount();
                }
            }

            player.sendMessage(CC.prefix("§a" + target.getName() + " §fa un total de §6" + apple + " §fpommes d'ors."));
            player.sendMessage(CC.CC_BAR);
            sharinganUses++;

        }
    }

    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event, Player player) {
        if (cible == null || Bukkit.getPlayer(cible) == null) return;
        if (event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().distance(Bukkit.getPlayer(cible).getLocation()) <= 2) {
            placedLava = true;
            if (playerIzanami == LAVA_BUCKET || playerIzanami2 == LAVA_BUCKET)
                playerAvancement++;
            check();
        }
    }

    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event, Player player) {
        if (cible != null) {
            if(event.getPlayer().getItemInHand().getType().equals(Material.GOLDEN_APPLE)) {
                if (playerIzanami == RECEIVE_APPLE || playerIzanami2 == RECEIVE_APPLE)
                    playerAvancement++;
                check();
            }
        }
    }

    public void check() {

        if (managed) return;

        if (this.playerAvancement >= 2 && cibleManaged) {
            managed = true;
            Bukkit.getPlayer(cible).sendMessage(prefix("&fVous passez dans le camp de l'§cAkatsuki§f."));
            getPlayer().sendMessage(prefix("&a" + Bukkit.getPlayer(cible).getName() + " §fa rejoint votre camp."));
            roleManager.setCamp(cible, Camp.AKATSUKI);
        }
    }

    public enum IzanamiPlayer {
        FIFTEEN_HIT("Vous devez mettre 15 coups à votre cible"),
        RECEIVE_HIT("Vous devez recevoir un coup de votre cible"),
        GIVE_APPLE("Vous devez donner une pomme d'or à votre cible"),
        RECEIVE_APPLE("Vous devez recevoir une pomme d'or de votre cible"),
        STAY_FIVE("Vous devez rester 5 minutes à côté de votre cible"),
        LAVA_BUCKET("Vous devez poser un seau de lave sous votre cible");

        private final String message;

        IzanamiPlayer(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum IzanamiTarget {
        WALK_1000("La cible doit marcher 1000 blocks"),
        KILL("La cible doit tuer quelqu'un"),
        EAT_FIVE_APPLE("La cible doit manger 5 pommes d'or");

        private final String message;

        IzanamiTarget(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Nagato extends NarutoRole {

    public int banshoCooldown = 0;
    public int shinraCooldown = 0;
    public boolean usedOsama = false;
    public List<UUID> latestDeaths = new ArrayList<>();
    private int sharinganUses = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.NAGATO;
    }

    @Override
    public void resetCooldowns() {
        banshoCooldown = 0;
        shinraCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (banshoCooldown > 0) {
            banshoCooldown--;
        }

        if (shinraCooldown > 0) {
            shinraCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Nagato";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §cNagato\n" +
                "§7▎ Objectif: §rSon but est de gagner avec l'§cAkatsuki\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item “§rChibaku Tensei§7”, à son utilisation, il est dirigé vers un menu dans lequel il peut utiliser deux pouvoirs différents :\n" +
                "§e §f\n" +
                "§7Banshô Ten’in : Lorsqu’il l’utilise, il dispose d’une liste de tous les joueurs dans un rayon de 30 blocs autour de lui, en cliquant sur l’un des joueurs celui-ci sera téléporté instantanément à sa position, son pouvoir possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§7Shinra Tensei : Lorsqu’il l’utilise, tous les joueurs et projectiles dans un rayon de 20 blocs seront propulsés à l’opposé de sa position, ils sont propulsés d’environ 10 blocs, son pouvoir possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item “§rOiseau§7”, celui-ci lui permet de voler pendant 10 secondes, il possède un délai de 20 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns sharingan <Joueur>§7, celle-ci lui permet d’avoir toutes les informations possible à propos du joueur : Son rôle, s’il a tué des joueurs, si oui qui il a tué ainsi que le nombre et combien de pomme d’or lui reste-t’il, il peut l'utiliser seulement 2 fois dans la partie.\n" +
                "§e §f\n" +
                "§r→ /ns osama§7, celui-ci lui permet de ressusciter tous les joueurs morts depuis 10 minutes (ils faut que les joueurs à ressusciter soient toujours connecter) cependant 1 minute après l’utilisation de sa commande, il meurt.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités : \n" +
                "§e §f\n" +
                "§7• Il dispose de la liste de tous les membres de l'§cAkatsuki§7. Mais attention §dObito§7 fait parti de cette liste.  \n" +
                "§e §f\n" +
                "§7• En restant à côté des joueurs, il peut augmenter leurs délai, toutes les 5 secondes proches d’un joueur augmente le délai des pouvoir du joueur de 1 seconde, cela ne fonctionne pas sur les membres de l’§cAkatsuki§7.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il reçoit un coup, il dispose de 5% de chance de recevoir l’effet §9Résistance 1§7 pendant 25 secondes,\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §bVitesse 1§7, §cForce 1§7 et §c2 cœurs§7 supplémentaires.\n" +
                "§e §f\n" +
                "§7• Lorsqu’il n’est pas en combat (ne reçoit/inflige aucun dégâts dans les 30 dernières secondes), il reçoit l’effet §dRégénération 1§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §9Suiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 4);

        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Chibaku Tensei")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Oiseau")).toItemStack());

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

        if (Item.interactItem(event.getItem(), "Oiseau")) {
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.setAllowFlight(true);
            player.setFlying(true);

            player.sendMessage(CC.prefix("§fVous pouvez §avoler §fpendant §a10 secondes§f."));

            Tasks.runLater(() -> {
                player.setAllowFlight(false);
                Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 10);
                player.sendMessage(CC.prefix("§fVous ne pouvez plus §avoler§f."));
            }, 10 * 20);

        }

        if (Item.interactItem(event.getItem(), "Chibaku Tensei")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Chibaku Tensei");

            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").toItemStack());
            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("§6Banshô Ten’in").setLore(
                    "§7Lorsqu’il l’utilise, il dispose d’une",
                    "§7liste de tous les joueurs dans un rayon",
                    "§7de 30 blocs autour de lui, en cliquant sur",
                    "§7l’un des joueurs celui-ci sera téléporté",
                    "§7instantanément à sa position, son pouvoir",
                    "§7possède un délai de 5 minutes."
            ).toItemStack());
            inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Shinra Tensei").setLore(
                    "§7Lorsqu’il l’utilise, tous les joueurs et",
                    "§7projectiles dans un rayon de 20 blocs",
                    "§7seront propulsés à l’opposé de sa position",
                    "§7ils sont propulsés d’environ 10 blocs, son",
                    "§7pouvoir possède un délai de 5 minutes."
            ).toItemStack());

            player.openInventory(inv);
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if (!latestDeaths.contains(player.getUniqueId())) {
            latestDeaths.add(player.getUniqueId());
            final UUID uuid;
            uuid = player.getUniqueId();
            Tasks.runLater(() -> {
                latestDeaths.remove(uuid);
            }, 10 * 20 * 60);
        }
    }

    private void giveStuff(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.LOG, 64));
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());

        if (roleManager.getRole(player) != null) {
            roleManager.getRole(player).onDistribute(player);
        }

    }


    @Override
    public void onSubCommand(Player player, String[] args) {

        if (args[0].equalsIgnoreCase("osama")) {

            if (usedOsama) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usedOsama = true;

            latestDeaths.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).forEach(target -> {
                player.sendMessage(CC.prefix("§fVous avez ressuscité §a" + target.getName()));

                target.teleport(player);
                target.setGameMode(GameMode.SURVIVAL);
                giveStuff(target);
                UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
            });

            Tasks.runLater(() -> player.setHealth(0), 60 * 20);

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

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
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
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Chibaku Tensei")) {
            event.setCancelled(true);
            if (event.getSlot() == 1) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 30)) {
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
                for (Player entity : Loc.getNearbyPlayers(player, 30)) {
                    inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                    j++;
                }
                player.openInventory(inv);
            }

            if (event.getSlot() == 2) {

                if (shinraCooldown > 0) {
                    player.sendMessage(Messages.cooldown(shinraCooldown));
                    return;
                }

                if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                    return;
                }
                NarutoUHC.usePower(player);

                player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aShinra Tensei§f."));

                for (Player entity : Loc.getNearbyPlayers(player, 20)) {
                    Location initialLocation = player.getLocation().clone();
                    Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(initialLocation.toVector());
                    fromPlayerToTarget.multiply(4); //6
                    fromPlayerToTarget.setY(1); // 2
                    entity.setVelocity(fromPlayerToTarget);
                }

                shinraCooldown = 5 * 60;

            }

        }

        if (event.getInventory().getName().equals("Banshô Ten’in")) {
            event.setCancelled(true);

            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            if (banshoCooldown > 0) {
                player.sendMessage(Messages.cooldown(banshoCooldown));
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            target.teleport(player);
            player.sendMessage(CC.prefix("§fVous avez téléporté §a" + target.getName() + " §fà vous."));
            target.sendMessage(CC.prefix("§cNagato §fvous a téléporté sur lui."));

            banshoCooldown = 5 * 60;

        }

    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        int random = (int) (Math.random() * 20);

        if (random == 2) {
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 25 * 20, 0, false, false));
            player.sendMessage(CC.prefix("§fVous avez eu de la §achance§f. Vous obtenez §aRésistance §fpendant 25 secondes."));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }
}

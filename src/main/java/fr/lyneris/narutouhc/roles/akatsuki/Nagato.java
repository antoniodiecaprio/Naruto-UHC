package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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
    private int sharinganUses = 0;
    public boolean usedOsama = false;
    public List<UUID> latestDeaths = new ArrayList<>();

    @Override
    public void resetCooldowns() {
        banshoCooldown = 0;
        shinraCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(banshoCooldown > 0) {
            banshoCooldown--;
        }

        if(shinraCooldown > 0) {
            shinraCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Nagato";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 4);

        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Chibaku Tensei")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Oiseau")).toItemStack());

        List<String> list = new ArrayList<>();
        //TODO CHECK CAMP
        UHC.getUHC().getGameManager().getPlayers().stream().filter(e -> Bukkit.getPlayer(e) != null).map(e -> Bukkit.getPlayer(e).getName()).forEach(list::add);
        if(Role.findPlayer(NarutoRoles.OBITO) != null) list.add(Role.findPlayer(NarutoRoles.OBITO).getName());

        player.sendMessage(CC.prefix("§cListe des Akatsuki:"));
        list.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.interactItem(event.getItem(), "Oiseau")) {
            player.setAllowFlight(true);
            player.setFlying(true);

            player.sendMessage(CC.prefix("§fVous pouvez §avoler §fpendant §a10 secondes§f."));

            Tasks.runLater(() -> {
                player.setAllowFlight(false);
                Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 10);
                player.sendMessage(CC.prefix("§fVous ne pouvez plus §avoler§f."));
            }, 10*20);

        }

        if(Item.interactItem(event.getItem(), "Chibaku Tensei")) {
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
        if(!latestDeaths.contains(player.getUniqueId())) {
            latestDeaths.add(player.getUniqueId());
            final UUID uuid;
            uuid = player.getUniqueId();
            Tasks.runLater(() -> {
                latestDeaths.remove(uuid);
            }, 10*20*60);
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

        if(roleManager.getRole(player) != null) {
            roleManager.getRole(player).onDistribute(player);
        }

    }


    @Override
    public void onSubCommand(Player player, String[] args) {

        if(args[0].equalsIgnoreCase("osama")) {

            if(usedOsama) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            usedOsama = true;

            latestDeaths.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).forEach(target -> {
                player.sendMessage(CC.prefix("§fVous avez ressuscité §a" + target.getName()));

                target.teleport(player);
                target.setGameMode(GameMode.SURVIVAL);
                giveStuff(target);
                UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
            });

            Tasks.runLater(() -> player.setHealth(0), 60*20);

        }

        if(args[0].equalsIgnoreCase("sharingan")) {
            if(args.length != 2) {
                player.sendMessage(Messages.syntax("/ns sharingan <player>"));
                return;
            }

            if(sharinganUses >= 2) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir 2 fois."));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            player.sendMessage(CC.CC_BAR);
            if(manager.getDeath().keySet().stream().noneMatch(s -> manager.getDeath().get(s).equals(target.getUniqueId()))) {
                player.sendMessage(CC.prefix("§c" + target.getName() + " §fn'a tué §cpersonne§f."));
            }
            manager.getDeath().keySet().forEach(s -> {
                if(manager.getDeath().get(s).equals(target.getUniqueId())) {
                    player.sendMessage(CC.prefix("§c" + target.getName() + " §fa tué §a" + s));
                }
            });
            if(roleManager.getRole(target) == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'a pas de rôle"));
            } else {
                player.sendMessage(CC.prefix("§a" + target.getName() + " §fest §a" + roleManager.getRole(target).getRoleName()));
            }

            int apple = 0;

            for (ItemStack content : target.getInventory().getContents()) {
                if(content != null && content.getType() == Material.GOLDEN_APPLE) {
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
        if(event.getInventory().getName().equals("Chibaku Tensei")) {
            event.setCancelled(true);
            if(event.getSlot() == 1) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Player ignored : Loc.getNearbyPlayers(player, 30)) {
                    nearbyPlayer++;
                }
                if(nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if(nearbyPlayer > 17) {
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

            if(event.getSlot() == 2) {

                if(shinraCooldown > 0) {
                    player.sendMessage(Messages.cooldown(shinraCooldown));
                    return;
                }

                player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aShinra Tensei§f."));

                for(Player entity : Loc.getNearbyPlayers(player, 20)) {
                    Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
                    entity.setVelocity(new Vector(0, 0.3, 0));
                    fromPlayerToTarget.multiply(6);
                    fromPlayerToTarget.setY(2);
                    entity.setVelocity(fromPlayerToTarget.normalize());
                }

                shinraCooldown = 5*60;

            }

        }

        if(event.getInventory().getName().equals("Banshô Ten’in")) {
            if(!event.getCurrentItem().hasItemMeta()) return;
            if(event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            event.setCancelled(true);
            
            if(banshoCooldown > 0) {
                player.sendMessage(Messages.cooldown(banshoCooldown));
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if(target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            target.teleport(player);
            player.sendMessage(CC.prefix("§fVous avez téléporté §a" + target.getName() + " §fà vous."));
            target.sendMessage(CC.prefix("§cNagato §fvous a téléporté sur lui."));

            banshoCooldown = 5*60;

        }

    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        int random = (int) (Math.random() * 20);

        if(random == 2) {
            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 25*20, 0, false, false));
            player.sendMessage(CC.prefix("§fVous avez eu de la §achance§f. Vous obtenez §aRésistance §fpendant 25 secondes."));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }
}

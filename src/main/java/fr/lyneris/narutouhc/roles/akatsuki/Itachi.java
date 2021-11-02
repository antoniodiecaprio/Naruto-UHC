package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

public class Itachi extends NarutoRole {

    public List<UUID> cannotMove = new ArrayList<>();
    public int tsukuyomiUses = 0;
    public int attaqueCooldown = 0;
    public boolean usingSusano = false;
    public int swordCooldown = 0;
    public int susanoCooldown = 0;
    public int sharinganUses = 0;
    public boolean usedIzanagi = false;

    @Override
    public void resetCooldowns() {
        attaqueCooldown = 0;
        swordCooldown = 0;
        susanoCooldown = 0;
    }

    @Override
    public void startRunnableTask() {
        if(attaqueCooldown > 0) {
            attaqueCooldown--;
        }

        if(swordCooldown > 0) {
            swordCooldown--;
        }

        if(susanoCooldown > 0) {
            susanoCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Itachi";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, "Kisame");
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Genjutsu")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Susano")).toItemStack());
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(usingSusano) {
            if(event.getEntity().getFireTicks() <= 0) {
                event.getEntity().setFireTicks(100);
            }
        }

        if(Item.specialItem(player.getItemInHand(), "Epee")) {
            if(usingSusano) {
                if(swordCooldown > 0) {
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

        if(Item.interactItem(event.getItem(), "Susano")) {

            if(usedIzanagi) {
                player.sendMessage("§7▎ §cVous ne pouvez pas utiliser cet item si vous avez utilisé le pouvoir Izanagi.");
                return;
            }

            if(susanoCooldown > 0) {
                player.sendMessage(Messages.cooldown(susanoCooldown));
                return;
            }

            usingSusano = true;
            player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 7).setName(Item.specialItem("Epee")).toItemStack());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*20*60, 0, false, false));
            Bukkit.getScheduler().runTaskLater(narutoUHC, () -> {
                for (ItemStack content : player.getInventory().getContents()) {
                    if(content != null && content.getType() == Material.IRON_SWORD && content.hasItemMeta() && content.getItemMeta().getDisplayName().equals(Item.specialItem("Epee"))) {
                        player.getInventory().removeItem(content);
                    }
                }
                usingSusano = false;
            }, 5*20*60);

            susanoCooldown = 20*60;

        }

        if(Item.interactItem(event.getItem(), "Genjutsu")) {
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
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if(event.getInventory().getName().equals("Genjutsu")) {
            event.setCancelled(true);
            if(event.getSlot() == 1) {
                if(player.getNearbyEntities(20, 20, 20).stream().filter(e -> e instanceof Player).toArray().length == 0) {
                    player.sendMessage("§7▎ §cIl n'y a personne autour de vous.");
                    return;
                }

                if(tsukuyomiUses >= 2) {
                    player.sendMessage("§7▎ §cVous avez déjà utilisé ce pouvoir 2 fois.");
                    return;
                }

                tsukuyomiUses++;

                player.getNearbyEntities(20, 20, 20).stream().filter(e -> e instanceof Player).map(e -> (Player)e).forEach(target -> {
                    cannotMove.add(target.getUniqueId());
                    target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8*20, 200, false, false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8*20, 200, false, false));
                    player.sendMessage("§7▎ §fVous avez immobilisé §c" + target.getName());
                });
                Bukkit.getScheduler().runTaskLater(narutoUHC, () -> cannotMove.clear(), 8*20);
                player.closeInventory();
            }

            if(event.getSlot() == 2) {
                int i = 9;
                int nearbyPlayer = 0;
                for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                    if (entity instanceof Player) {
                        nearbyPlayer++;
                    }
                }
                if(nearbyPlayer > 8 && nearbyPlayer <= 17) {
                    i = 18;
                } else if(nearbyPlayer > 17) {
                    i = 27;
                }
                Inventory inv = Bukkit.createInventory(null, i, "Attaque");
                int j = 1;
                inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
                for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                    if (entity instanceof Player) {
                        inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + entity.getName()).setSkullOwner(entity.getName()).toItemStack());
                        j++;
                    }
                }
                player.openInventory(inv);
            }
        }

        if(event.getInventory().getName().equals("Attaque")) {
            if(!event.getCurrentItem().hasItemMeta()) return;
            if(event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            event.setCancelled(true);

            if(attaqueCooldown > 0) {
                player.sendMessage(Messages.cooldown(attaqueCooldown));
                return;
            }

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if(target == null) {
                player.sendMessage("§7▎ §cCe joueur n'est pas connecté");
                return;
            }

            Location playerLocation = player.getLocation();
            Location damagerLocation = target.getLocation();

            Vector swap = damagerLocation.toVector()
                    .subtract(playerLocation.toVector()).normalize();
            Location targetLocation = damagerLocation.clone().add(swap);
            targetLocation.setY(target.getLocation().getY());

            player.teleport(targetLocation);

            player.sendMessage("§7▎ §fVous vous êtes téléporté derrière §a" + target.getName());
            attaqueCooldown = 5*60;

        }

    }

    @Override
    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {
        if(cannotMove.contains(entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if(cannotMove.contains(damager.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        Player itachi = Role.findPlayer("Itachi");
        if(itachi == null) return;

        if(Role.isRole(player, "Sasuke")) {
            itachi.sendMessage("§7▎ §cSasuke §fest mort. Vous obtenez un effet de §7Faiblesse 1 §fet vous perdez §c2 coeurs §fpermanent.");
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

        if(args[0].equalsIgnoreCase("izanagi")) {

            if(usedIzanagi) {
                player.sendMessage("§7▎ §cVous avez déjà utilisé ce pouvoir.");
                return;
            }

            player.sendMessage("§7▎ §fVous avez utilisé votre pouvoir §aIzanagi§f.");

            player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).toItemStack());
            player.setHealth(player.getMaxHealth());
            player.setMaxHealth(player.getMaxHealth()-2);
            usedIzanagi = true;
        }

        if(args[0].equalsIgnoreCase("sharingan")) {
            if(args.length != 2) {
                player.sendMessage(Messages.syntax("/ns sharingan <player>"));
                return;
            }

            if(sharinganUses >= 2) {
                player.sendMessage("§7▎ §cVous avez déjà utilisé ce pouvoir 2 fois.");
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            player.sendMessage("§8§m-------------------------------");
            if(manager.getDeath().keySet().stream().noneMatch(s -> manager.getDeath().get(s).equals(target.getUniqueId()))) {
                player.sendMessage("§7▎ §c" + target.getName() + " §fn'a tué §cpersonne§f.");
            }
            manager.getDeath().keySet().forEach(s -> {
                if(manager.getDeath().get(s).equals(target.getUniqueId())) {
                    player.sendMessage("§7▎ §c" + target.getName() + " §fa tué §a" + s);
                }
            });
            if(roleManager.getRole(target) == null) {
                player.sendMessage("§7▎ §cCe joueur n'a pas de rôle");
            } else {
                player.sendMessage("§7▎ §a" + target.getName() + " §fest §a" + roleManager.getRole(target).getRoleName());
            }

            int apple = 0;

            for (ItemStack content : target.getInventory().getContents()) {
                if(content != null && content.getType() == Material.GOLDEN_APPLE) {
                    apple += content.getAmount();
                }
            }

            player.sendMessage("§7▎ §a" + target.getName() + " §fa un total de §6" + apple + " §fpommes d'ors.");
            player.sendMessage("§8§m-------------------------------");
            sharinganUses++;

        }
    }
}

package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Kabuto extends NarutoRole {

    public int givePower1 = -1;
    public int givePower2 = -1;
    public int jutsuCooldown = 0;
    public boolean usingJutsu = false;

    public NarutoRoles getRole() {
        return NarutoRoles.KABUTO;
    }

    @Override
    public void resetCooldowns() {
        jutsuCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (jutsuCooldown > 0) {
            jutsuCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kabuto";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §5Kabuto\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp d'§5Orochimaru\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’item nommé “§rSenpô§7”, celui-ci lui permet, lors se son utilisation, d’immobiliser et aveugler tous les joueurs dans un rayon de 15 blocs autour de lui. Ils reçoivent donc les effets §0Cécité 1§7 et §8Lenteur 4§7 pendant 10 secondes.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns shosenjutsu <Joueur>§7, cette commande permet de donner l’effet §9Régénération 3§7 au joueur ciblé pendant 20 secondes. Il doit se situer à 5 blocs de la personne. La commande possède un délai de 10 minutes, il ne peut pas l’utiliser sur lui-même.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §bVitesse 1§7, §cForce 1§7 et il régénère §c1 cœur§7 par minute. \n" +
                "§e §f\n" +
                "§7• Deux fois par épisode, à un moment aléatoire, il reçoit l’effet §0Célérité 1§7 pendant 5 secondes.\n" +
                "§e §f\n" +
                "§7• Il a 5% de chance lorsqu’il tape un joueur, d’étourdir celui-ci, lorsque le joueur est étourdi, il reçoit les effets §0Cécité 1§7 et §8Lenteur 4§7 pendant 3 secondes.\n" +
                "§e §f\n" +
                "§7• À la mort d’§5Orochimaru§7, il perd l’effet §bVitesse 1§7 et reçoit l’effet §9Résistance 1§7 ainsi que l’item “§rEdo Tensei§7” (voir fiche §5Orochimaru§7).\n" +
                "§e §f\n" +
                "§7• Il connaît l’identité de §5Kimimaro§7 et d’§5Orochimaru§7.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §9Suiton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.KIMIMARO);
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senpô")).toItemStack());
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Edo Tensei")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            giveStuff(target);
            UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
            roleManager.setCamp(target.getUniqueId(), Camp.OROCHIMARU);

        }
    }

    public void giveStuff(Player player) {
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
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Edo Tensei")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            Inventory inv = Bukkit.createInventory(null, 18, "Edo Tensei");
            int j = 1;
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());
            for (UUID uuid : NarutoUHC.getNaruto().getManager().getDeathLocation().keySet()) {
                if (Bukkit.getPlayer(uuid) != null) {
                    if (NarutoUHC.getNaruto().getManager().getDeathLocation().get(uuid).distance(player.getLocation()) <= 20) {
                        if (!UHC.getUHC().getGameManager().getPlayers().contains(uuid)) {
                            Player target = Bukkit.getPlayer(uuid);
                            inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setName("§6" + target.getName()).setSkullOwner(target.getName()).toItemStack());
                            j++;
                        }
                    }
                }
            }
            player.openInventory(inv);
        }

        if (Item.interactItem(event.getItem(), "Senpô")) {
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            Loc.getNearbyPlayers(player, 15).forEach(target -> {
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0, false, false));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 3, false, false));
                target.sendMessage(CC.prefix("§cKabuto §fa utilisé son §aSenpô §fsur vous."));
            });
            player.sendMessage(CC.prefix("§fVous avez utilisé votre §aSenpô"));
        }
    }

    @Override
    public void onSecond(int timer, Player player) {
        if (timer == givePower1 || timer == givePower2) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 5 * 20, 0, false, false));
            player.sendMessage(CC.prefix("§fVous venez d'obtenir l'effet §eCélérité §fpendant §a5 secondes§f."));
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (!args[0].equalsIgnoreCase("shosenjutsu")) return;

        if (jutsuCooldown > 0) {
            player.sendMessage(Messages.cooldown(jutsuCooldown));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            player.sendMessage(Messages.offline(args[2]));
            return;
        }

        if (Objects.equals(target.getName(), player.getName())) {
            player.sendMessage(CC.prefix("§cVous ne pouvez pas utiliser ce pouvoir sur vous-même."));
            return;
        }

        if (target.getLocation().distance(player.getLocation()) > 5) {
            player.sendMessage(CC.prefix("§cCe joueur n'est pas à 5 blocks de vous."));
            return;
        }

        if(Kisame.isBlocked(player)) {
            player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
            return;
        }
        NarutoUHC.usePower(player);
        usingJutsu = true;

        player.sendMessage(CC.prefix("§fVous avez utilisé votre §aJutsu §fsur §a" + target.getName()));
        target.sendMessage(CC.prefix("§aNaruto §fa utilisé son §aJutsu §fsur vous."));
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*20, 2, false, false));

        jutsuCooldown = 10 * 60;

    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (!(event.getEntity() instanceof Player)) return;
        int random = (int) (Math.random() * 20);
        if (random == 2) {
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, false));
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 3, false, false));
            player.sendMessage(CC.prefix("§fVous n'avez pas eu de chance... §cKabuto §fvous a mit un coup vous obtenez §7Cécité §fet §7Lenteur§f."));
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        Player kabuto = Role.findPlayer(NarutoRoles.KABUTO);
        if (kabuto == null) return;
        if (!Role.isRole(player, NarutoRoles.OROCHIMARU)) return;

        kabuto.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Edo Tensei")).toItemStack());

    }

    @Override
    public void onNewEpisode(Player player) {
        givePower1 = (int) (Math.random() * 20 * 60);
        givePower2 = (int) (Math.random() * 20 * 60);
    }

    @Override
    public void onMinute(int minute, Player player) {

        player.setHealth(Math.min(player.getHealth() + 2, player.getMaxHealth()));
    }
    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}

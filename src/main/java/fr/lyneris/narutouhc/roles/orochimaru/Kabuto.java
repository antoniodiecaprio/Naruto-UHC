package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
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
    private Player jutsuPlayer = null;

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
    public List<String> getDescription() {
        return new ArrayList<>();
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
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            event.setCancelled(true);

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

        if (!args[1].equalsIgnoreCase("jutsu")) return;

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

        usingJutsu = true;
        jutsuPlayer = target;

        player.sendMessage(CC.prefix("§fVous avez utilisé votre §aJutsu §fsur §a" + target.getName() + "§f. Si un de vous deux bouge, §a" + target.getName() + " §fperdra son effet de §dRégénération§f."));
        target.sendMessage(CC.prefix("§aNaruto §fa utilisé son §aJutsu §fsur vous. Si un de vous deux bouge vous perdrez votre effet de §dRégénération§f."));
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2, false, false));

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
    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {

        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        Player sakura = Role.findPlayer(NarutoRoles.SAKURA);
        Player jutsu = jutsuPlayer;

        if (!event.getPlayer().equals(sakura) && !event.getPlayer().equals(jutsu)) return;

        if (jutsu == null || sakura == null) return;
        if (!usingJutsu) return;

        jutsu.removePotionEffect(PotionEffectType.REGENERATION);
        jutsu.sendMessage(CC.prefix("§a" + event.getPlayer().getName() + " §fa bougé."));
        sakura.sendMessage(CC.prefix("§a" + event.getPlayer().getName() + " §fa bougé."));
        usingJutsu = false;
        jutsuPlayer = null;

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

package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Sasori extends NarutoRole {

    public PotionEffectType[] positivePotionEffects = new PotionEffectType[] {
            PotionEffectType.SPEED,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.INCREASE_DAMAGE,
            PotionEffectType.JUMP,
            PotionEffectType.REGENERATION,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.INVISIBILITY,
    };
    private final HashMap<UUID, PotionEffectType> map = new HashMap<>();

    @Override
    public String getRoleName() {
        return "Sasori";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.setMaxHealth(30);
        Role.knowsRole(player, NarutoRoles.DEIDARA);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marionnettisme")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(Item.interactItem(event.getItem(), "Marionnettisme")) {
            List<Player> killed = new ArrayList<>();
            manager.getDeath().keySet().stream()
                    .filter(s ->  manager.getDeath().get(s).equals(player.getUniqueId()))
                    .filter(s -> Bukkit.getPlayer(s) != null)
                    .map(Bukkit::getPlayer).forEach(killed::add);

            int i;

            if(killed.size() <= 8) {
                i = 9;
            } else if(killed.size() <= 17) {
                i = 18;
            } else {
                i = 27;
            }

            Inventory inv = Bukkit.createInventory(null, i, "Marionnettisme");

            int j = 1;

            for (Player player1 : killed) {
                inv.setItem(j, new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setSkullOwner(player1.getName()).setName("§6" + player1.getName()).toItemStack());
                j++;
            }

            player.openInventory(inv);

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


        player.setMaxHealth(6 * 2);

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if(event.getInventory().getName().equals("Marionnettisme")) {
            event.setCancelled(true);
            if(event.getCurrentItem().getType() != Material.SKULL_ITEM) return;
            if(!event.getCurrentItem().hasItemMeta()) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if(target == null) {
                player.sendMessage(Messages.offline(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "")));
                return;
            }

            if(UHC.getUHC().getGameManager().getPlayers().contains(target.getUniqueId())) {
                player.sendMessage(CC.prefix("§cCe joueur est en vie."));
                return;
            }

            UHC.getUHC().getGameManager().getPlayers().add(target.getUniqueId());
            target.setGameMode(GameMode.SURVIVAL);
            target.teleport(player.getLocation());
            giveStuff(target);
            //TODO CHANGER LE CAMP
            target.setMaxHealth(12);
            player.setMaxHealth(player.getMaxHealth()-2);

            List<PotionEffectType> list = new ArrayList<>(Arrays.asList(positivePotionEffects));
            Collections.shuffle(list);
            map.put(target.getUniqueId(), list.get(0));

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir sur §a" + target.getName() + "§f."));
            target.sendMessage(CC.prefix("§fVous avez été ressuscité par §aSasori§f."));

        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, Player player) {
        Player sasori = Role.findPlayer(NarutoRoles.SASORI);
        if(sasori == null) return;
        if(!map.containsKey(player.getUniqueId())) return;

        PotionEffectType randomEffect = map.get(player.getUniqueId());

        if(sasori.getLocation().distance(player.getLocation()) <= 20) {
            if(randomEffect == PotionEffectType.SPEED) {
                player.addPotionEffect(new PotionEffect(randomEffect, 3*20, 1, false, false));
                return;
            }
            if(randomEffect == PotionEffectType.JUMP) {
                player.addPotionEffect(new PotionEffect(randomEffect, 3*20, 3, false, false));
                return;
            }
            player.addPotionEffect(new PotionEffect(randomEffect, 3*20, 0, false, false));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }
}

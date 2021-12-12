package fr.lyneris.narutouhc.roles.sankyodai;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kankuro extends NarutoRole {
    @Override
    public String getRoleName() {
        return "Kankurô";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.KANKURO;
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.GAARA);
        Role.knowsRole(player, NarutoRoles.TEMARI);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marionnettes")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event, "Marionnettes")) {
            Inventory inv = Bukkit.createInventory(null, 9, "Marionnettes");
            int i = 0;
            for (String s : manager.getDeath().keySet()) {
                if (manager.getDeath().get(s).equals(player.getUniqueId())) {
                    if (Bukkit.getPlayer(s) != null) {
                        inv.setItem(i, new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setName("§6" + s).toItemStack());
                        i++;
                    }
                }
            }
            player.openInventory(inv);
        }
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equalsIgnoreCase("Marionnettes")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getType() != Material.SKULL_ITEM) return;

            Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6", ""));

            if (target == null) {
                player.sendMessage(CC.prefix("§cCe joueur n'est pas connecté"));
                return;
            }

            Inventory inv = Bukkit.createInventory(null, 9, "Marionnettes " + target.getName());
            inv.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack());

            inv.setItem(1, new ItemBuilder(Material.NETHER_STAR).setName("Karasu").setLore(
                    "§7Le joueur choisit disposera d’un item",
                    "§7nommé 'Shuryûdan', celui-ci lui permet",
                    "§7de créer une zone de 10 blocs qui",
                    "§7empoisonnera pendant 10 secondes tous",
                    "§7les joueurs présents dans la zone, cette",
                    "§7zone est limité avec des particules, ce",
                    "§7pouvoir dure 1 minute et possède un délai",
                    "§7de 10 minutes. Ce joueur disposera aussi",
                    "§7de la particularité, avec 20 % de chance,",
                    "§7d’infliger poison pendant 5 secondes à",
                    "§7chaque lorsqu’il inflige un coup d’épée",
                    "§7ou une flèche à l’arc."
            ).toItemStack());

            player.openInventory(inv);
        }

        if (event.getInventory().getName().startsWith("Marionnettes ")) {
            event.setCancelled(true);
            Player target = Bukkit.getPlayer(event.getInventory().getName().replace("Marionnettes ", ""));
            if (target == null) {
                player.sendMessage(prefix("&cCe joueur n'est pas connecté&f."));
                return;
            }

            target.sendMessage(prefix("&aKankurô &fa décidé de vous ressuscité&f. Vous obtenez également l'item &aShuryûdan&f."));
            giveStuff(target);
        }

    }

    @Override
    public void onAllPlayerCampChange(Camp camp, Player player) {
        Player kankuro = getPlayer();
        if (Role.isRole(player, NarutoRoles.GAARA)) {
            kankuro.sendMessage(prefix("&aGaara &fa changé de camp &8(" + camp.getFormat() + "&8)&f. Vous avez donc rejoint ce camp."));
            roleManager.setCamp(kankuro, camp);
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

        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Shuryûdan")).toItemStack());
    }

    @Override
    public Camp getCamp() {
        return Camp.SANKYODAI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}

package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Orochimaru extends NarutoRole {

    int newHealth = 20;
    int timer = 0;
    private Chakra chakra = Chakra.KATON;

    public NarutoRoles getRole() {
        return NarutoRoles.OROCHIMARU;
    }

    @Override
    public void runnableTask() {
        if (timer > 0) {
            timer--;
        }

        if (timer == 0) {
            timer = -1;
            Player player = Role.findPlayer(NarutoRoles.OROCHIMARU);
            player.setMaxHealth(newHealth);
        }

    }

    @Override
    public String getRoleName() {
        return "Orochimaru";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        List<Chakra> chakras = new ArrayList<>(Arrays.asList(Chakra.values()));
        chakras.remove(Chakra.AUCUN);
        Collections.shuffle(chakras);

        this.chakra = chakras.get(0);

        List<String> orochimaru = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(e -> Bukkit.getPlayer(e) != null)
                .filter(uuid -> narutoUHC.getRoleManager().getCamp(uuid) == Camp.OROCHIMARU)
                .map(e -> Bukkit.getPlayer(e).getName())
                .forEach(orochimaru::add);

        List<String> taka = new ArrayList<>();
        UHC.getUHC().getGameManager().getPlayers().stream()
                .filter(e -> Bukkit.getPlayer(e) != null)
                .filter(uuid -> narutoUHC.getRoleManager().getCamp(uuid) == Camp.TAKA)
                .map(e -> Bukkit.getPlayer(e).getName())
                .forEach(taka::add);


        player.sendMessage(CC.prefix("§cListe des Orichimaru:"));
        orochimaru.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");

        player.sendMessage(CC.prefix("§cListe des Taka:"));
        taka.forEach(s -> player.sendMessage(" §8- §c" + s));
        player.sendMessage(" ");

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).setName(Item.specialItem("Kusanagi")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Edo Tensei")).toItemStack());
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

    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getInventory().getName().equals("Edo Tensei")) {
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
    public void onMinute(int minute, Player player) {


        if (minute == 40) {
            Tasks.runTimer(() -> {
                newHealth -= 2;
                player.setMaxHealth(player.getMaxHealth() - 2);
            }, 0, 20 * 60 * 10);
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        timer = 10 * 60;
        killer.setMaxHealth(20);

    }

    @Override
    public void onPlayerHealthRegain(EntityRegainHealthEvent event, Player player) {
        if (player.getHealth() > 8) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if (player.getHealth() <= 8) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        }
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event, Player player) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            Tasks.runLater(() -> {
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 1, false, false));
            }, 1L);
        }
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("marquemaudite")) {

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns marquemaudite <player>"));
                return;
            }


            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            HashMap<UUID, Integer> strength = NarutoUHC.getNaruto().getManager().getStrength();
            HashMap<UUID, Integer> resistance = NarutoUHC.getNaruto().getManager().getStrength();
            NarutoUHC.getNaruto().getManager().getStrength().put(target.getUniqueId(), strength.getOrDefault(target.getUniqueId(), 0) + 5);
            NarutoUHC.getNaruto().getManager().getResistance().put(target.getUniqueId(), resistance.getOrDefault(target.getUniqueId(), 0) + 5);

            player.sendMessage(CC.prefix("§fVous avez donné §c5% §fde §cForce §fet de §7Résistence §fen plus à §a" + target.getName()));
            target.sendMessage(CC.prefix("§aOrochimaru §fvous a donné §c5% §fde §cForce §fet de §7Résistence §fen plus."));

        }
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }
}

package fr.lyneris.narutouhc.pierre;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.SchematicManager;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.ItemPickaxe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PierreTombaleManager {

    private final NarutoUHC narutoUHC;
    private int pierreTombale;

    public PierreTombaleManager(NarutoUHC narutoUHC) {
        this.narutoUHC = narutoUHC;
        this.pierreTombale = 40*60;
    }

    public NarutoUHC getNarutoUHC() {
        return narutoUHC;
    }

    public int getPierreTombale() {
        return pierreTombale;
    }

    public void setPierreTombale(int pierreTombale) {
        this.pierreTombale = pierreTombale;
    }

    public void addPierreTombale(int add) {
        this.setPierreTombale(Math.min(this.getPierreTombale() + add, 60*60));
    }

    public void removePierreTombale(int remove) {
        this.setPierreTombale(Math.max(this.getPierreTombale() - remove, 60));
    }

    public void spawn(Pierre pierre) {
        World world = Bukkit.getWorld("world");
        int x = (int) (Math.random() * 300);
        int z = (int) (Math.random() * 300);
        int y = world.getHighestBlockYAt(x, z) - 1;
        Location loc = new Location(world, x, y, z);
        if(loc.getBlock().getType() != Material.DIRT || loc.getBlock().getType() != Material.GRASS) {
            spawn(pierre);
            return;
        }

        SchematicManager.pasteSchematic(narutoUHC.getDataFolder().getAbsolutePath() + "/PierreTombale.schematic", loc);

        Location first = new Location(world, x + 10, y + 10, z + 10);
        Location second = new Location(world, x - 10, y - 10, z - 10);
        Cuboid cuboid = new Cuboid(first, second);
        Chest block = (Chest) cuboid.getBlocks().stream().filter(block1 -> block1.getType() == Material.CHEST).findFirst().orElse(null);
        if (block != null) {
            block.getBlockInventory().addItem(pierre.getItemStack());
        }
    }

    public void replace(Location loc) {
//        World world = Bukkit.getWorld("world");
//        int x = (int) (Math.random() * 300);
//        int z = (int) (Math.random() * 300);
//        int y = world.getHighestBlockYAt(x, z) - 1;
//
//        HashMap<Location, Material> map = new HashMap<>();
//        Location first = new Location(world, x + 10, y + 10, z + 10);
//        Location second = new Location(world, x - 10, y - 10, z - 10);
//        Cuboid cuboid = new Cuboid(first, second);
//
//        cuboid.getBlocks().forEach(block -> map.put(block.getLocation(), block.getType()));
//        Tasks.runLater(() -> map.keySet().forEach(location -> location.getBlock().setType(map.get(location))), );

    }

    List<UUID> firstHit = new ArrayList<>();
    List<UUID> kabutowariCooldown = new ArrayList<>();
    List<UUID> shibukiCooldown = new ArrayList<>();
    UUID first = null;

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        if(Item.interactItem(((Player) event.getDamager()).getItemInHand(), "Nuibaru")) {
            if(first != null) {
                Location first = Bukkit.getPlayer(this.first).getLocation();
                Location second = event.getEntity().getLocation();
                event.getEntity().teleport(first);
                Bukkit.getPlayer(this.first).teleport(second);

                Bukkit.getPlayer(this.first).sendMessage(CC.prefix("Votre position a été échangée avec celle de &a" + event.getEntity().getName()));
                event.getEntity().sendMessage(CC.prefix("Votre position a été échangée avec celle de &a" + Bukkit.getPlayer(this.first)));
                this.first = null;
                return;
            }

            first = event.getEntity().getUniqueId();
            Tasks.runLater(() -> {
                if(first.equals(event.getEntity().getUniqueId())) {
                    first = null;
                }
            }, 2*20*60);
        }


        if(Item.interactItem(((Player) event.getDamager()).getItemInHand(), "Hiramekarei")) {
            if(!firstHit.contains(event.getDamager().getUniqueId())) {
                firstHit.add(event.getDamager().getUniqueId());
                event.setDamage(event.getFinalDamage() * 1.5);
                event.getDamager().sendMessage(CC.prefix("&fVotre coup à mis &a50% &fde dégâts en plus."));
            }
        }

        if(Item.interactItem(((Player) event.getDamager()).getItemInHand(), "Kiba")) {
            int random = (int) (Math.random() * 4);
            if(random <= 3) {
                event.setDamage(event.getFinalDamage() * 1.25);
                event.getDamager().sendMessage(CC.prefix("&fVotre coup à mis &a25% &fde dégâts en plus."));
            }

            if(random == 1) {
                event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
            }
        }

        if(Item.interactItem(((Player) event.getDamager()).getItemInHand(), "Kabutowari")) {
            if(!kabutowariCooldown.contains(event.getDamager().getUniqueId())) {
                Tasks.runLater(() -> {
                    ((Player) event.getEntity()).damage(event.getFinalDamage());
                    event.getEntity().setVelocity(event.getEntity().getLocation().getDirection().multiply(0.6).setY(0.2));
                    event.getDamager().sendMessage(CC.prefix("&fVotre coup à été &adupliqué&f."));
                }, 10);
                kabutowariCooldown.add(event.getDamager().getUniqueId());
                Tasks.runLater(() -> kabutowariCooldown.remove(event.getDamager().getUniqueId()), 5*20*60);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(Item.interactItem(event.getItem(), "Hiramekarei")) {
            if(event.getAction().name().contains("LEFT")) return;
            Block block = event.getPlayer().getTargetBlock((Set<Material>) null, 6);
            if(block != null) {
                block.setType(Material.AIR);
            }
        }

        if(Item.interactItem(event.getItem(), "Shibuki")) {
            Loc.getNearbyPlayers(event.getPlayer(), 50).forEach(player -> {
                if(Reach.getLookingAt(event.getPlayer(), player)) {
                    if (this.shibukiCooldown.contains(event.getPlayer().getUniqueId())) {
                        player.sendMessage(CC.prefix("&cCet item possède un cooldown de 30 secondes."));
                    } else {
                        shibukiCooldown.add(event.getPlayer().getUniqueId());
                        player.getWorld().createExplosion(player.getLocation(), 2);
                        Tasks.runLater(() -> {
                            shibukiCooldown.remove(event.getPlayer().getUniqueId());
                            player.sendMessage(CC.prefix("&fVotre cooldown pour l'item &aShibuki &fvient d'expirer."));
                        }, 30*20);
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if(Item.interactItem(event.getItem().getItemStack(), "Hiramekarei")) {
            Reach.addReachPlayerTemp(event.getPlayer().getUniqueId(), 120*60);
        }
    }

    public void onStart() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Pierre value : Pierre.values()) {
                    spawn(value);
                }
            }
        }.runTaskLater(narutoUHC, pierreTombale);
    }

    public enum Pierre {
        Hiramekarei(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.interactItem("Hiramekarei")).addEnchant(Enchantment.DAMAGE_ALL, 3).toItemStack()),
        Kabutowari(new ItemBuilder(Material.DIAMOND_AXE).setName(Item.interactItem("Kabutowari")).addEnchant(Enchantment.DAMAGE_ALL,4).toItemStack()),
        Kiba(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.interactItem("Kiba")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack()),
        Nuibaru(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.interactItem("Nuibaru")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack()),
        Shibuki(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.interactItem("Shibuki")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());

        private final ItemStack itemStack;

        Pierre(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

}

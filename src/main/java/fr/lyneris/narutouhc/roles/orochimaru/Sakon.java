package fr.lyneris.narutouhc.roles.orochimaru;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.Cuboid;
import fr.lyneris.narutouhc.packet.Pair;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sakon extends NarutoRole {

    public static int senpoTimer = 0;
    public static int barrierTimer = 0;
    public static boolean usedBarrier = false;
    public static int senpoCooldown = 0;
    public static boolean died = false;
    public static List<Location> unbreakableBlocks = new ArrayList<>();



    public static void useBarrier() {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        if (ukon == null) return;
        if (sakon == null) return;

        ukon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aBarrière protectrice§f."));
        sakon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aBarrière protectrice§f."));

        usedBarrier = true;


        Location firstCorner = sakon.getLocation();
        firstCorner.setY(0);


        Location secondCorner = ukon.getLocation();
        secondCorner.setY(256);

        buildPrisonGlass(firstCorner, secondCorner);
    }

    public static void useSenpo() {
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        if (ukon == null) return;
        if (sakon == null) return;

        ukon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aSenpô§f."));
        sakon.sendMessage(CC.prefix("§fVous avez utilisé votre item §aSenpô§f."));

        sakon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
        ukon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20 * 60, 0, false, false));
        sakon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));
        ukon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 0, false, false));

        Tasks.runLater(() -> {
            ukon.setMaxHealth(ukon.getMaxHealth() - 2);
            sakon.setMaxHealth(sakon.getMaxHealth() - 2);
        }, 5 * 20 * 60);

        senpoCooldown = 10 * 60;

    }

    private static void buildPrisonGlass(Location firstCorner, Location secondCorner) {
        Map<Location, Pair<Material, Byte>> originalBlocks = new HashMap<>();
        List<Block> wallsBlocks = getWalls(firstCorner, secondCorner);

        unbreakableBlocks.addAll(wallsBlocks.stream().map(Block::getLocation).collect(Collectors.toList()));

        wallsBlocks.forEach(block -> {
            originalBlocks.put(block.getLocation(), new Pair<>(block.getType(), block.getData()));
            block.setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 10, false);
        });

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Map.Entry<Location, Pair<Material, Byte>> e : originalBlocks.entrySet()) {
                    e.getKey().getBlock().setTypeIdAndData(e.getValue().getFirst().getId(), e.getValue().getSecond(), false);
                }
                unbreakableBlocks.clear();
            }
        }.runTaskLater(NarutoUHC.getNaruto(), 20 * 60 * 5);

    }


    public static List<Block> getWalls(Location firstCorner, Location secondCorner){
        List<Block> wallsBlocks = new ArrayList<>();

        Location min = getMinimumLocation(firstCorner.getWorld(), firstCorner, secondCorner);
        Location max = getMaximumLocation(firstCorner.getWorld(), firstCorner, secondCorner);

        //X PLANE
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), min.getX(), firstCorner.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), min.getX(), secondCorner.getY(), secondCorner.getZ())).getBlocks());
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), max.getX(), firstCorner.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), max.getX(), secondCorner.getY(), secondCorner.getZ())).getBlocks());

        //Z PLANE
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), firstCorner.getY(), min.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), secondCorner.getY(), min.getZ())).getBlocks());
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), firstCorner.getY(), max.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), secondCorner.getY(), max.getZ())).getBlocks());

        //TOP
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), max.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), max.getY(), secondCorner.getZ())).getBlocks());

        //GROUND
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), min.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), min.getY(), secondCorner.getZ())).getBlocks());
        return wallsBlocks;
    }

    public static Location getMinimumLocation(World world, Location loc1, Location loc2) {
        return new Location(
                world,
                Math.min(loc1.getX(), loc2.getX()),
                Math.min(loc1.getY(), loc2.getY()),
                Math.min(loc1.getZ(), loc2.getZ())
        );
    }

    public static Location getMaximumLocation(World world, Location loc1, Location loc2) {
        return new Location(
                world,
                Math.max(loc1.getX(), loc2.getX()),
                Math.max(loc1.getY(), loc2.getY()),
                Math.max(loc1.getZ(), loc2.getZ())
        );
    }

    public NarutoRoles getRole() {
        return NarutoRoles.SAKON;
    }

    @Override
    public void resetCooldowns() {
        senpoCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (senpoTimer > 0) {
            senpoTimer--;
        }

        if (senpoCooldown > 0) {
            senpoCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Sakon";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.UKON);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senpô")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Barrière protectrice")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Senpô")) {

            if (senpoCooldown > 0) {
                player.sendMessage(Messages.cooldown(senpoCooldown));
                return;
            }

            if (senpoTimer > 0) {
                player.sendMessage(CC.prefix("§cCet item est déjà en cours d'utilisation."));
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);

            senpoTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aUkon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));

            senpoCooldown = 10 * 60;

        }

        if (Item.interactItem(event.getItem(), "Barrière protectrice")) {

            if (usedBarrier) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player ukon = Role.findPlayer(NarutoRoles.UKON);
            if (ukon == null) {
                player.sendMessage(CC.prefix("§cUkon n'est pas en vie."));
                return;
            }

            if (ukon.getLocation().distance(player.getLocation()) > 40) {
                player.sendMessage(CC.prefix("§cUkon doit être au maximum à 40 blocks de vous."));
                return;
            }

            if (barrierTimer > 0) {
                player.sendMessage(CC.prefix("§cCet item est déjà en cours d'utilisation."));
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);

            barrierTimer = 15;

            player.sendMessage(CC.prefix("§fSi §aUkon §futilise cet item dans les 15 prochaines secondes l'item §as'activera§f."));

        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        if(ukon.getLocation().distance(player.getLocation()) > 15) {
            if(!(event.getFinalDamage() >= player.getHealth())) return;
            if(Ukon.died) {
                ukon.setGameMode(GameMode.SURVIVAL);
                ukon.setHealth(0);
                player.setHealth(0);
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            player.setGameMode(GameMode.SPECTATOR);
            died = true;
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, Player player, Player killer) {
        Player ukon = Role.findPlayer(NarutoRoles.UKON);

        if (ukon != null) {
            if (ukon.getLocation().distance(player.getLocation()) <= 15) {
                ukon.setHealth(0);
                ukon.sendMessage(CC.prefix("§cSakon est mort et vous a emporté avec lui..."));
            } else {
                died = true;
            }
        }
    }

    @Override
    public void onSecond(int timer, Player player) {
        Player ukon = Role.findPlayer(NarutoRoles.UKON);
        Player sakon = Role.findPlayer(NarutoRoles.SAKON);

        if (ukon == null || sakon == null) return;

        if (ukon.getLocation().distance(sakon.getLocation()) <= 15) {
            sakon.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            sakon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 0, false, false));
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

}

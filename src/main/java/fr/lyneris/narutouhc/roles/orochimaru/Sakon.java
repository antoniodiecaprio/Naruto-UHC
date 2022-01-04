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
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §5Sakon\n" +
                "§7▎ Objectif: §rSon but est de gagner avec le camp d'§5Orochimaru\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rSenpô§7”, il doit l’utiliser en même temps que §5Ukon§7 avec un délai de 15 secondes à son utilisation pour que l’item puisse être utilisé, à l’utilisation, lui et §5Ukon§7 reçoivent les effets §cForce 1§7 et §5Vitesse 1§7 pendant 5 minutes, il perd §c1 cœur§7 permanent à la fin de l’utilisation et il possède un délai de 10 minutes.\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rBarrière protectrice§7”, celui-ci lui permet de faire apparaître une barrière violette qui est indestructible, celle-ci est en forme de carré et se forme selon la position de §5Sakon§7 et de §5Ukon§7, cette barrière est donc utilisable de la façon suivante: §5Ukon§7 et §5Sakon§7 doivent utiliser leur item en même temps avec un délai de 15 secondes et être à 40 blocs maximum de distance l’un de l’autre. L’item ne peut être utilisé qu’une seule fois et la barrière reste active pendant 5 minutes.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Lorsqu’il se trouve dans un rayon de 15 blocs de §5Ukon§7, il reçoit l’effet §cForce 1§7.\n" +
                "§e §f\n" +
                "§7• Si §5Ukon§7 ou bien §5Sakon§7 vient à mourir dans un rayon de 15 blocs proche de l'autre, alors il mourra à son tour, cependant dans le cas contraire, si l’un meurt dans un rayon supérieur à 15 blocs de l’autre alors il ne mourra pas et ressuscitera 5 minutes après sa mort à l’emplacement de celui qui est toujours en vie mais si l’autre meurt dans les 5 minutes suivant la mort du premier alors tout deux mourront définitivement et ne pourront pas être ressusciter par n’importe quel autre rôle.\n" +
                "§e §f\n" +
                "§7• Il possède un chat permettant de communiquer avec §5Ukon§7, il lui suffit simplement d’écrire dans le chat avec l’aide du préfixe \"!\" pour pouvoir communiquer avec lui.\n" +
                "§e §f\n" +
                "§7• Il connaît l'identité de §5Ukon§7.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
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
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
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
            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
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
            if(!(event.getDamage() >= player.getHealth())) return;
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

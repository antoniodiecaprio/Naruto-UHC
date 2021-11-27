package fr.lyneris.narutouhc.packet;

import fr.lyneris.narutouhc.particle.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ForetOsGenerator {

    public static List<Block> spawnForetOs(Location center, int radius) {
        List<Block> blocks = WorldUtils.generateSphere(center, radius, false).stream().map(Location::getBlock).collect(Collectors.toList());

        blocks.stream().filter(block ->
                block.getType() == Material.LOG ||
                        block.getType() == Material.LOG_2 ||
                        block.getType() == Material.LEAVES ||
                        block.getType() == Material.LEAVES_2).forEach(block -> {
            block.setType(Material.AIR);
        });

        QuartzTreeDelegator delegate = new QuartzTreeDelegator(center.getWorld());

        List<Location> cylinder = generateCylinder(center, radius);
        List<Location> treeSpawns = new ArrayList<>();

        for (int i = 0; i < cylinder.size() / 2; i++) {
            int index = new Random().nextInt(cylinder.size());
            treeSpawns.add(cylinder.get(index));
            cylinder.remove(index);
        }

        for (Location treeSpawn : treeSpawns) {
            Block down = treeSpawn.getWorld().getHighestBlockAt(treeSpawn).getRelative(BlockFace.DOWN);
            if (down.getType() != Material.QUARTZ_BLOCK) down.setType(Material.GRASS);
            center.getWorld().generateTree(treeSpawn, TreeType.SMALL_JUNGLE, delegate);
        }
        return delegate.getBlocks();
    }

    public static List<Location> generateCylinder(Location center, int radius) {
        List<Location> locs = new ArrayList<>();
        World world = center.getWorld();

        int cx = center.getBlockX();
        int cz = center.getBlockZ();
        int rSquared = radius * radius;
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    locs.add(new Location(world, x, world.getHighestBlockYAt(x, z), z));
                }
            }
        }
        return locs;
    }

}

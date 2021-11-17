package fr.lyneris.narutouhc.packet;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class QuartzTreeDelegator implements BlockChangeDelegate {
    
    private final World world;
    private final List<Block> blocks;

    public QuartzTreeDelegator(World world) {
        this.world = world;
        this.blocks = new ArrayList<>();
    }

    @Override
    public boolean setRawTypeId(int x, int y, int z, int i3) {
        world.getBlockAt(x, y, z).setType(Material.QUARTZ_BLOCK);
        return true;
    }

    @Override
    public boolean setRawTypeIdAndData(int x, int y, int z, int i3, int i4) {
        world.getBlockAt(x, y, z).setType(Material.QUARTZ_BLOCK);
        return true;
    }

    @Override
    public boolean setTypeId(int x, int y, int z, int i3) {
        world.getBlockAt(x, y, z).setType(Material.QUARTZ_BLOCK);
        return true;
    }

    @Override
    public boolean setTypeIdAndData(int x, int y, int z, int i3, int i4) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.QUARTZ_BLOCK);
        blocks.add(block);
        return true;
    }

    @Override
    public int getTypeId(int x, int y, int z) {
        return Material.QUARTZ_BLOCK.getId();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean isEmpty(int i, int i1, int i2) {
        return false;
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }
}

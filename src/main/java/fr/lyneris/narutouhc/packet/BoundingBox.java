package fr.lyneris.narutouhc.packet;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_8_R3.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import net.minecraft.server.v1_8_R3.*;

public class BoundingBox
{
    private final Vector min;
    private final Vector max;
    
    public BoundingBox(final Vector min, final Vector max) {
        this.min = min;
        this.max = max;
    }
    
    public BoundingBox(final Block block) {
        final IBlockData blockData = ((CraftWorld)block.getWorld()).getHandle().getType(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        final net.minecraft.server.v1_8_R3.Block blockNative = blockData.getBlock();
        blockNative.updateShape(((CraftWorld)block.getWorld()).getHandle(), new BlockPosition(block.getX(), block.getY(), block.getZ()));
        this.min = new Vector(block.getX() + blockNative.B(), block.getY() + blockNative.D(), block.getZ() + blockNative.F());
        this.max = new Vector(block.getX() + blockNative.C(), block.getY() + blockNative.E(), block.getZ() + blockNative.G());
    }
    
    public BoundingBox(final Entity entity) {
        final AxisAlignedBB bb = ((CraftEntity)entity).getHandle().getBoundingBox();
        this.min = new Vector(bb.a, bb.b, bb.c);
        this.max = new Vector(bb.d, bb.e, bb.f);
    }
    
    public BoundingBox(final AxisAlignedBB bb) {
        this.min = new Vector(bb.a, bb.b, bb.c);
        this.max = new Vector(bb.d, bb.e, bb.f);
    }
    
    public Vector midPoint() {
        return this.max.clone().add(this.min).multiply(0.5);
    }
    
    public Vector getMin() {
        return this.min;
    }
    
    public Vector getMax() {
        return this.max;
    }
}

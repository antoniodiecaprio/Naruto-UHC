package fr.lyneris.narutouhc.packet;

import com.google.common.base.Predicates;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Collections;
import java.util.List;

public class PathFinderAttackPlayer extends PathfinderGoalNearestAttackableTarget {
    public PathFinderAttackPlayer(EntityCreature entitycreature, Class oclass) {
        super(entitycreature, oclass, false);
    }

    @Override
    public boolean a() {
        final double d0 = this.f();
        final List list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0), Predicates.and(this.c, IEntitySelector.d));
        Collections.sort(list, this.b);
        if (list.isEmpty())
            return false;
        else {
            this.d = (EntityLiving) list.get(0);
            if (this.d instanceof EntityHuman) {
                final EntityHuman eh = (EntityHuman) list.get(0);
                return eh.getBukkitEntity().getLocation().distance(this.e.getBukkitEntity().getLocation()) < 8.0D && eh.getBukkitEntity().getGameMode() != GameMode.SPECTATOR;
            }
        }
        return false;
    }

    public void c() {
        this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
        this.d.damageEntity(DamageSource.mobAttack(this.e), (float) 0.75);
    }
}

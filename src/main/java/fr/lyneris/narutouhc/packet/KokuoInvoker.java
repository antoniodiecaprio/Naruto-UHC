package fr.lyneris.narutouhc.packet;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Horse;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;

public class KokuoInvoker {

    private static Field gsa;
    private static Field goalSelector;
    private static Field targetSelector;

    static {
        try {
            gsa = PathfinderGoalSelector.class.getDeclaredField("b");
            gsa.setAccessible(true);
            goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
            goalSelector.setAccessible(true);
            targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
            targetSelector.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Horse invokeKokuo(Horse horse, Location location, String name) {
        try {
            World world = location.getWorld();
            horse = world.spawn(location, Horse.class);
            horse.setMaxHealth(2D*100D);
            horse.setHealth(horse.getMaxHealth());
            horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            horse.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
            horse.setCustomName(name);
            horse.setCustomNameVisible(true);
            horse.setVariant(Horse.Variant.SKELETON_HORSE);
            horse.setAdult();
            EntityLiving nmsEntity = ((CraftLivingEntity) horse).getHandle();
            ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
            if (nmsEntity instanceof EntityInsentient) {
                PathfinderGoalSelector goal = (PathfinderGoalSelector) goalSelector.get(nmsEntity);
                PathfinderGoalSelector target = (PathfinderGoalSelector) targetSelector.get(nmsEntity);
                gsa.set(goal, new UnsafeList<Object>());
                gsa.set(target, new UnsafeList<Object>());
                goal.a(0, new PathfinderGoalFloat((EntityInsentient) nmsEntity));
                goal.a(1, new PathfinderGoalMeleeAttack((EntityCreature) nmsEntity, EntityHuman.class, 1.0D, false));
                goal.a(2, new PathfinderGoalLookAtPlayer((EntityInsentient) nmsEntity, EntityHuman.class, 8.0F));
                target.a(4, new PathFinderAttackPlayer((EntityCreature) nmsEntity, EntityHuman.class));
                target.a(3, new PathfinderGoalNearestAttackableTarget<>((EntityCreature) nmsEntity, EntityHuman.class, true));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return horse;
    }

}

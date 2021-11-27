package fr.lyneris.narutouhc.particle;

import fr.lyneris.narutouhc.NarutoUHC;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DoubleCircleEffectRedstone extends ParticleEffect {

    private final EnumParticle particle;

    private final int r, g, b;

    public DoubleCircleEffectRedstone(int timeInTicks, EnumParticle effect, int r, int g, int b) {
        super(timeInTicks);
        this.particle = effect;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void start(Player player) {
        new BukkitRunnable() {
            int ticks;

            double var;


            @Override
            public void run() {
                if (ticks > getTimeInTicks())
                    cancel();

                var += Math.PI / 16;

                Location loc = player.getLocation();
                Location firstCircle = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                Location secondCircle = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));

                WorldUtils.spawnColoredParticle(firstCircle, particle, r, g, b);
                WorldUtils.spawnColoredParticle(secondCircle, particle, r, g, b);


                ticks++;
            }
        }.runTaskTimer(NarutoUHC.getNaruto(), 0, 1);
    }

}

package fr.lyneris.narutouhc.particle;

import org.bukkit.entity.Player;

public abstract class ParticleEffect {

    private final int timeInTicks;

    public ParticleEffect(int timeInTicks) {
        super();
        this.timeInTicks = timeInTicks;
    }

    public int getTimeInTicks() {
        return this.timeInTicks;
    }

    public abstract void start(Player player);

}

package fr.lyneris.narutouhc.biju;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public enum Bijus {

    CHOMEI(new Chomei(), Material.GHAST_TEAR),
    ISOBU(new Isobu(), Material.PRISMARINE_SHARD),
    KOKUO(new Kokuo(), Material.SADDLE),
    MATABI(new Matatabi(), Material.BLAZE_POWDER),
    SAIKEN(new Saiken(), Material.SLIME_BALL),
    SONGOKU(new SonGoku(), Material.MAGMA_CREAM);

    private final Biju biju;
    private final Material material;
    public static boolean start = false;

    Bijus(Biju biju, Material material) {
        this.biju = biju;
        this.material = material;
    }

    public Biju getBiju() {
        return biju;
    }

    public Material getMaterial() {
        return material;
    }

    public static void initBijus() {
        for (Bijus value : values()) {
            Bukkit.getServer().getPluginManager().registerEvents((Listener) value.getBiju(), NarutoUHC.getNaruto());
            value.getBiju().setupBiju();
        }
        start = true;
    }

}

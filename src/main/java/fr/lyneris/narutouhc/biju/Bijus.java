package fr.lyneris.narutouhc.biju;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.biju.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public enum Bijus {

    CHOMEI(new Chomei()),
    ISOBU(new Isobu()),
    KOKUO(new Kokuo()),
    MATABI(new Matatabi()),
    SAIKEN(new Saiken()),
    SONGOKU(new SonGoku());

    private final Biju biju;

    Bijus(Biju biju) {
        this.biju = biju;
    }

    public Biju getBiju() {
        return biju;
    }

    public static void initBijus() {
        for (Bijus value : values()) {
            Bukkit.getServer().getPluginManager().registerEvents((Listener) value.getBiju(), NarutoUHC.getNaruto());
            value.getBiju().setupBiju();
        }
    }

}

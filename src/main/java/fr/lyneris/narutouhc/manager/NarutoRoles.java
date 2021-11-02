package fr.lyneris.narutouhc.manager;

import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.roles.akatsuki.*;
import fr.lyneris.narutouhc.roles.shinobu.*;

public enum NarutoRoles {

    /**
     * SHINOBIS
     */
    NARUTO(Naruto.class),
    SAKURA(Sakura.class),
    SAI(Sai.class),
    MINATO(Minato.class),
    KAKASHI(Kakashi.class),
    JIRAYA(Jiraya.class),
    TSUNADE(Tsunade.class),
    ASUMA(Asuma.class),
    GAI_MAITO(GaiMaito.class),
    ROCK_LEE(RockLee.class),
    TEN_TEN(TenTen.class),
    NEJI(Neji.class),
    HINATA(Hinata.class),
    KIBA(Kiba.class),
    SHINO(Shino.class),
    INO(Ino.class),
    CHOJI(Choji.class),
    SHIKAMARU(Shikamaru.class),
    TEMARI(Temari.class),
    YONDAIME_RAIKAGE(YondaimeRaikage.class),
    KILLER_BEE(KillerBee.class),
    KONOHAMARU(Konohamaru.class),

    /**
     * AKATSUKI
     */
    DEIDARA(Deidara.class),
    SASORI(Sasori.class),
    HIDAN(Hidan.class),
    KAKUZU(Kakuzu.class),
    KISAME(Kisame.class),
    ITACHI(Itachi.class),
    KONAN(Konan.class),
    NAGATO(Nagato.class),
    ZETSU_NOIR(ZetsuNoir.class),
    ZETSU_BLANC(ZetsuBlanc.class),

    /**
     * OROCHIMARU
     */
    OROCHIMARU(null),
    KABUTO(null),
    KIMIMARO(null),
    SAKON(null),
    UKON(null),
    KIDOMARU(null),
    TAYUYA(null),
    JIROBO(null),

    /**
     * TAKA
     */
    SASUKE(null),
    SUIGETSU(null),
    KARIN(null),
    JUGO(null),

    /**
     * JÛBI
     */
    OBITO(null),
    MADARA(null),

    /**
     * SOLOS
     */
    DANZO(null),
    GAARA(null);

    private final Class<? extends NarutoRole> narutoRole;

    NarutoRoles(Class<? extends NarutoRole> narutoRole) {
        this.narutoRole = narutoRole;
    }

    public Class<? extends NarutoRole> getNarutoRole() {
        return narutoRole;
    }
}

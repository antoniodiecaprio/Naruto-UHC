package fr.lyneris.narutouhc.manager;

import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.roles.orochimaru.*;
import fr.lyneris.narutouhc.roles.akatsuki.*;
import fr.lyneris.narutouhc.roles.shinobu.*;
import fr.lyneris.narutouhc.roles.taka.*;

public enum NarutoRoles {

    /**
     * SHINOBIS
     */
    NARUTO(Naruto.class, "Naruto"),
    SAKURA(Sakura.class, "Sakura"),
    SAI(Sai.class, "Sai"),
    MINATO(Minato.class, "Minato"),
    KAKASHI(Kakashi.class, "Kakashi"),
    JIRAYA(Jiraya.class, "Jiraya"),
    TSUNADE(Tsunade.class, "Tsunade"),
    ASUMA(Asuma.class, "Asuma"),
    GAI_MAITO(GaiMaito.class, "Gaî Maito"),
    ROCK_LEE(RockLee.class, "Rock Lee"),
    TEN_TEN(TenTen.class, "Ten Ten"),
    NEJI(Neji.class, "Neji"),
    HINATA(Hinata.class, "Hinata"),
    KIBA(Kiba.class, "Kiba"),
    SHINO(Shino.class, "Shino"),
    INO(Ino.class, "Ino"),
    CHOJI(Choji.class, "Choji"),
    SHIKAMARU(Shikamaru.class, "Shikamaru"),
    TEMARI(Temari.class, "Temari"),
    YONDAIME_RAIKAGE(YondaimeRaikage.class, "Yondaime Raikage"),
    KILLER_BEE(KillerBee.class, "Killer Bee"),
    KONOHAMARU(Konohamaru.class, "Konohamaru"),

    /**
     * AKATSUKI
     */
    DEIDARA(Deidara.class, "Deidara"),
    SASORI(Sasori.class, "Sasori"),
    HIDAN(Hidan.class, "Hidan"),
    KAKUZU(Kakuzu.class, "Kakuzu"),
    KISAME(Kisame.class, "Kisame"),
    ITACHI(Itachi.class, "Itachi"),
    KONAN(Konan.class, "Konan"),
    NAGATO(Nagato.class, "Nagato"),
    ZETSU_NOIR(ZetsuNoir.class, "Zetsu Noir"),
    ZETSU_BLANC(ZetsuBlanc.class, "Zetsu Blanc"),

    /**
     * OROCHIMARU
     */
    OROCHIMARU(Orochimaru.class, "Orochimaru"),
    KABUTO(Kabuto.class, "Kabuto"),
    KIMIMARO(Kimimaro.class, "Kimimaro"),
    SAKON(Sakon.class, "Sakon"),
    UKON(Ukon.class, "Ukon"),
    KIDOMARU(Kidomaru.class, "Kidomaru"),
    TAYUYA(Tayuya.class, "Tayuya"),
    JIROBO(Jirobo.class, "Jirobo"),

    /**
     * TAKA
     */
    SASUKE(Sasuke.class, "Sasuke"),
    SUIGETSU(Suigetsu.class, "Suigetsu"),
    KARIN(Karin.class, "Karin"),
    JUGO(null, ""),

    /**
     * JÛBI
     */
    OBITO(null, ""),
    MADARA(null, ""),

    /**
     * SOLOS
     */
    DANZO(null, ""),
    GAARA(null, "");

    private final Class<? extends NarutoRole> narutoRole;
    private final String name;

    NarutoRoles(Class<? extends NarutoRole> narutoRole, String name) {
        this.narutoRole = narutoRole;
        this.name = name;
    }

    public Class<? extends NarutoRole> getNarutoRole() {
        return narutoRole;
    }

    public String getName() {
        return name;
    }
}

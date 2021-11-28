package fr.lyneris.narutouhc.manager;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.roles.akatsuki.*;
import fr.lyneris.narutouhc.roles.jubi.*;
import fr.lyneris.narutouhc.roles.orochimaru.*;
import fr.lyneris.narutouhc.roles.sankyodai.*;
import fr.lyneris.narutouhc.roles.shinobu.*;
import fr.lyneris.narutouhc.roles.solo.*;
import fr.lyneris.narutouhc.roles.taka.*;

public enum NarutoRoles {

    /**
     * SHINOBIS
     */
    NARUTO(Naruto.class, "Naruto", Camp.SHINOBI),
    SAKURA(Sakura.class, "Sakura", Camp.SHINOBI),
    SAI(Sai.class, "Sai", Camp.SHINOBI),
    MINATO(Minato.class, "Minato", Camp.SHINOBI),
    KAKASHI(Kakashi.class, "Kakashi", Camp.SHINOBI),
    JIRAYA(Jiraya.class, "Jiraya", Camp.SHINOBI),
    TSUNADE(Tsunade.class, "Tsunade", Camp.SHINOBI),
    ASUMA(Asuma.class, "Asuma", Camp.SHINOBI),
    GAI_MAITO(GaiMaito.class, "Gaî Maito", Camp.SHINOBI),
    ROCK_LEE(RockLee.class, "Rock Lee", Camp.SHINOBI),
    TEN_TEN(TenTen.class, "Ten Ten", Camp.SHINOBI),
    NEJI(Neji.class, "Neji", Camp.SHINOBI),
    HINATA(Hinata.class, "Hinata", Camp.SHINOBI),
    KIBA(Kiba.class, "Kiba", Camp.SHINOBI),
    SHINO(Shino.class, "Shino", Camp.SHINOBI),
    HIRUZEN(Hiruzen.class, "Hiruzen", Camp.SHINOBI),
    INO(Ino.class, "Ino", Camp.SHINOBI),
    CHOJI(Choji.class, "Choji", Camp.SHINOBI),
    SHIKAMARU(Shikamaru.class, "Shikamaru", Camp.SHINOBI),
    YONDAIME_RAIKAGE(YondaimeRaikage.class, "Yondaime Raikage", Camp.SHINOBI),
    KILLER_BEE(KillerBee.class, "Killer Bee", Camp.SHINOBI),
    KONOHAMARU(Konohamaru.class, "Konohamaru", Camp.SHINOBI),

    /**
     * AKATSUKI
     */
    DEIDARA(Deidara.class, "Deidara", Camp.AKATSUKI),
    SASORI(Sasori.class, "Sasori", Camp.AKATSUKI),
    HIDAN(Hidan.class, "Hidan", Camp.AKATSUKI),
    KAKUZU(Kakuzu.class, "Kakuzu", Camp.AKATSUKI),
    KISAME(Kisame.class, "Kisame", Camp.AKATSUKI),
    ITACHI(Itachi.class, "Itachi", Camp.AKATSUKI),
    KONAN(Konan.class, "Konan", Camp.AKATSUKI),
    NAGATO(Nagato.class, "Nagato", Camp.AKATSUKI),
    ZETSU_NOIR(ZetsuNoir.class, "Zetsu Noir", Camp.AKATSUKI),
    ZETSU_BLANC(ZetsuBlanc.class, "Zetsu Blanc", Camp.AKATSUKI),

    /**
     * OROCHIMARU
     */
    OROCHIMARU(Orochimaru.class, "Orochimaru", Camp.OROCHIMARU),
    KABUTO(Kabuto.class, "Kabuto", Camp.OROCHIMARU),
    KIMIMARO(Kimimaro.class, "Kimimaro", Camp.OROCHIMARU),
    SAKON(Sakon.class, "Sakon", Camp.OROCHIMARU),
    UKON(Ukon.class, "Ukon", Camp.OROCHIMARU),
    KIDOMARU(Kidomaru.class, "Kidomaru", Camp.OROCHIMARU),
    TAYUYA(Tayuya.class, "Tayuya", Camp.OROCHIMARU),
    JIROBO(Jirobo.class, "Jirobo", Camp.OROCHIMARU),

    /**
     * TAKA
     */
    SASUKE(Sasuke.class, "Sasuke", Camp.TAKA),
    SUIGETSU(Suigetsu.class, "Suigetsu", Camp.TAKA),
    KARIN(Karin.class, "Karin", Camp.TAKA),
    JUGO(Jugo.class, "Jugô", Camp.TAKA),

    /**
     * OBITO AND MADARA
     */
    OBITO(Obito.class, "Obito", Camp.MADARA_OBITO),
    MADARA(Madara.class, "Madara", Camp.MADARA_OBITO),


    /**
     * ZABUZA AND HAKU
     */
    ZABUZA(null, "", Camp.ZABUZA_HAKU),
    HAKU(null, "", Camp.ZABUZA_HAKU),

    /**
     * Sankyôdai
     */
    TEMARI(Temari.class, "Temari", Camp.SANKYODAI),
    KANKURO(null, "", Camp.SANKYODAI),
    GAARA(Gaara.class, "Gaara", Camp.SANKYODAI),

    /**
     * SOLOS
     */
    DANZO(Danzo.class, "Danzo", Camp.SOLO);

    private final Class<? extends NarutoRole> narutoRole;
    private final String name;
    private final Camp camp;

    NarutoRoles(Class<? extends NarutoRole> narutoRole, String name, Camp camp) {
        this.narutoRole = narutoRole;
        this.name = name;
        this.camp = camp;
    }

    public Class<? extends NarutoRole> getNarutoRole() {
        return narutoRole;
    }

    public String getName() {
        return name;
    }

    public Camp getCamp() {
        return camp;
    }
}

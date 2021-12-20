package fr.lyneris.narutouhc.module;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Hokage {

    public static final List<NarutoRoles> CAN_BE_HOKAGE;
    public static final List<NarutoRoles> DOESNT_SEE_HOKAGE;
    public int hokageTimer = 20 * 60;
    private final NarutoUHC narutoUHC;
    private UUID hokage;
    private boolean usedBoost;
    private boolean usedReveal;

    public Hokage(NarutoUHC narutoUHC) {
        this.narutoUHC = narutoUHC;
    }

    public void chooseHokage() {
        List<UUID> list = new ArrayList<>();
        Role.getAliveOnlinePlayers().stream().filter(player -> CAN_BE_HOKAGE.contains(Role.getRole(player).getRole())).map(Player::getUniqueId).forEach(list::add);
        Collections.shuffle(list);

        this.hokage = list.get(0);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (DOESNT_SEE_HOKAGE.contains(Role.getRole(player).getRole())) {
                player.sendMessage(CC.prefix("&fLe &aHokage &fa été choisi."));
            } else {
                player.sendMessage(CC.prefix("&fLe &aHokage &fde la partie est &a" + Bukkit.getPlayer(hokage).getName()));
            }
        });

        narutoUHC.getManager().getStrength().put(hokage, narutoUHC.getManager().getStrength().getOrDefault(hokage, 0));
        narutoUHC.getManager().getResistance().put(hokage, narutoUHC.getManager().getResistance().getOrDefault(hokage, 0));
    }


    public void chooseHokage(Player hokage) {
        this.hokage = hokage.getUniqueId();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (DOESNT_SEE_HOKAGE.contains(Role.getRole(player).getRole())) {
                player.sendMessage(CC.prefix("&fLe &aHokage &fa été choisi."));
            } else {
                player.sendMessage(CC.prefix("&fLe &aHokage &fde la partie est &a" + hokage.getName()));
            }
        });

        narutoUHC.getManager().getStrength().put(hokage.getUniqueId(), narutoUHC.getManager().getStrength().getOrDefault(hokage.getUniqueId(), 0));
        narutoUHC.getManager().getResistance().put(hokage.getUniqueId(), narutoUHC.getManager().getResistance().getOrDefault(hokage.getUniqueId(), 0));
    }


    public UUID getHokage() {
        return hokage;
    }

    public void setHokage(UUID hokage) {
        this.hokage = hokage;
    }

    public int getHokageTimer() {
        return hokageTimer;
    }

    public void setHokageTimer(int hokageTimer) {
        this.hokageTimer = hokageTimer;
    }

    public boolean isUsedBoost() {
        return usedBoost;
    }

    public void setUsedBoost(boolean usedBoost) {
        this.usedBoost = usedBoost;
    }

    public boolean isUsedReveal() {
        return usedReveal;
    }

    public void setUsedReveal(boolean usedReveal) {
        this.usedReveal = usedReveal;
    }

    static {
        CAN_BE_HOKAGE = Arrays.asList(NarutoRoles.NARUTO, NarutoRoles.SAI, NarutoRoles.MINATO, NarutoRoles.KAKASHI, NarutoRoles.JIRAYA, NarutoRoles.TSUNADE, NarutoRoles.HINATA, NarutoRoles.NEJI, NarutoRoles.GAI_MAITO, NarutoRoles.SHIKAMARU, NarutoRoles.SASUKE);
        DOESNT_SEE_HOKAGE = Arrays.asList(NarutoRoles.TEMARI, NarutoRoles.YONDAIME_RAIKAGE, NarutoRoles.KILLER_BEE, NarutoRoles.ZETSU_BLANC, NarutoRoles.ZETSU_NOIR, NarutoRoles.SAKURA, NarutoRoles.UKON, NarutoRoles.KIDOMARU, NarutoRoles.TAYUYA, NarutoRoles.JIROBO, NarutoRoles.JUGO, NarutoRoles.GAARA);
    }
}

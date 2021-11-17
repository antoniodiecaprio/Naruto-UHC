package fr.lyneris.narutouhc.roles.taka;

import com.sun.corba.se.spi.orb.ORBConfigurator;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Sasuke extends NarutoRole {
    @Override
    public String getRoleName() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public void onDistribute(Player player) {
        Role.knowsRole(player, NarutoRoles.OROCHIMARU);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 10);
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        if(Role.isRole(event.getEntity(), NarutoRoles.ITACHI)) {

        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if(Role.isRole(player, NarutoRoles.OROCHIMARU)) {
            Player sasuke = Role.findPlayer(NarutoRoles.SASUKE);
            if(sasuke == null) return;
            sasuke.sendMessage(CC.prefix("&aOrochimaru &fest mort..."));
            Role.knowsRole(sasuke, NarutoRoles.KARIN);
            Role.knowsRole(sasuke, NarutoRoles.SUIGETSU);
            Role.knowsRole(sasuke, NarutoRoles.JUGO);
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    @Override
    public Camp getCamp() {
        return Camp.TAKA;
    }
}

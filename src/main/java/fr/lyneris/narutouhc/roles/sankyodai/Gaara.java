package fr.lyneris.narutouhc.roles.sankyodai;

import fr.lyneris.common.utils.ItemBuilder;
import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Gaara extends NarutoRole {

    public static boolean narutoHit = false;

    @Override
    public void onAllPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player damager) {
        if(Role.isRole(damager, NarutoRoles.NARUTO)) {
            Player gaara = Role.findPlayer(NarutoRoles.GAARA);
            if(gaara == null) return;

            narutoHit = true;
            Tasks.runAsyncLater(() -> narutoHit = false, 30*20);
        }
    }

    @Override
    public String getRoleName() {
        return "Gaara";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.GAARA;
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemStack(Material.SAND, 128));
        Role.knowsRole(player, NarutoRoles.TEMARI);
        Role.knowsRole(player, NarutoRoles.KANKURO);
        player.getInventory().addItem(new ItemBuilder)
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    @Override
    public Camp getCamp() {
        return Camp.SANKYODAI;
    }
}

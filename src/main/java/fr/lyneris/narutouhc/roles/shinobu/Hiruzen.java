package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hiruzen extends NarutoRole {

    public NarutoRoles getRole() {
        return NarutoRoles.HIRUZEN;
    }

    @Override
    public String getRoleName() {
        return "Hiruzen";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }
}

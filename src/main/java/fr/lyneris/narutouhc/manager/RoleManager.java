package fr.lyneris.narutouhc.manager;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RoleManager {

    private final NarutoUHC naruto;
    private final List<NarutoRoles> roles;
    private final HashMap<UUID, NarutoRole> playerRole;
    private final HashMap<UUID, Camp> playerCamp;

    public RoleManager(NarutoUHC naruto) {
        this.roles = new ArrayList<>();
        this.naruto = naruto;
        this.playerRole = new HashMap<>();
        this.playerCamp = new HashMap<>();
    }

    public NarutoUHC getNaruto() {
        return naruto;
    }

    public List<NarutoRoles> getRoles() {
        return roles;
    }

    public void addRole(NarutoRoles var1) {
        this.roles.add(var1);
    }

    public void removeRole(NarutoRoles var1) {
        this.roles.remove(var1);
    }

    public NarutoRole getRole(UUID var1) {
        return this.playerRole.get(var1);
    }

    public NarutoRole getRole(Player var1) {
        return this.getRole(var1.getUniqueId());
    }

    public void setRole(UUID var1, NarutoRole var2) {
        this.playerRole.put(var1, var2);
    }

    public void setRole(Player var1, NarutoRole var2) {
        this.setRole(var1.getUniqueId(), var2);
    }

    public Camp getCamp(UUID var1) {
        return this.playerCamp.get(var1);
    }

    public Camp getCamp(Player var1) {
        return getCamp(var1.getUniqueId());
    }

    public void setCamp(UUID var1, Camp var2) {
        if(Bukkit.getPlayer(var1) != null) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(Role.getRole(onlinePlayer) != null) Role.getRole(onlinePlayer).onAllPlayerCampChange(var2, Bukkit.getPlayer(var1));
            }
        }
        this.playerCamp.put(var1, var2);
    }

    public void setCamp(Player var1, Camp var2) {
        this.setCamp(var1.getUniqueId(), var2);
    }

}

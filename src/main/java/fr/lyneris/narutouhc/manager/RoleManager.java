package fr.lyneris.narutouhc.manager;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RoleManager {

    private final NarutoUHC naruto;
    private final List<NarutoRoles> roles;
    private final HashMap<UUID, NarutoRole> playerRole;

    public RoleManager(NarutoUHC naruto) {
        this.roles = new ArrayList<>();
        this.naruto = naruto;
        this.playerRole = new HashMap<>();
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
}

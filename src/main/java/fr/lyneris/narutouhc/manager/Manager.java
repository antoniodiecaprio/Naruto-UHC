package fr.lyneris.narutouhc.manager;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.*;

public class Manager {

    private final NarutoUHC naruto;
    private final World narutoWorld;
    private final Location kamuiSpawn;
    private boolean isDay;
    private final HashMap<String, UUID> death;

    public Manager(NarutoUHC naruto) {
        this.narutoWorld = new WorldCreator("kamui").createWorld();
        this.naruto = naruto;
        this.kamuiSpawn = new Location(narutoWorld, 25108, 14, 25015);
        this.isDay = false;
        this.death = new HashMap<>();
    }

    public HashMap<String, UUID> getDeath() {
        return death;
    }

    public Location getKamuiSpawn() {
        return kamuiSpawn;
    }

    public World getNarutoWorld() {
        return narutoWorld;
    }

    public NarutoUHC getNaruto() {
        return naruto;
    }

    public void distributeRoles() throws Exception {
        ArrayList<NarutoRoles> roles = new ArrayList<>(NarutoUHC.getNaruto().getRoleManager().getRoles());
        Collections.shuffle(roles);

        for (UUID player : UHC.getUhc().getGameManager().getPlayers()) {

            if(roles.size() == 0) break;

            NarutoRoles role = roles.get(0);
            NarutoUHC.getNaruto().getRoleManager().setRole(player, role.getNarutoRole().newInstance());
            NarutoRole roleInstance = NarutoUHC.getNaruto().getRoleManager().getRole(player);
            for (String s : roleInstance.getDescription()) {
                Bukkit.getPlayer(player).sendMessage(s);
            }
            roleInstance.onDistribute(Bukkit.getPlayer(player));
            roles.remove(role);
        }

    }

    public boolean isDay() {
        return isDay;
    }
    public void setDay(boolean day) {
        isDay = day;
    }
}

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
    private final HashMap<UUID, Integer> strength;
    private final HashMap<UUID, Integer> resistance;
    private final HashMap<UUID, Location> deathLocation;

    public Manager(NarutoUHC naruto) {
        this.narutoWorld = new WorldCreator("kamui").createWorld();
        this.naruto = naruto;
        this.kamuiSpawn = new Location(narutoWorld, 25108, 14, 25015);
        this.isDay = false;
        this.death = new HashMap<>();
        this.strength = new HashMap<>();
        this.resistance = new HashMap<>();
        this.deathLocation = new HashMap<>();
    }

    public HashMap<UUID, Location> getDeathLocation() {
        return deathLocation;
    }

    public HashMap<UUID, Integer> getStrength() {
        return strength;
    }

    public HashMap<UUID, Integer> getResistance() {
        return resistance;
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

    public void distributeRoles() {
        ArrayList<NarutoRoles> roles = new ArrayList<>(NarutoUHC.getNaruto().getRoleManager().getRoles());
        Collections.shuffle(roles);

        UHC.getUHC().getGameManager().getPlayers().stream().filter(uuid -> roles.size() != 0).forEach(uuid -> {
            NarutoRoles role = roles.get(0);
            try {
                NarutoUHC.getNaruto().getRoleManager().setRole(uuid, role.getNarutoRole().newInstance());
                NarutoUHC.getNaruto().getRoleManager().setCamp(uuid, NarutoUHC.getNaruto().getRoleManager().getRole(uuid).getCamp());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            NarutoRole roleInstance = NarutoUHC.getNaruto().getRoleManager().getRole(uuid);
            for (String s : roleInstance.getDescription()) {
                Bukkit.getPlayer(uuid).sendMessage(s);
            }
            roleInstance.onDistribute(Bukkit.getPlayer(uuid));
            roles.remove(role);
        });
    }

    public boolean isDay() {
        return isDay;
    }
    public void setDay(boolean day) {
        isDay = day;
    }
}

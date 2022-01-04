package fr.lyneris.narutouhc.manager;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Manager {

    private final NarutoUHC naruto;
    private final World narutoWorld;
    private final Location kamuiSpawn;
    private final Location jumpLocation;
    private final Location endJumpLocation;
    private final HashMap<String, UUID> death;
    private final HashMap<UUID, Integer> strength;
    private final HashMap<UUID, Integer> resistance;
    private final HashMap<UUID, Location> deathLocation;
    private final List<UUID> stuned = new ArrayList<>();
    private boolean isDay;

    public Manager(NarutoUHC naruto) {
        this.narutoWorld = new WorldCreator("kamui").createWorld();
        this.naruto = naruto;
        this.kamuiSpawn = new Location(narutoWorld, 25108, 14, 25015);
        this.isDay = false;
        this.death = new HashMap<>();
        this.strength = new HashMap<>();
        this.resistance = new HashMap<>();
        this.deathLocation = new HashMap<>();
        this.jumpLocation = new Location(narutoWorld, 25109, 13, 25015);
        this.endJumpLocation = new Location(narutoWorld, 25109, 20, 25015);
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

    public Location getJumpLocation() {
        return jumpLocation;
    }

    public Location getEndJumpLocation() {
        return endJumpLocation;
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

    public void distributeRoles() throws InstantiationException, IllegalAccessException {
        ArrayList<NarutoRoles> roles = new ArrayList<>(NarutoUHC.getNaruto().getRoleManager().getRoles());
        Collections.shuffle(roles);

        for (UUID uuid : UHC.getUHC().getGameManager().getPlayers()) {
            if (roles.size() != 0) {
                try {
                    NarutoRoles role = roles.get(0);
                    NarutoRole roleInstance = role.getNarutoRole().newInstance();
                    NarutoUHC.getNaruto().getRoleManager().setRole(uuid, roleInstance);
                    NarutoUHC.getNaruto().getRoleManager().setCamp(uuid, roleInstance.getCamp());
                    Bukkit.getPlayer(uuid).sendMessage(roleInstance.getDescription());

                    roleInstance.onDistribute(Bukkit.getPlayer(uuid));
                    roles.remove(role);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Tasks.runLater(() -> naruto.getChakra().setupChakras(), 10 * 20);
    }

    public void setStuned(UUID uuid, Boolean stuned, Integer duration) {
        if (stuned) {
            this.stuned.add(uuid);

            Player player = Bukkit.getPlayer(uuid);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, 100, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration * 20, 100, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration * 20, 200, false, false));

            Tasks.runLater(() -> this.stuned.remove(uuid), duration * 20);
        } else {
            this.stuned.remove(uuid);
        }
    }

    public void setStuned(Player player, Boolean stuned, Integer duration) {
        setStuned(player.getUniqueId(), stuned, duration);
    }

    public boolean isStuned(UUID uuid) {
        return this.stuned.contains(uuid);
    }

    public boolean isStuned(Player player) {
        return this.stuned.contains(player.getUniqueId());
    }

    public boolean isDay() {
        return isDay;
    }

    public void setDay(boolean day) {
        isDay = day;
    }
}

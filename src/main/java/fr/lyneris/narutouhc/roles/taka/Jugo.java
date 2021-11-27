package fr.lyneris.narutouhc.roles.taka;

import com.mojang.authlib.properties.Property;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.changer.IdentityChanger;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.scoreboard.Reflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jugo extends NarutoRole {

    public boolean seeSkins = true, inMarqueMaudite, orochimaruDead;
    public ScoreboardTeam scoreboardTeam;
    public int marqueCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.JUGO;
    }

    @Override
    public void resetCooldowns() {
        marqueCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (marqueCooldown > 0) marqueCooldown--;
    }

    @Override
    public String getRoleName() {
        return "Jugô";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onAllPlayerJoin(PlayerJoinEvent event, Player player) {
        if (Role.findPlayer(NarutoRoles.JUGO) == null) return;
        if (player.getUniqueId().equals(Role.findPlayer(NarutoRoles.JUGO).getUniqueId())) {
            if (Role.getAliveOnlinePlayers().contains(player)) {
                this.sendNametagsTeam(player);
                if (!seeSkins || inMarqueMaudite) {
                    this.hideFullyPlayers(player);
                    player.sendMessage(prefix("L'indentité des joueurs vous est brouillés."));
                }
            }
        } else {
            if (!seeSkins || inMarqueMaudite) {
                if (Role.findPlayer(NarutoRoles.JUGO) != null) {
                    this.hideFullyPlayer(Role.findPlayer(NarutoRoles.JUGO), player);
                }
            }
        }
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Marque Maudite")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.KIMIMARO);
        this.scoreboardTeam = new ScoreboardTeam(new Scoreboard(), "nametag");
        this.scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

        new JugoSkinsTask().runTaskTimer(narutoUHC, 20 * 60 * 20, 20 * 60 * 20);
        this.sendNametagsTeam(player);
    }

    @Override
    public Camp getCamp() {
        return Camp.OROCHIMARU;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (!Item.interactItem(event, "Marque Maudite")) return;

        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 1, false, false));

        if (player.hasPotionEffect(PotionEffectType.SPEED)) player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 1, false, false));

        player.setMaxHealth(player.getMaxHealth() + (3D * 2D));
        player.setHealth(player.getHealth() + (3D * 2D));

        this.marqueCooldown = 60 * 20;
        this.inMarqueMaudite = true;

        if (seeSkins) {
            this.hideFullyPlayers(player);
            player.sendMessage(prefix("L'indentité des joueurs vous est brouillé."));
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

                if (player.hasPotionEffect(PotionEffectType.SPEED))
                    player.removePotionEffect(PotionEffectType.SPEED);

                player.setMaxHealth(player.getMaxHealth() - (3D * 2D) - (1.5D * 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

                Jugo.this.inMarqueMaudite = false;
                if (Jugo.this.seeSkins) {
                    Jugo.this.deleteNametagsTeam(player);
                    Jugo.this.sendNametagsTeam(player);
                    Jugo.this.resetPlayersSkins(player, Jugo.this.getPlayersToHide().stream().collect(Collectors.toList()));
                    player.sendMessage(prefix("L'indentité des joueurs ne vous est plus brouillé."));
                }
            }
        }.runTaskLater(narutoUHC, 20 * 60 * 5);
        player.sendMessage(prefix("&fVous avez utilisé votre &aMarque Maudite&f."));

    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        if (Role.isRole(player, NarutoRoles.OROCHIMARU)) {
            Player jugo = Role.findPlayer(NarutoRoles.JUGO);
            if (jugo == null) return;

            this.orochimaruDead = true;
            narutoUHC.getRoleManager().setCamp(jugo.getUniqueId(), Camp.SOLO);
            jugo.sendMessage(prefix("&cOrochimaru &fest mort, vous allez connaître l'indentité de &aSasuke&f. Vous n'êtes également plus dans aucun camp."));
            Role.knowsRole(jugo, NarutoRoles.SASUKE);
        }

        if (Role.isRole(player, NarutoRoles.ITACHI)) {
            Player jugo = Role.findPlayer(NarutoRoles.JUGO);
            if (jugo == null) return;
            if (!orochimaruDead) return;

            jugo.sendMessage(prefix("&cItachi &fest mort. Vous appartenez donc au camp de l'&cAkatsuki&f."));
            narutoUHC.getRoleManager().setCamp(jugo.getUniqueId(), Camp.AKATSUKI);
            Role.knowsRole(jugo, NarutoRoles.NAGATO);
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    public void sendNametagsTeam(Player player) {
        PacketPlayOutScoreboardTeam packetCreate = new PacketPlayOutScoreboardTeam(this.scoreboardTeam, 0);
        Reflection.sendPacket(player, packetCreate);
    }

    public void hidePlayersNameTags(Player player, List<String> names) {
        PacketPlayOutScoreboardTeam packetPlayers = new PacketPlayOutScoreboardTeam(this.scoreboardTeam, names, 3);
        Reflection.sendPacket(player, packetPlayers);
    }

    public void hidePlayersSkins(Player player, List<Player> players) {
        for (Player target : players) {
            this.hidePlayerSkin(player, target);
        }
    }

    private void hidePlayerSkin(Player player, Player target) {
        IdentityChanger.changeSkinForPlayer(target, new SasukeSkin(), player, NarutoUHC.getNaruto().getNpcManager().getPlayerTextures(target));
    }

    private List<Player> getPlayersToHide() {
        return Role.getAliveOnlinePlayers()
                .stream()
                .filter(Player::isOnline)
                .filter(player -> !(player.getUniqueId().equals(Role.findPlayer(NarutoRoles.JUGO).getUniqueId())))
                .filter(player -> !(Role.isRole(player, NarutoRoles.SASUKE)))
                .filter(player -> !(Role.isRole(player, NarutoRoles.KIMIMARO)))
                .collect(Collectors.toList());
    }

    private List<String> getPlayersNamesToHide() {
        return this.getPlayersToHide()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }


    public void resetPlayersSkins(Player player, List<Player> players) {
        for (Player target : players) {
            IdentityChanger.changeSkinForPlayer(target, NarutoUHC.getNaruto().getNpcManager().getPlayerTextures(target), player, null);
        }
    }

    public void hideFullyPlayers(Player player) {
        this.hidePlayersSkins(player, this.getPlayersToHide());
        new BukkitRunnable() {

            @Override
            public void run() {
                Jugo.this.hidePlayersNameTags(player, getPlayersNamesToHide());
            }
        }.runTaskLater(NarutoUHC.getNaruto(), 11);
    }

    public void hideFullyPlayer(Player player, Player target) {
        this.hidePlayerSkin(player, target);
        new BukkitRunnable() {

            @Override
            public void run() {
                Jugo.this.hidePlayersNameTags(player, Jugo.this.getPlayersNamesToHide());
            }
        }.runTaskLater(NarutoUHC.getNaruto(), 11);
    }

    public void deleteNametagsTeam(Player player) {
        PacketPlayOutScoreboardTeam packetDelete = new PacketPlayOutScoreboardTeam(this.scoreboardTeam, 1);
        Reflection.sendPacket(player, packetDelete);
    }

    public void setSeeSkins(boolean seeSkins) {
        Player jugo = Role.findPlayer(NarutoRoles.JUGO);
        this.seeSkins = seeSkins;
        if (seeSkins) {
            if (jugo != null) {
                this.deleteNametagsTeam(jugo);
                this.sendNametagsTeam(jugo);
                this.resetPlayersSkins(jugo, new ArrayList<>(this.getPlayersToHide()));
            }
        } else {
            if (jugo != null) {
                this.hideFullyPlayers(jugo);
            }
        }
    }

    public static class SasukeSkin extends Property {

        public SasukeSkin() {
            super("textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTYyNjI4ODg5NDM2NiwKICAicHJvZmlsZUlkIiA6ICI0ODhiMWZiMTBmNjI0YTJhODM3MjUyNTE2OTVlMzUwYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJaZXJ0ZWsiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU2ZjM2YWJhN2Y5MDcxY2MzYjRmODkzYWE1NDliZjc1NTE2YmU4ZWRiZGEzNTM4MjY5ZGRmNzFhMmJkYWU0MyIKICAgIH0KICB9Cn0=",
                    "INFUAvn5X4eak04w4eN3VOrkBaMJbgXaQSt41a6cG0sqazo0W1E1vxcvHNyAXkwm2gUJDXdTfijH/KyeSwUoYX0NlVwopbX8EGlpr8LWNglTOAjJnaM7+HCCOoHUZHpMuEUYXwFidBsdSripevG9plc2kECoD0IJoSUtvxxbbswNcdguwWw10mOTHvZeeEMVe1Sj67mZ8XbWFPiLa9thurhSAC0H/0t+u9ndzDMjl+7NN6tj8LIZTTaRO/Z2Cra5eacAhMkjpcoNmRCtt4am4Cpy/a8eo5SdGC/BWZtmpZx0ZkpACQ0niAksOa/CRnhFD9GO8nj2VrjyDKMsHwOp4I6mEeKqL1ImNeM9H8l1PPRnZ/q+yQ/JNHug8tf66lTELrPa63LKYg61fp258oWJni3sxfp4kHLn0hEAVmn10xV1sLvauJRohuJ/z2BsJ8lI+xj2QSEzQ0XIdEOEtpCSQwQ8W2Ol7xlhXmJYk206l0Lh90xR+6Z7l+DSGdDOzN8PNNISMGRzK0maaSQE7IbfK+wDnEfsmjgvWXuBXCY1CXXtClgBhZYM9U96doGq/qKnhWIteFd1rrwtoPROamMnGyZJ0nTXIkM9466xINsI8dwZEtaTopsjdPow/2zbPAdoWLoyY6vRiovmyE4r2BUG/8kV3ofmueIRr+y5tE+Ahvc=");
        }
    }

    public class JugoSkinsTask extends BukkitRunnable {

        @Override
        public void run() {
            Player jugoPlayer = Role.findPlayer(NarutoRoles.JUGO);
            if (jugoPlayer != null) {
                jugoPlayer.sendMessage(prefix("L'indentité des joueurs vous est brouillé."));
            }

            setSeeSkins(false);

            new BukkitRunnable() {

                @Override
                public void run() {
                    Player jugoPlayer = Role.findPlayer(NarutoRoles.JUGO);
                    if (jugoPlayer != null) {
                        jugoPlayer.sendMessage(prefix("L'indentité des joueurs ne vous est plus brouillé."));
                    }
                    setSeeSkins(true);
                }
            }.runTaskLater(narutoUHC, 20 * 3 * 60);
        }
    }

}

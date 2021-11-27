package fr.lyneris.narutouhc.packet.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.lyneris.narutouhc.NarutoUHC;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {
    private final Location loc;
    private final Property textures;
    private final NPCInteractEvent event;
    private final List<UUID> viewers;
    private String name;
    private EntityPlayer player;
    private EntityLiving vehicle;

    public NPC(final String name, final Location loc, final Property skin, final NPCInteractEvent event) {
        this.name = name;
        this.loc = loc;
        this.textures = skin;
        this.event = event;
        this.viewers = new ArrayList<UUID>();
        this.create();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Location getLocation() {
        return this.loc;
    }

    public NPCInteractEvent getEvent() {
        return this.event;
    }

    public EntityPlayer getEntity() {
        return this.player;
    }

    public NPC create() {
        final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        final WorldServer world = ((CraftWorld) this.loc.getWorld()).getHandle();
        final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), this.name);
        if (this.textures != null) {
            gameProfile.getProperties().removeAll("textures");
            gameProfile.getProperties().removeAll("texture");
            gameProfile.getProperties().put("textures", this.textures);
            gameProfile.getProperties().put("texture", this.textures);
        }
        (this.player = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world))).setLocation(this.loc.getX(), this.loc.getY(), this.loc.getZ(), this.loc.getYaw(), this.loc.getPitch());
        this.player.getDataWatcher().watch(10, (Object) (-1));
        return this;
    }

    public void sendNPC() {
        Bukkit.getOnlinePlayers().forEach(this::sendNPC);
    }

    public void sendNPC(final Player player) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.player));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.player));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.player, (byte) (this.player.yaw * 256.0f / 360.0f)));
        if (this.vehicle != null) {
            this.sitNPC(player);
        }
        this.addViewer(player.getUniqueId());
        new BukkitRunnable() {
            public void run() {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, NPC.this.player));
            }
        }.runTaskLater(NarutoUHC.getNaruto(), 20L);
    }

    public void removeNPC() {
        Bukkit.getOnlinePlayers().forEach(this::removeNPC);
    }

    public void removeNPC(final Player player) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.player));
        connection.sendPacket(new PacketPlayOutEntityDestroy(this.player.getId()));
        this.removeViewer(player.getUniqueId());
    }

    public void killNPC() {
        Bukkit.getOnlinePlayers().forEach(this::killNPC);
    }

    public void killNPC(final Player player) {
        final PacketPlayOutEntityStatus packetStatus = new PacketPlayOutEntityStatus(this.player, (byte) 3);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetStatus);
    }

    public void sitNPC() {
        if (this.vehicle == null) {
            (this.vehicle = new EntitySquid(this.getEntity().world)).setLocation(this.loc.getX(), this.loc.getY() - 0.9, this.loc.getZ(), this.loc.getYaw(), this.loc.getPitch());
            this.vehicle.setInvisible(true);
        }
        Bukkit.getOnlinePlayers().forEach(this::sitNPC);
    }

    public void sitNPC(final Player player) {
        if (this.vehicle == null) {
            (this.vehicle = new EntitySquid(this.getEntity().world)).setLocation(this.loc.getX(), this.loc.getY() - 0.9, this.loc.getZ(), this.loc.getYaw(), this.loc.getPitch());
            this.vehicle.setInvisible(true);
        }
        final PacketPlayOutSpawnEntityLiving spawnVehicle = new PacketPlayOutSpawnEntityLiving(this.vehicle);
        final PacketPlayOutEntityHeadRotation headVehicle = new PacketPlayOutEntityHeadRotation(this.vehicle, (byte) (this.player.yaw * 256.0f / 360.0f));
        final PacketPlayOutAttachEntity attachEntity = new PacketPlayOutAttachEntity(0, this.getEntity(), this.vehicle);
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(spawnVehicle);
        connection.sendPacket(headVehicle);
        connection.sendPacket(attachEntity);
    }

    public void unsitNPC() {
        Bukkit.getOnlinePlayers().forEach(this::unsitNPC);
    }

    public void unsitNPC(final Player player) {
        final PacketPlayOutAttachEntity dettachEntity = new PacketPlayOutAttachEntity(0, this.getEntity(), null);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(dettachEntity);
        this.vehicle = null;
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

    public void update(final Player player) {
        if (player.getLocation().distance(this.loc) > 128.0) {
            this.removeNPC(player);
        } else if (!this.viewers.contains(player.getUniqueId())) {
            this.sendNPC(player);
        }
    }

    public void addViewer(final UUID uuid) {
        if (!this.viewers.contains(uuid)) {
            this.viewers.add(uuid);
        }
    }

    public void removeViewer(final UUID uuid) {
        this.viewers.remove(uuid);
    }
}

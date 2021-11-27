package fr.lyneris.narutouhc.packet;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.packet.npc.NPC;
import fr.lyneris.narutouhc.packet.npc.NPCInteractEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NPCManager implements PacketListener {
    private final Map<String, NPC> npcRegistereds;

    public NPCManager(final PacketManager packetManager) {
        this.npcRegistereds = new HashMap<String, NPC>();
        packetManager.addListener(this);
        new BukkitRunnable() {
            public void run() {
                for (final NPC npc : NPCManager.this.npcRegistereds.values()) {
                    npc.update();
                }
            }
        }.runTaskTimer(NarutoUHC.getNaruto(), 0L, 20L);
    }

    @Override
    public void recievePacket(final Player player, final Packet<?> packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetUse = (PacketPlayInUseEntity) packet;
            final PacketPlayInUseEntity.EnumEntityUseAction action = packetUse.a();
            if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT || action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final int entityID = (int) this.getValue(packetUse, "a");
                final Optional<NPC> optionalNPC = this.getNPCWithEntityID(entityID);
                if (optionalNPC.isPresent()) {
                    final NPC npc = optionalNPC.get();
                    if (npc.getEvent() != null) {
                        npc.getEvent().onClick(player, npc);
                        if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
                            npc.getEvent().onRightClick(player, npc);
                        }
                        if (action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                            npc.getEvent().onLeftClick(player, npc);
                        }
                    }
                }
            }
        }
    }

    public NPC addNPC(final String key, final String name, final Location loc, final Property skin, final NPCInteractEvent event) {
        final NPC npc = new NPC(name, loc, skin, event);
        this.npcRegistereds.put(key, npc);
        return npc;
    }

    public void deleteNPC(final String key) {
        this.npcRegistereds.remove(key).removeNPC();
    }

    public void deleteNPCs() {
        this.npcRegistereds.values().forEach(NPC::removeNPC);
    }

    public void onJoin(final Player player) {
        for (final NPC npc : this.npcRegistereds.values()) {
            npc.sendNPC(player);
        }
    }

    public void onQuit(final Player player) {
        for (final NPC npc : this.npcRegistereds.values()) {
            npc.removeNPC(player);
        }
    }

    public Property getPlayerTextures(final Player player) {
        return new ArrayList<Property>(((CraftPlayer) player).getHandle().getProfile().getProperties().get("textures")).get(0);
    }

    public Property getPlayerTextures(final String name) {
        try {
            final URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            final InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            final String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();
            final URL url_2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final InputStreamReader reader_2 = new InputStreamReader(url_2.openStream());
            final String propertyJson = new JsonParser().parse(reader_2).getAsJsonObject().get("properties").getAsJsonArray().get(0).toString();
            return (Property) new Gson().fromJson(propertyJson, (Class) Property.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getValue(final Object instance, final String name) {
        Object result = null;
        try {
            final Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Optional<NPC> getNPCWithEntityID(final int id) {
        return this.npcRegistereds.values().stream().filter(npc -> npc.getEntity().getId() == id).findAny();
    }
}

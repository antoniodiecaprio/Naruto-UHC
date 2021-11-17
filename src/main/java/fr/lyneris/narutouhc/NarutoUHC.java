package fr.lyneris.narutouhc;

import fr.lyneris.narutouhc.commands.NarutoCommand;
import fr.lyneris.narutouhc.events.NarutoListener;
import fr.lyneris.narutouhc.manager.Manager;
import fr.lyneris.narutouhc.manager.RoleManager;
import fr.lyneris.narutouhc.module.NarutoModule;
import fr.lyneris.narutouhc.packet.NPCManager;
import fr.lyneris.narutouhc.packet.PacketManager;
import fr.lyneris.narutouhc.packet.npc.NPC;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.game.config.WorldGeneration;
import fr.lyneris.uhc.module.Module;
import org.bukkit.plugin.java.JavaPlugin;

public class NarutoUHC extends JavaPlugin {

    private static NarutoUHC naruto;
    private Manager manager;
    private Module module;
    private RoleManager roleManager;
    private NPCManager npcManager;
    private PacketManager packetManager;


    @Override
    public void onEnable() {

        this.module = new NarutoModule();
        UHC.getUHC().setModule(module);

        UHC.getUHC().getGameManager().getGameConfiguration().setRolesTime(5);
        UHC.getUHC().getGameManager().getGameConfiguration().setPvpTime(5);

        WorldGeneration.setFinished(true);

        naruto = this;
        this.roleManager = new RoleManager(this);
        this.manager = new Manager(this);
        this.packetManager = new PacketManager();
        this.npcManager = new NPCManager(this.packetManager);

        this.getServer().getPluginManager().registerEvents(new NarutoListener(), this);
        this.getCommand("ns").setExecutor(new NarutoCommand());

    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public Manager getManager() {
        return manager;
    }

    public static NarutoUHC getNaruto() {
        return naruto;
    }

    public Module getModule() {
        return module;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }
}

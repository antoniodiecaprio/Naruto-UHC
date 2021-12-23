package fr.lyneris.narutouhc;

import fr.lyneris.narutouhc.biju.Biju;
import fr.lyneris.narutouhc.biju.BijuListener;
import fr.lyneris.narutouhc.biju.Bijus;
import fr.lyneris.narutouhc.commands.BoostCommand;
import fr.lyneris.narutouhc.commands.NarutoCommand;
import fr.lyneris.narutouhc.commands.RevealCommand;
import fr.lyneris.narutouhc.events.NarutoListener;
import fr.lyneris.narutouhc.jubi.Jubi;
import fr.lyneris.narutouhc.manager.Manager;
import fr.lyneris.narutouhc.manager.RoleManager;
import fr.lyneris.narutouhc.module.Chakra;
import fr.lyneris.narutouhc.module.Hokage;
import fr.lyneris.narutouhc.module.NarutoGui;
import fr.lyneris.narutouhc.module.NarutoModule;
import fr.lyneris.narutouhc.packet.NPCManager;
import fr.lyneris.narutouhc.packet.PacketManager;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.roles.jubi.Madara;
import fr.lyneris.narutouhc.roles.shinobu.Hiruzen;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.game.config.WorldGeneration;
import fr.lyneris.uhc.module.Module;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NarutoUHC extends JavaPlugin {

    private static NarutoUHC naruto;
    private Manager manager;
    private Module module;
    private RoleManager roleManager;
    private NPCManager npcManager;
    private PacketManager packetManager;
    private Hokage hokage;
    private Chakra chakra;
    private BijuListener bijuListener;
    private Jubi jubi;
    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static NarutoUHC getNaruto() {
        return naruto;
    }

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
        this.hokage = new Hokage(this);
        this.chakra = new Chakra(this);
        this.jubi = new Jubi(this);
        Bijus.initBijus();
        this.bijuListener = new BijuListener();

        this.getServer().getPluginManager().registerEvents(new NarutoListener(), this);
        this.getServer().getPluginManager().registerEvents(new Madara(), this);
        this.getServer().getPluginManager().registerEvents(new NarutoGui(), this);
        this.getServer().getPluginManager().registerEvents(new Hiruzen(), this);
        this.getServer().getPluginManager().registerEvents(chakra, this);
        this.getServer().getPluginManager().registerEvents(bijuListener, this);
        this.getServer().getPluginManager().registerEvents(new Reach(), this);

        this.getCommand("ns").setExecutor(new NarutoCommand());
        this.getCommand("reveal").setExecutor(new RevealCommand());
        this.getCommand("boost").setExecutor(new BoostCommand());

        initRecipies();
    }

    public void initRecipies() {
        ShapedRecipe jubi = new ShapedRecipe(this.getJubi().getItem());
        jubi.shape(".*;", "!%#", ":::");
        jubi.setIngredient('.', Bijus.MATABI.getBiju().getItem().getData());
        jubi.setIngredient('*', Bijus.ISOBU.getBiju().getItem().getData());
        jubi.setIngredient(';', Bijus.SONGOKU.getBiju().getItem().getData());
        jubi.setIngredient('!', Bijus.KOKUO.getBiju().getItem().getData());
        jubi.setIngredient('%', Bijus.SAIKEN.getBiju().getItem().getData());
        jubi.setIngredient('#', Bijus.CHOMEI.getBiju().getItem().getData());
        this.getServer().addRecipe(jubi);
    }

    public Chakra getChakra() {
        return chakra;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public Manager getManager() {
        return manager;
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

    public Hokage getHokage() {
        return hokage;
    }

    public static ThreadLocalRandom getRandom() {
        return RANDOM;
    }

    public BijuListener getBijuListener() {
        return bijuListener;
    }

    public Jubi getJubi() {
        return jubi;
    }
}

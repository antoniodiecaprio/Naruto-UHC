package fr.lyneris.narutouhc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.lyneris.narutouhc.biju.BijuListener;
import fr.lyneris.narutouhc.biju.Bijus;
import fr.lyneris.narutouhc.commands.BoostCommand;
import fr.lyneris.narutouhc.commands.NarutoCommand;
import fr.lyneris.narutouhc.commands.RevealCommand;
import fr.lyneris.narutouhc.events.NarutoListener;
import fr.lyneris.narutouhc.gui.TimersMenu;
import fr.lyneris.narutouhc.jubi.Jubi;
import fr.lyneris.narutouhc.manager.Manager;
import fr.lyneris.narutouhc.manager.RoleManager;
import fr.lyneris.narutouhc.module.ChakraManager;
import fr.lyneris.narutouhc.module.Hokage;
import fr.lyneris.narutouhc.module.NarutoGui;
import fr.lyneris.narutouhc.module.NarutoModule;
import fr.lyneris.narutouhc.packet.NPCManager;
import fr.lyneris.narutouhc.packet.PacketManager;
import fr.lyneris.narutouhc.packet.Reach;
import fr.lyneris.narutouhc.pierre.PierreTombaleManager;
import fr.lyneris.narutouhc.roles.jubi.Madara;
import fr.lyneris.narutouhc.roles.shinobu.Hiruzen;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.module.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class NarutoUHC extends JavaPlugin {

    private static NarutoUHC naruto;
    private Manager manager;
    private Module module;
    private RoleManager roleManager;
    private NPCManager npcManager;
    private PacketManager packetManager;
    private Hokage hokage;
    private ChakraManager chakra;
    private BijuListener bijuListener;
    private Jubi jubi;
    private PierreTombaleManager pierreTombaleManager;
    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static NarutoUHC getNaruto() {
        return naruto;
    }

    @Override
    public void onEnable() {

        this.module = new NarutoModule();
        UHC.getUHC().setModule(module);

        naruto = this;
        this.roleManager = new RoleManager(this);
        this.manager = new Manager(this);
        this.packetManager = new PacketManager();
        this.npcManager = new NPCManager(this.packetManager);
        this.hokage = new Hokage(this);
        this.chakra = new ChakraManager(this);
        this.jubi = new Jubi(this);
        this.bijuListener = new BijuListener();
        this.pierreTombaleManager = new PierreTombaleManager(this);

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

        UHC.getUHC().registerMenu(new TimersMenu());

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

    public PierreTombaleManager getPierreTombaleManager() {
        return pierreTombaleManager;
    }

    public ChakraManager getChakra() {
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

    public String[] getFromName(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new String[] {texture, signature};
        } catch (IOException e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            return null;
        }
    }

    public static void usePower(Player player) {
        Role.getAliveOnlinePlayers().forEach(player1 -> Role.getRole(player1).onAllPlayerPowerUse(player));
    }

}

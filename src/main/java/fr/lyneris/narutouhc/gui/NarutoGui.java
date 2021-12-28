package fr.lyneris.narutouhc.gui;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.gui.config.ConfigurationMenu;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.inventory.CustomInventory;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static fr.lyneris.narutouhc.crafter.Camp.*;

public class NarutoGui implements CustomInventory {
    @Override
    public String getName() {
        return "Mode de Jeu";
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        HashMap<Camp, Integer> roles = new HashMap<>();
        HashMap<Camp, Integer> enabledRoles = new HashMap<>();

        for (NarutoRoles value : NarutoRoles.values()) {
            if (value.getNarutoRole() != null) {
                roles.put(value.getCamp(), roles.getOrDefault(value.getCamp(), 0) + 1);
                if(NarutoUHC.getNaruto().getRoleManager().getRoles().contains(value)) {
                    enabledRoles.put(value.getCamp(), enabledRoles.getOrDefault(value.getCamp(), 0) + 1);
                }
            }
        }

        List<String> lore = new ArrayList<>();
        roles.keySet().forEach(camp -> lore.add(CC.prefix(camp.getFormat() + "§8: §f" + enabledRoles.getOrDefault(camp, 0) + "/" + roles.getOrDefault(camp, 0))));

        for (int i : Utils.getGlassInInventory(getRows())) {
            slots[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName(" ").toItemStack();
        }
        slots[12] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(SHINOBI.getHash()).setLore(getLore()).setName(Camp.SHINOBI.getFormat()).toItemStack();
        slots[22] = new ItemBuilder(Material.WATCH).setName("§6Rôles activés").setLore(lore).toItemStack();
        slots[14] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(AKATSUKI.getHash()).setLore(getLore()).setName(Camp.AKATSUKI.getFormat()).toItemStack();

        slots[21] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(OROCHIMARU.getHash()).setLore(getLore()).setName(Camp.OROCHIMARU.getFormat()).toItemStack();
        slots[13] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(TAKA.getHash()).setLore(getLore()).setName(TAKA.getFormat()).toItemStack();
        slots[23] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(MADARA_OBITO.getHash()).setLore(getLore()).setName(Camp.MADARA_OBITO.getFormat()).toItemStack();

        slots[30] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(ZABUZA_HAKU.getHash()).setLore(getLore()).setName(Camp.ZABUZA_HAKU.getFormat()).toItemStack();
        slots[31] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(SANKYODAI.getHash()).setLore(getLore()).setName(Camp.SANKYODAI.getFormat()).toItemStack();
        slots[32] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(SOLO.getHash()).setLore(getLore()).setName(Camp.SOLO.getFormat()).toItemStack();

        slots[18] = new ItemBuilder(Material.COMPASS).setName("§6Timers").toItemStack();

        slots[40] = new ItemBuilder(Material.ARROW).setName("§6§l« §eRetour").toItemStack();

        return () -> slots;
    }

    public List<String> getLore() {
        return Arrays.asList("", "§8» §7Cliquez-ici pour y accéder");
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack is, int i, boolean b, boolean b1) throws InstantiationException, IllegalAccessException {
        if(is.getType() == Material.ARROW) {
            UHC.getUHC().getGameManager().getGuiManager().open(player, ConfigurationMenu.class);
            return;
        }

        if(is.getType() == Material.COMPASS) {
            UHC.getUHC().getGameManager().getGuiManager().open(player, TimersMenu.class);
            return;
        }

        Camp camp = null;

        for (Camp value : Camp.values()) {
            if (is.hasItemMeta() && is.getItemMeta().getDisplayName().equalsIgnoreCase(value.getFormat())) {
                camp = value;
            }
        }

        if (camp != null) {
            new fr.lyneris.narutouhc.module.NarutoGui.CampSelector(player, camp);
        }
    }

    @Override
    public int getRows() {
        return 5;
    }
}

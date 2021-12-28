package fr.lyneris.narutouhc.gui;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.uhc.UHC;
import fr.lyneris.uhc.gui.config.RulesMenu;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.inventory.CustomInventory;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class TimersMenu implements CustomInventory {
    @Override
    public String getName() {
        return "Activation PvP";
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        slots[getRows()*9-5] = new ItemBuilder(Material.ARROW).setName("§6§l« §eRetour").toItemStack();

        slots[0] = new ItemBuilder(Material.BANNER).setDurability(1).setName("§c-10m").toItemStack();
        slots[1] = new ItemBuilder(Material.BANNER).setDurability(14).setName("§6-5m").toItemStack();
        slots[2] = new ItemBuilder(Material.BANNER).setDurability(11).setName("§a-30s").toItemStack();

        slots[4] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA2ZTM0YzFjOTRjN2I4MjljNTlkMDFjNzQ1M2Q1ZjNlODI1OWYzODljMmFjYTJmYTMxNGRjYTQwODY5M2NlIn19fQ="
        ).setName("§8» §7Annonce des Rôles: §f" + Utils.getFormattedTime(UHC.getUHC().getGameManager().getGameConfiguration().getRolesTime())).toItemStack();

        slots[6] = new ItemBuilder(Material.BANNER).setDurability(11).setName("§a+30s").toItemStack();
        slots[7] = new ItemBuilder(Material.BANNER).setDurability(14).setName("§6+5m").toItemStack();
        slots[8] = new ItemBuilder(Material.BANNER).setDurability(1).setName("§c+10m").toItemStack();

        slots[9] = new ItemBuilder(Material.BANNER).setDurability(1).setName("§c-10m").toItemStack();
        slots[10] = new ItemBuilder(Material.BANNER).setDurability(14).setName("§6-5m").toItemStack();
        slots[11] = new ItemBuilder(Material.BANNER).setDurability(11).setName("§a-30s").toItemStack();

        slots[13] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU1MTY5YWYzNWViMjJkODZiNzY4MzY2NjI3N2ZkMTlkZTg0YjUyYzU4N2Y2M2I4ZGI4Y2U5NTIxNWFkMSJ9fX0="
        ).setName("§8» §7Annonce du Hokage: §f" + Utils.getFormattedTime(NarutoUHC.getNaruto().getHokage().getHokageTimer())).toItemStack();

        slots[15] = new ItemBuilder(Material.BANNER).setDurability(11).setName("§a+30s").toItemStack();
        slots[16] = new ItemBuilder(Material.BANNER).setDurability(14).setName("§6+5m").toItemStack();
        slots[17] = new ItemBuilder(Material.BANNER).setDurability(1).setName("§c+10m").toItemStack();

        slots[18] = new ItemBuilder(Material.BANNER).setDurability(1).setName("§c-10m").toItemStack();
        slots[19] = new ItemBuilder(Material.BANNER).setDurability(14).setName("§6-5m").toItemStack();
        slots[20] = new ItemBuilder(Material.BANNER).setDurability(11).setName("§a-30s").toItemStack();

        slots[22] = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4ZmQwYmM3ZTUyOTA0NzQ3MWIwZmI3MTY0Mjg0NTU2NjY2NGFlMmJkN2JmZmNlMGUxNzNjOTUyNWEwNTIzOSJ9fX0="
        ).setName("§8» §7Biju: §f" + Utils.getFormattedTime(NarutoUHC.getNaruto().getBijuListener().getBijuStart())).toItemStack();

        slots[24] = new ItemBuilder(Material.BANNER).setDurability(11).setName("§a+30s").toItemStack();
        slots[25] = new ItemBuilder(Material.BANNER).setDurability(14).setName("§6+5m").toItemStack();
        slots[26] = new ItemBuilder(Material.BANNER).setDurability(1).setName("§c+10m").toItemStack();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack current, int slot, boolean isLeftClick, boolean isRightClick) {

        if (current.getType() == Material.ARROW) {
            UHC.getUHC().getGameManager().getGuiManager().open(player, NarutoGui.class);
            return;
        }

        switch (slot) {
            case 0:
                UHC.getUHC().getGameManager().getGameConfiguration().removeRolesTime(10*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 1:
                UHC.getUHC().getGameManager().getGameConfiguration().removeRolesTime(5*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 2:
                UHC.getUHC().getGameManager().getGameConfiguration().removeRolesTime(30);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 6:
                UHC.getUHC().getGameManager().getGameConfiguration().addRolesTime(30);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 7:
                UHC.getUHC().getGameManager().getGameConfiguration().addRolesTime(5*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 8:
                UHC.getUHC().getGameManager().getGameConfiguration().addRolesTime(10*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;

            case 9:
                NarutoUHC.getNaruto().getHokage().removeHokageTime(10*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 10:
                NarutoUHC.getNaruto().getHokage().removeHokageTime(5*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 11:
                NarutoUHC.getNaruto().getHokage().removeHokageTime(30);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 15:
                NarutoUHC.getNaruto().getHokage().addHokageTime(30);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 16:
                NarutoUHC.getNaruto().getHokage().addHokageTime(5*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 17:
                NarutoUHC.getNaruto().getHokage().addHokageTime(10*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;

            case 18:
                NarutoUHC.getNaruto().getBijuListener().removeBijuStart(10*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 19:
                NarutoUHC.getNaruto().getBijuListener().removeBijuStart(5*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 20:
                NarutoUHC.getNaruto().getBijuListener().removeBijuStart(30);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 24:
                NarutoUHC.getNaruto().getBijuListener().addBijuStart(30);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 25:
                NarutoUHC.getNaruto().getBijuListener().addBijuStart(5*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
            case 26:
                NarutoUHC.getNaruto().getBijuListener().addBijuStart(10*60);
                UHC.getUHC().getGameManager().getGuiManager().open(player, this.getClass());
                break;
        }

    }

    @Override
    public int getRows() {
        return 4;
    }
}

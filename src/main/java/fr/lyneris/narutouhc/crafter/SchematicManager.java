package fr.lyneris.narutouhc.crafter;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.*;

import java.io.File;

public class SchematicManager {

    @SuppressWarnings("deprecation")
    public static void pasteSchematic(String path, int x, int y, int z) {
        try {
            CuboidClipboard clipBoard = SchematicFormat.MCEDIT.load(new File(path));
            for (int i = 0; i < 20; i++) {
                Location location = new Location(Bukkit.getWorld("uhc_world"), x, y, z);
                clipBoard.paste(new EditSession(BukkitUtil.getLocalWorld(location.getWorld()), Integer.MAX_VALUE), BukkitUtil.toVector(location), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pasteSchematic(String path, Location location) {
        pasteSchematic(path, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

}

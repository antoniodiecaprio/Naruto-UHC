package fr.lyneris.narutouhc.utils;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messages {

    public static String cooldown(int var1) {
        return "§7▎ §cVous êtes encore sur un cooldown de " + var1 + " secondes.";
    }

    public static Cooldown getCooldown(int var1) {
        return new Cooldown("§7▎ §cVous êtes encore sur un cooldown de " + var1 + " secondes.");
    }

    public static String offline(String var1) {
        return "§7▎ §c" + var1 + " n'est pas connecté.";
    }

    public static String syntax(String var1) {
        return "§7▎ §cMauvaise utilisation, utilisez " + var1;
    }

    public static void sendDeathMessage(Player player) {
        NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
        if (role == null) {
            Bukkit.broadcastMessage(CC.CC_BAR);
            Bukkit.broadcastMessage("§7▎ §fLe joueur §c" + player.getName() + " §fest mort.");
            Bukkit.broadcastMessage("§7▎ §fIl n'avait pas de rôle");
            Bukkit.broadcastMessage(CC.CC_BAR);
            return;
        }
        Bukkit.broadcastMessage(CC.CC_BAR);
        Bukkit.broadcastMessage("§7▎ §fLe joueur §c" + player.getName() + " §fest mort.");
        Bukkit.broadcastMessage("§7▎ §fIl était " + Role.getCamp(player).getColor() + role.getRoleName() + "§f.");
        Bukkit.broadcastMessage(CC.CC_BAR);
    }

    public static String not(String var1) {
        return "§7▎ §cVous n'êtes pas " + var1;
    }
}

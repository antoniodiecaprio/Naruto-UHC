package fr.lyneris.narutouhc.commands.naruto;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class MyRoleCommand {
    public MyRoleCommand(Player player, String[] args) {

        NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
        if (role == null || player.getGameMode().equals(GameMode.SPECTATOR)) {
            player.sendMessage(CC.prefix("&cVous n'avez pas de rôle."));
            return;
        }

        player.sendMessage(role.getDescription());
    }
}

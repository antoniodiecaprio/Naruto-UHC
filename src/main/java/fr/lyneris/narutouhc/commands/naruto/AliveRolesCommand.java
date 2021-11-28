package fr.lyneris.narutouhc.commands.naruto;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.entity.Player;

public class AliveRolesCommand {

    public AliveRolesCommand(Player player, String[] args) {

        if (Role.getPlayersWithRole().size() == 0) {
            player.sendMessage(CC.prefix("Les rôles n'ont pas été attribués."));
            return;
        }

        player.sendMessage(CC.CC_BAR);
        for (Camp value : Camp.values()) {
            if (Role.getPlayersWithRole().stream().anyMatch(p -> Role.getCamp(p) == value)) {
                player.sendMessage(value.getColor() + value.getName());
                Role.getPlayersInCamp(value).forEach(p -> {
                    NarutoRole role = Role.getRole(p);
                    if (role != null) player.sendMessage(CC.prefix("&e" + role.getRoleName()));
                });
            }
        }
        player.sendMessage(CC.CC_BAR);

    }
}

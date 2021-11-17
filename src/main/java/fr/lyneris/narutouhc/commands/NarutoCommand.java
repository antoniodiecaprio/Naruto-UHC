package fr.lyneris.narutouhc.commands;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.commands.naruto.AliveRolesCommand;
import fr.lyneris.narutouhc.commands.naruto.LastWord;
import fr.lyneris.narutouhc.commands.naruto.MyRoleCommand;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NarutoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(!(commandSender instanceof Player)) return true;

        Player player = (Player) commandSender;

        if(args.length == 0) {
            //TODO HELP MESSAGE
            return true;
        }

        if(args[0].equalsIgnoreCase("me")) {
            new MyRoleCommand(player, args);
            return true;
        }

        if(args[0].equalsIgnoreCase("roles") || args[0].equalsIgnoreCase("role")) {
            new AliveRolesCommand(player, args);
            return true;
        }

        if(args[0].equalsIgnoreCase("resetcd")) {
            NarutoRole role = NarutoUHC.getNaruto().getRoleManager().getRole(player);
            if(role != null && player.isOp()) {
                role.resetCooldowns();
                return true;
            }
        }

        if(args[0].equalsIgnoreCase("lastword")) {
            new LastWord(player, args);
        }

        if(NarutoUHC.getNaruto().getRoleManager().getRole(player) != null) {
            NarutoUHC.getNaruto().getRoleManager().getRole(player).onSubCommand(player, args);
        }


        return false;
    }
}

package fr.lyneris.narutouhc.commands;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.commands.naruto.LastWord;
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
            //HELP MESSAGE
            return true;
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

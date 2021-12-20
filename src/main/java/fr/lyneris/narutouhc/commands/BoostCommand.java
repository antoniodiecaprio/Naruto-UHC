package fr.lyneris.narutouhc.commands;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoostCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) return true;

        NarutoUHC narutoUHC = NarutoUHC.getNaruto();
        Player player = (Player) commandSender;

        if (!player.getUniqueId().equals(NarutoUHC.getNaruto().getHokage().getHokage())) {
            player.sendMessage(CC.prefix("&cVous devez être le Hokage pour executer cette commande."));
            return true;
        }

        if(NarutoUHC.getNaruto().getHokage().isUsedBoost()) {
            player.sendMessage(CC.prefix("&cCe pouvoir a déjà été utilisé."));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Messages.syntax("/boost <joueur>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.offline(args[0]));
            return true;
        }

        narutoUHC.getManager().getStrength().put(target.getUniqueId(), narutoUHC.getManager().getStrength().getOrDefault(target.getUniqueId(), 0));
        narutoUHC.getManager().getResistance().put(target.getUniqueId(), narutoUHC.getManager().getResistance().getOrDefault(target.getUniqueId(), 0));

        player.sendMessage(CC.prefix("Vous avez donné &a+10% &fde &cForce &fet de &7Résistance &fà &a" + target.getName()));
        target.sendMessage(CC.prefix("Le &aHokage &fa décidé d'utiliser son &aboost &fsur vous. De ce fait vous obtenez &a+10% &fde &cForce &fet de &7Résistance&f."));
        NarutoUHC.getNaruto().getHokage().setUsedBoost(true);
        return false;
    }
}

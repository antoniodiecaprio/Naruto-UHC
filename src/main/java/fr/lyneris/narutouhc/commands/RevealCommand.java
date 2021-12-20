package fr.lyneris.narutouhc.commands;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RevealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return true;

        NarutoUHC narutoUHC = NarutoUHC.getNaruto();
        Player player = (Player) commandSender;

        if (!player.getUniqueId().equals(NarutoUHC.getNaruto().getHokage().getHokage())) {
            player.sendMessage(CC.prefix("&cVous devez être le Hokage pour executer cette commande."));
            return true;
        }

        if(NarutoUHC.getNaruto().getHokage().isUsedReveal()) {
            player.sendMessage(CC.prefix("&cCe pouvoir a déjà été utilisé."));
            return true;
        }

        List<String> list = new ArrayList<>();
        List<Player> online = new ArrayList<>(Role.getAliveOnlinePlayers());
        Collections.shuffle(online);
        for (Player players : online) {
            if(Role.getCamp(players) == Camp.SHINOBI) {
                if(list.size() == 2) break;
                list.add(players.getName());
                online.remove(players);
            }
        }
        for (Player players : online) {
            if(Role.getCamp(players) != Camp.SHINOBI) {
                list.add(players.getName());
                online.remove(players);
                break;
            }
        }
        for (Player players : online) {
            list.add(players.getName());
            online.remove(players);
            break;
        }

        player.sendMessage(CC.prefix("&fVoici la liste des &a4 &fpseudos:"));
        list.forEach(s1 -> player.sendMessage(" §8- §a" + s1));
        narutoUHC.getHokage().setUsedReveal(true);

        return false;
    }
}

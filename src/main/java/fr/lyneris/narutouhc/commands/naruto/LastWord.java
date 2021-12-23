package fr.lyneris.narutouhc.commands.naruto;

import fr.lyneris.narutouhc.roles.shinobu.Ino;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LastWord {

    public LastWord(Player player, String[] args) {
        if (Ino.transfer != null && Ino.transfer.equals(player.getUniqueId())) {
            if (args.length < 2) {
                player.sendMessage(Messages.syntax("/ns lastword <message>"));
                return;
            }

            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= args.length; i++) {
                builder.append(args[i]).append(" ");
            }

            Ino.nearPlayers.stream()
                    .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                    .map(Bukkit::getPlayer)
                    .forEach(target -> {
                        target.sendMessage(CC.prefix("§fDernière volontée de §a" + player.getName() + "§f:"));
                        target.sendMessage(CC.prefix("§a" + builder));
                    });

            player.sendMessage(CC.prefix("§aVotre dernière volontée à été envoyée avec succèes."));
            Ino.transfer = null;

        } else {
            player.sendMessage(CC.prefix("§cVous ne pouvez pas utiliser cette commande. Vérifiez que votre rôle vous le permet ou que votre pouvoir n'a pas expiré."));
        }
    }

}

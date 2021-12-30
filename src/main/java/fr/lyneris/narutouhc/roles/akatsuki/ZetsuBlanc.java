package fr.lyneris.narutouhc.roles.akatsuki;

import com.mojang.authlib.properties.Property;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.changer.IdentityChanger;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.Utils;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ZetsuBlanc extends NarutoRole {

    private String originalName, copiedName;
    private Property originalSkin, copiedSkin;
    private Chakra chakra = Chakra.KATON;
    private int metamorphoseCooldown = 0;
    private boolean usedReset = false;

    public NarutoRoles getRole() {
        return NarutoRoles.ZETSU_BLANC;
    }

    @Override
    public String getRoleName() {
        return "Zetsu Blanc";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §cZetsu Blanc\n" +
                "§7▎ Objectif: §rSon but est de gagner avec l'§cAkatsuki\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns métamorphose <Joueur>§7, celle-ci lui permet de prendre le skin et le pseudo de la personne et donc se faire passer pour le joueur ciblé, le pouvoir dure 5 minutes et il possède un délai de 20 minutes.\n" +
                "§e §f\n" +
                "§r→ /ns reset <Joueur>§7, celle-ci lui permet de mettre à 0 le délai d’un joueur, cependant il perd §c2 cœurs permanents§7 à l’utilisation et il peut le faire qu’une seule fois dans la partie.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose d’une nature de Chakra aléatoire. \n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        List<Chakra> chakras = new ArrayList<>(Arrays.asList(Chakra.values()));
        chakras.remove(Chakra.AUCUN);
        Collections.shuffle(chakras);

        this.chakra = chakras.get(0);

    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("métamorphose")) {
            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns métamorphose <player>"));
                return;
            }

            if (metamorphoseCooldown > 0) {
                player.sendMessage(Messages.cooldown(metamorphoseCooldown));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            String copiedName = target.getName();
            Property copiedSkin = NarutoUHC.getNaruto().getNpcManager().getPlayerTextures(target);


            setCopiedName(copiedName);
            setCopiedSkin(copiedSkin);

            IdentityChanger.changePlayerName(player, copiedName);
            IdentityChanger.changeSkin(player, copiedSkin, false);

            new MetamorphoseTask().runTaskTimer(narutoUHC, 0, 20);

            player.sendMessage(CC.prefix("§fVous êtes maintenant métamorphosé en §a" + target.getName() + "§f."));


            metamorphoseCooldown = 20 * 60;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (metamorphoseCooldown <= 0) {
                        player.sendMessage(CC.prefix("§fVotre cooldown de la commande §a/ns métamorphose §fvient d'expirer."));
                        cancel();
                    } else {
                        metamorphoseCooldown--;
                    }
                }
            }.runTaskTimer(NarutoUHC.getNaruto(), 0, 20);

        }

        if (args[0].equalsIgnoreCase("reset")) {

            if (args.length != 2) {
                player.sendMessage(Messages.syntax("/ns reset <player>"));
                return;
            }

            if (usedReset) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }
            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(CC.prefix("§fVous avez reset les délais de §a" + target.getName()));
            roleManager.getRole(target).resetCooldowns();
            target.sendMessage(CC.prefix("§fLe §aZetsu Blanc §fa reset vos cooldowns."));
            player.setMaxHealth(player.getMaxHealth() - 4);

            usedReset = true;

        }

    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getCopiedName() {
        return copiedName;
    }

    public void setCopiedName(String copiedName) {
        this.copiedName = copiedName;
    }

    public Property getOriginalSkin() {
        return originalSkin;
    }

    public void setOriginalSkin(Property originalSkin) {
        this.originalSkin = originalSkin;
    }

    public Property getCopiedSkin() {
        return copiedSkin;
    }

    public void setCopiedSkin(Property copiedSkin) {
        this.copiedSkin = copiedSkin;
    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }


    public class MetamorphoseTask extends BukkitRunnable {

        private int timer = 5 * 60;

        @Override
        public void run() {
            Player zetsuPlayer = Role.findPlayer(NarutoRoles.ZETSU_BLANC);
            if (timer == 0) {
                if (zetsuPlayer != null) {
                    IdentityChanger.changePlayerName(zetsuPlayer, getOriginalName());
                    IdentityChanger.changeSkin(zetsuPlayer, getOriginalSkin(), false);
                    setCopiedName(null);
                    setCopiedSkin(null);
                    zetsuPlayer.sendMessage(CC.prefix("§cVous avez perdu votre métamorphose."));
                }
                cancel();
            }
            if (zetsuPlayer != null) {
                Title.sendActionBar(zetsuPlayer, "§7▎ §aMétamorphose §f§l» §7" + Utils.getFormattedTime(this.timer));
            }
            timer--;
        }
    }


}

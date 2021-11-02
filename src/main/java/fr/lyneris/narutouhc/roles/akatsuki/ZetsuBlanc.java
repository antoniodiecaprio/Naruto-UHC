package fr.lyneris.narutouhc.roles.akatsuki;

import com.mojang.authlib.properties.Property;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.packet.changer.IdentityChanger;
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

    @Override
    public String getRoleName() {
        return "Zetsu Blanc";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
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
        if(args[0].equalsIgnoreCase("métamorphose")) {
            if(args.length != 2) {
                player.sendMessage(Messages.syntax("/ns métamorphose <player>"));
                return;
            }

            if(metamorphoseCooldown > 0) {
                player.sendMessage(Messages.cooldown(metamorphoseCooldown));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            String copiedName = target.getName();
            Property copiedSkin = NarutoUHC.getNaruto().getNpcManager().getPlayerTextures(target);


            setCopiedName(copiedName);
            setCopiedSkin(copiedSkin);

            IdentityChanger.changePlayerName(player, copiedName);
            IdentityChanger.changeSkin(player, copiedSkin, false);

            new MetamorphoseTask().runTaskTimer(narutoUHC, 0, 20);

            player.sendMessage("§7▎ §fVous êtes maintenant métamorphosé en §a" + target.getName() + "§f.");


            metamorphoseCooldown = 20*60;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(metamorphoseCooldown <= 0) {
                        player.sendMessage("§7▎ §fVotre cooldown de la commande §a/ns métamorphose §fvient d'expirer.");
                        cancel();
                    } else {
                        metamorphoseCooldown--;
                    }
                }
            }.runTaskTimer(NarutoUHC.getNaruto(), 0, 20);

        }

        if(args[0].equalsIgnoreCase("reset")) {

            if(args.length != 2) {
                player.sendMessage(Messages.syntax("/ns reset <player>"));
                return;
            }

            if(usedReset) {
                player.sendMessage("§7▎ §cVous avez déjà utilisé ce pouvoir.");
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            player.sendMessage("§7▎ §fVous avez reset les délais de §a" + target.getName());
            roleManager.getRole(target).resetCooldowns();
            target.sendMessage("§7▎ §fLe §aZetsu Blanc §fa reset vos cooldowns.");
            player.setMaxHealth(player.getMaxHealth()-4);

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

        private int timer = 5*60;

        @Override
        public void run() {
            Player zetsuPlayer = Role.findPlayer("Zetsu Blanc");
            if(timer == 0) {
                if(zetsuPlayer != null){
                    IdentityChanger.changePlayerName(zetsuPlayer, getOriginalName());
                    IdentityChanger.changeSkin(zetsuPlayer, getOriginalSkin(), false);
                    setCopiedName(null);
                    setCopiedSkin(null);
                    zetsuPlayer.sendMessage("§7▎ §cVous avez perdu votre métamorphose.");
                }
                cancel();
            }
            if(zetsuPlayer != null){
                Title.sendActionBar(zetsuPlayer, "§7▎ §aMétamorphose §f§l» §7" + Utils.getFormattedTime(this.timer));
            }
            timer--;
        }
    }


}

package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Sakura extends NarutoRole {

    public int jutsuCooldown = 0;
    public boolean usingJutsu = false;
    private Player jutsuPlayer = null;

    @Override
    public void resetCooldowns() {
        jutsuCooldown = 0;
    }

    @Override
    public void startRunnableTask() {
        if(jutsuCooldown > 0) {
            jutsuCooldown--;
        }
    }
    
    @Override
    public String getRoleName() {
        return "Sakura";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(Arrays.asList("t'es sakura frérot", "ça va ?"));
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7*60*20, 0, false, false));
        Bukkit.getScheduler().runTaskTimer(NarutoUHC.getNaruto(), () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7*60*20, 0, false, false));
        }, 0, 11*20*60);
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }

    @Override
    public void onSubCommand(Player player, String[] args) {

        if(!args[0].equalsIgnoreCase("shosen")) return;

        if(args.length != 3) {
            player.sendMessage(Messages.syntax("/ns shosen jutsu <player>"));
            return;
        }

        if(jutsuCooldown > 0) {
            player.sendMessage(Messages.cooldown(jutsuCooldown));
            return;
        }

        if(!args[1].equalsIgnoreCase("jutsu")) return;

        Player target = Bukkit.getPlayer(args[2]);
        if(target == null) {
            player.sendMessage(Messages.offline(args[2]));
            return;
        }

        if(Objects.equals(target.getName(), player.getName())) {
            player.sendMessage("§7▎ §cVous ne pouvez pas utiliser ce pouvoir sur vous-même.");
            return;
        }

        if(target.getLocation().distance(player.getLocation()) > 5) {
            player.sendMessage("§7▎ §cCe joueur n'est pas à 5 blocks de vous.");
            return;
        }

        usingJutsu = true;
        jutsuPlayer = target;

        player.sendMessage("§7▎ §fVous avez utilisé votre §aJutsu §fsur §a" + target.getName() + "§f. Si un de vous deux bouge, §a" + target.getName() + " §fperdra son effet de §dRégénération§f.");
        target.sendMessage("§7▎ §aNaruto §fa utilisé son §aJutsu §fsur vous. Si un de vous deux bouge vous perdrez votre effet de §dRégénération§f.");
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2, false, false));

        jutsuCooldown = 10*60;

    }

    @Override
    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {

        Player sakura = Role.findPlayer("Sakura");
        Player jutsu = jutsuPlayer;

        if(!event.getPlayer().equals(sakura) && !event.getPlayer().equals(jutsu)) return;

        if(jutsu == null || sakura == null) return;
        if(!usingJutsu) return;

        jutsu.removePotionEffect(PotionEffectType.REGENERATION);
        jutsu.sendMessage("§7▎ §a" + event.getPlayer().getName() + " §fa bougé.");
        sakura.sendMessage("§7▎ §a" + event.getPlayer().getName() + " §fa bougé.");
        usingJutsu = false;
        jutsuPlayer = null;

    }

}

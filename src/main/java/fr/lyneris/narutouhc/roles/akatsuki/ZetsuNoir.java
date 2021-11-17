package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.UHC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ZetsuNoir extends NarutoRole {

    HashMap<String, Collection<PotionEffect>> effectsMap = new HashMap<>();

    @Override
    public String getRoleName() {
        return "Zetsu Noir";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {

    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if(args[0].equalsIgnoreCase("zetsu")) {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i <= 20; i++) {
                builder.append(args[i]).append(" ");
            }

            UHC.getUHC().getGameManager().getPlayers().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).forEach(target -> {
                target.sendMessage(CC.prefix("§aZetsu Noir §f§l» §7" + builder));
            });
            player.sendMessage(CC.prefix("§aZetsu Noir §f§l» §7" + builder));

        }

        if(args[0].equalsIgnoreCase("takealleffects")) {
            if(args.length != 2) {
                player.sendMessage(Messages.syntax("/ns takealleffects <player>"));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(Messages.offline(args[1]));
                return;
            }

            if(!effectsMap.containsKey(target.getName())) {
                player.sendMessage(CC.prefix("§cVous n'avez pas tué ce joueur."));
                return;
            }

            effectsMap.get(target.getName()).stream().filter(pe -> !player.hasPotionEffect(pe.getType())).forEach(player::addPotionEffect);

            player.sendMessage(CC.prefix("§fVous avez reçu tous les effets de §a" + target.getName()));
            effectsMap.remove(target.getName());

        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    private boolean correctBlock(Player player) {
        Block block = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock();
        return block.isEmpty() || block.getType() == Material.DIRT || block.getType() == Material.GRASS || block.getType() == Material.AIR || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2;
    }


    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {

        effectsMap.put(event.getEntity().getName(), event.getEntity().getActivePotionEffects());
        TextComponent text = new TextComponent("§7▎ §aCliquez-ici pour prendre les effets de ce joueur.");
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ns takealleffects " + event.getEntity().getName()));
        killer.spigot().sendMessage(text);

    }

    @Override
    public void onSecond(int timer, Player player) {
        PlayerInventory inv = player.getInventory();
        if(inv.getHelmet() == null && inv.getChestplate() == null && inv.getLeggings() == null && inv.getBoots() == null && correctBlock(player)) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 4*20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 4*20, 1, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }
}

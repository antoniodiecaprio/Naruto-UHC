package fr.lyneris.narutouhc.roles.zabuza_haku;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Haku extends NarutoRole {

    public boolean fourHearth = false;
    public int hyotonCooldown = 0;
    public boolean alreadyUsedHyoton;

    @Override
    public void resetCooldowns() {
        hyotonCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(hyotonCooldown > 0) hyotonCooldown--;
    }

    @Override
    public String getRoleName() {
        return "Haku";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.HAKU;
    }

    @Override
    public void onAllPlayerDamage(EntityDamageEvent event, Player entity) {
        if (getPlayer() != null && Role.isRole(entity, NarutoRoles.ZABUZA) && !fourHearth && entity.getHealth() <= 8) {
            TextComponent component = new TextComponent("&aZabuza &fest à moins de &c4 coeurs&f, cliquez-ici pour vous téléporter à sa position.");
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ns tpzabuza"));
            getPlayer().spigot().sendMessage(component);
        }
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event, Player player) {

        if (!event.getMessage().startsWith("!")) return;
        event.setCancelled(true);

        Player haku = Role.findPlayer(NarutoRoles.HAKU);
        if (haku == null) {
            player.sendMessage(prefix("&cHaku n'est pas dans la partie."));
            return;
        }

        String message = event.getMessage().substring(1);
        haku.sendMessage(prefix("&6&lZabuza&8: &7" + message));
        player.sendMessage(prefix("&6&lZabuza&8: &7" + message));
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("tpzabuza")) {
            if (fourHearth) {
                player.sendMessage(prefix("&cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            Player zabuza = Role.findPlayer(NarutoRoles.ZABUZA);
            if (zabuza == null) {
                player.sendMessage(prefix("&cZabuza n'est pas connecté."));
                return;
            }

            if (zabuza.getHealth() > 8) {
                player.sendMessage(prefix("&cZabuza n'est pas à moins de 4 coeurs."));
                return;
            }

            fourHearth = true;
            player.teleport(zabuza);
            player.sendMessage(prefix("&fVous vous êtes téléporté à &aZabuza&f."));
        }
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Role.knowsRole(player, NarutoRoles.ZABUZA);
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Hyôton")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if(Item.interactItem(event, "Hyôton")) {
            if(hyotonCooldown > 0) {
                player.sendMessage(Messages.cooldown(hyotonCooldown));
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2*60*20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2*60*20, 1, false, false));

            if(alreadyUsedHyoton) {
                Block block = player.getTargetBlock((Set<Material>) null, 100);
                if(block == null) {
                    player.sendMessage(prefix("&cVous n'avez pas été téléporté car vous ne regardez aucun block."));
                } else {
                    player.teleport(block.getLocation().add(0, 1, 0));
                    player.sendMessage(prefix("&aVous avez été téléporté au block que vous regardiez."));
                }
            } else {
                alreadyUsedHyoton = true;
            }

            for(Location location : sphere(player.getLocation(), 50, true)) {
                if(location.getBlock().getType() == Material.AIR || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER) {
                    location.getBlock().setType(Material.PACKED_ICE);
                }
            }

            hyotonCooldown = 15*60;
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.ZABUZA_HAKU;
    }

    public Set<Location> sphere(Location location, int radius, boolean hollow){
        Set<Location> blocks = new HashSet<Location>();
        World world = location.getWorld();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;

        if(hollow){
            for (int x = X - radius; x <= X + radius; x++) {
                for (int y = Y - radius; y <= Y + radius; y++) {
                    for (int z = Z - radius; z <= Z + radius; z++) {
                        if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
                            Location block = new Location(world, x, y, z);
                            blocks.add(block);
                        }
                    }
                }
            }
            return makeHollow(blocks, true);
        } else {
            for (int x = X - radius; x <= X + radius; x++) {
                for (int y = Y - radius; y <= Y + radius; y++) {
                    for (int z = Z - radius; z <= Z + radius; z++) {
                        if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
                            Location block = new Location(world, x, y, z);
                            blocks.add(block);
                        }
                    }
                }
            }
            return blocks;
        }
    }

    private Set<Location> makeHollow(Set<Location> blocks, boolean sphere){
        Set<Location> edge = new HashSet<Location>();
        if(!sphere){
            for(Location l : blocks){
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right))){
                    edge.add(l);
                }
            }
        } else {
            for(Location l : blocks){
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                Location top = new Location(w, X, Y + 1, Z);
                Location bottom = new Location(w, X, Y - 1, Z);
                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right) && blocks.contains(top) && blocks.contains(bottom))){
                    edge.add(l);
                }
            }
        }
        return edge;
    }

}

package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sakura extends NarutoRole {

    public int jutsuCooldown = 0;
    public boolean usingJutsu = false;
    public int katsuyuCooldown = 0;
    public boolean usingKatsuyu = false;
    public boolean sasukeKilled = false;
    public boolean metSasuke = false;
    private Player jutsuPlayer = null;

    public NarutoRoles getRole() {
        return NarutoRoles.SAKURA;
    }

    @Override
    public void resetCooldowns() {
        jutsuCooldown = 0;
        katsuyuCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (jutsuCooldown > 0) {
            jutsuCooldown--;
        }
        if (katsuyuCooldown > 0) {
            katsuyuCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Sakura";
    }

    @Override
    public void onSecond(int timer, Player player) {
        for (Player nearbyPlayer : Loc.getNearbyPlayers(player, 20)) {
            if (metSasuke) return;
            if (!sasukeKilled) return;

            if (Role.findPlayer(NarutoRoles.SASUKE) == null) return;
            if (nearbyPlayer.getUniqueId().equals(Role.findPlayer(NarutoRoles.SASUKE).getUniqueId())) {
                metSasuke = true;
                Role.knowsRole(player, NarutoRoles.SASUKE);
            }

        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {

        if (event.getEntity().getKiller() != null && Role.isRole(event.getEntity().getKiller(), NarutoRoles.SASUKE)) {
            sasukeKilled = true;
            return;
        }

        Player sakura = Role.findPlayer(NarutoRoles.SAKURA);
        if (sakura == null) return;
        if (Role.isRole(player, NarutoRoles.TSUNADE)) {
            sakura.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Katsuyu")).toItemStack());
        }
        if (Role.isRole(player, NarutoRoles.SASUKE)) {
            sakura.setMaxHealth(sakura.getMaxHealth() - 8);
            sakura.sendMessage(prefix("&cSasuke &fest mort. Vous perdez &c4 coeurs &fpermanent."));
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Katsuyu")) {

            if (katsuyuCooldown > 0) {
                player.sendMessage(Messages.cooldown(katsuyuCooldown));
                return;
            }

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20 * 60, 0, false, false));
            Tasks.runLater(() -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            }, 5 * 20 * 60);

            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aKatsuyu§f."));


            usingKatsuyu = true;

            Tasks.runLater(() -> {
                player.sendMessage(CC.prefix("§fVotre §cKatsuyu §fvient d'expirer."));
                usingKatsuyu = false;
            }, 20 * 60);

            katsuyuCooldown = 20 * 60;

        }
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7 * 60 * 20, 0, false, false));
        Tasks.runTimer(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7 * 60 * 20, 0, false, false)), 0, 11 * 20 * 60);
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

        if (!args[0].equalsIgnoreCase("shosen")) return;

        if (args.length != 3) {
            player.sendMessage(Messages.syntax("/ns shosen jutsu <player>"));
            return;
        }

        if (jutsuCooldown > 0) {
            player.sendMessage(Messages.cooldown(jutsuCooldown));
            return;
        }

        if (!args[1].equalsIgnoreCase("jutsu")) return;

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            player.sendMessage(Messages.offline(args[2]));
            return;
        }

        if (Objects.equals(target.getName(), player.getName())) {
            player.sendMessage(CC.prefix("§cVous ne pouvez pas utiliser ce pouvoir sur vous-même."));
            return;
        }

        if (target.getLocation().distance(player.getLocation()) > 5) {
            player.sendMessage(CC.prefix("§cCe joueur n'est pas à 5 blocks de vous."));
            return;
        }

        usingJutsu = true;
        jutsuPlayer = target;

        player.sendMessage(CC.prefix("§fVous avez utilisé votre §aJutsu §fsur §a" + target.getName() + "§f. Si un de vous deux bouge, §a" + target.getName() + " §fperdra son effet de §dRégénération§f."));
        target.sendMessage(CC.prefix("§aSakura §fa utilisé son §aJutsu §fsur vous. Si un de vous deux bouge vous perdrez votre effet de §dRégénération§f."));
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2, false, false));

        jutsuCooldown = 10 * 60;

    }

    @Override
    public void onAllPlayerMove(PlayerMoveEvent event, Player player) {

        if (event.getFrom().getBlock() == event.getTo().getBlock()) return;

        Player sakura = Role.findPlayer(NarutoRoles.SAKURA);
        Player jutsu = jutsuPlayer;

        if (!event.getPlayer().equals(sakura) && !event.getPlayer().equals(jutsu)) return;

        if (jutsu == null || sakura == null) return;
        if (!usingJutsu) return;

        jutsu.removePotionEffect(PotionEffectType.REGENERATION);
        jutsu.sendMessage(CC.prefix("§a" + event.getPlayer().getName() + " §fa bougé."));
        sakura.sendMessage(CC.prefix("§a" + event.getPlayer().getName() + " §fa bougé."));
        usingJutsu = false;
        jutsuPlayer = null;

    }

}

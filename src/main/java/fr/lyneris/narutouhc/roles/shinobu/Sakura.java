package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.*;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
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
    public boolean usingByakugo = false;
    public int byakugoCooldown = 0;
    public int damageTaken = 0;

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

        if (byakugoCooldown > 0) {
            byakugoCooldown--;
        }

        if(usingByakugo && damageTaken < 30) {
            Title.sendActionBar(getPlayer(), CC.prefix("&fDégâts: &c" + damageTaken + "&8/&730"));
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

        if(Role.getCamp(player) != null && Role.getCamp(player) == Camp.AKATSUKI) {
            int i = 0;
            for (Player player1 : Role.getAliveOnlinePlayers()) {
                if (Role.getCamp(player1) == Camp.AKATSUKI) {
                    i++;
                    break;
                }
            }
            if(i == 0) {
                getPlayer().sendMessage(prefix("&fToute l'&cAkatsuki &fest morte. Vous recevez donc l'item &aByakugô"));
                getPlayer().getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Byakugô")).toItemStack());
            }
        }

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
            sakura.setMaxHealth(sakura.getMaxHealth() - 4);
            sakura.sendMessage(prefix("&cSasuke &fest mort. Vous perdez &c4 coeurs &fpermanent."));
        }
    }



    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {

        if (usingByakugo) {
            damageTaken += event.getFinalDamage();
        }

        if (damageTaken >= 30) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.setMaxHealth(player.getMaxHealth() - 4);
            Tasks.runLater(() -> player.setMaxHealth(player.getMaxHealth() + 4), 15 * 20 * 60);
            player.sendMessage(CC.prefix("§fVous avez pris plus de 15 coeurs de dégâts. De ce fait, vous perdez votre effet de §dRégénération §fainsi que §b2 coeurs §fpendant 15 minutes."));
            usingByakugo = false;
            damageTaken = 0;
        }

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Byakugô")) {

            if (byakugoCooldown > 0) {
                player.sendMessage(Messages.cooldown(byakugoCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            usingByakugo = true;
            damageTaken = 0;

            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aByakugô§f."));

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false));

            byakugoCooldown = 30 * 60;

        }

        if (Item.interactItem(event.getItem(), "Katsuyu")) {

            if (katsuyuCooldown > 0) {
                player.sendMessage(Messages.cooldown(katsuyuCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20 * 60, 0, false, false));
            Tasks.runLater(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false)), 5 * 20 * 60);

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
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §rSakura\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r➜ /ns shosenjutsu <Joueur>§7, cette commande permet de donner l’effet §dRégénération 3§7 au joueur ciblé pendant 20 secondes. Elle doit se situer à 5 blocs de la personne. La commande possède un délai de 10 minutes, elle ne peut pas l’utiliser sur elle-même. \n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Lorsque §6Sasuke effectue un meurtre et qu’après cela il rencontre §aSakura§7 dans une zone de 20 blocs, elle connaîtra son identité, s'il meurt elle perdra §c4 cœurs§7 permanent. \n" +
                "§e §f\n" +
                "§7• Elle dispose de l'effet §cForce 1 permanent.\n" +
                "§e §f\n" +
                "§7• Lors de la mort de §aTsunade§7 elle hérite de l’item §rKatsuyu§7. (voir fiche §aTsunade§7)\n" +
                "§e §f\n" +
                "§7• Lorsque l’entièreté du camp de l’§cAkatsuki§7 sera mort, elle obtiendra l’item §rByakugô§7 (voir fiche §aTsunade§7)\n" +
                "§e §f\n" +
                "§7• Elle dispose de la nature de Chakra : §6Doton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
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

        if (!args[0].equalsIgnoreCase("shosenjutsu")) return;



        if (args.length != 2) {
            player.sendMessage(Messages.syntax("/ns shosenjutsu <player>"));
            return;
        }

        if (jutsuCooldown > 0) {
            player.sendMessage(Messages.cooldown(jutsuCooldown));
            return;
        }


        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Messages.offline(args[1]));
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

        if(Kisame.isBlocked(player)) {
            player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
            return;
        }
        NarutoUHC.usePower(player);

        usingJutsu = true;

        player.sendMessage(CC.prefix("§fVous avez utilisé votre §aJutsu §fsur §a" + target.getName()));
        target.sendMessage(CC.prefix("§aSakura §fa utilisé son §aJutsu §fsur vous."));
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*20, 2, false, false));

        jutsuCooldown = 10 * 60;

    }

}

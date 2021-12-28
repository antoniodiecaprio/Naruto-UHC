package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import fr.lyneris.uhc.utils.title.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Tsunade extends NarutoRole {

    public int katsuyuCooldown = 0;
    public int damageTaken = 0;
    public boolean usingByakugo = false;
    public int byakugoCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.TSUNADE;
    }

    @Override
    public void resetCooldowns() {
        katsuyuCooldown = 0;
        byakugoCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (katsuyuCooldown > 0) {
            katsuyuCooldown--;
        }

        if (byakugoCooldown > 0) {
            byakugoCooldown--;
        }

        if(damageTaken < 30) {
            Title.sendActionBar(getPlayer(), CC.prefix("&fDégâts: &c" + damageTaken + "&8/&730"));
        }
    }

    @Override
    public String getRoleName() {
        return "Tsunade";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Katsuyu")).toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Byakugô")).toItemStack());
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
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {

        int random = (int) (Math.random() * 100);

        if (random < 5) {
            player.sendMessage(CC.prefix("§fVous avez de la chance, §c" + event.getEntity().getName() + " §fa reçu des §ceffets négatifs§f."));
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, false));
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 3, false, false));
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if (Item.interactItem(event.getItem(), "Katsuyu")) {

            if (katsuyuCooldown > 0) {
                player.sendMessage(Messages.cooldown(katsuyuCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20 * 60, 0, false, false));
            Tasks.runLater(() -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            }, 5 * 20 * 60);

            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aKatsuyu§f."));


            Loc.getNearbyPlayers(player, 30).forEach(target -> target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 2, false, false)));

            Tasks.runLater(() -> {
                player.sendMessage(CC.prefix("§fVotre §cKatsuyu §fvient d'expirer."));
            }, 20 * 60);

            katsuyuCooldown = 20 * 60;

        }

        if (Item.interactItem(event.getItem(), "Byakugô")) {

            if (byakugoCooldown > 0) {
                player.sendMessage(Messages.cooldown(byakugoCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous êtes sous l'emprise de Samehada."));
                return;
            }
            NarutoUHC.usePower(player);

            usingByakugo = true;
            damageTaken = 0;

            player.sendMessage(CC.prefix("§fVous avez utilisé l'item §aByakugô§f."));

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false));

            byakugoCooldown = 30 * 60;

        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }
}

package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Damage;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Konohamaru extends NarutoRole {

    public boolean isUsingRasengan = false;
    public int rasenganCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.KONOHAMARU;
    }

    @Override
    public void resetCooldowns() {
        rasenganCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (rasenganCooldown > 0) {
            rasenganCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Konohamaru";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aKonohamaru\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’un item nommé “§rRasengan§7”, lorsqu’il l’utilise, et qu’après cela il frappe un joueur, celui-ci explosera de la valeur d’une dynamite (TNT) dans le jeu, son item possède un délai de 10 minutes. \n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il dispose de l’effet §bVitesse 1§7 permanent.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §aFûton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!isUsingRasengan) return;


        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 5);
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 5);

        Tasks.runLater(() -> player.getWorld().createExplosion(event.getEntity().getLocation(), 3.0f), 3);

        isUsingRasengan = false;

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(player.getItemInHand(), "Rasengan")) {
            if (rasenganCooldown > 0) {
                player.sendMessage(Messages.cooldown(rasenganCooldown));
                return;
            }
            if(Kisame.isBlocked(player)) {
                    player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);
            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aRasengan§f."));

            rasenganCooldown = 5 * 60;
            isUsingRasengan = true;

        }
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Rasengan")).toItemStack());
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }
}

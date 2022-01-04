package fr.lyneris.narutouhc.roles.solo;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Danzo extends NarutoRole {

    public static int lives = 1;
    public static int died = 0;
    public boolean usedIzanagi = false;

    @Override
    public void onPlayerDamageOnEntity(EntityDamageByEntityEvent event, Player player) {
        if(Item.specialItem(player.getItemInHand(), "Epee")) {
            int random = (int) (Math.random() * 20);
            if(random == 1) {
                player.sendMessage(prefix("&fVous avez mis &a25% &fde dégâts en plus."));
                event.setDamage(event.getDamage()*1.25);
            }
        }
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event, Player killer) {
        lives++;
        killer.sendMessage(prefix("&fVous avez désormais &a" + lives + " &fvie(s)."));
    }

    @Override
    public String getRoleName() {
        return "Danzo";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §8Danzo\n" +
                "§7▎ Objectif: §rSon but est de gagner seul\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Il dispose d’une épée en diamant Tranchant 4, qui a 5% de chance d’effectuer 25% de dégât supplémentaires.\n" +
                "§e §f\n" +
                "§7§l▎ Commandes :\n" +
                "§e §f\n" +
                "§r→ /ns izanagi§7, celle-ci lui permet de recevoir 5 pommes d’or et d’être entièrement régénéré, cependant il perd §c1 cœur permanent, il peut l’utiliser qu’une seule fois dans la partie.\n" +
                "§e §f\n" +
                "§r→ /ns hokage§7, celle-ci lui permet de retirer §c2 emplacements de cœurs§7 au Hokage et de lui donner les effets §lFaiblesse 1§7 et §8Lenteur 1§7, ceci pendant 2 minutes, ce pouvoir est utilisable une seule fois dans la partie.\n" +
                "§e §f\n" +
                "§7§l▎ Particularités :\n" +
                "§e §f\n" +
                "§7• Il a la particularité de pouvoir se ressusciter, il peut se faire ressusciter une fois, cependant à chaque éliminations qu’il effectue, il reçoit une vie de plus, lorsqu’il ressuscite, il apparaît dans un rayon de 30 blocs de sa mort, mais sa vie n’est pas régénéré, il apparaît donc avec §c5 cœurs§7 et il perd §c0.5 cœur§7 permanent par nouvelle vie utilisée.\n" +
                "§e §f\n" +
                "§7• Il dispose du rôle de l’Hokage lors de son annonce.\n" +
                "§e §f\n" +
                "§7• S’il vient à tuer l’Hokage, il devient à son tour Hokage 5 minutes après l’avoir tué, il obtiendra donc à ce moment les pouvoirs du Hokage (voir fiche Hokage).\n" +
                "§e §f\n" +
                "§7• Il dispose des effets §cForce 1§7, §9Résistance 1§7, §bVitesse 1§7 et §c2 cœurs§7 supplémentaires.\n" +
                "§e §f\n" +
                "§7• Il dispose de la nature de Chakra : §aFûton\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public NarutoRoles getRole() {
        return NarutoRoles.DANZO;
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(Item.specialItem("Epée")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
    }

    @Override
    public void onSubCommand(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("izanagi")) {

            if (usedIzanagi) {
                player.sendMessage(CC.prefix("§cVous avez déjà utilisé ce pouvoir."));
                return;
            }

            if(fr.lyneris.narutouhc.utils.Blocked.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            player.sendMessage(CC.prefix("§fVous avez utilisé votre pouvoir §aIzanagi§f."));

            player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).toItemStack());
            player.setHealth(player.getMaxHealth());
            player.setMaxHealth(player.getMaxHealth() - 2);
            usedIzanagi = true;
        }

    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    @Override
    public Camp getCamp() {
        return Camp.SOLO;
    }
}

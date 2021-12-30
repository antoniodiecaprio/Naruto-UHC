package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.particle.MathL;
import fr.lyneris.narutouhc.roles.akatsuki.Kisame;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TenTen extends NarutoRole {

    public int parcheminCooldown = 0;

    public NarutoRoles getRole() {
        return NarutoRoles.TEN_TEN;
    }

    @Override
    public void resetCooldowns() {
        parcheminCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (parcheminCooldown > 0) {
            parcheminCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Tenten";
    }

    @Override
    public String getDescription() {
        return "§7§m--------------------------------------\n" +
                "§e §f\n" +
                "§7▎ Rôle: §aTenTen\n" +
                "§7▎ Objectif: §rSon but est de gagner avec les §aShinobi\n" +
                "§e §f\n" +
                "§7§l▎ Items :\n" +
                "§e §f\n" +
                "§7• Elle dispose d'une épée en fer Tranchant 5 et d'un arc Puissance 3 Punch 1.\n" +
                "§e §f\n" +
                "§7• Elle dispose de l’item nommé \"§rParchemin§7\", celui-ci lui permet de recevoir l’effet §bVitesse 1§7 pendant 5 minutes et d’envoyer 20 flèches tout autour d’elle, pour la protéger. Ce pouvoir possède un délai de 5 minutes.\n" +
                "§e §f\n" +
                "§7§m--------------------------------------";
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).setName("§7▎ §6Epée").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3).addEnchant(Enchantment.ARROW_KNOCKBACK, 1).setName("§7▎ §6Arc").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Parchemin")).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Parchemin")) {

            if (parcheminCooldown > 0) {
                player.sendMessage(Messages.cooldown(parcheminCooldown));
                return;
            }

            if(Kisame.isBlocked(player)) {
                player.sendMessage(prefix("&cVous ne pouvez pas utiliser de pouvoir."));
                return;
            }
            NarutoUHC.usePower(player);

            final double radius = 1D;
            double powerHorizontal = 1;
            double powerVertical = 0.25;

            int arrowNb = 20;

            double slice = 2 * Math.PI / arrowNb;
            for (int i = 0; i < arrowNb; i++) {
                double angle = slice * i;
                double dx = radius * MathL.cos(angle);
                double dy = 1.25;
                double dz = radius * MathL.sin(angle);
                Arrow arrow = player.getWorld().spawn(player.getLocation().clone().add(dx, dy, dz), Arrow.class);
                arrow.setVelocity(new Vector(dx / powerHorizontal, powerVertical, dz / powerHorizontal));
                arrow.setShooter(player);
            }

            player.sendMessage(CC.prefix("§fVous avez utilisé votre item §aParchemin§f."));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 0, false, false));


            parcheminCooldown = 5 * 60;
        }
    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }
}

package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class YondaimeRaikage extends NarutoRole {

    public boolean usingArmor = false;
    public int armorTime = 20 * 60;

    @Override
    public String getRoleName() {
        return "Yondaime Raikage";
    }


    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Armure de foudre")).toItemStack());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Armure de foudre")) {

            if (usingArmor) {
                usingArmor = false;
                player.sendMessage(CC.prefix("§fVous avez désactivé votre §c  Armure§f."));
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            } else {
                usingArmor = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, armorTime * 20, 0, false, false));

                player.sendMessage(CC.prefix("§fVous avez activé votre §aArmure§f."));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (usingArmor) {
                            armorTime--;
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(narutoUHC, 0, 20);

            }
        }
    }

    @Override
    public void onAllPlayerDeath(PlayerDeathEvent event, Player player) {
        Player yondaime = Role.findPlayer(NarutoRoles.YONDAIME_RAIKAGE);
        if (yondaime == null) return;

        if (Role.isRole(player, NarutoRoles.KILLER_BEE)) {
            if (event.getEntity().getKiller() == null) {
                yondaime.sendMessage(CC.prefix("§aKiller Bee §fest mort de §cPVE§f."));
            } else {
                yondaime.sendMessage(CC.prefix("§aKiller Bee §fs'est fait tué par §c" + event.getEntity().getKiller().getName()));
            }
            yondaime.setMaxHealth(player.getMaxHealth() + 4);
        }

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.RAITON;
    }
}

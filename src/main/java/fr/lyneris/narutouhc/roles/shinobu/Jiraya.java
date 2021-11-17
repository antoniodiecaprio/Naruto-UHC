package fr.lyneris.narutouhc.roles.shinobu;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Jiraya extends NarutoRole {

    int senjutsuCooldown = 0;

    @Override
    public void resetCooldowns() {
        senjutsuCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if(senjutsuCooldown > 0) {
            senjutsuCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Jiraya";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Senjutsu")).toItemStack());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {

        if(!Item.interactItem(event.getItem(), "Senjutsu")) return;

        if(senjutsuCooldown > 0) {
            player.sendMessage(Messages.cooldown(senjutsuCooldown));
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 1, false, false));
        Tasks.runLater(() -> {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }, 5*20*60);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 0, false, false));

        Player naruto = Role.findPlayer(NarutoRoles.NARUTO);

        if(naruto != null) {
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();
            naruto.sendMessage(CC.prefix("§aJiraya §fa utilisé son §aSenjutsu§f. Voici ses coordonnées:"));
            naruto.sendMessage(" §f§l» §a" + x + "§f, §a" + y + "§f, §a" + z);
        }

        player.sendMessage(CC.prefix("§fVous avez utilisé votre §aSenjutsu§f. Attention, vos §ccoordonnées§f ont été envoyées à §aNaruto§f."));

        senjutsuCooldown = 30*60;

    }

    @Override
    public Camp getCamp() {
        return Camp.SHINOBI;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }
}

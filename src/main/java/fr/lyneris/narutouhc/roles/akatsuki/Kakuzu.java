package fr.lyneris.narutouhc.roles.akatsuki;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.crafter.Chakra;
import fr.lyneris.narutouhc.crafter.NarutoRole;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Loc;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.uhc.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kakuzu extends NarutoRole {

    private Chakra chakra = Chakra.DOTON;
    private int corpsCooldown = 0;
    private int healthUse = 0;
    private boolean healthCooldown = false;

    public NarutoRoles getRole() {
        return NarutoRoles.KAKUZU;
    }

    @Override
    public void resetCooldowns() {
        healthCooldown = true;
        corpsCooldown = 0;
    }

    @Override
    public void runnableTask() {
        if (corpsCooldown > 0) {
            corpsCooldown--;
        }
    }

    @Override
    public String getRoleName() {
        return "Kakuzu";
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setName(Item.interactItem("Corps rapiécé")).toItemStack());
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event, Player player) {
        if (healthUse >= 4) return;
        if (healthCooldown) return;
        if (player.getHealth() - event.getFinalDamage() <= 1) {
            event.setDamage(0.01);
            if (chakra == Chakra.DOTON) {
                chakra = Chakra.RAITON;
            } else if (chakra == Chakra.RAITON) {
                chakra = Chakra.FUTON;
            } else if (chakra == Chakra.FUTON) {
                chakra = Chakra.KATON;
            } else if (chakra == Chakra.KATON) {
                chakra = Chakra.SUITON;
            }
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40 * 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40 * 20, 1, false, false));
            healthCooldown = true;
            Tasks.runLater(() -> healthCooldown = false, 5 * 20 * 60);
            healthUse++;
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Player player) {
        if (Item.interactItem(event.getItem(), "Corps rapiécé")) {
            if (corpsCooldown > 0) {
                player.sendMessage(Messages.cooldown(corpsCooldown));
                return;
            }

            Loc.getNearbyPlayers(player, 20).forEach(player1 -> {
                if (roleManager.getRole(player1) != null) {
                    if (roleManager.getCamp(player1) != roleManager.getCamp(player)) {
                        manager.setStuned(player, true, 5);
                        player.sendMessage(CC.prefix("§fVous avez immobilisé §c" + player1.getName() + "§f."));
                    }
                }
            });

            corpsCooldown = 10 * 60;

        }
    }

    @Override
    public Camp getCamp() {
        return Camp.AKATSUKI;
    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }
}

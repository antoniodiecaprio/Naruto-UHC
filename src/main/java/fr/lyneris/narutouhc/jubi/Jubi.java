package fr.lyneris.narutouhc.jubi;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.NarutoUHC;
import fr.lyneris.narutouhc.crafter.Camp;
import fr.lyneris.narutouhc.manager.NarutoRoles;
import fr.lyneris.narutouhc.utils.CC;
import fr.lyneris.narutouhc.utils.Item;
import fr.lyneris.narutouhc.utils.Messages;
import fr.lyneris.narutouhc.utils.Role;
import fr.lyneris.uhc.UHC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jubi {

    private final NarutoUHC narutoUHC;
    private int jubiCooldown = 0;

    public void runnableTask() {
        if(jubiCooldown > 0) jubiCooldown--;
    }

    public NarutoUHC getNarutoUHC() {
        return narutoUHC;
    }

    public Jubi(NarutoUHC narutoUHC) {
        this.narutoUHC = narutoUHC;
    }

    public ItemStack getItem() {
        return Item.getInteractItem("Jûbi");
    }

    public void onInteract(PlayerInteractEvent event, Player player) {

        if(!(Role.isRole(event.getPlayer(), NarutoRoles.OBITO) || Role.isRole(event.getPlayer(), NarutoRoles.OBITO))) {
            player.sendMessage(CC.prefix("&cVous ne pouvez pas utiliser cet item..."));
            return;
        }

        if(this.getJubiCooldown() > 0) {
            Messages.getCooldown(getJubiCooldown()).queue(player);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*20*60, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20*60, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20*60, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5*20*60, 3, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5*20*60, 0, false, false));
        NarutoUHC.getNaruto().getBijuListener().setIsobuDamage(player.getUniqueId());
        NarutoUHC.getNaruto().getBijuListener().setKokuoUser(player.getUniqueId());
        NarutoUHC.getNaruto().getBijuListener().setMatatabiFire(player.getUniqueId());
        NarutoUHC.getNaruto().getBijuListener().setSaikenUser(player.getUniqueId());
        NarutoUHC.getNaruto().getBijuListener().setSonGokuUser(player.getUniqueId());
        Tasks.runLater(() -> {
            NarutoUHC.getNaruto().getBijuListener().setIsobuDamage(null);
            NarutoUHC.getNaruto().getBijuListener().setKokuoUser(null);
            NarutoUHC.getNaruto().getBijuListener().setMatatabiFire(null);
            NarutoUHC.getNaruto().getBijuListener().setSaikenUser(null);
            NarutoUHC.getNaruto().getBijuListener().setSonGokuUser(null);
        }, 5*20*60);
        this.setJubiCooldown(15*60);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if(event.getRecipe().getResult().getType() == Material.NETHER_STAR) {
            if(Role.isRole(event.getWhoClicked(), NarutoRoles.OBITO) || Role.isRole(event.getWhoClicked(), NarutoRoles.OBITO)) {
                UHC.getUHC().getGameManager().setGroups(100);
                Role.getAliveOnlinePlayers().forEach(player -> {
                    if(Role.getCamp(player) == Camp.TAKA) {
                        NarutoUHC.getNaruto().getRoleManager().setCamp(player, Camp.SHINOBI);
                        player.sendMessage(CC.prefix("§aVous avez rejoins le camp Shinobi."));
                    }
                });
                Tasks.runTimer(() -> {
                    Player obito = Role.findPlayer(NarutoRoles.OBITO);
                    Player madara = Role.findPlayer(NarutoRoles.MADARA);
                    if(obito != null) obito.setPlayerListName("§4" + obito.getDisplayName());
                    if(madara != null) madara.setPlayerListName("§4" + madara.getDisplayName());
                }, 0, 20);
            } else {
                event.getWhoClicked().sendMessage(CC.prefix("&cVous ne pouvez pas craft cet item..."));
            }
        }
    }

    public void setJubiCooldown(int jubiCooldown) {
        this.jubiCooldown = jubiCooldown;
    }

    public int getJubiCooldown() {
        return jubiCooldown;
    }
}

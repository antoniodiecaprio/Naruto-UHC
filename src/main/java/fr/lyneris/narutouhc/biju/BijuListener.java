package fr.lyneris.narutouhc.biju;

import fr.lyneris.common.utils.Tasks;
import fr.lyneris.narutouhc.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;


@SuppressWarnings("unused") public class BijuListener implements Listener {
    
    private int isobuCooldown = 0;
    private UUID isobuDamage = null;

    private int sonGokuCooldown = 0;
    private UUID sonGokuUser = null;

    private int kokuoCooldown = 0;
    private UUID kokuoUser = null;

    private int saikenCooldown = 0;
    private UUID saikenUser = null;

    private int matatabiCooldown = 0;
    private UUID matatabiFire = null;

    private int chomeiCooldown = 0;

    public void runnableTask() {
        if (matatabiCooldown > 0) matatabiCooldown--;
        if (isobuCooldown > 0) isobuCooldown--;
        if (sonGokuCooldown > 0) sonGokuCooldown--;
        if (kokuoCooldown > 0) kokuoCooldown--;
        if (saikenCooldown > 0) saikenCooldown--;
        if (chomeiCooldown > 0) chomeiCooldown--;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Bijus bijus = null;
        for (Bijus value : Bijus.values()) {
            if (value.getBiju().getItem().equals(event.getItem())) {
                bijus = value;
                break;
            }
        }

        if (bijus == null) return;

        bijus.getBiju().getItemInteraction(event, event.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Bijus bijus = null;
        for (Bijus value : Bijus.values()) {
            if (value.getBiju().getItem().equals(event.getItemDrop().getItemStack())) {
                bijus = value;
                break;
            }
        }
        if (bijus != null) event.setCancelled(true);
    }

    @EventHandler
    public void onDamageOnEntity(EntityDamageByEntityEvent event) {

        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        assert getMatatabiFire() != null;
        if (event.getDamager().getUniqueId().equals(getMatatabiFire())) {
            if (matatabiCooldown > 15 * 60) {
                event.getEntity().setFireTicks(60);
                event.getEntity().sendMessage(CC.prefix("&cVous avez été touché par Chomei"));
                event.getDamager().sendMessage(CC.prefix("&fVous avez enflammé &c" + event.getEntity().getName()));
            }
        }

        assert getSonGokuUser() != null;
        if (event.getDamager().getUniqueId().equals(getSonGokuUser())) {
            if (sonGokuCooldown > 15 * 60) {
                int random = (int) (Math.random() * 2);
                if(random == 0) {
                    event.getEntity().sendMessage(CC.prefix("&cVous avez été touché par Son Gokû"));
                    event.getDamager().sendMessage(CC.prefix("&fVous avez enflammé &c" + event.getEntity().getName()));
                    event.getEntity().setFireTicks(60);
                }
            }
        }

        assert getKokuoUser() != null;
        if (event.getDamager().getUniqueId().equals(getKokuoUser())) {
            int random = (int) (Math.random() * 7);
            if(random == 0) {
                ((Player) event.getDamager()).removePotionEffect(PotionEffectType.SPEED);
                ((Player) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 2, false, false));
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 0, false, false));
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 0, false, false));
                event.getEntity().sendMessage(CC.prefix("&cVous avez été touché par Kokuo"));
                event.getDamager().sendMessage(CC.prefix("&fVous avez obtenu &bSpeed 3 &fpendant 5 secondes."));
                Tasks.runLater(() -> ((Player) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 1, false, false)), 5*21);
            }
        }

        assert getSaikenUser() != null;
        if (event.getDamager().getUniqueId().equals(getSaikenUser())) {
            int random = (int) (Math.random() * 7);
            if(random == 0) {
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 0, false, false));
                event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 1.5F);
                event.getEntity().sendMessage(CC.prefix("&cVous avez été touché par Saiken"));
                event.getDamager().sendMessage(CC.prefix("&fVous avez régénéré &c0.5 &fcoeurs."));
                ((Player) event.getDamager()).setHealth(((Player) event.getDamager()).getHealth() + 1);
                Tasks.runLater(() -> ((Player) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20 * 60, 1, false, false)), 5*21);
            }
        }
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        assert getIsobuDamage() != null;
        if (event.getEntity().getUniqueId().equals(getIsobuDamage())) {
            int random = (int) (Math.random() * 25);
            if(random == 0) {
                event.setCancelled(true);
                event.getEntity().sendMessage(CC.prefix("&fVous avez eu de la chance, ce coup a été &cannulé&f."));
            }
        }
    }

    @EventHandler
    public void onEmpty(PlayerBucketEmptyEvent event) {
        if(this.sonGokuUser == null) return;
        if(Bukkit.getPlayer(this.sonGokuUser) == null) return;
        Player player = Bukkit.getPlayer(this.sonGokuUser);
        if(player.getLocation().distance(event.getPlayer().getLocation()) <= 15 && event.getItemStack().getType() == Material.LAVA_BUCKET) {
            event.setCancelled(true);
            player.sendMessage(CC.prefix("&cVous ne pouvez pas poser des seaux de lave à côté de l'utilisateur de Son Gokû."));
        }
    }

    public void setIsobuCooldown(int isobuCooldown) {
        this.isobuCooldown = isobuCooldown;
    }

    public void setSonGokuCooldown(int sonGokuCooldown) {
        this.sonGokuCooldown = sonGokuCooldown;
    }

    public void setKokuoCooldown(int kokuoCooldown) {
        this.kokuoCooldown = kokuoCooldown;
    }

    public void setSaikenCooldown(int saikenCooldown) {
        this.saikenCooldown = saikenCooldown;
    }

    public void setMatatabiCooldown(int matatabiCooldown) {
        this.matatabiCooldown = matatabiCooldown;
    }

    public UUID getMatatabiFire() {
        return matatabiFire;
    }

    public void setMatatabiFire(UUID matatabiFire) {
        this.matatabiFire = matatabiFire;
    }

    public int getIsobuCooldown() {
        return isobuCooldown;
    }

    public int getSonGokuCooldown() {
        return sonGokuCooldown;
    }

    public int getKokuoCooldown() {
        return kokuoCooldown;
    }

    public int getSaikenCooldown() {
        return saikenCooldown;
    }

    public int getMatatabiCooldown() {
        return matatabiCooldown;
    }

    public UUID getIsobuDamage() {
        return isobuDamage;
    }

    public UUID getSonGokuUser() {
        return sonGokuUser;
    }

    public void setSonGokuUser(UUID sonGokuUser) {
        this.sonGokuUser = sonGokuUser;
    }

    public void setIsobuDamage(UUID isobuDamage) {
        this.isobuDamage = isobuDamage;
    }

    public UUID getKokuoUser() {
        return kokuoUser;
    }

    public UUID getSaikenUser() {
        return saikenUser;
    }

    public void setSaikenUser(UUID saikenUser) {
        this.saikenUser = saikenUser;
    }

    public void setKokuoUser(UUID kokuoUser) {
        this.kokuoUser = kokuoUser;
    }

    public int getChomeiCooldown() {
        return chomeiCooldown;
    }

    public void setChomeiCooldown(int chomeiCooldown) {
        this.chomeiCooldown = chomeiCooldown;
    }

}

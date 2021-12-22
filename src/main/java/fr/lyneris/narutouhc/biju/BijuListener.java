package fr.lyneris.narutouhc.biju;

import fr.lyneris.narutouhc.utils.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;


public class BijuListener implements Listener {

    private int matatabiCooldown = 0;

    private int isobuCooldown = 0;
    private UUID isobuDamage = null;

    private int sonGokuCooldown = 0;
    private UUID sonGokuUser = null;

    private int KokuoCooldown = 0;
    private int saikenCooldown = 0;

    private int chomeiCooldown = 0;
    private UUID chomeiFire = null;

    public void runnableTask() {
        if (matatabiCooldown > 0) matatabiCooldown--;
        if (isobuCooldown > 0) isobuCooldown--;
        if (sonGokuCooldown > 0) sonGokuCooldown--;
        if (KokuoCooldown > 0) KokuoCooldown--;
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
        assert getChomeiFire() != null;
        if (event.getDamager().getUniqueId().equals(getChomeiFire())) {
            if (chomeiCooldown > 15 * 60) event.getEntity().setFireTicks(60);
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

    public void setMatatabiCooldown(int matatabiCooldown) {
        this.matatabiCooldown = matatabiCooldown;
    }

    public void setIsobuCooldown(int isobuCooldown) {
        this.isobuCooldown = isobuCooldown;
    }

    public void setSonGokuCooldown(int sonGokuCooldown) {
        this.sonGokuCooldown = sonGokuCooldown;
    }

    public void setKokuoCooldown(int kokuoCooldown) {
        KokuoCooldown = kokuoCooldown;
    }

    public void setSaikenCooldown(int saikenCooldown) {
        this.saikenCooldown = saikenCooldown;
    }

    public void setChomeiCooldown(int chomeiCooldown) {
        this.chomeiCooldown = chomeiCooldown;
    }

    public UUID getChomeiFire() {
        return chomeiFire;
    }

    public void setChomeiFire(UUID chomeiFire) {
        this.chomeiFire = chomeiFire;
    }

    public int getMatatabiCooldown() {
        return matatabiCooldown;
    }

    public int getIsobuCooldown() {
        return isobuCooldown;
    }

    public int getSonGokuCooldown() {
        return sonGokuCooldown;
    }

    public int getKokuoCooldown() {
        return KokuoCooldown;
    }

    public int getSaikenCooldown() {
        return saikenCooldown;
    }

    public int getChomeiCooldown() {
        return chomeiCooldown;
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

}

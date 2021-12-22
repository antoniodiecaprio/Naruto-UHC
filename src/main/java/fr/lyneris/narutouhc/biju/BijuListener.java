package fr.lyneris.narutouhc.biju;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BijuListener implements Listener {

    private int matatabiCooldown = 0;
    private int isobuCooldown = 0;
    private int sonGokuCooldown = 0;
    private int KokuoCooldown = 0;
    private int saikenCooldown = 0;
    private int chomeiCooldown = 0;

    public void runnableTask() {
        if(matatabiCooldown > 0) matatabiCooldown--;
        if(isobuCooldown > 0) isobuCooldown--;
        if(sonGokuCooldown > 0) sonGokuCooldown--;
        if(KokuoCooldown > 0) KokuoCooldown--;
        if(saikenCooldown > 0) saikenCooldown--;
        if(chomeiCooldown > 0) chomeiCooldown--;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Bijus bijus = null;
        for (Bijus value : Bijus.values()) {
            if(value.getBiju().getItem().equals(event.getItem())) {
                bijus = value;
                break;
            }
        }

        if(bijus == null) return;

        bijus.getBiju().getItemInteraction(event, event.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Bijus bijus = null;
        for (Bijus value : Bijus.values()) {
            if(value.getBiju().getItem().equals(event.getItemDrop().getItemStack())) {
                bijus = value;
                break;
            }
        }
        if(bijus != null) event.setCancelled(true);
    }
}

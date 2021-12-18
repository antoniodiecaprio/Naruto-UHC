package fr.lyneris.narutouhc.utils;

import org.bukkit.entity.Player;

public class Cooldown {

    private final String message;

    protected Cooldown(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void queue(Player player) {
        player.sendMessage(message);
    }
}

package fr.lyneris.narutouhc.crafter;

public enum Camp {

    SHINOBI("§a"),
    AKATSUKI("§c"),
    OROCHIMARU("§6"),
    TAKA("§d"),
    JUBI("§9"),
    SOLO("§4");

    private final String color;

    Camp(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

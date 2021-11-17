package fr.lyneris.narutouhc.crafter;

public enum Camp {

    SHINOBI("Shinobi", "§a"),
    AKATSUKI("Akatsuki", "§c"),
    OROCHIMARU("Orochimaru", "§6"),
    TAKA("Taka", "§d"),
    JUBI("Jubi", "§9"),
    SOLO("Solo", "§4");

    private final String color;
    private final String name;

    Camp(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}

package fr.lyneris.narutouhc.crafter;

public enum Camp {

    SHINOBI("Shinobi", "§a"),
    AKATSUKI("Akatsuki", "§c"),
    OROCHIMARU("Orochimaru", "§6"),
    TAKA("Taka", "§5"),
    MADARA_OBITO("Jubi", "§9"),
    ZABUZA_HAKU("Zabuza et Haku", "§e"),
    SANKYODAI("Sankyodai", "§e"),
    SOLO("Solo", "§4");

    private final String name;
    private final String color;

    Camp(String name, String color) {
        this.color = color;
        this.name = name;
    }

    public String getFormat() {
        return color + name;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}

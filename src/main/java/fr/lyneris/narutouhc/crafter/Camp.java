package fr.lyneris.narutouhc.crafter;

import fr.lyneris.narutouhc.NarutoUHC;

public enum Camp {

    SHINOBI("Shinobi", "§a", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA2ZTM0YzFjOTRjN2I4MjljNTlkMDFjNzQ1M2Q1ZjNlODI1OWYzODljMmFjYTJmYTMxNGRjYTQwODY5M2NlIn19fQ"),
    AKATSUKI("Akatsuki", "§c", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkyMjY0NmE4OTQ1YjVjNDAwZTkwNDZjN2JhMjA4MGZjNmQ0N2ExNjUxMjEyNmI4OWJmNzc3ZDg0MjllZTU1NiJ9fX0"),
    OROCHIMARU("Orochimaru", "§5", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ2YjgzZmU3MTBlOTg3NzdkMjlhZTgwNGVmNmQzYjhkMjRiYzVjNTlmZDM3YjViYTA4NDc1YmQ3Njc4ZWQwYSJ9fX0"),
    TAKA("Taka", "§6", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlYmNhMzcyOWJhOTgwOTM1OTg4OGNiNDY4MTM1YmI0OTNjOGQxOGM5OGI5NWMxNTdlZTk5MDQyOWExMCJ9fX0"),
    MADARA_OBITO("Jubi", "§d", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcwYzQzNWI1NDgwNmIyZDU4NzhlMDk3NWExM2ZiOTU1ZGFiMmZiNjg4ZjUwYjAxMTM1NzY0OGY4N2E0Y2QxIn19fQ"),
    ZABUZA_HAKU("Zabuza et Haku", "§b", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3NGYyN2E5YjM2ZmY1NmYwMTEyNzY3ODExNzZjZmY4ZWZjY2Y5YmEyNWU3Y2M3ZGM4M2Y5OTc4MmI1ZWJiZCJ9fX0="),
    SANKYODAI("Sankyodai", "§e", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFkYmQxMTQ0ZDZlZmM5NjMyMzYzOWU1MjUyNmI4MjkwYWQ0ZjA0OGQwMTFkMDRmYzhjMTRmMThmNjYzMTA0YSJ9fX0"),
    SOLO("Solo", "§7", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGZlNjE5NmRkN2QyYTYyNjIzNDFiODQwYzMyYzRkZTE1NGUyYmFkZWI2ODY4ZDA1ZjkzZTVjZGU2YWY4NzJlOSJ9fX0");

    private final String name;
    private final String color;
    private final String hash;

    Camp(String name, String color, String hash) {
        this.color = color;
        this.name = name;
        this.hash = hash;
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

    public String getHash() {
        return hash;
    }
}

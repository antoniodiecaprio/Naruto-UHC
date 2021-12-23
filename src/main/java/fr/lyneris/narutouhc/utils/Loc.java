package fr.lyneris.narutouhc.utils;

import fr.lyneris.uhc.UHC;
import jdk.nashorn.internal.runtime.regexp.joni.constants.EncloseType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Loc {

    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "Nord";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "Nord Est";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "Est";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "Sud Est";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "Sud";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "Sud Ouest";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "Ouest";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "Nord Ouest";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "Nord";
        } else {
            return "Aucune";
        }
    }

    public static List<Player> getNearbyPlayers(Entity entity, int distance) {
        List<Player> toReturn = new ArrayList<>();
        Bukkit.getOnlinePlayers().stream()
                .filter(target -> UHC.getUHC().getGameManager().getPlayers().contains(target.getUniqueId()))
                .filter(target -> target.getGameMode() != GameMode.SPECTATOR)
                .filter(target -> target.getUniqueId() != entity.getUniqueId())
                .filter(target -> target.getLocation().distance(entity.getLocation()) <= distance)
                .forEach(toReturn::add);
        return toReturn;
    }

    public static char getCharCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return 'N';
        } else if (22.5 <= rotation && rotation < 67.5) {
            return 'N';
        } else if (67.5 <= rotation && rotation < 112.5) {
            return 'E';
        } else if (112.5 <= rotation && rotation < 157.5) {
            return 'S';
        } else if (157.5 <= rotation && rotation < 202.5) {
            return 'S';
        } else if (202.5 <= rotation && rotation < 247.5) {
            return 'S';
        } else if (247.5 <= rotation && rotation < 292.5) {
            return 'W';
        } else if (292.5 <= rotation && rotation < 337.5) {
            return 'N';
        } else if (337.5 <= rotation && rotation < 360.0) {
            return 'N';
        } else {
            return 'N';
        }
    }

    public static double getDirectionTo(final Player paramPlayer, final Location paramLocation) {
        final Location localLocation = paramPlayer.getLocation().clone();
        localLocation.setY(0.0);
        paramLocation.setY(0.0);
        final Vector localVector1 = localLocation.getDirection();
        final Vector localVector2 = paramLocation.subtract(localLocation).toVector().normalize();
        double d1 = Math.toDegrees(Math.atan2(localVector1.getX(), localVector1.getZ()));
        d1 -= Math.toDegrees(Math.atan2(localVector2.getX(), localVector2.getZ()));
        d1 = (int) (d1 + 22.5) % 360;
        if (d1 < 0.0) {
            d1 += 360.0;
        }
        return d1;
    }

    public static String getDirectionMate(Player player, Player mate, int distance) {
        if (player.getWorld().equals(mate.getWorld())) {
            if (player.getLocation().distance(mate.getLocation()) > distance) {
                if (getDirectionTo(player, mate.getLocation().clone()) <= 45.0) {
                    return "§6" + mate.getName() + "§f \u2b06 ? ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 90.0) {
                    return "§6" + mate.getName() + "§f \u2b08 ? ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 135.0) {
                    return "§6" + mate.getName() + "§f➨ ? ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 180.0) {
                    return "§6" + mate.getName() + "§f \u2b0a ? ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 225.0) {
                    return "§6" + mate.getName() + "§f \u2b07 ? ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 270.0) {
                    return "§6" + mate.getName() + "§f \u2b0b ? ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 315.0) {
                    return "§6" + mate.getName() + "§f \u2b05 ? ";
                } else {
                    return "§6" + mate.getName() + "§f \u2b09 ? ";
                }
            } else {
                if (getDirectionTo(player, mate.getLocation().clone()) <= 45.0) {
                    return "§6" + mate.getName() + "§f \u2b06 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 90.0) {
                    return "§6" + mate.getName() + "§f \u2b08 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 135.0) {
                    return "§6" + mate.getName() + "§f➨ " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 180.0) {
                    return "§6" + mate.getName() + "§f \u2b0a " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 225.0) {
                    return "§6" + mate.getName() + "§f \u2b07 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 270.0) {
                    return "§6" + mate.getName() + "§f \u2b0b " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else if (getDirectionTo(player, mate.getLocation().clone()) <= 315.0) {
                    return "§6" + mate.getName() + "§f \u2b05 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                } else {
                    return "§6" + mate.getName() + "§f \u2b09 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
                }
            }
        } else {
            return "§6" + mate.getName() + " §f? ";
        }
    }

    public static String getDirectionMate(Player player, Player mate) {
        if (player.getWorld().equals(mate.getWorld())) {
            if (getDirectionTo(player, mate.getLocation().clone()) <= 45.0) {
                return "§6" + mate.getName() + "§f \u2b06 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else if (getDirectionTo(player, mate.getLocation().clone()) <= 90.0) {
                return "§6" + mate.getName() + "§f \u2b08 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else if (getDirectionTo(player, mate.getLocation().clone()) <= 135.0) {
                return "§6" + mate.getName() + "§f➨ " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else if (getDirectionTo(player, mate.getLocation().clone()) <= 180.0) {
                return "§6" + mate.getName() + "§f \u2b0a " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else if (getDirectionTo(player, mate.getLocation().clone()) <= 225.0) {
                return "§6" + mate.getName() + "§f \u2b07 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else if (getDirectionTo(player, mate.getLocation().clone()) <= 270.0) {
                return "§6" + mate.getName() + "§f \u2b0b " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else if (getDirectionTo(player, mate.getLocation().clone()) <= 315.0) {
                return "§6" + mate.getName() + "§f \u2b05 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            } else {
                return "§6" + mate.getName() + "§f \u2b09 " + ((int) player.getLocation().distance(mate.getLocation())) + " ";
            }
        } else {
            return "§6" + mate.getName() + " §f? ";
        }
    }

}

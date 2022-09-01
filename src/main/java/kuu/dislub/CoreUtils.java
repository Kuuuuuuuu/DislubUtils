package kuu.dislub;

import org.bukkit.Location;

public class CoreUtils {

    public static String LocationToString(Location loc) {
        return loc.getBlockX() + "|" + loc.getBlockY() + "|" + loc.getBlockZ();
    }

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}

package kuu.dislub.Chest;

import kuu.dislub.CoreUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DeathChestUtils {

    static Location getNearbyFreeLocation(Location location) {
        int i;
        if (location.getY() >= -64.0) {
            if (location.getBlock().getType().equals(Material.AIR)) {
                return location;
            }
            BlockFace[] var2 = BlockFace.values();
            i = var2.length;
            for (int var4 = 0; var4 < i; ++var4) {
                BlockFace blockFace = var2[var4];
                Block relative = location.getBlock().getRelative(blockFace);
                if (relative.getType().equals(Material.AIR)) {
                    return relative.getLocation();
                }
            }
        }
        if (location.getY() < 0.0 || location.getBlock().getType().equals(Material.LAVA) || location.getBlock().getType().equals(Material.LEGACY_STATIONARY_LAVA) || location.getBlock().getType().equals(Material.WATER) || location.getBlock().getType().equals(Material.LEGACY_STATIONARY_WATER)) {
            Location groundLocation = location.clone();
            if (groundLocation.getY() < 0.0) {
                groundLocation.setY(0.0);
            }
            for (i = 0; i < 11; ++i) {
                Location testLocation = groundLocation.clone().add(0.0, i, 0.0);
                if (testLocation.getBlock().getType().equals(Material.AIR)) {
                    return testLocation;
                }
            }
        }
        return null;
    }

    static Chest getNearbyChest(Location location) {
        Location freeLocation = getNearbyFreeLocation(location);
        if (freeLocation != null) {
            freeLocation.getBlock().setType(Material.CHEST);
            return (Chest) freeLocation.getBlock().getState();
        } else {
            return null;
        }
    }

    static void sendPlayerMessage(Player toSend, String message, String player) {
        if (!message.isEmpty()) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            if (player != null) {
                message = message.replaceAll("%player%", player);
            }
            toSend.sendMessage(message);
        }
    }

    static int getItemsInChest(Chest chest) {
        int amount = 0;
        ItemStack[] var2 = chest.getBlockInventory().getContents();
        for (ItemStack stack : var2) {
            if (stack != null) {
                ++amount;
            }
        }
        return amount;
    }

    static void storeChest(Location location, String username) {
        ChestStoreFile.getSection().set(CoreUtils.LocationToString(location), username);
        ChestStoreFile.save();
    }
}

package kuu.dislub.Chest;

import kuu.dislub.Loader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class ChestStoreFile {
    public static FileConfiguration cheststore;
    private static File file;

    public static void init() {
        file = new File(Loader.getInstance().getDataFolder(), "chests");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }
        cheststore = new YamlConfiguration();
        try {
            cheststore.load(file);
        } catch (InvalidConfigurationException | IOException var1) {
            throw new RuntimeException("Failed to load chest data file:", var1);
        }
    }

    public static ConfigurationSection getSection() {
        ConfigurationSection s = cheststore.getConfigurationSection("chests");
        if (s == null) {
            s = cheststore.createSection("chests");
        }
        return s;
    }

    public static void save() {
        try {
            cheststore.save(file);
        } catch (IOException var1) {
            throw new RuntimeException("Failed to save chest data file:", var1);
        }
    }
}
package kuu.dislub;

import kuu.dislub.Chest.ChestStoreFile;
import kuu.dislub.Command.CoreCommand;
import kuu.dislub.Discord.CoreDiscordLink;
import kuu.dislub.Events.CoreListener;
import kuu.dislub.Events.DeathChestEvent;
import kuu.dislub.Events.FireAspectEvent;
import kuu.dislub.Events.TreeCapitatorEvent;
import kuu.dislub.Task.OnePlayerSleep;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class Loader extends JavaPlugin {

    private static Loader instance;
    private static CoreCaches caches;
    private static CoreDiscordLink dislubDiscordLink;
    public final BukkitScheduler Scheduler = getServer().getScheduler();

    public static Loader getInstance() {
        return instance;
    }

    public static CoreCaches getCaches() {
        return caches;
    }

    public static CoreDiscordLink getDiscordBot() {
        return dislubDiscordLink;
    }

    @Override
    public void onLoad() {
        caches = new CoreCaches();
        instance = this;
    }

    @Override
    public void onEnable() {
        ChestStoreFile.init();
        registerConfig();
        registerCommands();
        registerEvents();
        registerDiscordLink();
        CoreTask();
    }

    private void registerConfig() {
        getConfig().addDefault("token", "*");
        getConfig().addDefault("prefix", "§cDislub §8» ");
        getConfig().addDefault("channelID", "*");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerDiscordLink() {
        try {
            if (getConfig().getString("token") == null || Objects.equals(getConfig().getString("token"), "*")) {
                getLogger().warning("Please set your token in the config.yml");
            } else {
                dislubDiscordLink = new CoreDiscordLink();
            }
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerEvents() {
        new CoreListener();
        new DeathChestEvent();
        new FireAspectEvent();
        new TreeCapitatorEvent();
    }

    private void CoreTask() {
        Scheduler.scheduleSyncRepeatingTask(Loader.getInstance(), () -> {
            OnePlayerSleep.update();
        }, 0L, 20L);
    }

    private void registerCommands() {
        getCommand("f").setExecutor(new CoreCommand());
    }

    @Override
    public void onDisable() {
        getDiscordBot().getJda().shutdown();
        getLogger().info("Disabled!");
    }
}

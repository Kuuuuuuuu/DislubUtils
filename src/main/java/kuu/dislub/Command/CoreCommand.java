package kuu.dislub.Command;

import kuu.dislub.Loader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final String name = sender.getName();
            switch (command.getName()) {
                case "respect":
                case "f":
                    Bukkit.getServer().broadcastMessage(name + " has respects.");
                    break;
                case "restart":
                    if (sender.hasPermission("dislub.restart")) {
                        Loader.getInstance().Scheduler.scheduleSyncRepeatingTask(Loader.getInstance(), () -> {
                            Loader.getCaches().RestartSec--;
                            Bukkit.broadcastMessage(Loader.getInstance().getConfig().getString("prefix") + "§aServer Restarting " + Loader.getCaches().RestartSec + " seconds.");
                            if (Loader.getCaches().RestartSec <= 0) {
                                Bukkit.shutdown();
                            }
                        }, 0L, 20L);
                    }
                    break;
            }
        } else {
            sender.sendMessage("This command is for players only.");
        }
        return true;
    }
}
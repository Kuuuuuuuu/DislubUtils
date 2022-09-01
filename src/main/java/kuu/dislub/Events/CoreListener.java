package kuu.dislub.Events;

import kuu.dislub.CoreUtils;
import kuu.dislub.Loader;
import kuu.dislub.Misc.AbstractListener;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

public class CoreListener extends AbstractListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        event.setJoinMessage("§f[§a+§f] §e" + player.getName());
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(player.getName());
        embedBuilder.setDescription("ได้เข้าเซิฟ");
        embedBuilder.setColor(Color.GREEN);
        Loader.getDiscordBot().getJda().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage("§f[§c-§f] §e" + player.getName());
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(player.getName());
        embedBuilder.setDescription("ได้ออกจากเซิฟ");
        embedBuilder.setColor(Color.RED);
        Loader.getDiscordBot().getJda().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final String location = CoreUtils.LocationToString(player.getLocation());
        final Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        event.setDeathMessage(Loader.getInstance().getConfig().getString("prefix") + "§e" + player.getName() + " §cwas died at: " + location);
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(player.getName());
        embedBuilder.setDescription("ได้ตายที่: " + location);
        embedBuilder.setColor(Color.ORANGE);
        Loader.getDiscordBot().getJda().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embedBuilder.build()).queue();
        entity.setCustomName("§c" + player.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();
        event.setFormat("§6" + player.getName() + "§f: " + message);
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(player.getName() + " - " + message);
        embedBuilder.setColor(Color.CYAN);
        Loader.getDiscordBot().getJda().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDimensionChange(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        String worldname = player.getWorld().getName();
        Color color = Color.GREEN;
        if (worldname.contains("nether")) {
            worldname = "Nether";
            color = Color.RED;
        } else if (worldname.contains("end")) {
            worldname = "End";
            color = Color.BLUE;
        } else {
            worldname = "Main-World";
        }
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(player.getName());
        embedBuilder.setDescription("ได้เข้าโลก: " + worldname);
        embedBuilder.setColor(color);
        Loader.getInstance().getServer().broadcastMessage(Loader.getInstance().getConfig().getString("prefix") + "§a" + player.getName() + " §bwas in dimension: " + worldname);
        Loader.getDiscordBot().getJda().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        final Entity entity = event.getEntity();
        try {
            if (entity.getType() == EntityType.ZOMBIE) {
                entity.getLocation().getWorld().dropItem(entity.getLocation(), new ItemStack(Material.PORKCHOP, CoreUtils.randomNumber(1, 3)));
                if (CoreUtils.randomNumber(1, 5) == 3) {
                    entity.getLocation().getWorld().dropItem(entity.getLocation(), new ItemStack(Material.IRON_INGOT, CoreUtils.randomNumber(1, 3)));
                }
            } else if (entity.getType() == EntityType.CREEPER) {
                if (CoreUtils.randomNumber(1, 5) == 3) {
                    entity.getLocation().getWorld().dropItem(entity.getLocation(), new ItemStack(Material.GUNPOWDER, CoreUtils.randomNumber(1, 3)));
                }
            } else if (entity.getType() == EntityType.SKELETON) {
                if (CoreUtils.randomNumber(1, 5) == 3) {
                    entity.getLocation().getWorld().dropItem(entity.getLocation(), new ItemStack(Material.BONE, CoreUtils.randomNumber(1, 3)));
                }
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (!event.isCancelled()) {
            if (Loader.getInstance().getServer().getOnlinePlayers().size() > 1) {
                if (Loader.getCaches().LastPlayerSleep != null) {
                    event.setCancelled(true);
                } else {
                    final int random = CoreUtils.randomNumber(5, 10);
                    event.getPlayer().sendMessage(Loader.getInstance().getConfig().getString("prefix") + "§aYou need to sleep for " + random + " seconds.");
                    Loader.getCaches().RandomSleepSec = random;
                    Loader.getCaches().LastPlayerSleep = event.getPlayer().getName().toLowerCase();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        if (Loader.getInstance().getServer().getOnlinePlayers().size() > 1) {
            Loader.getCaches().SleepSec = 0;
            Loader.getCaches().RandomSleepSec = 5;
            Loader.getCaches().LastPlayerSleep = null;
        }
    }
}
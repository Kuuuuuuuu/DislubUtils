package kuu.dislub.Discord;

import kuu.dislub.Loader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BotListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Server");
        embed.setDescription("Server Started");
        embed.setColor(Color.GREEN);
        event.getJDA().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        final EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Server");
        embed.setDescription("Shutting down...");
        embed.setColor(Color.RED);
        event.getJDA().getTextChannelById(Loader.getInstance().getConfig().getString("channelID")).sendMessageEmbeds(embed.build()).queue();
    }
}

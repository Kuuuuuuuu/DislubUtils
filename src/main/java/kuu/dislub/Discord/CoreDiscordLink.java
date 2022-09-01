package kuu.dislub.Discord;

import kuu.dislub.Loader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;

public class CoreDiscordLink {

    private final JDA jda;

    public CoreDiscordLink() throws LoginException {
        jda = JDABuilder.createDefault(Loader.getInstance().getConfig().getString("token")).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new BotListener());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setActivity(Activity.watching("Online Players: " + Loader.getInstance().getServer().getOnlinePlayers().size()));
            }
        }, 0, 5000);
    }

    public JDA getJda() {
        return jda;
    }
}
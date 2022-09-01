package kuu.dislub.Misc;

import kuu.dislub.Loader;
import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {

    protected AbstractListener() {
        Loader.getInstance().getServer().getPluginManager().registerEvents(this, Loader.getInstance());
    }
}

package kuu.dislub.Task;

import kuu.dislub.Loader;

public class OnePlayerSleep {

    public static void update() {
        if (Loader.getCaches().LastPlayerSleep != null && Loader.getInstance().getServer().getOnlinePlayers().size() > 1) {
            Loader.getCaches().SleepSec++;
            if (Loader.getCaches().SleepSec >= Loader.getCaches().RandomSleepSec) {
                for (var player : Loader.getInstance().getServer().getOnlinePlayers()) {
                    if (player.getName().toLowerCase().equals(Loader.getCaches().LastPlayerSleep)) {
                        for (var world : Loader.getInstance().getServer().getWorlds()) {
                            if (player.getWorld().getName().equals(world.getName())) {
                                world.setTime(0);
                                world.setThundering(false);
                                world.setStorm(false);
                            }
                        }
                        player.chat("ตื่นได้แล้วไอพวกหน้าหี");
                        Loader.getInstance().getServer().broadcastMessage(Loader.getInstance().getConfig().getString("prefix") + "§a" + player.getName() + " §bwas sleep for " + Loader.getCaches().RandomSleepSec + " seconds.");
                        Loader.getCaches().SleepSec = 0;
                        Loader.getCaches().LastPlayerSleep = null;
                    }
                }
            }
        }
    }
}

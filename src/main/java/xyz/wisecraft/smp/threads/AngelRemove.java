package xyz.wisecraft.smp.threads;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.Angel;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.UUID;

public class AngelRemove extends BukkitRunnable {
    private final java.util.UUID UUID;
    private final Angel angel;
    private final WisecraftSMP plugin;

    public AngelRemove(WisecraftSMP plugin, UUID UUID, Angel angel) {
        this.angel = angel;
        this.UUID = UUID;
        this.plugin = plugin;
    }
    @Override
    public void run() {

        if (angel.getQuit() || !angel.hasGraceResetTimer()) {
            plugin.getGearmap().remove(UUID);
        }
        else if (angel.getQuit() || angel.hasGraceResetTimer()) {
            new AngelRemove(plugin, UUID, angel).runTaskLater(plugin, 20*60*5); //5 minutes
        }
        else
            angel.setRemoveTime(false);

    }
}

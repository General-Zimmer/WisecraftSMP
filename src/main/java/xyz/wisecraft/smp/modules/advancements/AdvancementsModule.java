package xyz.wisecraft.smp.modules.advancements;

import xyz.wisecraft.smp.modules.advancements.listeners.QuestListeners;
import xyz.wisecraft.smp.modules.advancements.listeners.TimberListeners;

public class AdvancementsModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    @Override
    public void onEnable() {

        plugin.getCommand("autoroles").setExecutor(new Command());
    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new QuestListeners(), plugin);

        if (plugin.isTimberEnabled())
            plugin.getServer().getPluginManager().registerEvents(new TimberListeners(), plugin);
    }

}

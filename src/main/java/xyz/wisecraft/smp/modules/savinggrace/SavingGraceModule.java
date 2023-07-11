package xyz.wisecraft.smp.modules.savinggrace;

import xyz.wisecraft.smp.modules.savinggrace.listeners.AngelListeners;

public class SavingGraceModule implements xyz.wisecraft.smp.modulation.ModuleClass{
    @Override
    public void onEnable() {
    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new AngelListeners(), plugin);
    }

}

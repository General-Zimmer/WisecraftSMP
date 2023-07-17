package xyz.wisecraft.smp.modules.savinggrace;

import xyz.wisecraft.smp.modules.savinggrace.listeners.AngelListeners;

/**
 * Module class for SavingGrace
 */
public class SavingGraceModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    @Override
    public void onEnable() {
        // todo Take all online players and give them an Angel in case of a reload
    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new AngelListeners(), plugin);
    }

}

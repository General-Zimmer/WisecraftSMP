package xyz.wisecraft.smp.modules.heirloom;

import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;

public class HeirloomModule implements ModuleClass {


    @Override
    public void onEnable() {

    }

    @Override
    public void registerEvents() {
       plugin.getServer().getPluginManager().registerEvents(new BowListener(), plugin);
    }
}

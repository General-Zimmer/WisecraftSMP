package xyz.wisecraft.smp.modulation;

import xyz.wisecraft.smp.WisecraftSMP;

public interface ModuleClass {

    WisecraftSMP plugin = WisecraftSMP.getInstance();

    int id = -1;

    default void startModule() {
        onEnable();
        registerEvents();
        registerCommands();
    }

    default void stopModule() {
        onDisable();
    }

    void onEnable();

    default void onDisable() {}
    default void registerEvents() {}
    default void registerCommands() {}
}

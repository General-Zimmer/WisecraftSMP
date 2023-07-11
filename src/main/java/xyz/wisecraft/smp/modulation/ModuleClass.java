package xyz.wisecraft.smp.modulation;

import xyz.wisecraft.smp.WisecraftSMP;

public interface ModuleClass {

    WisecraftSMP plugin = WisecraftSMP.getInstance();

    int id = -1;

    /**
     * This method is called when the module is enabled.
     */
    default void startModule() {
        onEnable();
        registerEvents();
        registerCommands();
    }

    /**
     * This method is called when the module is disabled.
     */
    default void stopModule() {
        onDisable();
    }

    /**
     * This method should have all the startup code for the module.
     */
    void onEnable();

    /**
     * This method should have all the shutdown code for the module.
     */
    default void onDisable() {}

    /**
     * This method should register all the events for the module.
     */
    default void registerEvents() {}

    /**
     * This method should register all the commands for the module.
     */
    default void registerCommands() {}
}

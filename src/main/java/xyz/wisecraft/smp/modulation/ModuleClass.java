package xyz.wisecraft.smp.modulation;

import xyz.wisecraft.smp.WisecraftSMP;

/**
 * This interface is used to create modules.
 */
public interface ModuleClass extends Comparable<ModuleClass> {

    /**
     * The plugin instance.
     */
    WisecraftSMP plugin = WisecraftSMP.getInstance();

    /**
     * This method is called when the module is starting.
     */
    default void startModule() {
        onEnable();
        registerEvents();
        registerCommands();
    }

    /**
     * This method is called when the module is shutting down.
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

    default long getModuleID() {
        return plugin.getModuleConfig().getBoolean(plugin.getModulePath() + getModuleName() + ".enabled") ?
                plugin.getModuleConfig().getLong(plugin.getModulePath() + getModuleName() + ".id") : -1;
    }

    default void setModuleID(long id) {
        plugin.getModuleConfig().set(plugin.getModulePath() + getModuleName() + ".id", id);
    }

    default String getModuleName() {
        return this.getClass().getSimpleName().split("Module")[0];
    }

    @Override
    default int compareTo(ModuleClass module) {
        return Long.compare(this.getModuleID(), module.getModuleID());
    }

}

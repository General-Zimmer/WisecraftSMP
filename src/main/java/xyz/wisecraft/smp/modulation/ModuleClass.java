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

        if (!isModuleEnabled()) return;

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

    /**
     * Gets the module ID. Will return -1 if the id does not exist.
     * @return The module ID.
     */
    default long getModuleID() {
        return plugin.getModuleConfig().getLong(getModuleSettingPath(ModuleSettings.ID), -1);
    }

    /**
     * Gets the module name. All ModuleClass implementations should be named as [name]Module.
     * @return The module name.
     */
    default String getModuleName() {
        return this.getClass().getSimpleName().split("Module")[0];
    }

    /**
     * Gets the module enabled status.
     * @return The module enabled status.
     */
    private boolean isModuleEnabled() {
        return plugin.getModuleConfig().getBoolean(getModuleSettingPath(ModuleSettings.ENABLED));
    }

    /**
     * Gets the module setting path.
     * @param setting The setting to get the path for.
     * @return The module setting path.
     */
    private String getModuleSettingPath(ModuleSettings setting) {
        return plugin.getModulePath() + "." + getModuleName() + "." + setting.toString();
    }

    /**
     * Compares the module ID of this module to another module.
     * @param module The module to compare to.
     * @return 0 if they're equal, -1 if the other module is higher, and 1 if this method is higher.
     */
    @Override
    default int compareTo(ModuleClass module) {
        return Long.compare(this.getModuleID(), module.getModuleID());
    }

}

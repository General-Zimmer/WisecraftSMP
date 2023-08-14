package xyz.wisecraft.smp.modulation;

import org.bukkit.plugin.Plugin;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;

import java.util.ArrayList;

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
    default boolean startModule() {

        if (!isModuleEnabled() && !hasAllHardDependencies()) return false;

        onEnable();
        registerEvents();
        registerCommands();
        return true;
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
     * Sets up a dependency. This method gets an Object extending Plugin type.
     * @param pluginName The name of the dependency.
     * @param clazz The class of the dependency.
     * @return The dependency.
     * @param <T> The type of the dependency.
     */
    default <T> T setupDependency(String pluginName, Class<T> clazz) {
        T dependency = ModulationStorage.getDependency(pluginName, clazz);
        if (dependency != null) return dependency;

        return UtilModuleCommon.setupDependency(pluginName, clazz);
    }

    /**
     * Sets up a dependency. This method only validates if the plugin is enabled.
     * @param pluginName The name of the plugin.
     * @return true if the plugin is enabled.
     */
    default boolean setupDependency(String pluginName) {

        Plugin setupPlugin = plugin.getServer().getPluginManager().getPlugin(pluginName);
        if (setupPlugin == null) {return false;}
        return setupPlugin.isEnabled();

    }

    /**
     * Sets up a dependency. This method gets the registered service provider by the plugin.
     * @param clazz The class of the dependency.
     * @return The dependency.
     * @param <T> The type of the dependency.
     */
    default <T> T setupDependency(Class<T> clazz) {

        T dependency = ModulationStorage.getDependency(clazz.getName(), clazz);
        if (dependency != null) return dependency;

        return UtilModuleCommon.setupDependency(clazz);
    }

    /**
     * Checks if the module has all the hard dependencies.
     * @return true if the module has all the hard dependencies.
     */
    default boolean hasAllHardDependencies() {
        return true;
    }

    /**
     * Gets the module dependencies of other modules. This method should be overridden if the module has dependencies.
     * @return The module dependencies.
     */
    default ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        return null;
    }

    default ModuleClass getModule(Class<?> clazz) {
        for (ModuleClass module : plugin.getModules()) {
            if (module.getClass().equals(clazz))
                return module;
        }
        return null;
    }


    /**
     * Gets the module ID. Will return -1 if the id does not exist.
     * @return The module ID.
     */
    default long getModuleID() {
        return plugin.getModuleConfig().getLong(UtilModuleCommon.getSetting(this, ModuleSettings.ID), -1);
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
    default boolean isModuleEnabled() {
        return plugin.getModuleConfig().getBoolean(UtilModuleCommon.getSetting(this, ModuleSettings.ENABLED));
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

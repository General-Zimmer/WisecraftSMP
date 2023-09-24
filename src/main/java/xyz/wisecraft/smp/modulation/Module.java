package xyz.wisecraft.smp.modulation;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.models.ModuleClass;
import xyz.wisecraft.smp.modulation.models.ModuleInfo;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This interface is used to create modules. It has all the necessary methods to operate the modules silently
 * (as in, the modules and its developers doesn't see the extra checks).
 * @see ModuleInfo
 * @see ModuleClass ModuleClass for the class that implements this interface and should be used to create objects with.
 */
public interface Module extends Comparable<Module> {

    WisecraftSMP plugin = WisecraftSMP.getInstance();

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
    default @NotNull Set<Listener> registerListeners() {
        return new HashSet<>();
    }

    /**
     * This method should register all the commands for the module.
     */
    default @NotNull Set<BukkitCommand> registerCommands() {return new HashSet<>();}

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
        Plugin setupPlugin = WisecraftSMP.getInstance().getServer().getPluginManager().getPlugin(pluginName);
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
     * Checks if the module has all the hard dependencies or features/states. All dependencies needs to be setup in the constructor.
     * @return true if the module has all the hard dependencies.
     */
    default boolean hasAllHardDependencies() {
        return true;
    }

    /**
     * Gets the module dependencies of other modules. This method should be overridden if the module has dependencies.
     * @return The module dependencies.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    default ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        return depends;
    }

    /**
     * Gets the module ID. Will return -1 if the id does not exist.
     * @return The module ID.
     */
    long getModuleID();

    /**
     * Gets the module name. All Module implementations should be named as [name]Module.
     * @return The module name.
     */
    default String getModuleName() {
        return UtilModuleCommon.getModuleName(this.getClass());
    }

    /**
     * This method is called when the module is shutting down.
     */
    void disableModule();

    @SuppressWarnings("unused")
    default WisecraftSMP getPlugin(){
        return WisecraftSMP.getInstance();
    }

    /**
     * Compares the module ID of this module to another module.
     * @param module The module to compare to.
     * @return 0 if they're equal, -1 if the other module is higher, and 1 if this method is higher.
     */
    @Override
    default int compareTo(Module module) {
        return Long.compare(this.getModuleID(), module.getModuleID());
    }
}

package xyz.wisecraft.smp.modulation.storage;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.*;

@SuppressWarnings("unused")
public class ModulationStorage {


    private static final Object lockModule = new Object();
    private static final HashMap<String, Object> dependencies = new HashMap<>();
    private static final Set<ModuleClass> modules = new HashSet<>();

    /**
     * Adds a dependency.
     * @param pluginName The name of the dependency.
     * @param dependency The dependency.
     * @param <T> The type of the dependency.
     */
    public static <T> void addDependency(String pluginName, T dependency) {
        dependencies.put(pluginName, dependency);
    }

    /**
     * Gets a dependency.
     * @param pluginName The name of the dependency.
     * @param clazz The class of the dependency.
     * @return The dependency or null if it does not exist.
     * @param <T> The type of the dependency.
     */
    public static <T> T getDependency(String pluginName, Class<T> clazz) {
        try {
            return clazz.cast(dependencies.get(pluginName));
        } catch (ClassCastException e) {
            throw new ClassCastException("The dependency " + pluginName + " is not of type " + clazz.getName());
        }
    }

    /**
     * Gets all the listeners for a module.
     * @param clazz The class of the module.
     * @return The listeners.
     */
    public static @NotNull Set<Listener> getListeners(Class<?> clazz) {
        for (ModuleClass module : modules) {
            if (module.getClass().equals(clazz))
                return new HashSet<>(module.getModuleInfo().getListeners());
        }
        return new HashSet<>();
    }

    public static @NotNull Set<BukkitCommand> getCommands(Class<?> clazz) {
        for (ModuleClass module : modules) {
            if (module.getClass().equals(clazz))
                return new HashSet<>(module.getModuleInfo().getCommands());
        }
        return new HashSet<>();
    }

    /**
     * Gets an instance of the module or null if it isn't loaded.
     * @param clazz The class of the module.
     * @return The module instance or null if not found.
     */
    public static Module getModule(Class<?> clazz) {
        for (Module module : modules) {
            if (module.getClass().equals(clazz))
                return module;
        }
        return null;
    }

     public static void removeModule(Class<?> clazz) {
        synchronized (lockModule) {
            modules.removeIf(module -> module.getClass().equals(clazz));
        }

    }

    /**
     * Adds a module.
     */
     public static void addModule(@NotNull ModuleClass module) {
        synchronized (lockModule) {
            modules.add(module);
        }
    }

    /**
     * Gets all modules in a new Set.
     * @return The modules.
     */
    public static Set<ModuleClass> getModules() {
        return new HashSet<>(modules);
    }


}

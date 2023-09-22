package xyz.wisecraft.smp.modulation.storage;

import org.bukkit.event.Listener;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;
import java.util.HashMap;

public class ModulationStorage {


    private static final HashMap<String, Object> dependencies = new HashMap<>();
    private static final HashMap<ModuleClass, ArrayList<Listener>> modules = new HashMap<>();

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
     * Adds a module.
     */
    public static void addModule(ModuleClass module, ArrayList<Listener> listener) {
        modules.put(module, listener);
    }

    /**
     * Gets all modules in a new ArrayList.
     * @return The modules.
     */
    public static HashMap<ModuleClass, ArrayList<Listener>> getModules() {
        return new HashMap<>(modules);
    }


}

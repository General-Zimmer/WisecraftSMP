package xyz.wisecraft.smp.modulation.storage;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;
import java.util.HashMap;

public class ModulationStorage {


    private static final HashMap<String, Object> dependencies = new HashMap<>();
    private final ArrayList<ModuleClass> modules = new ArrayList<>();

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
     * @param module The module.
     */
    public void addModule(ModuleClass module) {
        modules.add(module);
    }

    /**
     * Gets all modules in a new ArrayList.
     * @return The modules.
     */
    public ArrayList<ModuleClass> getModules() {
        return new ArrayList<>(modules);
    }

    /**
     * Adds all modules from an ArrayList.
     * @param modules The ArrayList of modules.
     */
    public void addAllModules(ArrayList<ModuleClass> modules) {
        this.modules.clear();
        this.modules.addAll(modules);
    }

}

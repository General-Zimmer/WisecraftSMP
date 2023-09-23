package xyz.wisecraft.smp.modulation.storage;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.models.ModuleInfo;

import java.util.*;

public class ModulationStorage {


    private static final HashMap<String, Object> dependencies = new HashMap<>();
    private static final LinkedHashMap<Module, ModuleInfo> modules = new LinkedHashMap<>();

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
    public static @NotNull List<Listener> getListeners(Class<?> clazz) {
        for (Map.Entry<Module, ModuleInfo> module : modules.entrySet()) {
            if (module.getKey().getClass().equals(clazz))
                return new ArrayList<>(module.getValue().getListeners());
        }
        return new ArrayList<>();
    }

    public static @NotNull List<BukkitCommand> getCommands(Class<?> clazz) {
        for (Map.Entry<Module, ModuleInfo> module : modules.entrySet()) {
            if (module.getKey().getClass().equals(clazz))
                return new ArrayList<>(module.getValue().getCommands());
        }
        return new ArrayList<>();
    }

    /**
     * Gets an instance of the module or null if it isn't loaded.
     * @param clazz The class of the module.
     * @return The module instance or null if not found.
     */
    public static Module getModule(Class<?> clazz) {
        for (Module module : ModulationStorage.getModules().keySet()) {
            if (module.getClass().equals(clazz))
                return module;
        }
        return null;
    }

    /**
     * Adds a module.
     */
    public static void addModule(Module module, ModuleInfo moduleInfo) {
        modules.put(module, moduleInfo);
    }

    /**
     * Gets all modules and their info.
     * @return The modules.
     */
    public static Map<Module, ModuleInfo> getModules() {
        return new LinkedHashMap<>(modules);
    }


}

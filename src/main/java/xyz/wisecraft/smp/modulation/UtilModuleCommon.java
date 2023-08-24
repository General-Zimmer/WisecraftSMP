package xyz.wisecraft.smp.modulation;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;
import xyz.wisecraft.smp.util.UtilRandom;

import java.util.ArrayList;

public abstract class UtilModuleCommon {

    private static final WisecraftSMP plugin = WisecraftSMP.getInstance();

    /**
     * Gets the module setting path.
     * @param setting The setting to get the path for.
     * @return The module setting path.
     */
    public static String getSetting(ModuleClass module, ModuleSettings setting) {
        return plugin.getModulePath() + "." + module.getModuleName() + "." + setting.toString();
    }

    /**
     * Gets the module setting path.
     * @param moduleString The module string.
     * @param setting The setting to get the path for.
     * @return The module setting path.
     */
    public static String getSetting(String moduleString, ModuleSettings setting) {
        return plugin.getModulePath() + "." + moduleString + "." + setting.toString();
    }

    /**
     * Sets up a dependency. This method gets an Object extending Plugin type.
     * <p>
     * Please note that the name of the plugin is case-sensitive
     * @param pluginName The name of the plugin.
     * @return true the dependency was successfully setup.
     */
    public static <T> T setupDependency(String pluginName, Class<T> clazz) {
        Plugin setupPlugin = plugin.getServer().getPluginManager().getPlugin(pluginName);
        if (setupPlugin == null) {return null;}
        if(!setupPlugin.isEnabled()) {return null;}


        ModulationStorage.addDependency(pluginName, setupPlugin);
        return clazz.cast(setupPlugin);
    }

    /**
     * Sets up a dependency. This method gets the registered service provider by the plugin.
     * @param clazz The class of the dependency.
     * @return true the dependency was successfully setup.
     * @param <T> The type of the dependency.
     */
    public static <T> T setupDependency(Class<T> clazz) {

        RegisteredServiceProvider<T> provider = plugin.getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {return null;}

        T dependency = clazz.cast(provider.getProvider());
        ModulationStorage.addDependency(clazz.getName(), dependency);
        return dependency;
    }


    public static ArrayList<ModuleClass> sortModulesByTheirDependencies(ArrayList<ModuleClass> modules) {
        ArrayList<ModuleClass> allModules = new ArrayList<>(modules);
        ArrayList<ModuleClass> unsortedModules = new ArrayList<>(modules);
        ArrayList<ModuleClass> sortedModules = new ArrayList<>();


        // Sort modules by dependencies
        do {
            ArrayList<ModuleClass> tempList = UtilRandom.findDependencies(unsortedModules.get(0), allModules);
            sortedModules.addAll(tempList);
            unsortedModules.removeAll(tempList);
        } while (!unsortedModules.isEmpty());

        return sortedModules;
    }

    public static ArrayList<ModuleClass> sortDependTrimmed(ArrayList<ModuleClass> modules) {
        ArrayList<ModuleClass> sortedArray = sortModulesByTheirDependencies(modules);
        ArrayList<ModuleClass> sortedArrayTrimmed = new ArrayList<>();


        do {
            ModuleClass module = sortedArray.get(0);

            sortedArray.remove(module);
            if (!sortedArray.contains(module))
                sortedArrayTrimmed.add(module);
        } while (!sortedArray.isEmpty());

        return sortedArrayTrimmed;
    }

}

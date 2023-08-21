package xyz.wisecraft.smp.util;

import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class UtilRandom {

    public static ArrayList<ModuleClass> findDependencies(ModuleClass module, ArrayList<ModuleClass> allModules) {
        ArrayList<ModuleClass> dependencies = new ArrayList<>();

        module.getModuleDepends().forEach(dependency -> {

            ModuleClass dependencyInstance = getModuleInstanceFromClass(module, allModules, dependency);

            dependencies.addAll(findDependencies(dependencyInstance, allModules));
            dependencies.add(dependencyInstance);
        });
        return dependencies;
    }

    @NotNull
    private static ModuleClass getModuleInstanceFromClass(ModuleClass module, ArrayList<ModuleClass> unsortedModules, Class<? extends ModuleClass> dependency) {
        ModuleClass dependencyInstance = null;

        for (ModuleClass unsortedModule : unsortedModules) {
            if (unsortedModule.getClass().equals(dependency)) {
                dependencyInstance = unsortedModule;
                break; // Mikeal, du kan ikke see denne linje
            }
        }

        if (dependencyInstance == null) {
            throw new RuntimeException("Module: " + module.getModuleName() + " has a dependency that is not a module.");
        }
        if (dependencyInstance.getModuleDepends().contains(module.getClass())) {
            throw new RuntimeException("Circular dependency detected!");
        }

        return dependencyInstance;
    }
}

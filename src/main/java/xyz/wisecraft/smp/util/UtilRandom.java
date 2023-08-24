package xyz.wisecraft.smp.util;

import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class UtilRandom {

    public static ArrayList<ModuleClass> findDependencies(ModuleClass parentModule, ArrayList<ModuleClass> allModules) {
        ArrayList<ModuleClass> dependencies = new ArrayList<>();
        dependencies.add(parentModule);
        parentModule.getModuleDepends().forEach(dependency -> {

            ModuleClass dependencyInstance = getModuleInstanceFromClass(parentModule, allModules, dependency);

            dependencies.addAll(findDependencies(dependencyInstance, allModules));
        });

        return dependencies;
    }

    private static @NotNull ModuleClass getModuleInstanceFromClass(ModuleClass parentModule, ArrayList<ModuleClass> allModules, Class<? extends ModuleClass> dependency) {
        ModuleClass dependencyInstance = null;

        for (ModuleClass module : allModules) {
            if (module.getClass().equals(dependency)) {
                dependencyInstance = module;
                break; // Mikeal, du kan ikke see denne linje
            }
        }

        if (dependencyInstance == null) {
            throw new RuntimeException("Module: " + parentModule.getClass().getSimpleName() + " has a dependency that " +
                    "is not a module or doesn't exist.");
        }
        if (dependencyInstance.getModuleDepends().contains(parentModule.getClass())) {
            throw new RuntimeException("Direct circular dependency detected!");
        }

        return dependencyInstance;
    }
}

package xyz.wisecraft.smp.modulation.models;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.exceptions.MissingDependencyException;

import static xyz.wisecraft.smp.modulation.UtilModuleCommon.*;
import static xyz.wisecraft.smp.modulation.storage.ModulationStorage.getCommands;
import static xyz.wisecraft.smp.modulation.storage.ModulationStorage.getListeners;

/**
 * This class is used to create modules. All classes inhereting this class must end with "Module".
 * <p>
 *     Modulation was created to enable adding extensive features within one singular plugin and to divide and conquer
 *     the code so different developers doesn't need to know or see every feature in the plugin and can focus on their own module.
 *     This makes it easier to maintain the code and to add new features and helps new developers get into
 *     this framework and old developers will not have a hard time getting into this since modules are intended to
 *     seem like a standalone plugin.
 * @see Module
 * @see ModuleInfo
 */
public abstract class ModuleClass implements Module {


    private boolean isModuleEnabled = false;
    /**
     * The ID of the module. This is used to identify the module.
     * <p>
     *     Note: When the ID is -10, the module is in testing mode.
     */
    private final long ID;
    @Getter
    private ModuleInfo moduleInfo;

    public ModuleClass(long id) {
        if (!this.getClass().getSimpleName().endsWith("Module")) {
            throw new RuntimeException("Module class name must end with \"Module\"!");
        }
        this.ID = id;
    }

    public ModuleClass() {
        this.ID = -10;
        if (!plugin.getIsTesting())
            throw new RuntimeException("Module was initalized without an ID in production!");

    }

    /**
     * Gets the module enabled status.
     * @return The module enabled status.
     */
    @Override
    public boolean isModuleEnabled() {
        return isModuleEnabled;
    }

    /**
     * Will register all necessary events and commands for the module's function to work.
     * @return ModuleInfo if the module was enabled or null if it wasn't.
     */
    @Override
    public @NotNull ModuleInfo enableModule() {

        if (isModuleEnabled())
            throw new IllegalStateException("Module " + getModuleName() + " is already enabled!");
        isModuleEnabled = true;
        if (!hasAllHardDependencies())
            throw new MissingDependencyException("Module " + getModuleName() + " does not have all hard dependencies!");


        onEnable();
        ModuleInfo moduleInfo = new ModuleInfo(getModuleName(), registerListeners(), registerCommands());
        this.moduleInfo = moduleInfo;

        moduleInfo.getCommands().forEach(UtilModuleCommon::registerCommand);
        moduleInfo.getListeners().forEach(UtilModuleCommon::registerListener);
        return moduleInfo;
    }

    // todo prevent disabling modules that are required by other modules.
    /**
     * This method is called when the module is shutting down.
     */
    @Override
    public void disableModule() {

        if (!isModuleEnabled()) {
            throw new IllegalStateException("Module " + getModuleName() + " is already disabled!");
        }

        onDisable();
        getListeners(this.getClass()).forEach(HandlerList::unregisterAll);
        getCommands(this.getClass()).forEach(UtilModuleCommon::unregisterBukkitCommand);
        isModuleEnabled = false;
        refreshTabcompletion();
    }

    @Override
    public void reenableModule() {
        moduleInfo.getCommands().forEach(UtilModuleCommon::registerCommand);
        moduleInfo.getListeners().forEach(UtilModuleCommon::registerListener);
        isModuleEnabled = true;
        refreshTabcompletion();
    }

    @Override
    public long getModuleID() {
        return ID;
    }
}

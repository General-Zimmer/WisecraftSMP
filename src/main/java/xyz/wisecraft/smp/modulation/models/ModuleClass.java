package xyz.wisecraft.smp.modulation.models;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.enums.ModuleState;
import xyz.wisecraft.smp.modulation.exceptions.MissingDependencyException;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;

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


    /**
     * -- GETTER --
     *  Gets the module state.
     *
     *
     */
    @Getter
    private ModuleState moduleState = plugin.getModuleConfig().getBoolean(getSetting(this, ModuleSettings.ENABLED), false)
            ? ModuleState.INITIAL : ModuleState.DISABLED;
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
        moduleState = ModuleState.TESTING;
    }

    /**
     * Will register all necessary events and commands for the module's function to work.
     * @return ModuleInfo if the module was enabled or null if it wasn't.
     */
    public boolean enableModule() {

        if (getModuleState() == ModuleState.DISABLED | getModuleState() == ModuleState.ENABLED)
            throw new IllegalStateException(getErrorMessage());
        if (!hasAllHardDependencies())
            throw new MissingDependencyException("Module " + getModuleName() + " does not have all hard dependencies!");


        onEnable();
        this.moduleInfo = new ModuleInfo(getModuleName(), registerListeners(), registerCommands());

        reenableModule();
        return true;
    }

    // todo prevent disabling modules that are required by other modules.
    /**
     * This method is called when the module is shutting down.
     */
    @Override
    public void disableModule() {

        if (getModuleState() == ModuleState.DISABLED) {
            throw new IllegalStateException(getErrorMessage());
        }
        if (getModuleState() == ModuleState.INITIAL) {
            throw new IllegalStateException(getErrorMessage());
        }

        onDisable();
        getListeners(this.getClass()).forEach(HandlerList::unregisterAll);
        getCommands(this.getClass()).forEach(UtilModuleCommon::unregisterBukkitCommand);
        moduleState = ModuleState.DISABLED;
        refreshTabcompletion();
    }

    public void reenableModule() {
        if (getModuleState() == ModuleState.ENABLED) {
            throw new IllegalStateException(getErrorMessage());
        }
        moduleInfo.getCommands().forEach(UtilModuleCommon::registerCommand);
        moduleInfo.getListeners().forEach(UtilModuleCommon::registerListener);
        moduleState = ModuleState.ENABLED;
        refreshTabcompletion();
    }

    @Override
    public long getModuleID() {
        return ID;
    }

    private String getErrorMessage() {
        if (getModuleState() == ModuleState.INITIAL)
            return "Module " + getModuleName() + " have not been initialized!";
        else
            return "Module " + getModuleName() + " is already " + moduleState +"!";
    }
}

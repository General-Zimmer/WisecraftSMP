package xyz.wisecraft.smp.modulation;

import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;

public abstract class UtilModule {

    private static final WisecraftSMP plugin = WisecraftSMP.getInstance();

    /**
     * Gets the module setting path.
     * @param setting The setting to get the path for.
     * @return The module setting path.
     */
    public static String getSetting(ModuleClass module, ModuleSettings setting) {
        return plugin.getModulePath() + "." + module.getModuleName() + "." + setting.toString();
    }


}

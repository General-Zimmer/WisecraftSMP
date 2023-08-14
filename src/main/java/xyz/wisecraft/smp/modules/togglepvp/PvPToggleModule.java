package xyz.wisecraft.smp.modules.togglepvp;

import com.craftaro.ultimatetimber.UltimateTimber;
import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PVPTimberListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PlayerListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PvPListener;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;
import xyz.wisecraft.smp.modules.togglepvp.utils.PlaceholderAPIHook;

import java.io.File;
import java.util.Objects;

/**
 * Module class for PVPToggle
 */
public class PvPToggleModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    private static PvPToggleModule module;
    private final boolean isTimberEnabled = setupDependency("UltimateTimber", UltimateTimber.class) != null;
    private final boolean isPAPIEnabled = setupDependency("PlaceholderAPI", PlaceholderAPIPlugin.class) != null;
    private final INametagApi nametagAPI = (setupDependency("NametagEdit", NametagEdit.class) != null ? NametagEdit.getApi() : null);
    private final WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);

    public PvPToggleModule() {
        module = this;
    }

    @Override
    public void onEnable() {
        setupPAPI();


        // PVPToggle data
        File PVPData = new File(plugin.getDataFolder(), "togglepvp");
        PVPStorage.setPVPDataUtils(new PersistentData(PVPData));

        PVPStorage.setBlockedWorlds(plugin.getConfig().getStringList("SETTINGS.BLOCKED_WORLDS"));

    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PvPListener(), plugin);
        if (isTimberEnabled && core != null) {
            plugin.getServer().getPluginManager().registerEvents(new PVPTimberListener(core), plugin);
        }
    }

    @Override
    public void registerCommands() {

        Objects.requireNonNull(plugin.getCommand("pvp"), "command isn't registered").setExecutor(new PVPCMD());
    }

    private void setupPAPI() {
        if(isPAPIEnabled) {
            new PlaceholderAPIHook().register();
        }
    }

    public INametagApi getNametagAPI() {
        return nametagAPI;
    }

    public static PvPToggleModule getModule() {
        return module;
    }
}

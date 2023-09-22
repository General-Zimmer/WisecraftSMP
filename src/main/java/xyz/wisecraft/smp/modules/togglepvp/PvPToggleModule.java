package xyz.wisecraft.smp.modules.togglepvp;

import com.nametagedit.plugin.api.INametagApi;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PVPTimberListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PlayerListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PvPListener;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.modules.togglepvp.utils.PlaceholderAPIHook;

import java.io.File;
import java.util.ArrayList;

/**
 * Module class for PVPToggle
 */
public class PvPToggleModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    private static PvPToggleModule module;
    private final boolean isTimberEnabled = setupDependency("UltimateTimber");
    private final boolean isPAPIEnabled = setupDependency("PlaceholderAPI");
    private final INametagApi nametagAPI = setupDependency("NametagEdit", INametagApi.class);
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
    public ArrayList<Listener> registerListeners() {
        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerListener());
        listeners.add(new PvPListener());
        if (isTimberEnabled && core != null) {
            listeners.add(new PVPTimberListener(core));
        }
        return listeners;
    }

    @Override
    public ArrayList<BukkitCommand> registerCommands() {
        ArrayList<BukkitCommand> commands = new ArrayList<>();
        commands.add(new PVPCMD());
        return commands;
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

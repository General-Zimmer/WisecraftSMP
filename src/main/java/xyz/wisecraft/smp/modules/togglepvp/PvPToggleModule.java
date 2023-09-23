package xyz.wisecraft.smp.modules.togglepvp;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modulation.models.ModuleClass;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PVPTimberListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PlayerListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PvPListener;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.modules.togglepvp.utils.PlaceholderAPIHook;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Module class for PVPToggle
 */
public class PvPToggleModule extends ModuleClass {

    @Getter
    private static PvPToggleModule module;
    private final boolean isTimberEnabled = setupDependency("UltimateTimber");
    private final boolean isPAPIEnabled = setupDependency("PlaceholderAPI");
    @Getter
    private final INametagApi nametagAPI = setupDependency("NametagEdit") ? NametagEdit.getApi() : null;
    private final WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);

    public PvPToggleModule(long ID) {
        super(ID);
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
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        listeners.add(new PlayerListener());
        listeners.add(new PvPListener());
        if (isTimberEnabled && core != null) {
            listeners.add(new PVPTimberListener(core));
        }
        return listeners;
    }

    @Override
    public @NotNull Set<BukkitCommand> registerCommands() {
        HashSet<BukkitCommand> commands = new HashSet<>();
        commands.add(new PVPCMD("pvp"));
        return commands;
    }

    private void setupPAPI() {
        if(isPAPIEnabled) {
            new PlaceholderAPIHook().register();
        }
    }

}

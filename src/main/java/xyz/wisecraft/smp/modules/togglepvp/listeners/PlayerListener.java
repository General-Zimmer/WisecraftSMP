package xyz.wisecraft.smp.modules.togglepvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperCollection;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperGeneric;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilChat;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilPlayers;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Listener class for player events.
 */
public class PlayerListener implements Listener {

    private final WisecraftSMP plugin = WisecraftSMP.getInstance();
    private final FileConfiguration config = plugin.getConfig();
    private final StorageHelperMaps<HashMap<UUID,Boolean>, UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();
    private final StorageHelperGeneric<PersistentData> PVPDataUtils = PVPStorage.getPVPPersistentData();
    private final StorageHelperCollection<List<String>, String> blockedWorlds = PVPStorage.getBlockedWorlds();

    /**
     * Constructor for the PlayerListener class.
     */
    public PlayerListener() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!config.getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
                pvpPlayers.put(p.getUniqueId(), config.getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
            } else {
                PVPDataUtils.get().addPlayer(p);
                pvpPlayers.put(p.getUniqueId(), PVPDataUtils.get().GetPlayerPvPState(p));
            }
            if(!pvpPlayers.get(p.getUniqueId())) {
                if(config.getBoolean("SETTINGS.PARTICLES")) {
                    UtilPlayers.particleEffect(p.getPlayer());
                }
                if(config.getBoolean("SETTINGS.NAMETAG")) {
                    UtilPlayers.ChangeNametag(p.getPlayer(), "&c");
                }
            }
        }
    }

    /**
     * Saves the player's PVP state to the database when they leave.
     * @param event The PlayerQuitEvent.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(!config.getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
            pvpPlayers.put(p.getUniqueId(), config.getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
        } else {
            PVPDataUtils.get().addPlayer(p);
            pvpPlayers.put(p.getUniqueId(), PVPDataUtils.get().GetPlayerPvPState(p));
        }
        if(!pvpPlayers.get(p.getUniqueId())) {
            if(config.getBoolean("SETTINGS.PARTICLES")) {
                UtilPlayers.particleEffect(p.getPlayer());
            }
            if(config.getBoolean("SETTINGS.NAMETAG")) {
                UtilPlayers.ChangeNametag(p.getPlayer(), "&c");
            }
        }
    }

    /**
     * Saves the player's PVP state to the database when they leave.
     * @param event The PlayerQuitEvent.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(config.getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
            PVPDataUtils.get().UpdatePlayerPvPState(p);
        }
        pvpPlayers.remove(p.getUniqueId());
    }

    /**
     * Handles the player changing worlds.
     * @param event The PlayerChangedWorldEvent.
     */
    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        boolean playerPvpEnabled = !UtilPlayers.getPlayerState(player.getUniqueId());

        // If PVP isn't enabled in the world but the player has it enabled, disable it.
        if (!world.getPVP() && playerPvpEnabled) {
            UtilPlayers.setPlayerState(player.getUniqueId(), true);
            UtilChat.send(player, "PVP_WORLD_CHANGE_DISABLED");
            if(config.getBoolean("SETTINGS.NAMETAG"))
                UtilPlayers.ChangeNametag(player, "reset");
            return;
        }

        // If PVP is required (i.e. the world has PVP enabled, and it is in the blocked worlds) and the player has it disabled, enable it.
        if (player.getWorld().getPVP() && blockedWorlds.contains(world.getName()) && !playerPvpEnabled) {
            UtilPlayers.setPlayerState(player.getUniqueId(), false);
            UtilChat.send(player, "PVP_WORLD_CHANGE_REQUIRED");
            if (config.getBoolean("SETTINGS.PARTICLES"))
                UtilPlayers.particleEffect(player);
            if(config.getBoolean("SETTINGS.NAMETAG"))
                UtilPlayers.ChangeNametag(player, "&c");
        }
    }
}

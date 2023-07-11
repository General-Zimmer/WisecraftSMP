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
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilChat;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilPlayers;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final WisecraftSMP instance = WisecraftSMP.getInstance();
    private final FileConfiguration config = instance.getConfig();
    private final HashMap<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();
    private final PersistentData PVPDataUtils = PVPStorage.getPVPDataUtils();
    private final List<String> blockedWorlds = PVPStorage.getBlockedWorlds();

    public PlayerListener() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!config.getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
                pvpPlayers.put(p.getUniqueId(), config.getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
            } else {
                PVPDataUtils.addPlayer(p);
                pvpPlayers.put(p.getUniqueId(), PVPDataUtils.GetPlayerPvPState(p));
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(!config.getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
            pvpPlayers.put(p.getUniqueId(), config.getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
        } else {
            PVPDataUtils.addPlayer(p);
            pvpPlayers.put(p.getUniqueId(), PVPDataUtils.GetPlayerPvPState(p));
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
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(config.getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
            PVPDataUtils.UpdatePlayerPvPState(p);
        }
        pvpPlayers.remove(p.getUniqueId());
    }

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

package xyz.wisecraft.smp.togglepvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.togglepvp.Chat;
import xyz.wisecraft.smp.togglepvp.utils.Util;

public class PlayerListener implements Listener {

    public PlayerListener() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
                WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
            } else {
                WisecraftSMP.instance.dataUtils.addPlayer(p);
                WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.dataUtils.GetPlayerPvPState(p));
            }
            if(!WisecraftSMP.instance.players.get(p.getUniqueId())) {
                if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES")) {
                    Util.particleEffect(p.getPlayer());
                }
                if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG")) {
                    Util.ChangeNametag(p.getPlayer(), "&c");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(!WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
            WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
        } else {
            WisecraftSMP.instance.dataUtils.addPlayer(p);
            WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.dataUtils.GetPlayerPvPState(p));
        }
        if(!WisecraftSMP.instance.players.get(p.getUniqueId())) {
            if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES")) {
                Util.particleEffect(p.getPlayer());
            }
            if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG")) {
                Util.ChangeNametag(p.getPlayer(), "&c");
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
            WisecraftSMP.instance.dataUtils.UpdatePlayerPvPState(p);
        }
        WisecraftSMP.instance.players.remove(p.getUniqueId());
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        boolean playerPvpEnabled = !Util.getPlayerState(player.getUniqueId());

        // If PVP isn't enabled in the world but the player has it enabled, disable it.
        if (!world.getPVP() && playerPvpEnabled) {
            Util.setPlayerState(player.getUniqueId(), true);
            Chat.send(player, "PVP_WORLD_CHANGE_DISABLED");
            if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG"))
                Util.ChangeNametag(player, "reset");
            return;
        }

        // If PVP is required (i.e. the world has PVP enabled, and it is in the blocked worlds) and the player has it disabled, enable it.
        if (player.getWorld().getPVP() && WisecraftSMP.blockedWorlds.contains(world.getName()) && !playerPvpEnabled) {
            Util.setPlayerState(player.getUniqueId(), false);
            Chat.send(player, "PVP_WORLD_CHANGE_REQUIRED");
            if (WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES"))
                Util.particleEffect(player);
            if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG"))
                Util.ChangeNametag(player, "&c");
        }
    }
}

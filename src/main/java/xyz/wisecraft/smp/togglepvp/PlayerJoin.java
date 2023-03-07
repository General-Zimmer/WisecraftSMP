package xyz.wisecraft.smp.togglepvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.togglepvp.utils.Util;


public class PlayerJoin implements Listener {
	
	public PlayerJoin() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
				WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.DEFAULT_PVP_OFF")); //add player to players hash map and set their pvp state
				if(WisecraftSMP.instance.players.get(p.getUniqueId()) == false) {
					if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES")) {
						Util.particleEffect(p.getPlayer());
					}
					if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG")) {
	    				Util.ChangeNametag(p.getPlayer(), "&c");
					}
				}	
			} else {
				WisecraftSMP.instance.dataUtils.addPlayer(p.getPlayer());
				WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.dataUtils.GetPlayerPvPState(p.getPlayer())); //add player to players hash map and set their pvp state
				if(WisecraftSMP.instance.players.get(p.getUniqueId()) == false) {
					if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES")) {
						Util.particleEffect(p.getPlayer());	
					}
					if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG")) {
	    				Util.ChangeNametag(p.getPlayer(), "&c");
					}
				}
			}
		}
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(!WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
        	WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.DEFAULT_PVP_OFF")); //add player to players hash map and set their pvp state
    		if(WisecraftSMP.instance.players.get(p.getUniqueId()) == false) {
    			if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES")) {
    				Util.particleEffect(p.getPlayer());	
    			}
    			if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG")) {
    				Util.ChangeNametag(p.getPlayer(), "&c");
    			}
    		}	
    	} else {
			WisecraftSMP.instance.dataUtils.addPlayer(p.getPlayer());
			WisecraftSMP.instance.players.put(p.getUniqueId(), WisecraftSMP.instance.dataUtils.GetPlayerPvPState(p.getPlayer())); //add player to players hash map and set their pvp state
			if(WisecraftSMP.instance.players.get(p.getUniqueId()) == false) {
				if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PARTICLES")) {
					Util.particleEffect(p.getPlayer());	
				}
				if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.NAMETAG")) {
    				Util.ChangeNametag(p.getPlayer(), "&c");
				}
			}
		}
	}

}
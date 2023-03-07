package xyz.wisecraft.smp.togglepvp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.wisecraft.smp.WisecraftSMP;


public class PlayerLeave implements Listener {
	
	@EventHandler
    public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if(WisecraftSMP.instance.getConfig().getBoolean("SETTINGS.PERSISTENT_PVP_STATE")) {
			WisecraftSMP.instance.dataUtils.UpdatePlayerPvPState(p);
		}
		WisecraftSMP.instance.players.remove(p.getUniqueId()); //remove player from players hash map
	}

}
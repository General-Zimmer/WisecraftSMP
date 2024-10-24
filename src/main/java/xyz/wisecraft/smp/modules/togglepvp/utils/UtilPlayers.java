package xyz.wisecraft.smp.modules.togglepvp.utils;

import com.nametagedit.plugin.api.INametagApi;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.togglepvp.TogglePVPModule;
import xyz.wisecraft.smp.modules.togglepvp.events.PlayerChangedPVPStateEvent;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;


import java.util.*;


/**
 * Utility class for player related methods.
 */
public abstract class UtilPlayers {
	private static final WisecraftSMP plugin = WisecraftSMP.getInstance();
	private static final FileConfiguration config = plugin.getConfig();
	private static final Map<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();
	private static final Collection<String> blockedWorlds = PVPStorage.getBlockedWorlds();
	private static final Map<UUID, Date> cooldowns = PVPStorage.getCooldowns();
	private static final float radius = .75f;

	/**
	 * Get the state the player is in.
	 * @param uuid The UUID of the player to check
	 * @return True if the player is in PVP mode, false if they aren't
	 */
	public static boolean getPlayerState(UUID uuid){
		Boolean result = pvpPlayers.get(uuid);
		if(result==null) return false;
		else return result;
	}


	/**
	 * Set the state of the player used directly from the plugin.
	 * @param uuid The UUID of the player to set
	 * @param state The state to set the player to
	 */
	public static void setPlayerState(UUID uuid, boolean state){
		pvpPlayers.put(uuid,state);
	}

	/**
	 * Set the PVP state of the player from player commands.
	 * @param player The player to check
	 * @param state The state to set the player to
	 * @param caller The command sender that called the method
	 * @return True if the state was changed, false if it wasn't
	 */
	public static boolean setPlayerState(Player player, boolean state, CommandSender caller) {
		if (player == null) {
			return false;
		}

		World world = player.getWorld();
		if (!world.getPVP() && !state) {
			if (caller == player) {
				UtilChat.send(caller, "PVP_WORLD_CANNOT_CHANGE_SELF");
			} else {
				UtilChat.send(caller, "PVP_WORLD_CANNOT_CHANGE_OTHERS");
			}
			return false;
		}

		if (world.getPVP() && blockedWorlds.contains(world.getName()) && state) {
			if (caller == player) {
				UtilChat.send(caller, "PVP_WORLD_CANNOT_CHANGE_SELF");
			} else {
				UtilChat.send(caller, "PVP_WORLD_CANNOT_CHANGE_OTHERS");
			}
			return false;
		}
		PlayerChangedPVPStateEvent event = new PlayerChangedPVPStateEvent(player);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return false;
		}

		setPlayerState(player.getUniqueId(), state);
		return true;
	}

	/**
	 * Set the cooldown time for the player.
	 * @param p Player to set cooldown time for
	 */
	public static void setCooldownTime(Player p) {
		if (p.hasPermission("pvptoggle.bypass")) return;

		cooldowns.put(p.getUniqueId(), new Date());
	}

	/**
	 * Remove the cooldown time for the player.
	 * @param p Player to remove cooldown time for
	 */
	public static void removeCooldownTime(Player p) {
		cooldowns.remove(p.getUniqueId());
	}

	/**
	 * Check if the player has a cooldown.
	 * @param p Player to check
	 * @return True if the player has a cooldown, false if not
	 */
	public static boolean hasCooldown(Player p) {
		if(cooldowns.containsKey(p.getUniqueId()) ) {
			Date previousTime = cooldowns.get(p.getUniqueId());
			Date currentTime = new Date();
			int seconds = (int) (currentTime.getTime() - previousTime.getTime())/1000;
			if(seconds > config.getInt("SETTINGS.COOLDOWN") || p.hasPermission("WisecraftSMP.bypass")) {
				UtilPlayers.removeCooldownTime(p);
				return false;
			} else {
				UtilChat.send(p, "PVP_COOLDOWN", String.valueOf(config.getInt("SETTINGS.COOLDOWN") - seconds));
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Change the player's nametag color.
	 * @param p Player to change nametag color of
	 * @param color Color to change nametag to
	 */
	public static void ChangeNametag(Player p, String color) {
		INametagApi nameAPI = TogglePVPModule.getModule().getNametagAPI();
		if(nameAPI == null) {return;}

		if(color.equals("reset")) {
			nameAPI.clearNametag(p);
		} else {
			nameAPI.setPrefix(p, color);
		}

	}

	/**
	 * Spawn particles around the player.
	 * @param p Player to spawn particles around
	 */
	public static void particleEffect(Player p) {
		new BukkitRunnable() {

			@Override
			public void run() {			
				if(!p.isOnline() || pvpPlayers.get(p.getUniqueId())) {
					this.cancel();
				} else if(!p.isDead()) {
					double angle = 0;
					Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
					Location location = p.getLocation();
					

					for(int i = 0; i < 25; i++) {
						double x = (radius * Math.sin(angle));
						double z = (radius * Math.cos(angle));
						angle += 0.251;
						p.getWorld().spawnParticle(Particle.REDSTONE, location.getX()+x, location.getY(), location.getZ()+z, 0, 0, 1, 0, dustOptions);
					}
				}
				
			}
			
		}.runTaskTimer(plugin, 0L, 2L);
	}
	
}





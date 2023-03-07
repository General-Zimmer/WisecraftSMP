package xyz.wisecraft.smp.togglepvp.utils;

import com.nametagedit.plugin.NametagEdit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.togglepvp.Chat;


import java.util.Date;
import java.util.UUID;


public class Util {
	
	private static final float radius = .75f;

	public static boolean getPlayerState(UUID uuid){
		Boolean result = WisecraftSMP.instance.players.get(uuid);
		if(result==null) return false;
		else return result;
	}
	
	public static void setPlayerState(UUID uuid, boolean state){
		WisecraftSMP.instance.players.put(uuid,state);
	}

	// Set player state while performing checks to make sure it's a valid switch.
	public static boolean setPlayerState(Player player, boolean state, CommandSender caller) {
		if (player == null) {
			return false;
		}

		World world = player.getWorld();
		// You can't set the state to false (PVP enabled) if the world doesn't allow it
		if (!world.getPVP() && !state) {
			if (caller == player) {
				Chat.send(caller, "PVP_WORLD_CANNOT_CHANGE_SELF");
			} else {
				Chat.send(caller, "PVP_WORLD_CANNOT_CHANGE_OTHERS");
			}
			return false;
		}
		// You can't set the state to true (PVP disabled) if the world requires it
		if (world.getPVP() && WisecraftSMP.blockedWorlds.contains(world.getName()) && state) {
			if (caller == player) {
				Chat.send(caller, "PVP_WORLD_CANNOT_CHANGE_SELF");
			} else {
				Chat.send(caller, "PVP_WORLD_CANNOT_CHANGE_OTHERS");
			}
			return false;
		}

		setPlayerState(player.getUniqueId(), state);
		return true;
	}

	public static void setCooldownTime(Player p) {
		if (p.hasPermission("pvptoggle.bypass")) return;

		WisecraftSMP.instance.cooldowns.put(p.getUniqueId(), new Date());
	}
	
	public static void removeCooldownTime(Player p) {
		WisecraftSMP.instance.cooldowns.remove(p.getUniqueId());
	}
	
	public static boolean hasCooldown(Player p) {
		if(WisecraftSMP.instance.cooldowns.containsKey(p.getUniqueId()) ) {
			Date previousTime = WisecraftSMP.instance.cooldowns.get(p.getUniqueId());
			Date currentTime = new Date();
			int seconds = (int) (currentTime.getTime() - previousTime.getTime())/1000;
			if(seconds > WisecraftSMP.instance.getConfig().getInt("SETTINGS.COOLDOWN") || p.hasPermission("WisecraftSMP.bypass")) {
				Util.removeCooldownTime(p);
				return false;
			} else {
				Chat.send(p, "PVP_COOLDOWN", String.valueOf(WisecraftSMP.instance.getConfig().getInt("SETTINGS.COOLDOWN") - seconds));
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static void ChangeNametag(Player p, String color) {
		if(WisecraftSMP.instance.getServer().getPluginManager().isPluginEnabled("NametagEdit")) {
			if(color.equals("reset")) {
				NametagEdit.getApi().clearNametag(p);
			} else {
				NametagEdit.getApi().setPrefix(p, color);
			}	
		}
	}
	
	public static void particleEffect(Player p) {
		new BukkitRunnable() {

			@Override
			public void run() {			
				if(!p.isOnline() || WisecraftSMP.instance.players.get(p.getUniqueId())) {
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
			
		}.runTaskTimer(WisecraftSMP.instance, 0L, 2L);
	}
	
}





package xyz.wisecraft.smp.modules.togglepvp.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;


public class PersistentData {
	private final WisecraftSMP instance = WisecraftSMP.getInstance();
	private final FileConfiguration config = instance.getConfig();
	private final HashMap<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();
	private final File dir;
	
	public PersistentData(File file) {
		file.mkdir();
		this.dir = file;
	}
	
	public void addPlayer(Player p) {
		File file = new File(dir.getPath(), p.getUniqueId() + ".yml");
		
		if(!file.exists()) {
			try {
				FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);
				playerData.createSection("PvPState");
				playerData.set("PvPState", config.getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
				playerData.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void UpdatePlayerPvPState(Player p) {
		File file = new File(dir.getPath(), p.getUniqueId() + ".yml");
		try {
			FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);
			playerData.set("PvPState", pvpPlayers.get(p.getUniqueId()));
			playerData.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean GetPlayerPvPState(Player p) {
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(dir.getPath(), p.getUniqueId() + ".yml"));
		return playerData.getBoolean("PvPState");
	}

}
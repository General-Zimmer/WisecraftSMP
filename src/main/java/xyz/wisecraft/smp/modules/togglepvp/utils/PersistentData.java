package xyz.wisecraft.smp.modules.togglepvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class for handling persistent data.
 */
public class PersistentData {
	private final WisecraftSMP plugin = WisecraftSMP.getInstance();
	private final FileConfiguration config = plugin.getConfig();
	private final StorageHelperMaps<UUID, Boolean> pvpPlayers;
	private final File dir;

	/**
	 * Constructor for the PersistentData class.
	 * @param file The file.
	 */
	public PersistentData(File file, ModuleClass module) {
		//noinspection ResultOfMethodCallIgnored
		file.mkdir();
		this.dir = file;

		// setup PVPPlayers
		HashMap<UUID, Boolean> PVPPlayers = new HashMap<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			PVPPlayers.put(player.getUniqueId(), GetPlayerPvPState(player));
		}
		PVPStorage.PVPPlayers = new StorageHelperMaps<>(module, "PVPPlayers", PVPPlayers);
		this.pvpPlayers = PVPStorage.PVPPlayers;
	}

	/**
	 * Adds the player to the database.
	 * @param p The Player.
	 */
	public void addPlayer(Player p) {
		File file = new File(dir.getPath(), p.getUniqueId() + ".yml");
		
		if(!file.exists()) {
			try {
				FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);
				playerData.createSection("PvPState");
				playerData.set("PvPState", config.getBoolean("SETTINGS.DEFAULT_PVP_OFF"));
				playerData.save(file);
			} catch (Exception e) {
				plugin.getLogger().log(java.util.logging.Level.SEVERE, "Could not create player data file for " + p.getName() + "!", e);
			}
		}
	}

	/**
	 * Saves the player's PVP state to the database when they leave.
	 * @param p The Player.
	 */
	public void UpdatePlayerPvPState(Player p) {
		File file = new File(dir.getPath(), p.getUniqueId() + ".yml");
		try {
			FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);
			playerData.set("PvPState", pvpPlayers.get(p.getUniqueId()));
			playerData.save(file);
		} catch (Exception e) {
			plugin.getLogger().log(java.util.logging.Level.SEVERE, "Could not save player data file for " + p.getName() + "!", e);
		}
	}

	/**
	 * Gets the player's PVP state.
	 * @param p The Player.
	 * @return The player's PVP state.
	 */
	public boolean GetPlayerPvPState(Player p) {
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(dir.getPath(), p.getUniqueId() + ".yml"));
		return playerData.getBoolean("PvPState");
	}

}
package xyz.wisecraft.smp.modules.togglepvp.utils;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PlaceholderAPIHook
 */
public class PlaceholderAPIHook extends PlaceholderExpansion {

	private final WisecraftSMP plugin = WisecraftSMP.getInstance();
	private final Map<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String identifier) {
		if(player == null) { return ""; }

		//Placeholder: %WisecraftSMP_positive_rep%
		if(identifier.equals("pvp_state")) {
			return pvpPlayers.get(player.getUniqueId()) ? "&aOff" : "&cOn";
		}

		return null;
	}
	
	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public @NotNull String getIdentifier() {
		return "WisecraftSMP";
	}

	@Override
	public @NotNull String getAuthor() {
		return "General_Zimmer";
	}


	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

}
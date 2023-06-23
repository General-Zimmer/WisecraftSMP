package xyz.wisecraft.smp.togglepvp.utils;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	private final WisecraftSMP plugin;
	
	public PlaceholderAPIHook(WisecraftSMP plugin) {
		this.plugin = plugin;
	}

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String identifier) {
		if(player == null) { return ""; }

		//Placeholder: %WisecraftSMP_positive_rep%
		if(identifier.equals("pvp_state")) {
			return plugin.PVPPlayers.get(player.getUniqueId()) ? "&aOff" : "&cOn";
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
package xyz.wisecraft.smp.togglepvp.utils;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.WisecraftSMP;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	private WisecraftSMP plugin;
	
	public PlaceholderAPIHook(WisecraftSMP plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if(player == null) { return ""; }
		
		//Placeholder: %WisecraftSMP_positive_rep%
		if(identifier.equals("pvp_state")) {
			return WisecraftSMP.instance.players.get(player.getUniqueId()) ? "&aOff" : "&cOn";
		}
		
		return null;
	}
	
	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public String getIdentifier() {
		return "WisecraftSMP";
	}

	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}


	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

}
package xyz.wisecraft.smp.togglepvp.utils;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;

//todo Fix this hook
public class PlaceholderAPIHook extends PlaceholderExpansion {

	private final WisecraftSMP plugin;
	
	public PlaceholderAPIHook(WisecraftSMP plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String onPlaceholderRequest(Player player, @NotNull String identifier) {
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
	public @NotNull String getIdentifier() {
		return "WisecraftSMP";
	}

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}


	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

}
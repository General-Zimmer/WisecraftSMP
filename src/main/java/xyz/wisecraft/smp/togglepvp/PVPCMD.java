package xyz.wisecraft.smp.togglepvp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.togglepvp.utils.PVPCMDUtil;
import xyz.wisecraft.smp.togglepvp.utils.Util;

import java.util.ArrayList;
import java.util.List;


public class PVPCMD implements TabExecutor {

	WisecraftSMP plugin = WisecraftSMP.instance;
	String color = "&c";

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {


		if (!cmd.getName().equalsIgnoreCase("pvp"))
			return true;
		if (sender instanceof ConsoleCommandSender console)
			PVPCMDUtil.consoleCase(console, args, color);


		//check if command sender is player
		if (!(sender instanceof Player p)) return true;

		if (!p.hasPermission("pvptoggle.allow")) return true;


		if (args.length == 0) {
			if (Util.hasCooldown(p)) return true;

			// zero arg case
			PVPCMDUtil.zeroArg(p, color);
			return true;
		}
		switch (args[0]) {
			case "reload" -> PVPCMDUtil.reloadCase(p);
			case "help" -> PVPCMDUtil.getHelp(p);
			case "status" -> PVPCMDUtil.status(sender, p, args);
			default -> {
				if (!sender.hasPermission("pvptoggle.others.set")) {
					Chat.send(p, "COMMAND_NO_PERMISSION");
					return true;
				}
				// Check if command target exists
				Player other = Bukkit.getPlayerExact(args[0]);
				if (other == null) {
					Chat.send(p, "NO_PLAYER", args[0]);
					return true;
				}
				// Switch PVP State on target
				Boolean otherPlayerPVPState = plugin.players.get(other.getUniqueId());
				if (otherPlayerPVPState) {
					// Turn off
					if (Util.setPlayerState(other, false, sender)) {
						Chat.send(other, "PVP_STATE_ENABLED");
						if (other != p)
							Chat.send(p, "PVP_STATE_CHANGED_OTHERS", other.getName(), Util.getPlayerState(other.getUniqueId()));

						if (plugin.getConfig().getBoolean("SETTINGS.PARTICLES"))
							Util.particleEffect(other.getPlayer());
						if (plugin.getConfig().getBoolean("SETTINGS.NAMETAG"))
							Util.ChangeNametag(other.getPlayer(), color);
					}
				} else {
					// Turn on
					if (Util.setPlayerState(other, true, sender)) {
						Chat.send(other, "PVP_STATE_DISABLED");
						Chat.send(p, "PVP_STATE_CHANGED_OTHERS", other.getName(), Util.getPlayerState(other.getUniqueId()));
						if (plugin.getConfig().getBoolean("SETTINGS.NAMETAG"))
							Util.ChangeNametag(other.getPlayer(), "reset");
					}
				}
			}
		}
		return false;
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		List<String> words = new ArrayList<>();

		switch (args.length) {
			case 1 -> {
				if (sender.hasPermission("pvptoggle.allow")) {
					words.add("help");
				}
				if (sender.hasPermission("pvptoggle.reload")) {
					words.add("reload");
				}
				if (sender.hasPermission("pvptoggle.others")) {
					words.add("status");
				}
				return StringUtil.copyPartialMatches(args[0], words, new ArrayList<>());
			}

			//https://www.spigotmc.org/threads/tabcompleter-not-working.406512/
		}
		return null;

	}

}
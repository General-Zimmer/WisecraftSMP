package xyz.wisecraft.smp.features.togglepvp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.wisecraft.smp.WisecraftSMP;

public abstract class Chat {

	private static final WisecraftSMP instance = WisecraftSMP.getInstance();
	private static final FileConfiguration config = instance.getConfig();

	/**
	 * sends message without a argument
	 */
	public static void send(CommandSender sender, String message) {
		String msg = config.getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	/**
	 * sends message with a argument
	 */
	public static void send(CommandSender sender, String message, String argument) {
		String msg = config.getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		String output = msg.replaceAll("<argument>", argument);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}

	/**
	 * sends message with a argument and pvp state
	 */
	public static void send(CommandSender sender, String message, String argument, Boolean pvpState) {
		String msg = config.getString("MESSAGES." + message);
		if(msg != null && msg.equals(""))
			return;
		String output = msg.replaceAll("<argument>", argument);
		if(pvpState) {
			output = output.replaceAll("<pvpstate>", "off");
		} else {
			output = output.replaceAll("<pvpstate>", "on");
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}

}
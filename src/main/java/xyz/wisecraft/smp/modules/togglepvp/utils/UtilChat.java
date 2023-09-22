package xyz.wisecraft.smp.modules.togglepvp.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.wisecraft.smp.WisecraftSMP;

/**
 * Utility class for chat related methods.
 */
public abstract class UtilChat {

	private static final WisecraftSMP instance = WisecraftSMP.getInstance();
	private static final FileConfiguration config = instance.getConfig();

	/**
	 * sends message
	 * @param sender The sender to send the message to
	 * @param message The message to send
	 */
	public static void send(CommandSender sender, String message) {
		String msg = config.getString("MESSAGES." + message);
		if(msg.isEmpty())
			return;
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	/**
	 * sends message with a argument
	 * @param sender The sender to send the message to
	 * @param message The message to send
	 * @param argument The argument to replace argument with
	 */
	public static void send(CommandSender sender, String message, String argument) {
		String msg = config.getString("MESSAGES." + message);
		if(msg.isEmpty())
			return;
		String output = msg.replaceAll("<argument>", argument);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}

	/**
	 * sends message with a argument and pvp state
	 * @param sender The sender to send the message to
	 * @param message The message to send
	 * @param argument The argument to replace argument with
	 * @param pvpState The state of the player
	 */
	public static void send(CommandSender sender, String message, String argument, Boolean pvpState) {
		String msg = config.getString("MESSAGES." + message);
		if(msg != null && msg.isEmpty())
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
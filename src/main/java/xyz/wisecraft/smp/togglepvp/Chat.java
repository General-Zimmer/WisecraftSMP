package xyz.wisecraft.smp.togglepvp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.wisecraft.smp.WisecraftSMP;

public class Chat {

	/**
	 * sends message without a argument
	 */
	public static void send(CommandSender sender, String message) {
		String msg = WisecraftSMP.instance.getConfig().getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	/**
	 * sends message with a argument
	 */
	public static void send(CommandSender sender, String message, String argument) {
		String msg = WisecraftSMP.instance.getConfig().getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		String output = msg.replaceAll("<argument>", argument);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}

	/**
	 * sends message with a argument and pvp state
	 */
	public static void send(CommandSender sender, String message, String argument, Boolean pvpState) {
		String msg = WisecraftSMP.instance.getConfig().getString("MESSAGES." + message);
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
package xyz.wisecraft.smp.togglepvp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.wisecraft.smp.WisecraftSMP;

public class Chat {

	// sends message without a parameter
	public static void send(CommandSender sender, String message) {
		String msg = WisecraftSMP.instance.getConfig().getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	// sends message with a parameter
	public static void send(CommandSender sender, String message, String parameter) {
		String msg = WisecraftSMP.instance.getConfig().getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		String output = msg.replaceAll("<parameter>", parameter);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}
	
	// sends message with a parameter and pvp state
	public static void send(CommandSender sender, String message, String parameter, Boolean pvpState) {
		String msg = WisecraftSMP.instance.getConfig().getString("MESSAGES." + message);
		if(msg.equals(""))
			return;
		String output = msg.replaceAll("<parameter>", parameter);
		if(pvpState == true) {
			output = output.replaceAll("<pvpstate>", "off");
		} else {
			output = output.replaceAll("<pvpstate>", "on");
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}

}
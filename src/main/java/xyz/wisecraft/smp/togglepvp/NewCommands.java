package xyz.wisecraft.smp.togglepvp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.togglepvp.utils.Util;


public class NewCommands implements CommandExecutor {

	WisecraftSMP plugin = WisecraftSMP.instance;
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		String color = "&c";
		if(!cmd.getName().equalsIgnoreCase("pvp")) {return true;}


		if(sender instanceof Player p) { //check if command sender is player
				if(!p.hasPermission("pvptoggle.allow")) {return true;}


				if (args.length == 0) {
					if(!Util.getCooldown(p) || p.hasPermission("pvptoggle.bypass")) {
						Boolean current = plugin.players.get(p.getUniqueId());
							if(current) {
								Util.setCooldownTime(p);
								if (Util.setPlayerState(p, false, p)) {
                                    Chat.send(p, "PVP_STATE_ENABLED");
                                    if (plugin.getConfig().getBoolean("SETTINGS.PARTICLES")) {
                                        Util.particleEffect(p.getPlayer());
                                    }
                        			if(plugin.getConfig().getBoolean("SETTINGS.NAMETAG")) {
                        				Util.ChangeNametag(p.getPlayer(), color);
                        			}
                                }
							} else {
							    if (Util.setPlayerState(p, true, p)) {
                                    Chat.send(p, "PVP_STATE_DISABLED");
    								if(plugin.getConfig().getBoolean("SETTINGS.NAMETAG"))
    									Util.ChangeNametag(p.getPlayer(), "reset");
                                }
							}
					}
					return true;
				}
				switch (args[0]) {
					case "reload" -> {
						if (p.hasPermission("pvptoggle.reload")) {
							reloadConfig();
							Chat.send(p, "RELOAD");

						}

					}
					case "help" -> getHelp(p);
					case "status" -> {
						if (!sender.hasPermission("pvptoggle.others")) {
							return true;
						}
						if (args.length < 2) {
							p.sendMessage("&cToo few args");
						}
						Player other = Bukkit.getPlayerExact(args[1]);
						if (other == null) {
							Chat.send(p, "NO_PLAYER", args[1]);
						} else {
							Boolean current = plugin.players.get(other.getUniqueId());
							Chat.send(p, "PVP_STATUS_OTHERS", other.getName(), current);
						}

					}
					default -> {
						if (!sender.hasPermission("pvptoggle.others.set")) {
							Chat.send(p, "COMMAND_NO_PERMISSION");
						}
						Player other1 = Bukkit.getPlayerExact(args[0]);
						if (other1 == null) {
							Chat.send(p, "NO_PLAYER", args[0]);
							return true;
						}
						Boolean current = plugin.players.get(other1.getUniqueId());
						if (current) {
							if (Util.setPlayerState(other1, false, sender)) {
								Chat.send(other1, "PVP_STATE_ENABLED");
								if (other1 != p)
									Chat.send(p, "PVP_STATE_CHANGED_OTHERS", other1.getName(), Util.getPlayerState(other1.getUniqueId()));

								if (plugin.getConfig().getBoolean("SETTINGS.PARTICLES"))
									Util.particleEffect(other1.getPlayer());
								if (plugin.getConfig().getBoolean("SETTINGS.NAMETAG"))
									Util.ChangeNametag(other1.getPlayer(), color);
							}
						} else {
							if (Util.setPlayerState(other1, true, sender)) {
								Chat.send(other1, "PVP_STATE_DISABLED");
								Chat.send(p, "PVP_STATE_CHANGED_OTHERS", other1.getName(), Util.getPlayerState(other1.getUniqueId()));
								if (plugin.getConfig().getBoolean("SETTINGS.NAMETAG"))
									Util.ChangeNametag(other1.getPlayer(), "reset");
							}
						}

					}

				}
				return true;

		}

		if(sender instanceof ConsoleCommandSender console) { //check if command sender is console
			if(args.length == 0) {
				Chat.send(console, "HELP_HEADER");
				Chat.send(console, "HELP_SET_OTHERS");
			} else {
				Player other = Bukkit.getPlayerExact(args[0]);
				if(other == null) { //make sure the player is online
					Chat.send(console, "NO_PLAYER", args[1]);
				} else { //set pvp state
					if(args[0].equals("reload")) {
						reloadConfig();
						return true;
					} else if(args[0].equals("toggle")) {
						Boolean current = plugin.players.get(other.getUniqueId());
						if(current) {
							if (Util.setPlayerState(other, false, console)) {
								Chat.send(other, "PVP_STATE_ENABLED");
								if (plugin.getConfig().getBoolean("SETTINGS.PARTICLES")) {
									Util.particleEffect(other.getPlayer());
								}
								if(plugin.getConfig().getBoolean("SETTINGS.NAMETAG")) {
									Util.ChangeNametag(other.getPlayer(), color);
								}
							}
						} else {
							if (Util.setPlayerState(other, true, console)) {
								Chat.send(other, "PVP_STATE_DISABLED");
							}
						}
					} else if(args[0].equalsIgnoreCase("on")) {
						if (Util.setPlayerState(other, false, console)) {
							Boolean current = plugin.players.get(other.getUniqueId());
							Chat.send(other, "PVP_STATE_ENABLED");
							if (current) {
								if(plugin.getConfig().getBoolean("SETTINGS.PARTICLES")) {
									Util.particleEffect(other.getPlayer());
								}
								if(plugin.getConfig().getBoolean("SETTINGS.NAMETAG")) {
									Util.ChangeNametag(other.getPlayer(), color);
								}
							}
						}
					} else if(args[0].equalsIgnoreCase("off")) {
						if (Util.setPlayerState(other, true, console)) {
							Chat.send(other, "PVP_STATE_DISABLED");
							if(plugin.getConfig().getBoolean("SETTINGS.NAMETAG")) {
								Util.ChangeNametag(other.getPlayer(), "reset");
							}
						}
					}

					Boolean current = plugin.players.get(other.getUniqueId());
					Chat.send(console, "PVP_STATE_CHANGED_OTHERS", other.getName(), current);
				}

			}
		}
		return true;
	}

	private void getHelp(Player p) {
		Chat.send(p, "PVP_STATUS", null, plugin.players.get(p.getUniqueId()));
		Chat.send(p, "HELP_HEADER");
		Chat.send(p, "HELP_GENERAL_USEAGE");
		if (p.hasPermission("pvptoggle.others"))
			Chat.send(p, "HELP_VIEW_OTHERS");
		if (p.hasPermission("pvptoggle.others.set"))
			Chat.send(p, "HELP_SET_OTHERS");
	}

    public void reloadConfig() {
    	plugin.reloadConfig();
    }


	
}
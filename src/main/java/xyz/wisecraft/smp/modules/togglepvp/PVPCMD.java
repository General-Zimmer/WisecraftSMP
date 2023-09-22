package xyz.wisecraft.smp.modules.togglepvp;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilChat;
import xyz.wisecraft.smp.modules.togglepvp.utils.PVPCMDUtil;
import xyz.wisecraft.smp.modules.togglepvp.utils.UtilPlayers;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Command class for the /pvp command.
 */
public class PVPCMD extends BukkitCommand {

	private final WisecraftSMP instance = WisecraftSMP.getInstance();
	private final FileConfiguration config = instance.getConfig();
	private final HashMap<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();

	protected PVPCMD() {
		super("pvp");
		setDescription("used to change PvP state.");
		setUsage("/pvp");
		setLabel(this.getName());
		setPermission("pvptoggle.allow");
		setPermissionMessage("You don't have the pvptoggle.allow permission node.");
	}

	@Override
	public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
		List<String> words = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("pvptoggle.allow")) {
                words.add("help");
            }
            if (sender.hasPermission("pvptoggle.reload")) {
                words.add("reload");
            }
            if (sender.hasPermission("pvptoggle.others")) {
                words.add("status");
            }
        }
		return words;
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {


		if (!commandLabel.equalsIgnoreCase(this.getName()))
			return true;
		String color = "&c";
		if (sender instanceof ConsoleCommandSender console)
			PVPCMDUtil.consoleCase(console, args, color);


		//check if command sender is player
		if (!(sender instanceof Player p)) return true;

		if (!p.hasPermission("pvptoggle.allow")) return true;


		if (args.length == 0) {
			if (UtilPlayers.hasCooldown(p)) return true;

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
					UtilChat.send(p, "COMMAND_NO_PERMISSION");
					return true;
				}
				// Check if command target exists
				Player other = Bukkit.getPlayerExact(args[0]);
				if (other == null) {
					UtilChat.send(p, "NO_PLAYER", args[0]);
					return true;
				}
				// Switch PVP State on target
				Boolean otherPlayerPVPState = pvpPlayers.get(other.getUniqueId());
				if (otherPlayerPVPState) {
					// Turn off
					if (UtilPlayers.setPlayerState(other, false, sender)) {
						UtilChat.send(other, "PVP_STATE_ENABLED");
						if (other != p)
							UtilChat.send(p, "PVP_STATE_CHANGED_OTHERS", other.getName(), UtilPlayers.getPlayerState(other.getUniqueId()));

						if (config.getBoolean("SETTINGS.PARTICLES"))
							UtilPlayers.particleEffect(other.getPlayer());
						if (config.getBoolean("SETTINGS.NAMETAG"))
							UtilPlayers.ChangeNametag(other.getPlayer(), color);
					}
				} else {
					// Turn on
					if (UtilPlayers.setPlayerState(other, true, sender)) {
						UtilChat.send(other, "PVP_STATE_DISABLED");
						UtilChat.send(p, "PVP_STATE_CHANGED_OTHERS", other.getName(), UtilPlayers.getPlayerState(other.getUniqueId()));
						if (config.getBoolean("SETTINGS.NAMETAG"))
							UtilPlayers.ChangeNametag(other.getPlayer(), "reset");
					}
				}
			}
		}
		return true;
	}
}
package xyz.wisecraft.smp.modules.togglepvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.HashMap;
import java.util.UUID;

/**
 * Utility class for command related methods.
 */
public abstract class PVPCMDUtil {
    private static final WisecraftSMP plugin = WisecraftSMP.getInstance();
    private static final FileConfiguration config = plugin.getConfig();
    private static final StorageHelperMaps<HashMap<UUID, Boolean>, UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();


    /**
     * I don't know exactly what this does. Your guess is as good as mine.
     * @param sender The sender
     * @param p      The player
     * @param args   The arguments
     */
    public static void status(CommandSender sender, Player p, String[] args) {
        if (!sender.hasPermission("pvptoggle.others")) {
            return;
        }
        if (args.length < 2) {
            UtilChat.send(p, "HELP_VIEW_OTHERS");
            return;
        }
        Player other = Bukkit.getPlayerExact(args[1]);
        if (other == null) {
            UtilChat.send(p, "NO_PLAYER", args[1]);
        } else {
            Boolean current = pvpPlayers.get(other.getUniqueId());
            UtilChat.send(p, "PVP_STATUS_OTHERS", other.getName(), current);
        }
    }

    /**
     * @param p     The player
     * @param color The color
     */
    public static void zeroArg(Player p, String color) {
        Boolean current = pvpPlayers.get(p.getUniqueId());
        if (current) {
            UtilPlayers.setCooldownTime(p);
            if (UtilPlayers.setPlayerState(p, false, p)) {
                UtilChat.send(p, "PVP_STATE_ENABLED");
                // Particles
                if (config.getBoolean("SETTINGS.PARTICLES"))
                    UtilPlayers.particleEffect(p.getPlayer());
                // Nametag
                if (config.getBoolean("SETTINGS.NAMETAG"))
                    UtilPlayers.ChangeNametag(p.getPlayer(), color);
            }
        } else {
            if (UtilPlayers.setPlayerState(p, true, p)) {
                UtilChat.send(p, "PVP_STATE_DISABLED");
                if (config.getBoolean("SETTINGS.NAMETAG"))
                    UtilPlayers.ChangeNametag(p.getPlayer(), "reset");
            }
        }
    }

    /**
     * The help case
     * @param p The player
     */
    public static void getHelp(Player p) {
        UtilChat.send(p, "PVP_STATUS", null, pvpPlayers.get(p.getUniqueId()));
        UtilChat.send(p, "HELP_HEADER");
        UtilChat.send(p, "HELP_GENERAL_USEAGE");
        if (p.hasPermission("pvptoggle.others"))
            UtilChat.send(p, "HELP_VIEW_OTHERS");
        if (p.hasPermission("pvptoggle.others.set"))
            UtilChat.send(p, "HELP_SET_OTHERS");
    }

    /**
     * The reload case
     * @param p The player
     */
    public static void reloadCase(Player p) {
        if (p.hasPermission("pvptoggle.reload")) {
            plugin.reloadConfig();
            UtilChat.send(p, "RELOAD");
        }
    }

    /**
     * The console case
     * @param console The console
     * @param args    The arguments of the command
     * @param color   The color of the nametag
     */
    // This is a fucking mess, too bad!
    public static void consoleCase(ConsoleCommandSender console, String[] args, String color) {
        if (args.length == 0) {
            UtilChat.send(console, "HELP_HEADER");
            UtilChat.send(console, "HELP_SET_OTHERS");
        } else {
            Player other = Bukkit.getPlayerExact(args[0]);
            if (other == null) { //make sure the player is online
                UtilChat.send(console, "NO_PLAYER", args[1]);
            } else { //set pvp state
                if (args[0].equals("reload")) {
                    plugin.reloadConfig();
                    return;
                } else if (args[0].equals("toggle")) {
                    Boolean current = pvpPlayers.get(other.getUniqueId());
                    if (current) {
                        if (UtilPlayers.setPlayerState(other, false, console)) {
                            UtilChat.send(other, "PVP_STATE_ENABLED");
                            if (config.getBoolean("SETTINGS.PARTICLES")) {
                                UtilPlayers.particleEffect(other.getPlayer());
                            }
                            if (config.getBoolean("SETTINGS.NAMETAG")) {
                                UtilPlayers.ChangeNametag(other.getPlayer(), color);
                            }
                        }
                    } else {
                        if (UtilPlayers.setPlayerState(other, true, console)) {
                            UtilChat.send(other, "PVP_STATE_DISABLED");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("on")) {
                    if (UtilPlayers.setPlayerState(other, false, console)) {
                        Boolean current = pvpPlayers.get(other.getUniqueId());
                        UtilChat.send(other, "PVP_STATE_ENABLED");
                        if (current) {
                            if (config.getBoolean("SETTINGS.PARTICLES")) {
                                UtilPlayers.particleEffect(other.getPlayer());
                            }
                            if (config.getBoolean("SETTINGS.NAMETAG")) {
                                UtilPlayers.ChangeNametag(other.getPlayer(), color);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("off")) {
                    if (UtilPlayers.setPlayerState(other, true, console)) {
                        UtilChat.send(other, "PVP_STATE_DISABLED");
                        if (config.getBoolean("SETTINGS.NAMETAG")) {
                            UtilPlayers.ChangeNametag(other.getPlayer(), "reset");
                        }
                    }
                }

                Boolean current = pvpPlayers.get(other.getUniqueId());
                UtilChat.send(console, "PVP_STATE_CHANGED_OTHERS", other.getName(), current);
            }

        }
    }
}

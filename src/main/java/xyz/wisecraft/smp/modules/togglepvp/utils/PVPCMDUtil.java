package xyz.wisecraft.smp.modules.togglepvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.HashMap;
import java.util.UUID;

public abstract class PVPCMDUtil {
    private static final WisecraftSMP plugin = WisecraftSMP.getInstance();
    private static final FileConfiguration config = plugin.getConfig();
    private static final HashMap<UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();


    public static void status(CommandSender sender, Player p, String[] args) {
        if (!sender.hasPermission("pvptoggle.others")) {
            return;
        }
        if (args.length < 2) {
            Chat.send(p, "HELP_VIEW_OTHERS");
            return;
        }
        Player other = Bukkit.getPlayerExact(args[1]);
        if (other == null) {
            Chat.send(p, "NO_PLAYER", args[1]);
        } else {
            Boolean current = pvpPlayers.get(other.getUniqueId());
            Chat.send(p, "PVP_STATUS_OTHERS", other.getName(), current);
        }
    }

    public static void zeroArg(Player p, String color) {
        Boolean current = pvpPlayers.get(p.getUniqueId());
        if (current) {
            Util.setCooldownTime(p);
            if (Util.setPlayerState(p, false, p)) {
                Chat.send(p, "PVP_STATE_ENABLED");
                // Particles
                if (config.getBoolean("SETTINGS.PARTICLES"))
                    Util.particleEffect(p.getPlayer());
                // Nametag
                if (config.getBoolean("SETTINGS.NAMETAG"))
                    Util.ChangeNametag(p.getPlayer(), color);
            }
        } else {
            if (Util.setPlayerState(p, true, p)) {
                Chat.send(p, "PVP_STATE_DISABLED");
                if (config.getBoolean("SETTINGS.NAMETAG"))
                    Util.ChangeNametag(p.getPlayer(), "reset");
            }
        }
    }

    public static void getHelp(Player p) {
        Chat.send(p, "PVP_STATUS", null, pvpPlayers.get(p.getUniqueId()));
        Chat.send(p, "HELP_HEADER");
        Chat.send(p, "HELP_GENERAL_USEAGE");
        if (p.hasPermission("pvptoggle.others"))
            Chat.send(p, "HELP_VIEW_OTHERS");
        if (p.hasPermission("pvptoggle.others.set"))
            Chat.send(p, "HELP_SET_OTHERS");
    }

    public static void reloadCase(Player p) {
        if (p.hasPermission("pvptoggle.reload")) {
            plugin.reloadConfig();
            Chat.send(p, "RELOAD");
        }
    }

    public static void consoleCase(ConsoleCommandSender console, String[] args, String color) {
        if (args.length == 0) {
            Chat.send(console, "HELP_HEADER");
            Chat.send(console, "HELP_SET_OTHERS");
        } else {
            Player other = Bukkit.getPlayerExact(args[0]);
            if (other == null) { //make sure the player is online
                Chat.send(console, "NO_PLAYER", args[1]);
            } else { //set pvp state
                if (args[0].equals("reload")) {
                    plugin.reloadConfig();
                    return;
                } else if (args[0].equals("toggle")) {
                    Boolean current = pvpPlayers.get(other.getUniqueId());
                    if (current) {
                        if (Util.setPlayerState(other, false, console)) {
                            Chat.send(other, "PVP_STATE_ENABLED");
                            if (config.getBoolean("SETTINGS.PARTICLES")) {
                                Util.particleEffect(other.getPlayer());
                            }
                            if (config.getBoolean("SETTINGS.NAMETAG")) {
                                Util.ChangeNametag(other.getPlayer(), color);
                            }
                        }
                    } else {
                        if (Util.setPlayerState(other, true, console)) {
                            Chat.send(other, "PVP_STATE_DISABLED");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("on")) {
                    if (Util.setPlayerState(other, false, console)) {
                        Boolean current = pvpPlayers.get(other.getUniqueId());
                        Chat.send(other, "PVP_STATE_ENABLED");
                        if (current) {
                            if (config.getBoolean("SETTINGS.PARTICLES")) {
                                Util.particleEffect(other.getPlayer());
                            }
                            if (config.getBoolean("SETTINGS.NAMETAG")) {
                                Util.ChangeNametag(other.getPlayer(), color);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("off")) {
                    if (Util.setPlayerState(other, true, console)) {
                        Chat.send(other, "PVP_STATE_DISABLED");
                        if (config.getBoolean("SETTINGS.NAMETAG")) {
                            Util.ChangeNametag(other.getPlayer(), "reset");
                        }
                    }
                }

                Boolean current = pvpPlayers.get(other.getUniqueId());
                Chat.send(console, "PVP_STATE_CHANGED_OTHERS", other.getName(), current);
            }

        }
    }
}

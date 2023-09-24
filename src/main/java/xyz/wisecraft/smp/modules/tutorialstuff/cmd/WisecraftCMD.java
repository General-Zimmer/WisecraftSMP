package xyz.wisecraft.smp.modules.tutorialstuff.cmd;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.tutorialstuff.util.UtilRandom;
import xyz.wisecraft.smp.util.UtilCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * WisecraftCMD
 */
public class WisecraftCMD extends BukkitCommand {

    private final WisecraftCoreApi core;
    private final IEssentials ess;
    private final boolean isMultiverseEnabled;

    /**
     * Constructor
     */
    public WisecraftCMD(String name, WisecraftCoreApi core, IEssentials ess, boolean isMultiverseEnabled) {
        super(name);
        setDescription("used to change PvP state.");
        setUsage("/wisecraft <parameter>");
        setLabel(this.getName());
        setPermission("wisecraft.manage");
        setPermissionMessage("bad boi!");
        setAliases(List.of("wshop"));

        this.core = core;
        this.ess = ess;
        this.isMultiverseEnabled = isMultiverseEnabled;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> words = new ArrayList<>();
        if (args.length == 1) {
            words.add("shop");
            words.add("tutorial");
            if (sender.hasPermission("wisecraft.manage")) {
                words.add("save");
                words.add("load");
            }
        }


        return words;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if ( !(sender instanceof Player)) return true;
        Player p = ((Player) sender).getPlayer();
        if (p == null) return true;

        if (commandLabel.equals("wshop")) {
            UtilRandom.tpworld(ess, Bukkit.getWorld("shop"), p, isMultiverseEnabled);
            return true;
        }

        if (commandLabel.equals(this.getName())) {

            if (args.length == 0) {
                p.sendMessage(ChatColor.YELLOW + "You can choose to teleport to either shop or tutorial");
                return true;
            }

            switch (args[0]) {
                case "shop", "tutorial" -> {
                    UtilRandom.tpworld(ess, Bukkit.getWorld(args[0]), p, isMultiverseEnabled);
                    return true;
                }
                case "save" -> {
                    if (core == null) return true;

                    if (p.hasPermission("wisecraft.manage"))
                        core.savePlayerdata();
                }
                case "load" -> {
                    if (core == null) return true;

                    if (p.hasPermission("wisecraft.manage"))
                        core.loadPlayerdata();
                }
                case "time" -> {
                    if (core == null) return true;

                    p.sendMessage(Double.toString(UtilCommon.calcCurrentSeconds(
                            core.getTimers().get(
                                    p.getUniqueId()).getTree())));
                }

            }
        }
        return true;
    }
}

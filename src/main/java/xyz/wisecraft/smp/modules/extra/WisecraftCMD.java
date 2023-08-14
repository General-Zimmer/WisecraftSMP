package xyz.wisecraft.smp.modules.extra;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;
import xyz.wisecraft.smp.modules.extra.util.UtilRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * WisecraftCMD
 */
public class WisecraftCMD implements TabExecutor {

    private final WisecraftCoreApi core;
    private final IEssentials ess;

    /**
     * Constructor
     */
    public WisecraftCMD(WisecraftCoreApi core, IEssentials ess) {
        this.core = core;
        this.ess = ess;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if ( !(sender instanceof Player)) return true;
        Player p = ((Player) sender).getPlayer();
        if (p == null) return true;

        if (cmd.getName().equals("wshop")) {
            UtilRandom.tpworld(ess, Bukkit.getWorld("shop"), p);
            return true;
        }

        if (cmd.getName().equals("wisecraft")) {

            if (args.length == 0) {
                p.sendMessage(ChatColor.YELLOW + "You can choose to teleport to either shop or tutorial");
                return true;
            }

            switch (args[0]) {
                case "shop", "tutorial" -> {
                    UtilRandom.tpworld(ess, Bukkit.getWorld(args[0]), p);
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

                    p.sendMessage(Double.toString(UtilAdv.calcCurrentSeconds(
                            core.getTimers().get(
                                    p.getUniqueId()).getTree())));
                }

            }
        }
        return true;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> words = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                words.add("shop");
                words.add("tutorial");
                if (sender.hasPermission("wisecraft.manage")) {
                    words.add("save");
                    words.add("load");
                }
                return words;
            }


        }


        return null;
    }


}

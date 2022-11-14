package xyz.wisecraft.smp.cmds;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.wisecraftcore.WisecraftCoreApi;
import xyz.wisecraft.smp.Methods;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.ArrayList;
import java.util.List;

public record wisecraft(IEssentials ess, WisecraftCoreApi core, WisecraftSMP plugin) implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) return true;

        if (cmd.getName().equals("wshop")) {
            Methods.tpworld(ess, Bukkit.getWorld("shop"), sender);
            return true;
        }

        if (cmd.getName().equals("wisecraft")) {

            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + "You can choose to teleport to either shop or tutorial");
                return true;
            }

            switch (args[0]) {
                case "shop", "tutorial" -> {
                    Methods.tpworld(ess, Bukkit.getWorld(args[0]), sender);
                    return true;
                }
                case "save" -> {
                    if (core == null) return true;

                    if (sender.hasPermission("wisecraft.manage"))
                        core.savePlayerdata();
                }
                case "load" -> {
                    if (core == null) return true;

                    if (sender.hasPermission("wisecraft.manage"))
                        core.loadPlayerdata();
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


        return words;
    }


}

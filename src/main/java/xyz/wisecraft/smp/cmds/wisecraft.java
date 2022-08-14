package xyz.wisecraft.smp.cmds;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import xyz.wisecraft.core.wisecraftcore.WisecraftCoreApi;
import xyz.wisecraft.smp.util.tpback;

import java.util.ArrayList;
import java.util.List;

public class wisecraft implements TabExecutor {

    private final WisecraftCoreApi core;
    IEssentials ess;

    public wisecraft(IEssentials ess, WisecraftCoreApi core) {
        this.ess = ess;
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) return true;

        if (cmd.getName().equals("wshop")) {
            if (Bukkit.getWorld("shop") == null) {
                sender.sendMessage(ChatColor.RED + "World does not exist");
                return true;
            }
            tpback.setBack(ess, Bukkit.getPlayerExact(sender.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + "shop");
            sender.sendMessage(ChatColor.GOLD + "Teleported to shop world");
            return true;
        }

        if (cmd.getName().equals("wisecraft")) {

            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + "You can choose to teleport to either shop or tutorial");
                return true;
            }

            switch (args[0]) {
                case "shop" -> {
                    if (Bukkit.getWorld("shop") == null) {
                        sender.sendMessage(ChatColor.RED + "World does not exist");
                        return true;
                    }
                    tpback.setBack(ess, Bukkit.getPlayerExact(sender.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + "shop");
                    sender.sendMessage(ChatColor.GOLD + "Teleported to shop world");
                    return true;
                }
                case "tutorial" -> {
                    if (Bukkit.getWorld("tutorial") == null) {
                        sender.sendMessage(ChatColor.RED + "World does not exist");
                        return true;
                    }
                    tpback.setBack(ess, Bukkit.getPlayerExact(sender.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + "tutorial");
                    sender.sendMessage(ChatColor.GOLD + "Teleported to tutorial world");
                    return true;
                }
                case "save" -> {
                    if (core == null) {return true;}

                    if (sender.hasPermission("wisecraft.manage"))
                    core.savePlayerdata();
                }
                case "load" -> {
                    if (core == null) return true;

                    if (sender.hasPermission("wisecraft.manage"))
                        core.loadPlayerdata();
                }
            }
        } else {
            sender.sendMessage("You are not a player, I think?");
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> words = new ArrayList<>();
        words.add("shop");
        words.add("tutorial");
        if (sender.hasPermission("wisecraft.manage")) {
            words.add("save");
            words.add("load");
        }


        return words;
    }



}

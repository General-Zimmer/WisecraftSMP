package xyz.wisecraft.smp.modules.heirloom.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

public class CmdSetup extends BukkitCommand {


    public CmdSetup(@NotNull String name) {
        super(name);
        setDescription("It does something veri useful");
        setLabel("idk wat dis is");
        setPermission("heirloom.admin");
        setPermissionMessage("No, bad person!");
        setUsage("You test ze code");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        // Dan, do your thing here
        return true;
    }
}
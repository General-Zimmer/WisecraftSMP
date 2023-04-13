package xyz.wisecraft.smp.advancements;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.threads.gibRoles;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, Tabcompletion {

    private final WisecraftSMP plugin;

    public Command() {
    this.plugin = WisecraftSMP.instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String label, @NotNull String[] args) {


        if (sender.hasPermission("Wisecraftadv.manage") && cmd.getName().equalsIgnoreCase("autoroles")) {
            switch (args[0]) {
                case "save" -> sender.sendMessage("Saving private Ryan");
                case "load" -> sender.sendMessage("WOOO you tried to load");
                case "rank" -> {
                    new gibRoles().runTask(plugin);
                    sender.sendMessage("Auto citizen triggered");
                }
            }
            return true;

        }
        else {return false;}
    }
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args) {
        List<String> cmds = new ArrayList<>();
        
        switch (args.length) {
            case 1 -> {
                if (sender.hasPermission("Wisecraftadv.manage")) {
                    cmds.add("save");
                    cmds.add("load");
                    cmds.add("rank");
                }
                return cmds;
            }

        }


        return cmds;
    }

}
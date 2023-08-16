package xyz.wisecraft.smp.modules.advancements;

import net.luckperms.api.LuckPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.threads.GibRoles;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for managing the auto roles.
 */
public class Command implements TabExecutor {

    private final WisecraftSMP plugin = WisecraftSMP.getInstance();

    private final WisecraftCoreApi core;
    private final LuckPerms luck;

    public Command(WisecraftCoreApi core, LuckPerms luck) {
        this.core = core;
        this.luck = luck;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             @NotNull org.bukkit.command.Command cmd,
                             @NotNull String label,
                             @NotNull String[] args) {


        if (sender.hasPermission("Wisecraftadv.manage") && cmd.getName().equalsIgnoreCase("autoroles")) {
            switch (args[0]) {
                case "save" -> sender.sendMessage("Saving private Ryan");
                case "load" -> sender.sendMessage("WOOO you tried to load");
                case "rank" -> {
                    new GibRoles(core, luck).runTask(plugin);
                    sender.sendMessage("Auto citizen triggered");
                }
            }
            return true;

        }
        else {return false;}
    }
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      org.bukkit.command.@NotNull Command cmd,
                                      @NotNull String alias,
                                      String[] args) {
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
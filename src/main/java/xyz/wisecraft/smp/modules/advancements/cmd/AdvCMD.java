package xyz.wisecraft.smp.modules.advancements.cmd;

import net.luckperms.api.LuckPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.threads.GibRoles;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for managing the auto roles.
 */
public class AdvCMD extends BukkitCommand {

    private final WisecraftSMP plugin = WisecraftSMP.getInstance();

    private final WisecraftCoreApi core;
    private final LuckPerms luck;

    public AdvCMD(WisecraftCoreApi core, LuckPerms luck) {
        super("autoroles");
        setDescription("Used to manually save or load data");
        setUsage("/autoroles <save | load>");
        setLabel(this.getName());
        setPermission("Wisecraftadv.manage");
        setPermissionMessage("No, bad person!");


        this.core = core;
        this.luck = luck;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender.hasPermission("Wisecraftadv.manage") && commandLabel.equalsIgnoreCase(this.getName())) {
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

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("Wisecraftadv.manage")) {
                cmds.add("save");
                cmds.add("load");
                cmds.add("rank");
            }
            return cmds;
        }


        return cmds;
    }
}
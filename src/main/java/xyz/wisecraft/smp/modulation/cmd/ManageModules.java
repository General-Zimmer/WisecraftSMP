package xyz.wisecraft.smp.modulation.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ManageModules extends BukkitCommand {
    public ManageModules(@NotNull String name) {
        super(name);
        setDescription("It does something veri useful");
        setLabel("WEEEEEEEEEEEEEEEE");
        setPermission("wisecraft.admin");
        setPermissionMessage("No, bad person!");
        setUsage("You test ze code");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (!commandLabel.equalsIgnoreCase(this.getName())) return false;

        if (args.length == 2) {
            Set<Module> modules = ModulationStorage.getModules().keySet();

            modules.removeIf(module -> !module.getModuleName().equalsIgnoreCase(args[1]));

            if (modules.isEmpty()) return false;

            if (args[0].equalsIgnoreCase("enable")) {
                modules.forEach(module -> {
                    module.reenableModule();
                    sender.sendMessage("Module " + module.getModuleName() + " was enabled.");
                });
            } else if (args[0].equalsIgnoreCase("disable")) {
                modules.forEach(module -> {
                    module.disableModule();
                    sender.sendMessage("Module " + module.getModuleName() + " was disabled.");
                });
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        Set<Module> modules = ModulationStorage.getModules().keySet();
        List<String> names = new ArrayList<>();

        if (args.length == 2) {
            for (Module module : modules) {
                names.add(module.getModuleName());
            }
        }
        if (args.length == 1) {
            names.add("disable");
            names.add("enable");

        }

        return names;
    }
}

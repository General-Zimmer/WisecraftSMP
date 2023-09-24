package xyz.wisecraft.smp.modulation.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.enums.ModuleState;
import xyz.wisecraft.smp.modulation.models.ModuleClass;
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
            Set<ModuleClass> modules = ModulationStorage.getModules();

            modules.removeIf(module -> !module.getModuleName().equalsIgnoreCase(args[1]));

            if (modules.isEmpty()) {
                sender.sendMessage("Module " + args[1] + " does not exist.");
                return true;
            }

            switch (args[0]) {
                case ("enable") -> modules.forEach(module -> {

                    if (module.getModuleState() != ModuleState.ENABLED) {
                        module.reenableModule();
                        sender.sendMessage("Module " + module.getModuleName() + " was enabled.");
                    } else {
                        sender.sendMessage("Module " + module.getModuleName() + " was already enabled.");
                    }
                });
                case "disable" -> modules.forEach(module -> {
                    if (module.getModuleState() != ModuleState.DISABLED) {
                        module.disableModule();
                        sender.sendMessage("Module " + module.getModuleName() + " was disabled.");
                    } else {
                        sender.sendMessage("Module " + module.getModuleName() + " was already disabled.");
                    }
                });
                case "reload" -> modules.forEach(module -> {
                    module.reloadModule();
                    sender.sendMessage("Module " + module.getModuleName() + " was reloaded.");
                });
                default -> sender.sendMessage("Invalid argument.");
            }

        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        Set<ModuleClass> modules = ModulationStorage.getModules();
        List<String> names = new ArrayList<>();

        if (args.length == 2) {
            for (Module module : modules) {
                names.add(module.getModuleName());
            }
        }
        if (args.length == 1) {
            names.add("disable");
            names.add("enable");
            names.add("reload");
        }

        return names;
    }
}

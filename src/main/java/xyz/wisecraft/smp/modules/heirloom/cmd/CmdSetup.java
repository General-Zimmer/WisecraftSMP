package xyz.wisecraft.smp.modules.heirloom.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

import java.util.List;

public class CmdSetup extends BukkitCommand implements TabExecutor {


    public CmdSetup(@NotNull String name) {
        super(name);
        setDescription("It does something veri useful");
        setLabel("heirloombow");
        setPermission("heirloom.admin");
        setPermissionMessage("No, bad person!");
        setUsage("You test ze code");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        sender.sendMessage("You tested ze code!");
        if (commandLabel.equalsIgnoreCase("heirloombow")) {
            if(sender instanceof Player p) {
                ItemStack item = p.getInventory().getItemInMainHand();
                BaseHeirloom.createHeirLoom(item, HeirloomType.BOWHEIRLOOM);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("heirloombow")) {
            if(sender instanceof Player p) {
                ItemStack item = p.getInventory().getItemInMainHand();
                BaseHeirloom.createHeirLoom(item, HeirloomType.BOWHEIRLOOM);
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
package xyz.wisecraft.smp.modules.heirloom.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

public class CmdSetup extends BukkitCommand {


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
        if (commandLabel.equalsIgnoreCase("heirloombow")) {
            if(sender instanceof Player p) {
                ItemStack item = p.getInventory().getItemInMainHand();
                BaseHeirloom.createHeirLoom(item, HeirloomType.BOWHEIRLOOM);
            }
        }
        return true;
    }
}
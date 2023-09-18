package xyz.wisecraft.smp.modules.heirloom.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

import java.util.ArrayList;
import java.util.List;

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
        sender.sendMessage("You tested ze code!");
        if (commandLabel.equalsIgnoreCase(this.getName())) {
            if(sender instanceof Player p) {
                ItemStack item = p.getInventory().getItemInMainHand();
                BaseHeirloom.createHeirLoom(item, HeirloomType.BOWHEIRLOOM);
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        ArrayList<String> list = new ArrayList<>();
        list.add("heirloombow");
        list.add("heirloomhoe");
        list.add("heirloomshovel");
        list.add("heirloomaxe");
        list.add("heirloompickaxe");
        list.add("heirloomhelmet");
        list.add("heirloomchestplate");
        list.add("heirloomleggings");
        list.add("heirloomboots");
        list.add("heirloomshield");
        list.add("heirloomtrident");
        list.add("heirloomcrossbow");
        list.add("heirloomfishingrod");
        list.add("heirloomelytra");
        list.add("heirloomsword");
        return list;

    }
}
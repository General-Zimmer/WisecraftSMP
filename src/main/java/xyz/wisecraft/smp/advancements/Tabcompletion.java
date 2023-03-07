package xyz.wisecraft.smp.advancements;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Tabcompletion {
    List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd,
                               String alias, String[] args);
}

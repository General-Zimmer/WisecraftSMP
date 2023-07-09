package xyz.wisecraft.smp.features.advancements;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Tabcompletion {

    @SuppressWarnings("unused")
    List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd,
                               String alias, String[] args);
}

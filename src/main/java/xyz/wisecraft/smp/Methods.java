package xyz.wisecraft.smp;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Methods {

    public static void noFinishTut(PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED + "Finish the tutorial first!");
        e.getPlayer().sendMessage(ChatColor.GOLD + "You can always return with /wisecraft tutorial");
    }


    public static void tpworld(IEssentials ess, World world, CommandSender sender) {


        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World does not exist");
            return;
        }

        setBack(ess, Bukkit.getPlayerExact(sender.getName()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + world.getName());
        sender.sendMessage(ChatColor.GOLD + "Teleporting to " + world.getName() + " in 1 second(s)");
    }

    public static void setBack(IEssentials ess, Player p) {
        if (ess != null)
            ess.getUser(p).setLastLocation();
        else
            Bukkit.getConsoleSender().sendMessage("Ess is null - wc SMP");
    }
}

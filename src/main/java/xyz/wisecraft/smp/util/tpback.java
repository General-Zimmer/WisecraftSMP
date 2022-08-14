package xyz.wisecraft.smp.util;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class tpback {


    public static void setBack(IEssentials ess, Player p) {
        if (ess != null)
            ess.getUser(p).setLastLocation();
        else
            Bukkit.getConsoleSender().sendMessage("Ess is null - wc SMP");
    }
}

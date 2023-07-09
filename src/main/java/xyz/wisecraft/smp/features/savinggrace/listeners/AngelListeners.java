package xyz.wisecraft.smp.features.savinggrace.listeners;

import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.features.savinggrace.Angel;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AngelListeners implements Listener {


    private final IEssentials ess;
    private final HashMap<UUID, Angel> angels;

    public AngelListeners() {
        this.ess = WisecraftSMP.getEss();
        this.angels = OtherStorage.getAngels();

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!angels.containsKey(p.getUniqueId())) {
            Angel angel = new Angel(p.hasPermission("wisecraft.donator"));
            angels.put(p.getUniqueId(), angel);
        }


    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();


        Angel angel = angels.get(UUID);

        // Prevent items from being destroyed after leaving
        if (angel.hasDied())
            angel.safeDelete(UUID);
        else if (!angel.hasDied())
            angels.remove(UUID);

    }



    //The exception comes from kit expand
    @EventHandler(priority = EventPriority.NORMAL)
    public void getItemsBack(PlayerRespawnEvent e) throws Exception {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        Angel angel = angels.get(UUID);

        //Does dis person have home?
        User user = ess.getUser(e.getPlayer());
        if (user.hasHome() || e.isBedSpawn() || e.isAnchorSpawn()) {
            angel.giveGrace(e);
        } else {
            angels.get(e.getPlayer().getUniqueId()).giveStarter(user, e.getPlayer());
        }


    }

    @EventHandler
    public void savingGrace(PlayerDeathEvent e) {
        Angel angel = this.angels.get(e.getEntity().getUniqueId());

        if (angel.getGraces() <= 0) {return;}

        List<ItemStack> drops = e.getDrops();
        PlayerInventory inv = e.getEntity().getInventory();

        angel.saveGear(drops, inv);

    }



}

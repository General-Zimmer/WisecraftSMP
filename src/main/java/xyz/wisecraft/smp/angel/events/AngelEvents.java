package xyz.wisecraft.smp.angel.events;

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
import xyz.wisecraft.smp.angel.Angel;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AngelEvents implements Listener {


    private final IEssentials ess;
    private final HashMap<UUID, Angel> gearMap;
    private final WisecraftSMP plugin;

    public AngelEvents() {
        this.plugin = WisecraftSMP.instance;
        this.ess = WisecraftSMP.ess;
        this.gearMap = plugin.getGearmap();

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getGearmap().containsKey(p.getUniqueId()))
            gearMap.put(p.getUniqueId(), new Angel(p.hasPermission("wisecraft.donator")));

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();


        Angel angel = plugin.getGearmap().get(UUID);

        // Prevent items from being destroyed after leaving
        if (angel.hasDied())
            angel.safeDelete(plugin, UUID);
        else if (!angel.safeDelete(plugin, UUID))
            plugin.getGearmap().remove(UUID);

    }



    //The exception comes from kit expand
    @EventHandler(priority = EventPriority.NORMAL)
    public void getItemsBack(PlayerRespawnEvent e) throws Exception {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        Angel angel = plugin.getGearmap().get(UUID);

        //Does dis person have home?
        User user = ess.getUser(e.getPlayer());
        if (user.hasHome() || e.isBedSpawn() || e.isAnchorSpawn()) {
            angel.giveGrace(e);
        } else {
            gearMap.get(e.getPlayer().getUniqueId()).giveStarter(plugin, ess, user, e.getPlayer(), gearMap);
        }


    }

    @EventHandler
    public void savingGrace(PlayerDeathEvent e) {
        Angel angel = this.gearMap.get(e.getEntity().getUniqueId());

        if (angel.getGraces() <= 0) {return;}

        List<ItemStack> drops = e.getDrops();
        PlayerInventory inv = e.getEntity().getInventory();

        angel.saveGear(drops, inv);

    }



}

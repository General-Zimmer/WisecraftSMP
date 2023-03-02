package xyz.wisecraft.smp.events;

import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.Angel;
import xyz.wisecraft.smp.Methods;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Events implements Listener{


    private final IEssentials ess;
    private final HashMap<UUID, Angel> gearMap;
    private final WisecraftSMP plugin;


    public Events(WisecraftSMP plugin, IEssentials ess) {
        this.ess = ess;
        this.gearMap = plugin.getGearmap();
        this.plugin = plugin;
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onjoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getGearmap().containsKey(p.getUniqueId()))
            gearMap.put(p.getUniqueId(), new Angel(p.hasPermission("wisecraft.donator")));


        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.hasPlayedBefore() && Bukkit.getWorld("tutorial") != null)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + p.getName() + " tutorial");
            }
        }.runTaskLater(plugin, 4);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Angel angel = plugin.getGearmap().get(p.getUniqueId());

        if (!angel.isGraceActive())
            plugin.getGearmap().remove(p.getUniqueId());

    }



    //todo merge getting starter kit with saving Grace and make player not drop if they get kit
    //The exception comes from kit expand
    @EventHandler(priority = EventPriority.NORMAL)
    public void getItemsBack(PlayerRespawnEvent e) throws Exception {

        //todo Move this to Player deaths and prevent player drops
        //Does dis person have home?
        User user = ess.getUser(e.getPlayer());
        if (user.hasHome() || e.isBedSpawn() || e.isAnchorSpawn()) {
            Methods.giveGrace(plugin, e);

        } else {
            gearMap.get(e.getPlayer().getUniqueId()).getStarter(plugin, ess, user, e.getPlayer(), gearMap);
        }


    }

    @EventHandler
    public void finishTutorial(PlayerCommandPreprocessEvent e) {
        String world = e.getPlayer().getWorld().getName();
        String cmd = e.getMessage().toLowerCase().split(" ")[0];
        if (world.equals("tutorial"))
            switch (cmd) {
            //I love this, makes it real clean looking.
            case "/resourceworld", "/sethome", "/resource", "/wisecraft" -> Methods.noFinishTut(e);
        }
    }

    @EventHandler
    public void savingGrace(PlayerDeathEvent e) {
        Angel angel = this.gearMap.get(e.getEntity().getUniqueId());
        if (angel.getGraces() <= 0) {return;}

        List<ItemStack> drops = e.getDrops();
        PlayerInventory inv = e.getEntity().getInventory();

        angel.clear();
        angel.toolSave(drops, inv);
        angel.armorSave(drops, inv);

    }

}

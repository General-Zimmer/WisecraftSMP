package xyz.wisecraft.smp.events;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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



    //todo add a limitation to getting these items
    //The exception comes from kit expand
    @EventHandler(priority = EventPriority.NORMAL)
    public void getItemsBack(PlayerRespawnEvent e) throws Exception {

        //todo move getstarter items mechanic to angel for compatibility
        //Does dis person have home?
        boolean hasHome = false;
        User user = ess.getUser(e.getPlayer());
        if (user.hasHome() || e.isBedSpawn() || e.isAnchorSpawn())
            hasHome = true;
        //continue if no home
        if (hasHome) return;

        World tut = Bukkit.getWorld("tutorial");
        if (tut != null)
            //todo Need a tick delay otherwise they will teleport to spawn. Need to figure out why
            new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = new Location(tut, 5.5, 204, 8.5, -85, 2);
                e.getPlayer().teleport(loc);
            }
        }.runTaskLater(plugin, 1);


        Kit kit = new Kit("starter", ess);
        kit.expandItems(user);
        this.gearMap.get(e.getPlayer().getUniqueId()).clear();
        e.getPlayer().sendMessage(ChatColor.BLUE + "You didn't /sethome or place a bed! You have been granted some new items.");
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

    @EventHandler(priority = EventPriority.HIGH)
    public void getGrace(PlayerRespawnEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        Angel angel = this.gearMap.get(UUID);

        if (angel.getGraces() <= 0) {return;}


        List<ItemStack> tools = angel.getTools();
        for(int i = 0; i < tools.size(); i++) {
            inv.setItem(i, tools.get(i));
        }

        inv.setArmorContents(angel.getArmor());
        angel.clear();
        angel.decreaseGraces();
        p.sendMessage(ChatColor.AQUA + "Your gear have been saved. You have " + angel.getGraces() + " graces left!");

        if (!angel.isGraceActive()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player p2 = Bukkit.getPlayer(UUID);
                        if (p2 == null) {
                            plugin.getGearmap().remove(UUID);
                            return;
                        }

                        Angel angel2 = plugin.getGearmap().get(UUID);

                        angel2.resetGrace(p2.hasPermission("wisecraft.donator"));
                        angel2.setGraceActive(false);
                        p2.sendMessage(ChatColor.AQUA + "Your graces has reset");
                    }

            }.runTaskLater(plugin, 20*60*60); //1 hour
            angel.setGraceActive(true);
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
        angel.armorsave(drops, inv);

    }

}

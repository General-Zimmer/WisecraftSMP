package xyz.wisecraft.smp.modules.savinggrace.listeners;

import net.ess3.api.IEssentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.savinggrace.enums.PlayerState;
import xyz.wisecraft.smp.modules.savinggrace.models.Angel;
import xyz.wisecraft.smp.modules.savinggrace.storage.AngelStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class for the AngelListeners object.
 */
public class AngelListeners implements Listener {

    private final IEssentials ess;
    private final StorageHelperMaps<HashMap<UUID, Angel>, UUID, Angel> angels;
    /**
     * Constructor for the class.
     */
    public AngelListeners(IEssentials ess) {
        this.ess = ess;
        this.angels = AngelStorage.getAngels();
    }

    /**
     * Adds the player to the hashmap when they join.
     * @param e The PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!angels.containsKey(p.getUniqueId())) {
            Angel angel = new Angel(p.hasPermission("wisecraft.donator"));
            angels.put(p.getUniqueId(), angel);
        }
    }

    /**
     * Removes the player from the hashmap when they leave.
     * @param e The PlayerQuitEvent.
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();

        Angel angel = angels.get(UUID);

        if (angel.isGraceInactive())
            angels.remove(UUID);
    }

    /**
     * Gives the player their items back when they respawn.
     * @param e The PlayerRespawnEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void getItemsBack(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        Angel angel = angels.get(UUID);

        if (angel.getHasGraceRecently() == PlayerState.STARTER_KIT)
            angel.tpTutorial(p);

        angel.giveGraceMessage(p);
    }

    /**
     * Saves the player's gear when they die.
     * @param e The PlayerDeathEvent.
     */
    @EventHandler
    public void savingGrace(PlayerDeathEvent e) throws Exception {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        Angel angel = angels.get(UUID);
        boolean hasHome = true;

        if (ess != null)
            hasHome = ess.getUser(e.getPlayer()).hasHome();

        //Does dis person have home?
        if (hasHome) {
            angel.saveGear(e);
        } else {
            angel.giveStarter(ess, e);
        }

    }
}

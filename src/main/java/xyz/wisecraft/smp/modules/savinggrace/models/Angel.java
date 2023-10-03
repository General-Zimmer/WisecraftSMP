package xyz.wisecraft.smp.modules.savinggrace.models;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import lombok.Getter;
import net.ess3.api.IEssentials;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.savinggrace.storage.AngelStorage;
import xyz.wisecraft.smp.modules.tutorialstuff.util.UtilRandom;

import java.util.*;
import java.util.logging.Level;

import static xyz.wisecraft.smp.modules.tutorialstuff.util.UtilRandom.getToolTypes;

/**
 * Class for the Angel object.
 */
public class Angel {

    private boolean isGraceInactive = true;

    /**
     * -- GETTER --
     *  Gets graces
     *
     */
    @Getter
    private int graces;
    private boolean hasGraceRecently = false;
    private final WisecraftSMP plugin;

    /**
     * Get the amount of graces the player has left
     * @param hasDonator If the player has donator
     */
    public Angel(boolean hasDonator) {
        plugin = WisecraftSMP.getInstance();
        this.resetGrace(hasDonator);
    }

    /**
     * Give player their saved gear
     * @param e PlayerRespawnEvent
     */
    public void giveGraceMessage(Player p) {
        hasGraceRecently = false;

        p.sendMessage(ChatColor.AQUA + "Your gear have been saved. You have " + this.getGraces() + " graces left!");
    }


    /**
     * Give starter gear and teleport to tutorial
     * @param p Player
     * @throws Exception If the kit doesn't exist
     */
    public void giveStarter(IEssentials ess, Player p) throws Exception {
        World tut = Bukkit.getWorld("tutorial");
        if (tut != null)
            //todo Need a tick delay otherwise they will teleport to essentials spawn. Need to figure out why
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = new Location(tut, 5.5, 204, 8.5, -85, 2);
                    p.teleport(loc);
                }
            }.runTaskLater(plugin, 1);


        if (ess != null) {
            User user = ess.getUser(p);
            Kit kit = new Kit("starter", ess);
            kit.expandItems(user);
            p.sendMessage("You have been granted some new items.");
        }

        p.sendMessage(ChatColor.BLUE + "You didn't /sethome or place a bed!");
    }


    public void saveGear(PlayerDeathEvent e) {
        if (this.getGraces() <= 0) {return;}
        PlayerInventory inv = e.getPlayer().getInventory();

        HashMap<Integer, ItemStack> gear = getArmorToSave(inv);
        gear.putAll(getToolsToSave(inv));

        gear.forEach((slot, item) -> {
            if (item != null) {
                e.getDrops().remove(item);
                e.getItemsToKeep().add(item);
            }
        });

        graces--;
        this.safeDelete(e.getPlayer().getUniqueId());
    }

    private HashMap<Integer, ItemStack> getToolsToSave(PlayerInventory inv) {
        HashMap<Integer, ItemStack> tools = new HashMap<>(4);
        ArrayList<Material> toolTypes = getToolTypes();


        int currentAmount = 0;
        //Tool save
        for(int i = 0; i < 9; i++) {
            ItemStack item = inv.getItem(i);

            if (item != null && currentAmount < 4 && toolTypes.contains(item.getType())) {
                //add tool for recovery
                tools.put(i, item);
                //grace limit
                currentAmount++;
            }
        }
        return tools;
    }

    /**
     * Save armor the player was wearing
     * <p>
     * Note: Indexes 36 through 39 refer to the armor slots. 36 is boots, 37 is leggings, 38 is chestplate, and 39 is helmet
     * @param inv The inventory of the player
     */
    private HashMap<Integer, ItemStack> getArmorToSave(PlayerInventory inv) {
        HashMap<Integer, ItemStack> armor = new HashMap<>(4);
        ArrayList<Material> containers = UtilRandom.getContainerTypes();

        for (int i = 36; i < 39; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && !containers.contains(item.getType())) {
                armor.put(i, item);
            }
        }
        return armor;
    }

    /**
     * Reset graces for a player
     * @param hasPerm Whether or not the player has the donator permission
     */
    public void resetGrace(boolean hasPerm) {
        if (hasPerm)
            this.graces = 2;
        else
            this.graces = 1;
    }

    /**
     * Deletes a player's angel if they've left the server
     * @param UUID The UUID of the player to delete
     */
    public void safeDelete(UUID UUID) {

        if (this.isGraceInactive()) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    Player player = Bukkit.getPlayer(UUID);
                    if (player == null) {
                        AngelStorage.getAngels().remove(UUID);
                        return;
                    }

                    Angel angel = AngelStorage.getAngels().get(UUID);

                    angel.resetGrace(player.hasPermission("wisecraft.donator"));
                    angel.setGraceActive(false);
                    plugin.getLogger().log(Level.INFO, "Grace timer stopped for: " + Bukkit.getPlayer(UUID));
                    player.sendMessage(ChatColor.AQUA + "Your graces have been reset");
                }


            }.runTaskLater(plugin, 20*30);
            //}.runTaskLater(plugin, 20*60*60*2); // 2 hour
            plugin.getLogger().log(Level.INFO, "Grace timer started for: " + Bukkit.getPlayer(UUID));
            this.setGraceActive(true);
        }
    }


    public boolean isGraceInactive() {return this.isGraceInactive;}

    /**
     * Sets whether or not a player has a grace timer active
     * @param bool Whether or not a player has a grace timer active
     */
    public void setGraceActive(boolean bool) {this.isGraceInactive = bool;}

}

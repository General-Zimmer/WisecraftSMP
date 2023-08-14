package xyz.wisecraft.smp.modules.savinggrace;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.extra.util.UtilRandom;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.wisecraft.smp.modules.extra.util.UtilRandom.getToolTypes;

/**
 * Class for the Angel object.
 */
public class Angel {

    private boolean hasGraceResetTimer = false;
    private ItemStack[] armor = null;
    private List<ItemStack> tools = null;
    private int graces;
    private boolean hasDied = false;
    private final WisecraftSMP plugin;
    private final IEssentials ess;
    private final HashMap<UUID, Angel> angels = OtherStorage.getAngels();

    /**
     * Get the amount of graces the player has left
     * @param hasDonator If the player has donator
     */
    public Angel(IEssentials ess, boolean hasDonator) {
        plugin = WisecraftSMP.getInstance();
        this.ess = ess;
        this.resetGrace(hasDonator);
    }

    /**
     * Give player their saved gear
     * @param e PlayerRespawnEvent
     */
    public void giveGrace(PlayerRespawnEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        this.setDied(false);
        // Check for remaining graces
        if (this.getGraces() <= 0) {return;}


        // Give tools
        List<ItemStack> tools = this.getTools();
        if (tools != null)
            for(int i = 0; i < tools.size(); i++)
                inv.setItem(i, tools.get(i));

        // Give armor
        ItemStack[] armor = this.getArmor();
        if (armor != null)
            inv.setArmorContents(armor);

        // Last few things
        this.clear();
        this.decreaseGraces();
        p.sendMessage(ChatColor.AQUA + "Your gear have been saved. You have " + this.getGraces() + " graces left!");

        this.safeDelete(UUID);

    }


    /**
     * Give starter gear and teleport to tutorial
     * @param user Essentials user
     * @param p Player
     * @throws Exception If the kit doesn't exist
     */
    public void giveStarter(User user, Player p) throws Exception {
        this.setDied(false);
        World tut = Bukkit.getWorld("tutorial");
        if (tut != null)
            //todo Need a tick delay otherwise they will teleport to spawn. Need to figure out why
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = new Location(tut, 5.5, 204, 8.5, -85, 2);
                    p.teleport(loc);
                }
            }.runTaskLater(plugin, 1);

        Kit kit = new Kit("starter", ess);
        kit.expandItems(user);
        angels.get(p.getUniqueId()).clear();
        p.sendMessage(ChatColor.BLUE + "You didn't /sethome or place a bed! You have been granted some new items.");
    }

    /**
     * Method to easily save the gear of a player
     * @param drops The drops of the player
     * @param inv The inventory of the player
     */
    public void saveGear(List<ItemStack> drops, PlayerInventory inv) {
        this.clear();
        this.toolSave(drops, inv);
        this.armorSave(drops, inv);
        this.setDied(true);
    }

    /**
     * Save the tools of the player
     * @param drops The drops of the player
     * @param inv The inventory of the player
     */
    private void toolSave(List<ItemStack> drops, PlayerInventory inv) {
        int saveAmount = 4; // amount of tools to save
        List<ItemStack> tools = new ArrayList<>(saveAmount);
        ItemStack air = new ItemStack(Material.AIR);
        ArrayList<Material> toolTypes = getToolTypes();


        int currentAmount = 0;
        //Tool save
        for(int i = 0; i < inv.getSize(); i++) {
            if (i > 8) break;
            ItemStack item = inv.getItem(i);
            if (item == null) {
                //Add air to keep item in the same position
                tools.add(air);
                continue;
            }

            if (currentAmount < saveAmount && toolTypes.contains(item.getType())) {
                //add tool for recovery
                tools.add(item);
                if (!drops.remove(item))
                    Logger.getLogger("WisecraftSMP").log(Level.WARNING, "Tool WASN'T removed");
                //grace limit
                currentAmount++;
            }
            else
                //Add air to keep item in the same position
                tools.add(air);
        }
        this.tools = tools;
    }

    /**
     * Save armor the player was wearing
     * @param drops The drops of the player
     * @param inv The inventory of the player
     */
    private void armorSave(List<ItemStack> drops, PlayerInventory inv) {
        //Armor save
        ItemStack[] armor = inv.getArmorContents();
        ArrayList<Material> containers = UtilRandom.getContainerTypes();

        List<ItemStack> changeArmor = new ArrayList<>(Arrays.stream(armor).toList());
        int i = 0;
        for (ItemStack item : armor) {

            if (item != null && containers.contains(item.getType()))
                changeArmor.set(i, new ItemStack(Material.AIR));
            else drops.remove(item);
            i++;
        }
        this.armor = changeArmor.toArray(new ItemStack[0]);
    }

    /**
     * Clears the saved gear
     */
    public void clear() {
        this.tools = null;
        this.armor = null;
    }

    /**
     * Gets the armor that was saved
     * @return The armor that was saved
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * Get tools
     * @return The tools that were saved
     */
    public List<ItemStack> getTools() {
        return this.tools;
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
     * Gets graces
     * @return The amount of graces a player has
     */
    public int getGraces() {return this.graces;}

    /**
     * Decrease the amount of graces a player has
     */
    public void decreaseGraces() {--this.graces;}

    //todo Make tools be placed into enderchest or into a shulker box in the enderchest (if full), otherwise drop saved items on the corpse.
    /**
     * Deletes a player's angel if they've left the server
     * @param UUID The UUID of the player to delete
     */
    public void safeDelete(UUID UUID) {


        if (!this.isGraceActive()) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    Player player = Bukkit.getPlayer(UUID);
                    if (player == null) {
                        angels.remove(UUID);
                        return;
                    }

                    Angel angel = angels.get(UUID);

                    angel.resetGrace(player.hasPermission("wisecraft.donator"));
                    angel.setGraceActive(false);
                    Logger.getLogger("WisecraftSMP").log(Level.INFO, "Grace timer stopped for: " + Bukkit.getPlayer(UUID));
                    player.sendMessage(ChatColor.AQUA + "Your graces have been reset");
                }


            }.runTaskLater(plugin, 20*30);
            // }.runTaskLater(plugin, 20*60*60); // 1 hour
            Logger.getLogger("WisecraftSMP").log(Level.INFO, "Grace timer started for: " + Bukkit.getPlayer(UUID));
            this.setGraceActive(true);
        }
    }

    /**
     * Checks if a player has a grace timer active
     * @return Whether or not a player has a grace timer active
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isGraceActive() {return this.hasGraceResetTimer;}

    /**
     * Sets whether or not a player has a grace timer active
     * @param bool Whether or not a player has a grace timer active
     */
    public void setGraceActive(boolean bool) {this.hasGraceResetTimer = bool;}

    /**
     * Checks if a player has died
     * @return Whether or not a player has died
     */
    public boolean hasDied() {return this.hasDied;}

    /**
     * Sets whether or not a player has died
     * @param bool Whether or not a player has died
     */
    public void setDied(boolean bool) {this.hasDied = bool;}

}

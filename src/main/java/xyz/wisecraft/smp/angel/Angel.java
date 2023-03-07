package xyz.wisecraft.smp.angel;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.util.Methods;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.wisecraft.smp.util.Methods.getToolTypes;

public class Angel {

    private boolean hasGraceResetTimer = false;
    private ItemStack[] armor = null;
    private List<ItemStack> tools = null;
    private int graces;
    private boolean hasDied = false;
    private final WisecraftSMP plugin;

    public Angel(boolean hasPerm) {
        plugin = WisecraftSMP.instance;
        this.resetGrace(hasPerm);
    }

    public void giveGrace(PlayerRespawnEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        this.hasDied = false;
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

        this.safeDelete(plugin, UUID);



    }

    public void giveStarter(WisecraftSMP plugin, IEssentials ess, User user, Player p, HashMap<UUID, Angel> gearMap) throws Exception {
        this.hasDied = false;
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
        gearMap.get(p.getUniqueId()).clear();
        p.sendMessage(ChatColor.BLUE + "You didn't /sethome or place a bed! You have been granted some new items.");
    }

    public void saveGear(List<ItemStack> drops, PlayerInventory inv) {
        this.clear();
        this.toolSave(drops, inv);
        this.armorSave(drops, inv);
        this.setDied(true);
    }

    public void toolSave(List<ItemStack> drops, PlayerInventory inv) {
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

    public void armorSave(List<ItemStack> drops, PlayerInventory inv) {
        //Armor save
        ItemStack[] armor = inv.getArmorContents();
        ArrayList<Material> containers = Methods.getContainerTypes();

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
    public void clear() {
        this.tools = null;
        this.armor = null;
    }

    public ItemStack[] getArmor() {
        return this.armor;
    }
    public List<ItemStack> getTools() {
        return this.tools;
    }
    public void resetGrace(boolean hasPerm) {
        if (hasPerm)
            this.graces = 2;
        else
            this.graces = 1;
    }
    public int getGraces() {return this.graces;}
    public void decreaseGraces() {--this.graces;}

    //todo Make tools be placed into enderchest or into a shulker box in the enderchest (if full), otherwise drop saved items on the corpse.
    public boolean safeDelete(WisecraftSMP plugin, UUID UUID) {

        if (!this.isGraceActive()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(UUID);
                    if (p == null) {
                        plugin.getGearmap().remove(UUID);
                        return;
                    }

                    Angel angel = plugin.getGearmap().get(UUID);

                    angel.resetGrace(p.hasPermission("wisecraft.donator"));
                    angel.setGraceActive(false);
                    p.sendMessage(ChatColor.AQUA + "Your graces has reset");
                }

            }.runTaskLater(plugin, 20*60*60); // 1 hour
            this.setGraceActive(true);
            return true;
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isGraceActive() {return this.hasGraceResetTimer;}
    public void setGraceActive(boolean bool) {this.hasGraceResetTimer = bool;}

    public boolean hasDied() {return this.hasDied;}
    public void setDied(boolean bool) {this.hasDied = bool;}

}

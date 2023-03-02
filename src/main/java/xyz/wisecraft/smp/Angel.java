package xyz.wisecraft.smp;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.wisecraft.smp.Methods.getToolTypes;

public class Angel {

    private boolean hasGraceResetTimer;
    private ItemStack[] armor;
    private List<ItemStack> tools;
    private int graces;

    public Angel(boolean hasPerm) {


        this.resetGrace(hasPerm);
        this.hasGraceResetTimer = false;
        this.armor = null;
        this.tools = null;
    }


    public void getStarter(WisecraftSMP plugin, IEssentials ess, User user, Player p, HashMap<UUID, Angel> gearMap) throws Exception {
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

        //todo move this to the thingy above for easier integration
        Kit kit = new Kit("starter", ess);
        kit.expandItems(user);
        gearMap.get(p.getUniqueId()).clear();
        p.sendMessage(ChatColor.BLUE + "You didn't /sethome or place a bed! You have been granted some new items.");
    }

    public void toolSave(List<ItemStack> drops, PlayerInventory inv) {
        List<ItemStack> tools = new ArrayList<>();
        ItemStack air = new ItemStack(Material.AIR);
        ArrayList<Material> toolTypes = getToolTypes();


        int tool_amount = 0;
        //Tool save
        for(int i = 0; i < inv.getSize(); i++) {
            if (i > 8) break;
            ItemStack item = inv.getItem(i);
            if (item == null) {
                //Add air to keep item in the same position
                tools.add(air);
                continue;
            }

            if (tool_amount < 4 && toolTypes.contains(item.getType())) {
                //add tool for recovery
                tools.add(item);
                if (!drops.remove(item))
                    Logger.getLogger("WisecraftSMP").log(Level.WARNING, "Tool WASN'T removed");
                //grace limit
                tool_amount++;
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


    public boolean isGraceActive() {return this.hasGraceResetTimer;}
    public void setGraceActive(boolean bool) {this.hasGraceResetTimer = bool;}

}

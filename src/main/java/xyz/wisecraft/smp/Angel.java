package xyz.wisecraft.smp;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.wisecraft.smp.Methods.getToolTypes;

public class Angel {

    private boolean hasGraceResetTimer;
    private ItemStack[] armor;
    private List<ItemStack> tools;
    private int graces;
    private boolean hasQuit;
    private boolean hasRemoveTimerOn;

    public Angel(boolean hasPerm) {


        this.resetGrace(hasPerm);
        this.hasGraceResetTimer = false;
        this.hasRemoveTimerOn = false;
        this.armor = new ItemStack[]{};
        this.tools = new ArrayList<>();
        this.hasQuit = false;
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

    public void armorsave(List<ItemStack> drops, PlayerInventory inv) {
        //Armor save
        ItemStack[] armor = inv.getArmorContents();
        for (ItemStack itemStack : armor)
            drops.remove(itemStack);
        this.armor = armor;
    }
    public void clear() {
        this.tools.clear();
        this.armor = new ItemStack[]{};
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

    public void setQuit(boolean bool) {this.hasQuit = bool;}
    public boolean getQuit() {return this.hasQuit;}

    public boolean hasRemoveTimer() {return this.hasRemoveTimerOn;}
    public void setRemoveTime(boolean bool) {this.hasRemoveTimerOn = bool;}

    public boolean hasGraceResetTimer() {return this.hasGraceResetTimer;}
    public void setGraceResetTimer(boolean bool) {this.hasGraceResetTimer = bool;}

}

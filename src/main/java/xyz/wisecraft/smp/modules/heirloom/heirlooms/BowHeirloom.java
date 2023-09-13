package xyz.wisecraft.smp.modules.heirloom.heirlooms;

import org.bukkit.Effect;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;

import java.util.ArrayList;
import java.util.List;

public class BowHeirloom extends BaseHeirloom {

    private final static ArrayList<ArrayList<Effect>> effects = new ArrayList<>();

    public BowHeirloom(int level, float xp, HeirloomType type) {
        super(level, xp, type);
    }

    public boolean equals(ItemStack itemstack) {
        NamespacedKey key = new NamespacedKey(HeirloomModule.plugin, "heirloom");
        return itemstack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(HeirloomType.BOWHEIRLOOM.toString());
    }


    /*
    Returns a Arraylist of enums with the types of potions this bow can use.
     */
    public ArrayList<PotionType> canUsePotionTypes() {
        ArrayList<PotionType> potions = new ArrayList<>();
        if (getLevel() >= 0) {
            potions.add(PotionType.NORMAL);
        }
        if (getLevel() >= 1) {
            potions.add(PotionType.SPLASH);
        }
        if (getLevel() >= 2) {
            potions.add(PotionType.LINGERING);
        }
        return potions;
    }


    /**
     * Returns an ArrayList of all the effects permissible for the bow to use at
     * its current level
     * @return
     */
    public ArrayList<Effect> canUseEffects() {
        ArrayList<Effect> effects = new ArrayList<>();
        for (int i = 0; i < getLevel(); i++) {
            effects.addAll(this.effects.get(i));
        }
        return effects;
    }




}

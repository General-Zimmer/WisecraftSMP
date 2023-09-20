package xyz.wisecraft.smp.modules.heirloom.listeners;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;
import xyz.wisecraft.smp.modules.heirloom.util.UtilRandom;

import java.util.ArrayList;
import java.util.List;

public class BowListener implements Listener {


    /**
     * Checks whether the player is holding an Heirloom-bow when an arrow is fired
     * if it is, then the necessary potion effects are added to the arrow fired
     * NOTE: Implementation is not fully done and is not supporting of different Heirloom lvls or other PotionTypes
     * @param e the entity that shot the bow
     */
    @EventHandler
    public void onBowShot(EntityShootBowEvent e) {
        LivingEntity le = e.getEntity();
        ItemStack bow = e.getBow();

        /*
        if (le instanceof Player p) {
            UtilRandom.createBowHeirLoom(p);
        }

         */


        ItemMeta itemMeta = (bow != null) ? e.getBow().getItemMeta(): null;
        PersistentDataContainer pdc = (itemMeta != null) ? itemMeta.getPersistentDataContainer(): null;
        String pdcString = (pdc != null) ? pdc.get(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING): null;
        if (pdcString == null || !pdcString.equals(HeirloomType.BOWHEIRLOOM.toString())) {
            return;
        }

        if (le instanceof Player p) {
            UtilRandom.createBowHeirLoom(p);
            ItemStack itemStack = p.getInventory().getItemInOffHand();
            if (itemStack.getType().equals(Material.POTION)) {
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                PotionData potiondata = meta.getBasePotionData();
                Color color = meta.getColor();
                Entity a = e.getProjectile();
                if(!(a instanceof Arrow)) {
                    return;
                }
                Arrow arrow = (Arrow) a;
                arrow.setBasePotionData(potiondata);
                arrow.setColor(color);
            }
        }
    }
}

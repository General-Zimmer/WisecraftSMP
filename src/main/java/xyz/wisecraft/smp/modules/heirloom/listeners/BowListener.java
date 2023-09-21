package xyz.wisecraft.smp.modules.heirloom.listeners;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
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


        ItemMeta itemMeta = (bow != null) ? e.getBow().getItemMeta(): null;
        PersistentDataContainer pdc = (itemMeta != null) ? itemMeta.getPersistentDataContainer(): null;
        String pdcString = (pdc != null) ? pdc.get(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING): null;
        if (pdcString == null || !pdcString.equals(HeirloomType.BOWHEIRLOOM.toString())) {
            return;
        }

        if (le instanceof Player p) {
            ItemStack itemStack = p.getInventory().getItemInOffHand();
            if (itemStack.getType().equals(Material.POTION)) {
                ItemMeta offHandMeta = itemStack.getItemMeta();
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                PotionData potiondata = meta.getBasePotionData();
                Color color = meta.getColor();
                Entity a = e.getProjectile();
                if(!(a instanceof Arrow)) {
                    return;
                }
                if (itemStack.getType().equals(Material.SPLASH_POTION)) {

                }
                Arrow arrow = (Arrow) a;
                arrow.setBasePotionData(potiondata);
                arrow.setColor(color);


            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        ProjectileSource projectileSource = event.getEntity().getShooter();
        if (projectileSource instanceof Player p) {
            ItemStack itemStack = p.getInventory().getItemInOffHand();
            if (itemStack.getType().equals(Material.SPLASH_POTION)) {
                Entity hitEntity = event.getHitEntity();
                if (hitEntity instanceof LivingEntity le) {
                    le.setNoDamageTicks(0);
                }

                World world = event.getEntity().getWorld();
                Location location = event.getEntity().getLocation();
                ItemMeta offHandMeta = itemStack.getItemMeta();
                PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                PotionData potiondata = meta.getBasePotionData();
                Location spawnLocation = new Location(world, location.getX(), location.getY(), location.getZ());
                ThrownPotion thrownPotion = (ThrownPotion) world.spawnEntity(location, EntityType.SPLASH_POTION);
                thrownPotion.setPotionMeta(meta);
            }
        }
    }
}

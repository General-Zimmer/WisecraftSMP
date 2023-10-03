package xyz.wisecraft.smp.modules.heirloom.listeners;

import org.bukkit.*;
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
import org.bukkit.potion.*;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BowHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;
import xyz.wisecraft.smp.modules.heirloom.util.UtilRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BowListener implements Listener {


    private static NamespacedKey potionKey = new NamespacedKey(HeirloomModule.plugin, "potionData");
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


        if (!UtilRandom.checkItemIsHeirloom(bow)) {
            return;
        }

        if (le instanceof Player p) {
            ItemStack itemStack = p.getInventory().getItemInOffHand();
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

                UtilRandom.setPotionNBT(arrow, itemStack.getType(), potionKey);
                String itemMeta = arrow.getItemStack().getItemMeta().toString();
        }
    }


    public void addInfoToHashMaps() {

    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        Entity a = event.getEntity();
        if(!(a instanceof Arrow)) {
            return;
        }


        Arrow arrow = (Arrow) a;

        String getTypeString = arrow.getItemStack().getItemMeta().getPersistentDataContainer().get(potionKey, PersistentDataType.STRING);
        if (arrow.getShooter() instanceof Player p) {
            p.sendMessage("type: " + getTypeString);
        }
        if (getTypeString == null) {
            return;
        }
        Material potionType = Material.valueOf(getTypeString);
        ItemStack itemStack = new ItemStack(potionType);
        ProjectileSource projectileSource = event.getEntity().getShooter();
        if (projectileSource instanceof Player p) {
            // ItemStack itemStack = p.getInventory().getItemInOffHand();
            if (itemStack.getType().equals(Material.SPLASH_POTION) || itemStack.getType() == Material.LINGERING_POTION) {
                Entity hitEntity = event.getHitEntity();
                if (hitEntity instanceof LivingEntity le) {
                    le.setNoDamageTicks(0);
                }
                World world = event.getEntity().getWorld();
                Location location = event.getEntity().getLocation();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ThrownPotion thrownPotion = (ThrownPotion) world.spawnEntity(location, EntityType.SPLASH_POTION);
                            thrownPotion.setItem(itemStack);
                            thrownPotion.splash();
                        }
                    }.runTaskLater(WisecraftSMP.getInstance(), 1);

            }
        }
    }


}

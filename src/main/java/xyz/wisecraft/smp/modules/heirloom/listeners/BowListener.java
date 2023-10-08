package xyz.wisecraft.smp.modules.heirloom.listeners;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
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
import xyz.wisecraft.smp.modules.heirloom.storage.HeirloomStorage;
import xyz.wisecraft.smp.modules.heirloom.util.UtilRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BowListener implements Listener {


    private static final WisecraftSMP plugin = HeirloomModule.plugin;
    private static NamespacedKey potionKey = new NamespacedKey(HeirloomModule.plugin, "potionData");
    private static NamespacedKey bowHeirloomKey = new NamespacedKey(HeirloomModule.plugin, "arrowHeirloom");
    private static NamespacedKey lingeringPotionXpEvent = new NamespacedKey(HeirloomModule.plugin, "lingeringPotionXpEvent");

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

        BowHeirloom myBow = BaseHeirloom.getHeirloom(bow, BowHeirloom.class);
        if (myBow == null) {
            return;
        }

        if (le instanceof Player p) {
            ItemStack itemStack = p.getInventory().getItemInOffHand();
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            PotionData potiondata = meta.getBasePotionData();
            Entity a = e.getProjectile();

                if(!(a instanceof Arrow)) {
                    return;
                }

                Arrow arrow = (Arrow) a;
                arrow.setBasePotionData(potiondata);

                UtilRandom.setPotionNBT(arrow, itemStack.getType(), potionKey, bowHeirloomKey, myBow.getID());
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        Entity a = event.getEntity();
        if(!(a instanceof Arrow)) {
            return;
        }

        Arrow arrow = (Arrow) a;


        String getTypeString = arrow.getPersistentDataContainer().get(potionKey, PersistentDataType.STRING);
        String getbowHeirloomKey = arrow.getPersistentDataContainer().get(bowHeirloomKey, PersistentDataType.STRING);

        if (getbowHeirloomKey == null || getTypeString == null) {
            return;
        }

        PotionMeta potionMeta = (PotionMeta) arrow.getItemStack().getItemMeta();
        Material potionType = Material.valueOf(getTypeString);
        final ItemStack itemStack = new ItemStack(potionType);
        itemStack.setItemMeta(potionMeta);

            if (itemStack.getType().equals(Material.SPLASH_POTION) || itemStack.getType() == Material.LINGERING_POTION) {
                Entity hitEntity = event.getHitEntity();
                if (hitEntity instanceof LivingEntity le) {
                    le.setNoDamageTicks(0);
                }
                final World world = event.getEntity().getWorld();
                final Location location = event.getEntity().getLocation();

                Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                    ThrownPotion thrownPotion = (ThrownPotion) world.spawnEntity(location, EntityType.SPLASH_POTION);
                    thrownPotion.setItem(itemStack);
                    thrownPotion.getPersistentDataContainer().set(bowHeirloomKey, PersistentDataType.STRING, getbowHeirloomKey);
                    thrownPotion.splash();
                });

            }
    }

    @EventHandler
    public void onPotionDamageToLivingEntity(PotionSplashEvent pse) {
        ThrownPotion thrownPotion = pse.getPotion();

        BowHeirloom bowHeirloom = UtilRandom.getBowHeirloomFromThrownPotion(thrownPotion);

        ProjectileSource shooter = pse.getEntity().getShooter();
        if (!(shooter instanceof LivingEntity)) {
            return;
        }

        if (bowHeirloom != null) {
            if (!pse.getAffectedEntities().isEmpty()) {
                bowHeirloom.giveXP();
            }
        }
    }

    @EventHandler
    public void onPotionCloudAreaEffefct(AreaEffectCloudApplyEvent aecae) {

        if (!(aecae.getEntity() instanceof ThrownPotion tp)) {
            return;
        }

        BowHeirloom bowHeirloom = UtilRandom.getBowHeirloomFromThrownPotion(tp);
        String BowRecXPForthisPot = tp.getPersistentDataContainer().get(lingeringPotionXpEvent, PersistentDataType.STRING);

        if (!aecae.getAffectedEntities().isEmpty() && bowHeirloom != null) {
            if (BowRecXPForthisPot != null && !BowRecXPForthisPot.equals("true"))
                bowHeirloom.giveXP();
                tp.getPersistentDataContainer().set(lingeringPotionXpEvent, PersistentDataType.STRING, "true");
        }
    }


}

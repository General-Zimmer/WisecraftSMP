package xyz.wisecraft.smp.modules.heirloom.listeners;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.*;
import org.bukkit.projectiles.ProjectileSource;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BowHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;
import xyz.wisecraft.smp.modules.heirloom.util.UtilRandom;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class BowListener implements Listener {


    private static final WisecraftSMP plugin = HeirloomModule.plugin;
    public static final NamespacedKey potionKey = new NamespacedKey(HeirloomModule.plugin, "potionData");
    public static final NamespacedKey heirloomArrowKey = new NamespacedKey(HeirloomModule.plugin, "arrowHeirloom");
    public static final NamespacedKey hasXpEvent = new NamespacedKey(HeirloomModule.plugin, "lingeringPotionXpEvent");
    public static final NamespacedKey playerToXPKey = new NamespacedKey(HeirloomModule.plugin, "playerToXPKey");

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


        if (!UtilRandom.checkItemIsHeirloom(bow, HeirloomType.BOWHEIRLOOM)) {
            return;
        }

        // bow cannot be null at this point due to the check above.
        @SuppressWarnings("ConstantConditions")
        BowHeirloom myBow = BaseHeirloom.getHeirloom(bow, BowHeirloom.class);

        if (le instanceof Player p) {
            ItemStack itemStack = p.getInventory().getItemInOffHand();
            if (!itemStack.getType().equals(Material.POTION) && !itemStack.getType().equals(Material.SPLASH_POTION) && !itemStack.getType().equals(Material.LINGERING_POTION)) {
                return;
            }
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            PotionData potiondata = meta.getBasePotionData();
            Entity a = e.getProjectile();

            if(!(a instanceof Arrow arrow)) {
                return;
            }

            arrow.setBasePotionData(potiondata);
            UUID playerUUID = p.getUniqueId();

            UtilRandom.setArrowPDC(arrow, itemStack.getType(), myBow.getID(), playerUUID);
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();
        if(!(proj instanceof Arrow arrow)) {
            return;
        }

        String getTypeString = arrow.getPersistentDataContainer().get(potionKey, PersistentDataType.STRING);
        String arrowKey = arrow.getPersistentDataContainer().get(heirloomArrowKey, PersistentDataType.STRING);

        if (arrowKey == null || getTypeString == null) {
            return;
        }

        PotionMeta potionMeta = (PotionMeta) arrow.getItemStack().getItemMeta();
        Material potionType = Material.valueOf(getTypeString);
        final ItemStack itemStack = new ItemStack(potionType);
        itemStack.setItemMeta(potionMeta);

            if (itemStack.getType().equals(Material.SPLASH_POTION) || itemStack.getType() == Material.LINGERING_POTION) {
                Entity hitEntity = event.getHitEntity();
                final boolean hasHitEntity;
                if (hitEntity instanceof LivingEntity le) {
                    le.setNoDamageTicks(0);
                    hasHitEntity = true;
                    BaseHeirloom.getHeirloom(arrow.getItemStack(), BowHeirloom.class).giveXP((Player) arrow.getShooter());
                } else {
                    hasHitEntity = false;
                }
                final World world = event.getEntity().getWorld();
                final Location location = event.getEntity().getLocation();

                Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                    ThrownPotion thrownPotion = (ThrownPotion) world.spawnEntity(location, EntityType.SPLASH_POTION);
                    thrownPotion.setShooter(arrow.getShooter());
                    thrownPotion.setItem(itemStack);
                    thrownPotion.getPersistentDataContainer().set(heirloomArrowKey, PersistentDataType.STRING, arrowKey);
                    thrownPotion.getPersistentDataContainer().set(hasXpEvent, PersistentDataType.BOOLEAN, hasHitEntity);
                    thrownPotion.getPersistentDataContainer().set(playerToXPKey, PersistentDataType.STRING,
                            Objects.requireNonNull(
                                    arrow.getPersistentDataContainer().get(playerToXPKey, PersistentDataType.STRING),
                                    "heirloom Arrow didn't have player UUID")); //todo prevent thrown potion from being spawned if dis is null
                    thrownPotion.splash();
                });
                if (event.getHitBlock() != null) {
                    arrow.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
                } else if (event.getHitEntity() != null) {
                    arrow.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));

                }
            }
    }

    @EventHandler
    public void onPotionDamageToLivingEntity(PotionSplashEvent pse) {
        ThrownPotion thrownPotion = pse.getPotion();
        ProjectileSource shooter = pse.getEntity().getShooter();

        if (thrownPotion.getItem().getType().equals(Material.LINGERING_POTION)) {
            return;
        }

        if (!(shooter instanceof LivingEntity)) {
            return;
        }

        BowHeirloom bowHeirloom = UtilRandom.getBowHeirloomFromPDC(thrownPotion.getPersistentDataContainer());

        if (bowHeirloom != null) {
            if (!pse.getAffectedEntities().isEmpty()) {

                String uuid = thrownPotion.getPersistentDataContainer().get(playerToXPKey, PersistentDataType.STRING);
                if (uuid == null) {
                    WisecraftSMP.getInstance().getLogger().log(Level.SEVERE, "ThrownPotion does not have a player UUID");
                    return;
                }
                bowHeirloom.giveXP(Bukkit.getPlayer(UUID.fromString(uuid)));
            }
        }
    }

    @EventHandler
    public void onPotionCloudAreaEffect(AreaEffectCloudApplyEvent aecae) {

        AreaEffectCloud cloud = aecae.getEntity();
        if (!cloud.getPersistentDataContainer().has(heirloomArrowKey)) {
            return;
        }

        BowHeirloom bowHeirloom = UtilRandom.getBowHeirloomFromPDC(cloud.getPersistentDataContainer());
        boolean BowRecXPForthisPot = cloud.getPersistentDataContainer().get(hasXpEvent, PersistentDataType.BOOLEAN);

        if (!aecae.getAffectedEntities().isEmpty() && bowHeirloom != null) {
            if (!BowRecXPForthisPot) {
                cloud.getPersistentDataContainer().set(hasXpEvent, PersistentDataType.BOOLEAN, true);
                String uuid = cloud.getPersistentDataContainer().get(playerToXPKey, PersistentDataType.STRING);
                if (uuid == null) {
                    WisecraftSMP.getInstance().getLogger().log(Level.SEVERE, "ThrownPotion does not have a player UUID");
                    return;
                }
                bowHeirloom.giveXP(Bukkit.getPlayer(UUID.fromString(uuid)));
            }
        }
    }

    @EventHandler
    public void onPotionCloudthingy(LingeringPotionSplashEvent e) {
        PersistentDataContainer potion = e.getEntity().getPersistentDataContainer();
        if (potion.has(heirloomArrowKey)) {

            PersistentDataContainer cloud = e.getAreaEffectCloud().getPersistentDataContainer();

            cloud.set(hasXpEvent, PersistentDataType.BOOLEAN, potion.get(hasXpEvent, PersistentDataType.BOOLEAN));
            cloud.set(heirloomArrowKey, PersistentDataType.STRING, potion.get(heirloomArrowKey, PersistentDataType.STRING));
            cloud.set(playerToXPKey, PersistentDataType.STRING, potion.get(playerToXPKey, PersistentDataType.STRING));
        }

    }

}

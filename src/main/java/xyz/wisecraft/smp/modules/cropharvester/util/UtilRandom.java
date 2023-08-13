package xyz.wisecraft.smp.modules.cropharvester.util;

import com.craftaro.ultimatetimber.core.hooks.protection.GriefPreventionProtection;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.sk89q.worldguard.bukkit.ProtectionQuery;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.ArrayList;
import java.util.Random;

/**
 * Utility class for random methods
 */
public abstract class UtilRandom {

    private static final WisecraftSMP plugin = WisecraftSMP.getInstance();
    /**
     * Return Ageable from a block
     * @param block Block to check
     * @return Ageable if block is a crop
     */
    private static Ageable getAgeAbleFromBlock(Block block) {
        return (Ageable) block.getBlockData();
    }

    /**
     * Type what this does and relevent information
     * @param mainLocation Location of the block that was interacted with
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param item Item used to break block naturally
     * @param player Player to get mainhand item from
     */
    public static void farmCropWithHoeIfMaxAgeOnLocation(Location mainLocation, int x, int y, int z, ItemStack item, Player player) {
        boolean gpEnabled = plugin.isGriefPreventionEnabled();
        boolean tgEnabled = plugin.isWorldGuardEnabled();
        boolean twEnabled = plugin.isTownyEnabled();

        boolean blockWasBroken = false;
        GriefPreventionProtection gpp = new GriefPreventionProtection(plugin);

        Location checkLocation = new Location(mainLocation.getWorld(),
                (mainLocation.getX() + x), mainLocation.getY() + y, mainLocation.getZ() + z);

        Block currentBlock = checkLocation.getBlock();
        if (!(currentBlock.getBlockData() instanceof Ageable)) {
            return;
        }

        Material blockMaterial = currentBlock.getBlockData().getMaterial();
        ProtectionQuery pq = new ProtectionQuery();

        boolean townyCanDestroy = PlayerCacheUtil.getCachePermission(player, currentBlock.getLocation(), currentBlock.getType(), TownyPermission.ActionType.DESTROY);
        boolean blockIsMaxAge = getAgeAbleFromBlock(currentBlock).getAge() == getAgeAbleFromBlock(currentBlock).getMaximumAge();

        ArrayList<Boolean> farmChecksArrayList = new ArrayList<>();
        if (gpEnabled) {
            farmChecksArrayList.add(gpp.canBreak(player, checkLocation));
        }
        if (tgEnabled) {
            farmChecksArrayList.add(pq.testBlockBreak(player, currentBlock));
        }
        if (twEnabled) {
            farmChecksArrayList.add(townyCanDestroy);
        }
        farmChecksArrayList.add(blockIsMaxAge);

        boolean canDestroyBlock = true;
        int i = 0;
        while (canDestroyBlock && i < farmChecksArrayList.size()) {
            if (!farmChecksArrayList.get(i)) {
                canDestroyBlock = false;
            }
            i++;
        }


        if (canDestroyBlock) {
            currentBlock.breakNaturally(item);
            currentBlock.setType(blockMaterial);
            blockWasBroken = true;
        }

        if (!blockWasBroken) {
            return;
        }

        if (player.getGameMode() == org.bukkit.GameMode.CREATIVE) {
            return;
        }

        doDamageToItem(item, item.getEnchantmentLevel(Enchantment.DURABILITY), player);
    }

    /**
     * Type what this does and relevent information
     * @param width Width of the area to farm
     * @param block Block that was interacted with
     * @param item Item used to break block naturally
     * @param player Player to get mainhand item from
     */
    public static void farmBlocksXByX(int width, Block block, ItemStack item, Player player) {

        Location mainLocation = block.getLocation();

        int size = width / (-2);
        if (width == 0) {
            farmCropWithHoeIfMaxAgeOnLocation(mainLocation, 0, 0, 0, item, player);
            return;
        }

        for (int z = size; z <= size * (-1); z++) {

            for (int x = size; x <= size * (-1); x++) {
                farmCropWithHoeIfMaxAgeOnLocation(mainLocation, x, 0, z, item, player);
            }
        }
    }

    public static void farmOptimalFarmingBlocks(int width, int height, Block startBlock, ItemStack item, Player player) {
        boolean[] checkLocations = new boolean[4];
        ArrayList<Location> locationsToBeFarmed = new ArrayList<>();
        // int max = width * height;
        int count = 0;
        Location mainLocation = startBlock.getLocation();

        Location checkLocation = mainLocation;

        int y = 1;
        boolean farmingAreaFound = false;

        locationsToBeFarmed = findOptimalFarmingBlocks(mainLocation, width, height);



            // player.sendMessage("Count final: " + count);
            // player.sendMessage("" + locationsToBeFarmed.size());
            player.sendMessage("New Farming run");
            player.sendMessage("Locationstobefarmed: " + locationsToBeFarmed.size());
            for (Location location: locationsToBeFarmed) {
                farmCropWithHoeIfMaxAgeOnLocation(location, 0, 0, 0, item, player);
                player.sendMessage("X: " + location.getX() + "  Z: " + location.getZ());
            }
        }


/*

            if (!checkLocations[1]) { // Right check ... Z decreases
                for (int i = -1; i >= width * (-1); i--) {
                    checkLocation = mainLocation.add(0, 0, i);
                    Block currentBlock = checkLocation.getBlock();

                    if (currentBlock.getBlockData() instanceof Ageable) {
                        locationsToBeFarmed.add(checkLocation);
                        count++;
                    }
                }
            }
            player.sendMessage("Count: " + count);
            count = 0;

             */

        /*
           // Right check ... X increases
                for (int i = 0; i < width; i++) {
                    checkLocation = new Location(mainLocation.getWorld(),
                            (mainLocation.getX() + i), mainLocation.getY(), mainLocation.getZ());
                    Block currentBlock = checkLocation.getBlock();
                    if (currentBlock.getBlockData() instanceof Ageable) {
                        locationsToBeFarmed.add(checkLocation);
                        count++;
                    }
                }


            player.sendMessage("Count: " + count);
            if (count < 4) {
                count = 0;
            } else {
                checkLocations[0] = true;
            }
            if (count == 0) { // Left check ... X decreases
            for (int i = 0; i > width * (-1); i--) {
                checkLocation = new Location(mainLocation.getWorld(),
                        (mainLocation.getX() + i), mainLocation.getY(), mainLocation.getZ());
                Block currentBlock = checkLocation.getBlock();
                if (currentBlock.getBlockData() instanceof Ageable) {
                    locationsToBeFarmed.add(checkLocation);
                    count++;
                }
            }
                if (count < 4) {
                    count = 0;
                } else {
                    checkLocations[1] = true;
                }
            }
             */

    private static ArrayList<Location> findOptimalFarmingBlocks(Location mainLocation, int width, int height) {
            Location checkLocation = mainLocation;
            ArrayList<Location> finalFarmingList = new ArrayList<>();
            int totalBlocksToBeFarmed = width * height;
            int totalCount = 0;

            for (int z = (height * (-1)) + 1; z < height && totalCount < totalBlocksToBeFarmed; z++) {
            int xCount = 0;
            for (int x = (width * (-1)) + 1; x < width && (totalCount < totalBlocksToBeFarmed|| xCount < width); x++) {
                checkLocation = new Location(mainLocation.getWorld(),
                        (mainLocation.getX() + x), mainLocation.getY(), mainLocation.getZ() + z);
                Block currentBlock = checkLocation.getBlock();
                if (currentBlock.getBlockData() instanceof Ageable) {
                    if (getAgeAbleFromBlock(currentBlock).getAge() == getAgeAbleFromBlock(currentBlock).getMaximumAge()) {
                        finalFarmingList.add(checkLocation);
                        totalCount++;
                        xCount++;
                    }
                }
            }
        }
        return finalFarmingList;
    }
    private static ArrayList<Location> checkFarmingBlocksInRow(Location mainLocation, int width, int height, int direction) {
        Location checkLocation = mainLocation;
        ArrayList<Location> locationsToBeFarmed = new ArrayList<>();
        int count = 0;
        int directionFactorI = 0;
        int directionFactorMax = 0;

        switch (direction) {
            case 1: // Right
                directionFactorMax = width;
                break;
            case 2: // Left
                directionFactorI = (width * (-1)) + 1;
                break;
            case 3: // Up
                directionFactorI = (height * (-1)) + 1;
            break;
            case 4:// Down
                directionFactorMax = height;
                break;
        }

        if (direction == 1 || direction == 2) {
            for (int z = -1; z > (height * (-1)); z--) {

                for (int x = 0; x < width; x++) {
                    checkLocation = new Location(mainLocation.getWorld(),
                            (mainLocation.getX() + x), mainLocation.getY(), mainLocation.getZ() + z);
                    Block currentBlock = checkLocation.getBlock();
                    if (currentBlock.getBlockData() instanceof Ageable) {
                        locationsToBeFarmed.add(checkLocation);
                        count++;
                    }
                }
            }
            /*
            for (int i = directionFactorI; i <= directionFactorMax; i++) {
                checkLocation = new Location(mainLocation.getWorld(),
                        (mainLocation.getX() + i), mainLocation.getY(), mainLocation.getZ());
                Block currentBlock = checkLocation.getBlock();
                if (currentBlock.getBlockData() instanceof Ageable) {
                    locationsToBeFarmed.add(checkLocation);
                    count++;
                }
            }

             */
        }
        if (direction == 3 || direction == 4) {
            for (int i = directionFactorI; i <= directionFactorMax; i++) {
                checkLocation = new Location(mainLocation.getWorld(),
                        (mainLocation.getX()), mainLocation.getY(), mainLocation.getZ() + i);
                Block currentBlock = checkLocation.getBlock();
                if (currentBlock.getBlockData() instanceof Ageable) {
                    locationsToBeFarmed.add(checkLocation);
                    count++;
                }
            }
        }

        return count > 3 ? locationsToBeFarmed : null;
    }




    // TODO - Make the break Item sound play once a tool is broken
    /**
     * Does damage to an Item in the Players main hand, taking into account the durability level of the item and
     * damaging the item according to the unbreaking enchantment it possess
     * @param item in players main hand
     * @param durabilityLevel of the item in players main hand
     * @param player that interacts with this action
     */
    private static void doDamageToItem(ItemStack item, int durabilityLevel, Player player) {
        Random rand = new Random();
        float randomNumber = rand.nextFloat(100);
        float changeOfDamage = 100 / (durabilityLevel + 1f);
        if (randomNumber < changeOfDamage) {
            Damageable pdmg = (Damageable)player.getInventory().getItemInMainHand().getItemMeta();
            pdmg.setDamage(pdmg.getDamage() + 1);
            if (pdmg.getDamage() >= item.getType().getMaxDurability()) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            player.getInventory().getItemInMainHand().setItemMeta((pdmg));
        }
    }
}

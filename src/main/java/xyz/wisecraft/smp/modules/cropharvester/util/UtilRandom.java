package xyz.wisecraft.smp.modules.cropharvester.util;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.sk89q.worldguard.bukkit.ProtectionQuery;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.cropharvester.CropHarvesterModule;

import java.util.ArrayList;
import java.util.Arrays;
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
     *
     * @param blockToFarm  Block that will be farmed
     * @param item         Item used to break block naturally
     * @param player       Player to get mainhand item from
     */
    private static void farmCropWithHoe(Block blockToFarm, ItemStack item, Player player) {
        boolean blockWasBroken = false;
        Block currentBlock = blockToFarm;

        if (!(currentBlock.getBlockData() instanceof Ageable)) { // This can be put into getAgeAbleFromBlock
            return;
        }

        // Grief plugin checks
        boolean canBreak = true; // canPlayerBreak(player, currentBlock);

        if (getAgeAbleFromBlock(currentBlock).getAge() == getAgeAbleFromBlock(currentBlock).getMaximumAge() && canBreak) {
            Material blockMaterial = currentBlock.getBlockData().getMaterial();

            currentBlock.breakNaturally(item);
            currentBlock.setType(blockMaterial);
            blockWasBroken = true;
        }

        if (!blockWasBroken || player.getGameMode() == org.bukkit.GameMode.CREATIVE) {
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
            // farmCropWithHoe(mainLocation, 0, 0, item, player);
            return;
        }

        for (int z = size; z <= size * (-1); z++) {

            for (int x = size; x <= size * (-1); x++) {
                // farmCropWithHoe(mainLocation, x, z, item, player);
            }
        }
    }

    public static Block[][] initBlocks(int width,int height, Block block, ItemStack item, Player player) {
        int rows = 0;
        int cols = 0;
        int biggest = 0;
        int smallest = 0;
        if (width > height) {
            biggest = width;
            smallest = height;
        } else {
            biggest = height;
            smallest = width;
        }
        rows = (biggest * 2) - 1;
        cols = (biggest * 2) - 1;
        Block[][] blockField = new Block[rows][cols];

        int xStart = block.getX() - ((rows / 2));
        int zStart = block.getZ() - ((cols / 2));
        int yStart = block.getY();


        int xMax = (height * 2) - 1;
        int zMax = (height * 2) - 1;

        World world = block.getWorld();

        // Loop for all
        for (int x = 0; x < xMax; x++) {

            for (int z = 0; z < zMax; z++) {
                int checker = biggest-smallest;
                if ((x < checker && z < checker) || (x < checker && z >= zMax-checker) || (x >= zMax-checker && z < checker) || (x >= zMax-checker && z >= zMax-checker)) {
                    continue;
                }
                Block blockToBreak = world.getBlockAt(xStart + x,yStart, zStart + z);
                blockField[x][z] = blockToBreak;
            }
        }


        /*
        // Loop to test break of full Array
        for (int i = 0; i < blockField.length; i++) {

            for (int j = 0; j < blockField[i].length; j++) {
                if (blockField[i][j] != null) {
                    farmCropWithHoe(blockField[i][j],item, player);
                }
            }
        }

         */

        return blockField;
    }


    /*
    public static void newFarmBlocksXByX(int width,int height, Block block, ItemStack item, Player player) {
        ArrayList<Location> farmLocations = new ArrayList<>();
        Location mainLocation = block.getLocation();

        farmLocations = findOptimalFarmArea(height, width, mainLocation.getBlockX(), mainLocation.getBlockZ(),mainLocation, item, player, 0, new ArrayList<>());

        for (Location lc: farmLocations) {
            farmCropWithHoe(mainLocation, lc.getBlockX(), lc.getBlockZ(), item, player);
        }
    }

     */



    public static void farmAreaWithMostCrops(int width,int height, Block block, ItemStack item, Player player) {
        Block[][] blockField = initBlocks(width,height, block, item, player);
        int rows = blockField.length;
        int cols = blockField[0].length;


        int xStart = 4;
        int zStart = 4;
        ArrayList<Block> blocksToFarm = createSubFarmGrid(blockField, xStart, zStart, height, width);

        player.sendMessage("" + blocksToFarm.size());
        for (Block b: blocksToFarm) {
            farmCropWithHoe(b, item, player);
        }

    }
    private static ArrayList<Block> findOptimalFarmArea(int height, int width,int xStart, int zStart, Block startBlock, int count, Block[][] blockField, ArrayList<Block> optimalFarmBlocks) {

        ArrayList<Block> currentCheckedBlocks;
        // Base case
        if (count == width * 2) {
            return optimalFarmBlocks;
        }

        if (count == width) {
            xStart += (height - 1);
            zStart -= width;
        }
        Block tempBlock = startBlock.getWorld().getBlockAt(startBlock.getX() - xStart,startBlock.getY(), startBlock.getZ() - zStart);


        currentCheckedBlocks = createSubFarmGrid(blockField,xStart - (height - 1),zStart - (width - 1),height,width);
        count++;
        zStart++;

        if (currentCheckedBlocks.size() == (width * height)-1) {
            return currentCheckedBlocks;
        }
        if (currentCheckedBlocks.size() > optimalFarmBlocks.size()) {
            optimalFarmBlocks = currentCheckedBlocks;
        }

        return findOptimalFarmArea(height,width,xStart, zStart,tempBlock, count, blockField,optimalFarmBlocks);
    }

    private static ArrayList<Block> createSubFarmGrid(Block[][] field, int startRow, int startCol, int rowSize, int colSize) {
        Block[][] farmArray = new Block[field.length][field[0].length];
        ArrayList<Block> blocks = new ArrayList<>();

        for (int x = startRow; x < (startRow + rowSize); x++) {

            for (int z = startCol; z < (startCol + colSize); z++) {
               Block currentBlock = field[x][z];
                if (currentBlock != null && (currentBlock.getBlockData() instanceof Ageable)) {
                    if (getAgeAbleFromBlock(currentBlock).getAge() == getAgeAbleFromBlock(currentBlock).getMaximumAge()) {
                        blocks.add(currentBlock);
                        // farmArray[x][z] = currentBlock;
                    }
                }
            }
        }
        return blocks;
    }

    private static boolean canPlayerBreak(Player player, Block currentBlock) {
        CropHarvesterModule cropModule = CropHarvesterModule.getInstance();

        boolean griefCheck = true;
        boolean blockCanBreakYes = true;
        boolean townyCanDestroy = true;
        if (cropModule.isGriefpreventionEnabled()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(currentBlock.getLocation(), true, null);
            if (claim != null) {
                // todo use checkPermission(Player, ClaimPermission, Event) instead
                griefCheck = claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
            }
        }
        if (cropModule.isWorldGuardEnabled())
            blockCanBreakYes = new ProtectionQuery().testBlockBreak(player, currentBlock); // World Guard
        if (cropModule.isTownyEnabled())
            townyCanDestroy = PlayerCacheUtil.getCachePermission(player, currentBlock.getLocation(), currentBlock.getType(), TownyPermission.ActionType.DESTROY);

        return griefCheck && blockCanBreakYes && townyCanDestroy;
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
            Damageable pdmg = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
            pdmg.setDamage(pdmg.getDamage() + 1);
            if (pdmg.getDamage() >= item.getType().getMaxDurability()) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            player.getInventory().getItemInMainHand().setItemMeta((pdmg));
        }
    }
}

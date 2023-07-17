package xyz.wisecraft.smp.modules.cropharvester.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Random;

/**
 * Utility class for random methods
 */
public abstract class UtilRandom {

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

        boolean blockWasBroken = false;
        Location checkLocation = new Location(mainLocation.getWorld(),
                (mainLocation.getX() + x), mainLocation.getY() + y, mainLocation.getZ() + z);

        Block currentBlock = checkLocation.getBlock();
        if (!(currentBlock.getBlockData() instanceof Ageable)) {
            return;
        }

        Material blockMaterial = currentBlock.getBlockData().getMaterial();

        if (getAgeAbleFromBlock(currentBlock).getAge() == getAgeAbleFromBlock(currentBlock).getMaximumAge()) {
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

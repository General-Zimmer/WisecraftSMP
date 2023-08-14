package xyz.wisecraft.smp.modules.extra.util;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.ArrayList;

/**
 * A class for random utilities
 */
public abstract class UtilRandom {

    /**
     * Teleports the player to the world
     * @param world the world to teleport to
     * @param sender the player to teleport
     */
    public static void tpworld(IEssentials ess, World world, CommandSender sender, boolean isMultiverseEnabled) {
        WisecraftSMP plugin = WisecraftSMP.getInstance();

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World does not exist");
            return;
        }
        sender.sendMessage(ChatColor.GOLD + "Teleporting to " + world.getName() + " in 1 second(s)");

        if (isMultiverseEnabled)
            new BukkitRunnable() {
                @Override
                public void run() {
                    setBack(ess, Bukkit.getPlayerExact(sender.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + world.getName());
                }
            }.runTaskLater(plugin, 20);


    }

    /**
     * Sets the players back location to their current location
     * @param ess the essentials plugin
     * @param p the player to set the back location
     */
    public static void setBack(IEssentials ess, Player p) {
        if (ess != null)
            ess.getUser(p).setLastLocation();
    }

    /**
     * Gets all the tool types
     * @return an arraylist of all the tool types
     */
    //This is reeeal ugly but kinda beautiful
    public static ArrayList<Material> getToolTypes() {
        ArrayList<Material> toolTypes = new ArrayList<>();
        //axe
        toolTypes.add(Material.NETHERITE_AXE);
        toolTypes.add(Material.DIAMOND_AXE);
        toolTypes.add(Material.IRON_AXE);
        toolTypes.add(Material.STONE_AXE);
        toolTypes.add(Material.WOODEN_AXE);
        toolTypes.add(Material.GOLDEN_AXE);
        //pickaxe
        toolTypes.add(Material.NETHERITE_PICKAXE);
        toolTypes.add(Material.DIAMOND_PICKAXE);
        toolTypes.add(Material.IRON_PICKAXE);
        toolTypes.add(Material.STONE_PICKAXE);
        toolTypes.add(Material.WOODEN_PICKAXE);
        toolTypes.add(Material.GOLDEN_PICKAXE);
        //Shovel
        toolTypes.add(Material.NETHERITE_SHOVEL);
        toolTypes.add(Material.DIAMOND_SHOVEL);
        toolTypes.add(Material.IRON_SHOVEL);
        toolTypes.add(Material.STONE_SHOVEL);
        toolTypes.add(Material.WOODEN_SHOVEL);
        toolTypes.add(Material.GOLDEN_SHOVEL);
        //Sword
        toolTypes.add(Material.NETHERITE_SWORD);
        toolTypes.add(Material.DIAMOND_SWORD);
        toolTypes.add(Material.IRON_SWORD);
        toolTypes.add(Material.STONE_SWORD);
        toolTypes.add(Material.WOODEN_SWORD);
        toolTypes.add(Material.GOLDEN_SWORD);
        //Hoe
        toolTypes.add(Material.NETHERITE_HOE);
        toolTypes.add(Material.DIAMOND_HOE);
        toolTypes.add(Material.IRON_HOE);
        toolTypes.add(Material.STONE_HOE);
        toolTypes.add(Material.WOODEN_HOE);
        toolTypes.add(Material.GOLDEN_HOE);
        //others
        toolTypes.add(Material.TRIDENT);
        toolTypes.add(Material.BOW);
        return toolTypes;
    }

    /**
     * Gets all the container types
     * @return an arraylist of all the container types
     */
    public static ArrayList<Material> getContainerTypes() {
        ArrayList<Material> containers = new ArrayList<>();
        containers.add(Material.SHULKER_BOX);
        containers.add(Material.WHITE_SHULKER_BOX);
        containers.add(Material.ORANGE_SHULKER_BOX);
        containers.add(Material.MAGENTA_SHULKER_BOX);
        containers.add(Material.LIGHT_BLUE_SHULKER_BOX);
        containers.add(Material.YELLOW_SHULKER_BOX);
        containers.add(Material.LIME_SHULKER_BOX);
        containers.add(Material.PINK_SHULKER_BOX);
        containers.add(Material.GRAY_SHULKER_BOX);
        containers.add(Material.LIGHT_GRAY_SHULKER_BOX);
        containers.add(Material.CYAN_SHULKER_BOX);
        containers.add(Material.PURPLE_SHULKER_BOX);
        containers.add(Material.BLUE_SHULKER_BOX);
        containers.add(Material.BROWN_SHULKER_BOX);
        containers.add(Material.GREEN_SHULKER_BOX);
        containers.add(Material.RED_SHULKER_BOX);
        containers.add(Material.BLACK_SHULKER_BOX);
        containers.add(Material.BUNDLE);

        return containers;
    }

}

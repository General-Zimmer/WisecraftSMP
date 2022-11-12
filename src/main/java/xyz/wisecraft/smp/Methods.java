package xyz.wisecraft.smp;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Methods {

    public static void noFinishTut(PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED + "Finish the tutorial first!");
        e.getPlayer().sendMessage(ChatColor.GOLD + "You can always return with /wisecraft tutorial");
    }


    public static void tpworld(IEssentials ess, World world, CommandSender sender) {


        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World does not exist");
            return;
        }

        setBack(ess, Bukkit.getPlayerExact(sender.getName()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + world.getName());
        sender.sendMessage(ChatColor.GOLD + "Teleporting to " + world.getName() + " in 1 second(s)");
    }

    public static void setBack(IEssentials ess, Player p) {
        if (ess != null)
            ess.getUser(p).setLastLocation();
        else
            Bukkit.getConsoleSender().sendMessage("Ess is null - wc SMP");
    }


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
}

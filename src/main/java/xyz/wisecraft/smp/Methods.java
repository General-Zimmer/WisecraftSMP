package xyz.wisecraft.smp;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Methods {

    public static void noFinishTut(PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED + "Finish the tutorial first!");
        e.getPlayer().sendMessage(ChatColor.GOLD + "You can always return with /wisecraft tutorial");
    }


    public static void tpworld(WisecraftSMP plugin, IEssentials ess, World world, CommandSender sender) {


        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World does not exist");
            return;
        }
        sender.sendMessage(ChatColor.GOLD + "Teleporting to " + world.getName() + " in 1 second(s)");
        new BukkitRunnable() {
            @Override
            public void run() {
                setBack(ess, Bukkit.getPlayerExact(sender.getName()));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + sender.getName() + " " + world.getName());
            }
        }.runTaskLater(plugin, 20);


    }

    public static void setBack(IEssentials ess, Player p) {
        if (ess != null)
            ess.getUser(p).setLastLocation();
        else
            Bukkit.getConsoleSender().sendMessage("Ess is null - wc SMP");
    }

    public static void giveGrace(WisecraftSMP plugin, PlayerRespawnEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();
        Angel angel = plugin.getGearmap().get(UUID);

        // Check for remaining graces
        if (angel.getGraces() <= 0) {return;}


        // Give tools
        List<ItemStack> tools = angel.getTools();
        if (tools != null)
            for(int i = 0; i < tools.size(); i++)
                inv.setItem(i, tools.get(i));

        // Give armor
        ItemStack[] armor = angel.getArmor();
        if (armor != null)
            inv.setArmorContents(armor);

        // Last few things
        angel.clear();
        angel.decreaseGraces();
        p.sendMessage(ChatColor.AQUA + "Your gear have been saved. You have " + angel.getGraces() + " graces left!");

        // Cooldown
        if (!angel.isGraceActive()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player p2 = Bukkit.getPlayer(UUID);
                    if (p2 == null) {
                        plugin.getGearmap().remove(UUID);
                        return;
                    }

                    Angel angel2 = plugin.getGearmap().get(UUID);

                    angel2.resetGrace(p2.hasPermission("wisecraft.donator"));
                    angel2.setGraceActive(false);
                    p2.sendMessage(ChatColor.AQUA + "Your graces has reset");
                }

            }.runTaskLater(plugin, 20*60*60); // 1 hour
            angel.setGraceActive(true);
        }
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

package xyz.wisecraft.smp.advancements.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.util.exceptions.AdvancementNoExist;

import java.util.Date;


public class Methods {

    private static AdvancementProgress gibAdv(NamespacedKey key, Player p) throws Exception {
        Advancement a = Bukkit.getAdvancement(key);
        if (a == null)
            throw new AdvancementNoExist(key.toString());
        return p.getAdvancementProgress(a);

    }

    public static void gibCri(String cri, NamespacedKey key, Player p) {
        WisecraftSMP plugin = WisecraftSMP.instance;

        try {
            AdvancementProgress prog = Methods.gibAdv(key, p);
            if (!prog.isDone()) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        prog.awardCriteria(cri);
                    }
                }.runTask(plugin);

            }
        }	catch (Exception ex) {
            if ("AdvancementNoExist".equals(ex.toString())) {
                Bukkit.getConsoleSender().sendMessage("Missing Advancement: " + ex.getMessage());
            }
        }
    }

    public static double calcCurrentSeconds(Date previousDate) {
        Date currentDate = new Date();
        return (currentDate.getTime() - previousDate.getTime())/1000.0;
    }

    public static InheritanceNode createRole(WisecraftSMP plugin, LuckPerms luck, String group) {
        //Create group and check its existance
        Group Gro = luck.getGroupManager().getGroup(group);
        if (Gro == null) {return null;}
        //Create role with server context
        return InheritanceNode.builder(Gro).withContext("server", plugin.getServer_name()).build();
    }
    @SuppressWarnings("ConstantConditions")
    public static void addRole(WisecraftSMP plugin, LuckPerms luck, Player p, String roleName) {
        luck.getUserManager().modifyUser(p.getUniqueId(), user -> {
            InheritanceNode role = Methods.createRole(plugin, luck, roleName);
            //Add role and save/commit

            user.data().add(role);
            luck.getUserManager().saveUser(user);
        });

    }
    @SuppressWarnings("ConstantConditions")
    public static void removeRole(WisecraftSMP plugin, LuckPerms luck, Player p, String roleName) {
        luck.getUserManager().modifyUser(p.getUniqueId(), user -> {
            InheritanceNode role = Methods.createRole(plugin, luck, roleName);
            //Add role and save/commit
            user.data().remove(role);
            luck.getUserManager().saveUser(user);
        });
    }

    public static Node buildNode(WisecraftSMP plugin, String group) {
        return Node.builder("group." + group).withContext("server", plugin.getServer_name()).build();
    }

}

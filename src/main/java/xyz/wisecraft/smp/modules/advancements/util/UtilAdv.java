package xyz.wisecraft.smp.modules.advancements.util;

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
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.Date;


public abstract class UtilAdv {

    private static final WisecraftSMP plugin = WisecraftSMP.getPlugin(WisecraftSMP.class);
    private static final LuckPerms luck = WisecraftSMP.getLuck();

    //todo move methods expected to be used once to UtilAdvRandom and rename this UtilAdvCommon
    private static AdvancementProgress gibAdv(NamespacedKey key, Player p) {
        Advancement a = Bukkit.getAdvancement(key);
        if (a == null)
            throw new RuntimeException("AdvancementNoExist: " + key);
        return p.getAdvancementProgress(a);

    }

    public static void gibCri(String cri, NamespacedKey key, Player p) {

        try {
            AdvancementProgress prog = UtilAdv.gibAdv(key, p);
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

    public static InheritanceNode createRole(String group) {
        //Create group and check its existance
        Group Gro = luck.getGroupManager().getGroup(group);
        if (Gro == null) {return null;}
        //Create role with server context
        return InheritanceNode.builder(Gro).withContext("server", OtherStorage.getServer_name()).build();
    }
    @SuppressWarnings("ConstantConditions")
    public static void addRole(Player p, String roleName) {
        luck.getUserManager().modifyUser(p.getUniqueId(), user -> {
            InheritanceNode role = UtilAdv.createRole(roleName);
            //Add role and save/commit

            user.data().add(role);
            luck.getUserManager().saveUser(user);
        });

    }
    @SuppressWarnings("ConstantConditions")
    public static void removeRole(Player p, String roleName) {
        luck.getUserManager().modifyUser(p.getUniqueId(), user -> {
            InheritanceNode role = UtilAdv.createRole(roleName);
            //Add role and save/commit
            user.data().remove(role);
            luck.getUserManager().saveUser(user);
        });
    }

    public static Node buildNode(String group) {
        return Node.builder("group." + group).withContext("server", OtherStorage.getServer_name()).build();
    }

}

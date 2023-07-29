package xyz.wisecraft.smp.modules.advancements.util;

import com.craftaro.ultimatetimber.events.TreeFellEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.Date;
import java.util.UUID;

/**
 * Advancement utilities
 */
public abstract class UtilAdv {

    private static final WisecraftSMP plugin = WisecraftSMP.getPlugin(WisecraftSMP.class);
    private static final LuckPerms luck = WisecraftSMP.getLuck();

    //todo move methods expected to be used once to UtilAdvRandom and rename this UtilAdvCommon

    /**
     * Give advancement code
     */
    private static AdvancementProgress gibAdv(NamespacedKey key, Player p) {
        Advancement a = Bukkit.getAdvancement(key);
        if (a == null)
            throw new RuntimeException("AdvancementNoExist: " + key);
        return p.getAdvancementProgress(a);

    }

    /**
     * Give player the criteria
     * @param cri Criteria to give
     * @param key Advancement key
     * @param p Player to give criteria to
     */
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

    /**
     * Get the time in seconds since previousDate
     * @param previousDate Date to compare to
     * @return Seconds since previousDate
     */
    public static double calcCurrentSeconds(Date previousDate) {
        Date currentDate = new Date();
        return (currentDate.getTime() - previousDate.getTime())/1000.0;
    }

    /**
     * Create a role
     * @param group Group to create role from
     * @return Role
     */
    public static InheritanceNode createRole(String group) {
        //Create group and check its existance
        Group Gro = luck.getGroupManager().getGroup(group);
        if (Gro == null) {return null;}
        //Create role with server context
        return InheritanceNode.builder(Gro).withContext("server", OtherStorage.getServer_name()).build();
    }

    /**
     * Add role to player
     * @param p Player to add role to
     * @param roleName Role to add
     */
    public static void addRole(Player p, String roleName) {
        luck.getUserManager().modifyUser(p.getUniqueId(), user -> {
            InheritanceNode role = UtilAdv.createRole(roleName);
            //Add role and save/commit

            user.data().add(role);
            luck.getUserManager().saveUser(user);
        });

    }

    /**
     * Remove role from player
     * @param p Player to remove role from
     * @param roleName Role to remove
     */
    public static void removeRole(Player p, String roleName) {
        luck.getUserManager().modifyUser(p.getUniqueId(), user -> {
            InheritanceNode role = UtilAdv.createRole(roleName);
            //Add role and save/commit
            user.data().remove(role);
            luck.getUserManager().saveUser(user);
        });
    }

    /**
     * Build a node
     * @param group Group to build node from
     * @return Node
     */
    public static Node buildNode(String group) {
        return Node.builder("group." + group).withContext("server", OtherStorage.getServer_name()).build();
    }

    public static void advLvl(BaseAdvancement adv, PlayerLevelChangeEvent e) {
        Player p = e.getPlayer();
        int lvl = e.getNewLevel();

        int prog = adv.getProgression(p);
        if (lvl > prog && lvl <= adv.getMaxProgression()) {
            adv.setProgression(p, lvl);
        } else if (lvl > adv.getMaxProgression()) {
            adv.setProgression(p, adv.getMaxProgression());
        }
    }

    public static void checkTimber(TreeFellEvent e, BaseAdvancement adv) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();


        int treesTimbered = WisecraftSMP.getCore().getInfom().get(UUID).getTimber();

        if (treesTimbered >= adv.getMaxProgression()) {
            adv.incrementProgression(p);
        }
    }

}

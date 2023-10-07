package xyz.wisecraft.smp.modules.advancements.util;

import com.craftaro.ultimatetimber.events.TreeFellEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import com.gamingmesh.jobs.container.Job;
import net.essentialsx.api.v2.services.discord.DiscordService;
import net.essentialsx.api.v2.services.discord.MessageType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.Firstspecialty;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Advancement utilities
 */
public abstract class UtilAdv {

    private static final WisecraftSMP plugin = WisecraftSMP.getPlugin(WisecraftSMP.class);
    private static final LuckPerms luck = AdvancementsModule.getModule().getLuck();

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

            user.data().add(Objects.requireNonNull(role));
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
            user.data().remove(Objects.requireNonNull(role));
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


        int treesTimbered = AdvancementsModule.getModule().getCore().getInfom().get(UUID).getTimber();

        if (treesTimbered >= adv.getMaxProgression()) {
            adv.setProgression(p, adv.getMaxProgression());
        } else if (treesTimbered > adv.getProgression(p)) {
            adv.setProgression(p, treesTimbered);
        }
    }

    public static void fixRole_AdvMismatch(Player p, com.fren_gor.ultimateAdvancementAPI.advancement.Advancement adv, String role) {

        Node node = UtilAdv.buildNode(role);

        boolean isAdvGranted = adv.isGranted(p);
        boolean hasRole = luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(node);

        //Citzen check
        if (!isAdvGranted && hasRole)
            UtilAdv.removeRole(p, role);
        else if (isAdvGranted && !hasRole)
            adv.revoke(p);
    }

    public static void isJobMaxed(JobsLevelUpEvent e, BaseAdvancement adv) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer().getPlayer();

        if (adv.getProgression(player) < adv.getMaxProgression() && e.getLevel() >= e.getJob().getMaxLevel()) {
            adv.incrementProgression(player);
        }
    }

    public static void isJobMaxed(JobsLevelUpEvent e, BaseAdvancement adv, Job job) {
        if (!job.equals(e.getJob())) return;

        isJobMaxed(e, adv);
    }

    public static void IsCraftedItem(org.bukkit.event.inventory.CraftItemEvent e, BaseAdvancement adv, Material itemType) {
        @NotNull Material resType = e.getRecipe().getResult().getType();

        Player p = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
        // todo use google's common library for null checks
        if (p != null && resType.equals(itemType)) {
            adv.incrementProgression(p);
        }
    }

    public static void grantFirstSpecialty(Player p) {
        com.fren_gor.ultimateAdvancementAPI.advancement.Advancement first = AdvancementsModule.plugin.getAdvapi().getAdvancement(Firstspecialty.KEY);
        if (first != null)
            first.grant(p);
    }

    public static void sendAdvancementGrantedAnnouncementDiscord(Player p, com.fren_gor.ultimateAdvancementAPI.advancement.Advancement adv) {
        final MessageType channel = MessageType.DefaultTypes.CHAT;
        final boolean allowGroupMentions = false;
        boolean gameRule = Boolean.TRUE.equals(p.getWorld().getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS));
        @Nullable DiscordService apiDiscord = AdvancementsModule.getModule().getApiDiscord();

        
        if (apiDiscord != null && gameRule && adv.getDisplay().doesAnnounceToChat())
            apiDiscord.sendMessage(channel, Arrays.toString(adv.getAnnounceMessage(p)), allowGroupMentions);
    }
}

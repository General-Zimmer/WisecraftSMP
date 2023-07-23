package xyz.wisecraft.smp.modules.advancements.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class LegacyRoles implements Listener {

    private final WisecraftSMP plugin;
    private final LuckPerms luck;

    /**
     * Constructor
     */
    public LegacyRoles() {
        this.plugin = WisecraftSMP.getInstance();
        this.luck = WisecraftSMP.getLuck();
    }

    /**
     * Give player roles when they've done the advancement
     * @param e Event
     */
    @EventHandler
    public void roles(PlayerAdvancementDoneEvent e) {

        Advancement adv = e.getAdvancement();
        Player p = e.getPlayer();

        //todo replace these checks with a method
        NamespacedKey keyCit = new NamespacedKey(plugin, "citizen");
        Advancement advCitizen = Bukkit.getAdvancement(keyCit);
        //Citzen check
        if (adv.equals(advCitizen))
            UtilAdv.addRole(p, "citizen");

        NamespacedKey keyNob = new NamespacedKey(plugin, "nobility");
        Advancement advNoble = Bukkit.getAdvancement(keyNob);
        //Noble check
        if (adv.equals(advNoble))
            UtilAdv.addRole(p, "noble");
    }


    /**
     * Check if player has the advancement, if not remove the role
     * @param e Event
     */
    @EventHandler
    public void RoleAdvMissingCheck(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        NamespacedKey keyCit = new NamespacedKey(plugin, "citizen");
        Advancement advCitizen = Bukkit.getAdvancement(keyCit);
        NamespacedKey keyNob = new NamespacedKey(plugin, "nobility");
        Advancement advNoble = Bukkit.getAdvancement(keyNob);

        Node citizenN = UtilAdv.buildNode("citizen");
        Node nobleN = UtilAdv.buildNode("noble");

        boolean advC = p.getAdvancementProgress(advCitizen).isDone();
        boolean roleC = luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(citizenN);

        //Citzen check
        if (!advC && roleC)
            UtilAdv.removeRole(p, "citizen");
        else if (advC && !roleC)
            //sync with main
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getAdvancementProgress(advCitizen).revokeCriteria("citizen");
                }
            }.runTask(plugin);


        //Noble check
        if (!p.getAdvancementProgress(advNoble).isDone() &&
                luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(nobleN))
            UtilAdv.removeRole(p, "noble");
        else if (p.getAdvancementProgress(advNoble).isDone() &&
                !luck.getPlayerAdapter(Player.class).getUser(p).getNodes().contains(nobleN))
            //sync with main
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getAdvancementProgress(advNoble).revokeCriteria("noble");
                }
            }.runTask(plugin);

    }
}

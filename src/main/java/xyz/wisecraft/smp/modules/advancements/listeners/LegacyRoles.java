package xyz.wisecraft.smp.modules.advancements.listeners;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import net.luckperms.api.LuckPerms;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.adv.legacy.Citizen;
import xyz.wisecraft.smp.modules.advancements.adv.legacy.Nobility;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class LegacyRoles implements Listener {

    private final WisecraftSMP plugin;
    private final LuckPerms luck;

    /**
     * Constructor
     */
    public LegacyRoles(LuckPerms luck) {
        this.plugin = WisecraftSMP.getInstance();
        this.luck = luck;
    }



    /**
     * Check if player is missing either adv or role and remove the other if one is missing.
     * @param e Event
     */
    @EventHandler
    public void Role_AdvMissingCheck(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        Advancement advCitizen = plugin.getAdv().getAdvancement(Citizen.KEY);
        Advancement advNoble = plugin.getAdv().getAdvancement(Nobility.KEY);

        if (advCitizen != null)
            UtilAdv.fixRole_AdvMismatch(p, advCitizen, "citizen");

        if (advNoble != null)
            UtilAdv.fixRole_AdvMismatch(p, advNoble, "noble");
    }
}

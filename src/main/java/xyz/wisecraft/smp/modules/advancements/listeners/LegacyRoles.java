package xyz.wisecraft.smp.modules.advancements.listeners;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.Citizen;
import xyz.wisecraft.smp.modules.advancements.advs.legacy.Nobility;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class LegacyRoles implements Listener {

    private final WisecraftSMP plugin;

    /**
     * Constructor for LegacyRoles
     *
     */
    public LegacyRoles() {
        this.plugin = WisecraftSMP.getInstance();
    }



    /**
     * Check if player is missing either adv or role and remove the other if one is missing.
     * @param e Event
     */
    @EventHandler
    public void Role_AdvMissingCheck(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        Advancement advCitizen = AdvancementsModule.getAdvapi().getAdvancement(Citizen.KEY);
        Advancement advNoble = AdvancementsModule.getAdvapi().getAdvancement(Nobility.KEY);

        if (advCitizen != null)
            UtilAdv.fixRole_AdvMismatch(p, advCitizen, "citizen");

        if (advNoble != null)
            UtilAdv.fixRole_AdvMismatch(p, advNoble, "noble");
    }
}

package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Omega_xp extends BaseAdvancement implements VanillaVisibility {

    public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "omega_xp");


    public Omega_xp(Advancement parent, float x, float y) {
        super(KEY.getKey(), new AdvancementDisplay(Material.EXPERIENCE_BOTTLE, "OMEEEEEEGA XP!", AdvancementFrameType.CHALLENGE, true, true, x, y, "Level 1000 aquired"), parent, 1000);

        registerEvent(PlayerLevelChangeEvent.class, e -> UtilAdv.advLvl(this, e));
    }

    @Override
    public void onGrant(@NotNull Player player, boolean giveRewards) {
        super.onGrant(player, giveRewards);
        UtilAdv.sendAdvancementGrantedAnnouncementDiscord(player, this);
    }
}
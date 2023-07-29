package xyz.wisecraft.smp.modules.advancements.adv.common_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Omega_xp extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "omega_xp");


  public Omega_xp(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.EXPERIENCE_BOTTLE, "OMEEEEEEGA XP!", AdvancementFrameType.CHALLENGE, true, true, x, y , "Level 1000 aquired"), parent, 1000);

    registerEvent(PlayerLevelChangeEvent.class, e -> {
      UtilAdv.advLvl(this, e);

    });
  }
}
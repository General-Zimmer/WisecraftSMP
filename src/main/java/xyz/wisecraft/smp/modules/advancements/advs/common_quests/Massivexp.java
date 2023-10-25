package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Massivexp extends BaseAdvancement implements ParentGrantedVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "massivexp");


  public Massivexp(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.EXPERIENCE_BOTTLE, "MASSIVE XP!", AdvancementFrameType.CHALLENGE, true, true, x, y , "Level 250 acquired"), parent, 250);

    registerEvent(PlayerLevelChangeEvent.class, e -> UtilAdv.advLvl(this, e));
  }

}
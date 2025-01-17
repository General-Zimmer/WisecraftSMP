package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.craftaro.ultimatetimber.events.TreeFellEvent;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

public class Expertlumb extends BaseAdvancement implements ParentGrantedVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "expertlumb");


  public Expertlumb(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.IRON_AXE, "Lumber expert", AdvancementFrameType.CHALLENGE, true, true, x, y , "Expert with 10000 trees cut down!"), parent, 10000);

    if (AdvancementsModule.getModule().isTimberEnabled())
      registerEvent(TreeFellEvent.class, e -> UtilAdv.checkTimber(e, this));
  }

}
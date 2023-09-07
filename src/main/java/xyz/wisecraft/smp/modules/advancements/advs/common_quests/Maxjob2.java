package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.MultiTasksAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Maxjob2 extends MultiTasksAdvancement implements ParentGrantedVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxjob2");


  public Maxjob2(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.FLETCHING_TABLE, "marathon Jobbin'", AdvancementFrameType.CHALLENGE, true, true, x, y , "Max out 5 unique jobs in total"), parent, 5);
  }
}
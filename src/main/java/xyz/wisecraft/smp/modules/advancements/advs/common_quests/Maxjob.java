package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Maxjob extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxjob");


  public Maxjob(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.FLETCHING_TABLE, "Casually jobbin'", AdvancementFrameType.GOAL, true, true, x, y , "Max out a job"), parent, 1);
  }
}
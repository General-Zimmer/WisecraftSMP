package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Maxjob3 extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxjob3");


  public Maxjob3(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.FLETCHING_TABLE, "Jobster", AdvancementFrameType.CHALLENGE, true, true, x, y , "Max out 9 unique jobs in total"), parent, 1);
  }
}
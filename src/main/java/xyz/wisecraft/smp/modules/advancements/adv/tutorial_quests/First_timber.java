package xyz.wisecraft.smp.modules.advancements.adv.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class First_timber extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "first_timber");


  public First_timber(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.WOODEN_AXE, "TIIIMBBEEER!", AdvancementFrameType.TASK, true, false, x, y , "Is going down! "), parent, 1);
  }
}
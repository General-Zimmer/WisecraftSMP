package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Gettownplot extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "gettownplot");


  public Gettownplot(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.GRASS_BLOCK, "Rent a plot!", AdvancementFrameType.TASK, true, false, x, y , "By using /plot claim"), parent, 1);
  }
}
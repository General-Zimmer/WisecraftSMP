package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.MultiTasksAdvancement;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

public class Allspecialty extends MultiTasksAdvancement  {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "allspecialty");


  public Allspecialty(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.DEBUG_STICK, "Job hopping", AdvancementFrameType.TASK, true, true, x, y , "Use every job's specialty available"), parent, 5);
  }

}
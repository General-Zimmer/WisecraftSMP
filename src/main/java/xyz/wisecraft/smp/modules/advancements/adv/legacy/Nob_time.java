package xyz.wisecraft.smp.modules.advancements.adv.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Nob_time extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "nob_time");


  public Nob_time(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1200 );
  }
}
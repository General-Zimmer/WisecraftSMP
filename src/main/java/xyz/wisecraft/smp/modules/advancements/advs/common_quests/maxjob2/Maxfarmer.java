package xyz.wisecraft.smp.modules.advancements.advs.common_quests.maxjob2;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;

public class Maxfarmer extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxfarmer");


  public Maxfarmer(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );
  }
}
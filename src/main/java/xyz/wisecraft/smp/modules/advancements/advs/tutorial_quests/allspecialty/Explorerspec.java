package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.allspecialty;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;

public class Explorerspec extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "explorerspec");


  public Explorerspec(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );
  }
}
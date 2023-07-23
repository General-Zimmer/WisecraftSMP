package xyz.wisecraft.smp.modules.advancements.adv.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Cit_block_break extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "cit_block_break");


  public Cit_block_break(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 4000 );
  }
}
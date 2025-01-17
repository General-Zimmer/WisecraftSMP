package xyz.wisecraft.smp.modules.advancements.advs.common_quests.maxjob2;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.isJobMaxed;

public class Maxfarmer extends TaskAdvancement {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxfarmer");


  public Maxfarmer(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );
    registerEvent(JobsLevelUpEvent.class, e -> isJobMaxed(e, this, Jobs.getJob("Farmer")));
  }
}
package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

import java.util.List;

public class Maxjob2atonce extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxjob2atonce");


  public Maxjob2atonce(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.FLETCHING_TABLE, "Dual wielder", AdvancementFrameType.GOAL, true, true, x, y , "Max out 2 jobs at once"), parent, 1);

    registerEvent(JobsLevelUpEvent.class, e -> {
      if (e.isCancelled()) return;

      Player player = e.getPlayer().getPlayer();

      List<JobProgression> jobs = e.getPlayer().getJobProgression();

      if (jobs.size() < 2) {
        return;
      }

      int jobsMaxed = 0;
      do {
        JobProgression job = jobs.get(jobsMaxed);
        if (job.getLevel() >= job.getJob().getMaxLevel()) {
          jobsMaxed++;
        }

      } while (jobsMaxed < jobs.size() && jobsMaxed <= 2);

      if (jobsMaxed >= 2) {
        grant(player);
      }
    });
  }
}
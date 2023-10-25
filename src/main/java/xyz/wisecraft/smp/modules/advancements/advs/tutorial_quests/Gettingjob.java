package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Gettingjob extends BaseAdvancement  {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "gettingjob");


  public Gettingjob(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.FLETCHING_TABLE, "Get a job (hippie!)", AdvancementFrameType.TASK, true, false, x, y , "Join a job by /jobs browse. \nNote: Leaving will make you lose a portion of your job level"), parent, 1);

    registerEvent(JobsJoinEvent.class, e -> {
      if (e.isCancelled()) return;

      Player p = e.getPlayer().getPlayer();
      if (!isGranted(p))
        grant(p);
    });
  }

}
package xyz.wisecraft.smp.modules.advancements.advs.common_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.isJobMaxed;

public class Maxjob extends BaseAdvancement  {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "maxjob");


  public Maxjob(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.FLETCHING_TABLE, "Casually jobbin'", AdvancementFrameType.GOAL, true, true, x, y , "Max out a job"), parent, 1);

    registerEvent(JobsLevelUpEvent.class, e -> isJobMaxed(e, this));
  }
}
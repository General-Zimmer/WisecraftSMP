package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.allspecialty;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import xyz.wisecraft.smp.modules.jobsextra.event.ExplorerObtainsElytraEvent;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.grantFirstSpecialty;

public class Explorerspec extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "explorerspec");


  public Explorerspec(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );

    registerEvent(ExplorerObtainsElytraEvent.class, e -> {
        if (e.isCancelled()) return;

        Player p = e.getPlayer();

        if (!isGranted(p)) {
            incrementProgression(p);
            grantFirstSpecialty(p);
        }
    });
  }
}
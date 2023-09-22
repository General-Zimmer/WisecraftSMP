package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.allspecialty;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.grantFirstSpecialty;

public class Minerspec extends TaskAdvancement {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "minerspec");


  public Minerspec(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );
    registerEvent(org.bukkit.event.block.BlockBreakEvent.class, e -> {
      if (e.isCancelled() || !e.getBlock().getType().equals(Material.ANCIENT_DEBRIS)) return;

      org.bukkit.entity.Player p = e.getPlayer();
      if (!isGranted(p)) {
        incrementProgression(p, 1);
        grantFirstSpecialty(p);
      }
    });
  }
}
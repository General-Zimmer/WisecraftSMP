package xyz.wisecraft.smp.modules.advancements.advs.legacy.citizen;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

import java.util.UUID;

public class Cit_time extends TaskAdvancement {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "cit_time");


  public Cit_time(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 300 );

    registerEvent(BlockPlaceEvent.class, e -> {
      Player p = e.getPlayer();
      UUID UUID = p.getUniqueId();


      int time = AdvancementsModule.getModule().getCore().getInfom().get(UUID).getTime();

      if (time >= getMaxProgression()) {
        setProgression(p, getMaxProgression());
      } else if (time > getProgression(p)) {
        setProgression(p, time);
      }
    });
  }
}
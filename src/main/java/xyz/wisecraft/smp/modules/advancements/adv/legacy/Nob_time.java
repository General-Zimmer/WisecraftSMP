package xyz.wisecraft.smp.modules.advancements.adv.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

import java.util.UUID;

public class Nob_time extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "nob_time");


  public Nob_time(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1200 );

    registerEvent(BlockPlaceEvent.class, e -> {
      Player p = e.getPlayer();
      UUID UUID = p.getUniqueId();


      int time = WisecraftSMP.getCore().getInfom().get(UUID).getTime();

      if (time >= getMaxProgression()) {
        setProgression(p, getMaxProgression());
      } else if (time > getProgression(p)) {
        setProgression(p, time);
      }
    });
  }
}
package xyz.wisecraft.smp.modules.advancements.advs.legacy.citizen;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

import java.util.UUID;

public class Cit_block_place extends TaskAdvancement {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "cit_block_place");


  public Cit_block_place(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 2000);

    registerEvent(BlockPlaceEvent.class, e -> {
      Player p = e.getPlayer();
      UUID UUID = p.getUniqueId();


      int blocksPlace = AdvancementsModule.getModule().getCore().getInfom().get(UUID).getBlocksPlace();

      if (blocksPlace >= getMaxProgression()) {
        setProgression(p, getMaxProgression());
      } else if (blocksPlace > getProgression(p)) {
        setProgression(p, blocksPlace);
      }
    });
  }
}
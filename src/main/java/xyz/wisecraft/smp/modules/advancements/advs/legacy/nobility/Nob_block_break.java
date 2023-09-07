package xyz.wisecraft.smp.modules.advancements.advs.legacy.nobility;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

import java.util.UUID;

public class Nob_block_break extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "nob_block_break");


  public Nob_block_break(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 15000 );

    registerEvent(BlockBreakEvent.class, e -> {
      Player p = e.getPlayer();
      UUID UUID = p.getUniqueId();


      int blocksBroke = AdvancementsModule.getModule().getCore().getInfom().get(UUID).getBlocksBroke();

      if (blocksBroke >= getMaxProgression()) {
        setProgression(p, getMaxProgression());
      } else if (blocksBroke > getProgression(p)) {
        setProgression(p, blocksBroke);
      }
    });
  }
}
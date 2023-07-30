package xyz.wisecraft.smp.modules.advancements.adv.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

import java.util.UUID;

public class Cit_block_break extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "cit_block_break");


  public Cit_block_break(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 4000);

    registerEvent(BlockBreakEvent.class, e -> {
      Player p = e.getPlayer();
      UUID UUID = p.getUniqueId();


      int blocksBroke = WisecraftSMP.getCore().getInfom().get(UUID).getBlocksBroke();

      if (blocksBroke >= getMaxProgression()) {
        setProgression(p, getMaxProgression());
      } else if (blocksBroke > getProgression(p)) {
        setProgression(p, blocksBroke);
      }
    });
  }
}
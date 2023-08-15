package xyz.wisecraft.smp.modules.advancements.adv.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Cit_Dia_pick extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "dia_pick");


  public Cit_Dia_pick(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );

    registerEvent(PlayerInventorySlotChangeEvent.class, e -> {
      Player p = e.getPlayer();
      String item = e.getNewItemStack().getType().toString();

      if (item.contains("DIAMOND_PICKAXE") || item.contains("NETHERITE_PICKAXE")) {
        incrementProgression(p);
      }
    });

  }
}
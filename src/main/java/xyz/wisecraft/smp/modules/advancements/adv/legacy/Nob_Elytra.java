package xyz.wisecraft.smp.modules.advancements.adv.legacy;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Nob_Elytra extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.legacy_NAMESPACE, "elytra");


  public Nob_Elytra(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );

    registerEvent(PlayerInventorySlotChangeEvent.class, e -> {
      Player p = e.getPlayer();
      if (e.getNewItemStack().getType().toString().contains("ELYTRA")) {
        incrementProgression(p);
      }
    });
  }
}
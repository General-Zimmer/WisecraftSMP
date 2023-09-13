package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.allspecialty;

import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.ArrayList;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.grantFirstSpecialty;

public class Blacksmithspec extends TaskAdvancement {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "blacksmithspec");


  public Blacksmithspec(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );

    registerEvent(CraftItemEvent.class, e -> {
      if (e.isCancelled() || !(e.getWhoClicked() instanceof Player p)) return;

      ArrayList<Material> blacksmithCrafts = OtherStorage.getBlacksmithCrafts();
      Material craftMaterial = e.getRecipe().getResult().getType();

      if (!isGranted(p) && blacksmithCrafts.contains(craftMaterial)) {
        incrementProgression(p, 1);
        grantFirstSpecialty(p);
      }
    });
  }
}
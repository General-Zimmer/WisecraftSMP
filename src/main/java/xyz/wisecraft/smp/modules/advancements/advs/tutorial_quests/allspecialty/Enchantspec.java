package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests.allspecialty;

import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.AbstractMultiTasksAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.tasks.TaskAdvancement;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingRecipe;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.heirloom.recipes.SmithRecipes;
import xyz.wisecraft.smp.modules.heirloom.util.UtilRandom;

import java.util.List;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.grantFirstSpecialty;

public class Enchantspec extends TaskAdvancement {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "enchantspec");


  public Enchantspec(AbstractMultiTasksAdvancement multitask) {
    super(KEY.getKey(), multitask, 1 );

    registerEvent(SmithItemEvent.class, e -> {
      if (e.isCancelled() || !(e.getWhoClicked() instanceof Player p) || !(e.getInventory().getRecipe() instanceof SmithingRecipe recipe)) return;

      List<SmithingRecipe> recipes = SmithRecipes.getRecipe();
      boolean hasRecipe = UtilRandom.isRecipe(recipes, recipe);

      if (!isGranted(p) && hasRecipe) {
        incrementProgression(p, 1);
        grantFirstSpecialty(p);
      }
    });
  }
}
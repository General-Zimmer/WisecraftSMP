package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

public class Craft_elytra extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "craft_elytra");


  public Craft_elytra(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.ELYTRA, "Flying craftsmanship", AdvancementFrameType.TASK, true, true, x, y , "Craft an elytra. Check our wiki on wisecraft.xyz for recipes"), parent, 1);

    registerEvent(CraftItemEvent.class, e -> {
      @NotNull Material resType = e.getRecipe().getResult().getType();

      Player p = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
      // todo use google's common library for null checks
      if (p != null && resType.equals(Material.ELYTRA)) {
        incrementProgression(p);
      }
    });
  }
}
package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.IsCraftedItem;

public class Craft_elytra extends BaseAdvancement implements VanillaVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "craft_elytra");


  public Craft_elytra(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.ELYTRA, "Flying craftsmanship", AdvancementFrameType.TASK, true, true, x, y , "Craft an elytra. Check our wiki on wisecraft.xyz for recipes"), parent, 1);

    registerEvent(CraftItemEvent.class, e -> IsCraftedItem(e, this, Material.ELYTRA));
  }

}
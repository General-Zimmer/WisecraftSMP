package xyz.wisecraft.smp.modules.advancements.advs.tutorial_quests;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.bukkit.event.inventory.CraftItemEvent;
import xyz.wisecraft.smp.modules.advancements.advs.AdvancementTabNamespaces;

import static xyz.wisecraft.smp.modules.advancements.util.UtilAdv.IsCraftedItem;

public class Craft_bundle extends BaseAdvancement implements VanillaVisibility {

  public static final AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tutorial_quests_NAMESPACE, "craft_bundle");


  public Craft_bundle(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.BUNDLE, "Bundling things together!", AdvancementFrameType.TASK, true, false, x, y , "Craft a bundle. Check our wiki on wisecraft.xyz for recipes"), parent, 1);

    registerEvent(CraftItemEvent.class, e -> IsCraftedItem(e, this, Material.BUNDLE));
  }

}
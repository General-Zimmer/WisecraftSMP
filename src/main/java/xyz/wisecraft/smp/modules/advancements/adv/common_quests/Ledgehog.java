package xyz.wisecraft.smp.modules.advancements.adv.common_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Ledgehog extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "ledgehog");


  public Ledgehog(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.SPECTRAL_ARROW, "The hog of ledges", AdvancementFrameType.CHALLENGE, true, true, x, y , "Have 25 arrows in ya body at once and die from falling"), parent, 1);
  }
}
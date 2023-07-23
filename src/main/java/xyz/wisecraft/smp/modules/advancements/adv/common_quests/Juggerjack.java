package xyz.wisecraft.smp.modules.advancements.adv.common_quests;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import xyz.wisecraft.smp.modules.advancements.adv.AdvancementTabNamespaces;

public class Juggerjack extends BaseAdvancement implements VanillaVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.common_quests_NAMESPACE, "juggerjack");


  public Juggerjack(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(Material.DIAMOND_AXE, "Jugger-Jack", AdvancementFrameType.CHALLENGE, true, true, x, y , "You're the juggernauts of LumberJacks"), parent, 1);
  }
}